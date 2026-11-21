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
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import java.time.LocalDateTime
import cr.ac.utn.examenI.domain.model.Med
import cr.ac.utn.examenI.viewmodel.MedViewModel
import android.util.Patterns

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

        txtId = findViewById(R.id.txtId)
        txtName = findViewById(R.id.txtName)
        txtFirstName = findViewById(R.id.txtFirstName)
        txtLastName = findViewById(R.id.txtLastName)
        txtPhone = findViewById(R.id.txtPhone)
        txtEmail = findViewById(R.id.txtEmail)
        txtAddress = findViewById(R.id.txtAddress)
        txtBP = findViewById(R.id.txtBP)
        txtWeight = findViewById(R.id.txtWeight)
        txtHeight = findViewById(R.id.txtHeight)
        txtOxygen = findViewById(R.id.txtOxygen)
        txtDateTime = findViewById(R.id.txtDateTime)

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtDateTime.setText(util.formatDateTime(LocalDateTime.now()))
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
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
        val firstName = txtFirstName.text.toString().trim()
        val lastName = txtLastName.text.toString().trim()
        val phoneStr = txtPhone.text.toString().trim()
        val email = txtEmail.text.toString().trim()
        val address = txtAddress.text.toString().trim()
        val bp = txtBP.text.toString().trim()
        val dateTimeStr = txtDateTime.text.toString().trim()

        // Required fields
        if (id.isEmpty() || name.isEmpty() ||
            firstName.isEmpty() || lastName.isEmpty() ||
            phoneStr.isEmpty() || email.isEmpty() ||
            address.isEmpty() || bp.isEmpty() ||
            txtWeight.text.isBlank() ||
            txtHeight.text.isBlank() ||
            txtOxygen.text.isBlank() ||
            dateTimeStr.isEmpty()) {

            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        // Validate names
        val nameRegex = Regex("^[\\p{L}]+(?:[ '-][\\p{L}]+)*$")

        if (!nameRegex.matches(name) ||
            !nameRegex.matches(firstName) ||
            !nameRegex.matches(lastName)) {

            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        // Validate phone and email
        if (!phoneStr.matches(Regex("^[0-9]{8}$")) ||
            !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        // Validate blood pressure
        val bpRegex = Regex("""^\d{2,3}/\d{2,3}$""")

        if (!bpRegex.matches(bp)) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        // Convert values
        val dt = util.parseStringToDateTimeModern(dateTimeStr)
        val phone = phoneStr.toIntOrNull()
        val weight = txtWeight.text.toString().toDoubleOrNull()
        val height = txtHeight.text.toString().toDoubleOrNull()
        val oxygen = txtOxygen.text.toString().toIntOrNull()

        // Validate numeric values and date
        if (dt == null || phone == null ||
            weight == null || !weight.isFinite() || weight <= 0 ||
            height == null || !height.isFinite() || height <= 0 ||
            oxygen == null || oxygen !in 0..100) {

            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        // Create medical record
        val item = Med(
            id = id,
            name = name,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            email = email,
            address = address,
            bloodPressure = bp,
            weight = weight,
            height = height,
            oxygenation = oxygen,
            checkDateTime = dt
        )

        // Save or update
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
