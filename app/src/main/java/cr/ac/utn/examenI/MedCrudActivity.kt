package cr.ac.utn.examenI

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.examenI.domain.model.MedNursingCheck
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.MedViewModel
import java.time.LocalDateTime

class MedCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtBP: EditText
    private lateinit var txtWeight: EditText
    private lateinit var txtHeight: EditText
    private lateinit var txtOxygen: EditText
    private lateinit var txtDateTime: EditText

    private var currentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_med_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtId = findViewById(R.id.med_txtId)
        txtName = findViewById(R.id.med_txtName)
        txtFirstName = findViewById(R.id.med_txtFirstName)
        txtLastName = findViewById(R.id.med_txtLastName)
        txtPhone = findViewById(R.id.med_txtPhone)
        txtEmail = findViewById(R.id.med_txtEmail)
        txtAddress = findViewById(R.id.med_txtAddress)
        txtBP = findViewById(R.id.med_txtBP)
        txtWeight = findViewById(R.id.med_txtWeight)
        txtHeight = findViewById(R.id.med_txtHeight)
        txtOxygen = findViewById(R.id.med_txtOxygen)
        txtDateTime = findViewById(R.id.med_txtDateTime)

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtDateTime.setText(util.formatDateTime(LocalDateTime.now()))
        }

        findViewById<Button>(R.id.med_btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.med_btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.med_btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        val item = MedViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        txtBP.setText(item.bloodPressure)
        txtWeight.setText(item.weight.toString())
        txtHeight.setText(item.height.toString())
        txtOxygen.setText(item.oxygenation.toString())
        txtDateTime.setText(util.formatDateTime(item.checkDateTime))
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val bp = txtBP.text.toString().trim()
        val dateTimeStr = txtDateTime.text.toString().trim()

        if (id.isEmpty() || name.isEmpty() || bp.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos requeridos.", Toast.LENGTH_SHORT).show()
            return
        }

        val bpRegex = Regex("""^\d{2,3}/\d{2,3}$""")
        if (!bpRegex.matches(bp)) {
            Toast.makeText(this, "Formato de presión arterial debe ser 120/80.", Toast.LENGTH_SHORT).show()
            return
        }

        val dt = util.parseStringToDateTimeModern(dateTimeStr) ?: LocalDateTime.now()

        val item = MedNursingCheck(
            id = id,
            name = name,
            firstName = txtFirstName.text.toString().trim(),
            lastName = txtLastName.text.toString().trim(),
            phone = txtPhone.text.toString().toIntOrNull() ?: 0,
            email = txtEmail.text.toString().trim(),
            address = txtAddress.text.toString().trim(),
            bloodPressure = bp,
            weight = txtWeight.text.toString().toDoubleOrNull() ?: 0.0,
            height = txtHeight.text.toString().toDoubleOrNull() ?: 0.0,
            oxygenation = txtOxygen.text.toString().toDoubleOrNull() ?: 0.0,
            checkDateTime = dt
        )

        if (currentId.isNullOrEmpty()) {
            if (MedViewModel.getById(id) != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            MedViewModel.save(item)
        } else {
            MedViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            MedViewModel.delete(id)
            Toast.makeText(this, getString(R.string.TextDeleteSuccess), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> { doSave(); true }
            R.id.mnu_delete -> { doDelete(); true }
            R.id.mnu_cancel -> { finish(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
