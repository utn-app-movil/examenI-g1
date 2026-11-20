package cr.ac.utn.examenI

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.examenI.domain.model.WatWaterMonitoring
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.WatViewModel
import java.time.LocalDateTime

class WatCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtNisId: EditText
    private lateinit var txtPh: EditText
    private lateinit var txtTurbidity: EditText
    private lateinit var chkCloro: CheckBox
    private lateinit var chkPlomo: CheckBox
    private lateinit var chkBacterias: CheckBox
    private lateinit var chkNitratos: CheckBox
    private lateinit var txtDateTime: EditText

    private var currentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wat_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtId = findViewById(R.id.wat_txtId)
        txtName = findViewById(R.id.wat_txtName)
        txtFirstName = findViewById(R.id.wat_txtFirstName)
        txtLastName = findViewById(R.id.wat_txtLastName)
        txtPhone = findViewById(R.id.wat_txtPhone)
        txtEmail = findViewById(R.id.wat_txtEmail)
        txtAddress = findViewById(R.id.wat_txtAddress)
        txtNisId = findViewById(R.id.wat_txtNisId)
        txtPh = findViewById(R.id.wat_txtPh)
        txtTurbidity = findViewById(R.id.wat_txtTurbidity)
        chkCloro = findViewById(R.id.wat_chkCloro)
        chkPlomo = findViewById(R.id.wat_chkPlomo)
        chkBacterias = findViewById(R.id.wat_chkBacterias)
        chkNitratos = findViewById(R.id.wat_chkNitratos)
        txtDateTime = findViewById(R.id.wat_txtDateTime)

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtDateTime.setText(util.formatDateTime(LocalDateTime.now()))
        }

        findViewById<Button>(R.id.wat_btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.wat_btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.wat_btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        val item = WatViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        txtNisId.setText(item.nisId)
        txtPh.setText(item.ph.toString())
        txtTurbidity.setText(item.turbidity.toString())
        chkCloro.isChecked = item.contaminants.contains("Cloro")
        chkPlomo.isChecked = item.contaminants.contains("Plomo")
        chkBacterias.isChecked = item.contaminants.contains("Bacterias")
        chkNitratos.isChecked = item.contaminants.contains("Nitratos")
        txtDateTime.setText(util.formatDateTime(item.monitoringDateTime))
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val nis = txtNisId.text.toString().trim()
        val phVal = txtPh.text.toString().toDoubleOrNull() ?: -1.0
        val turbVal = txtTurbidity.text.toString().toDoubleOrNull() ?: -1.0
        val dateTimeStr = txtDateTime.text.toString().trim()

        if (id.isEmpty() || name.isEmpty() || nis.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos requeridos.", Toast.LENGTH_SHORT).show()
            return
        }

        if (phVal < 0.0 || phVal > 14.0) {
            Toast.makeText(this, "El pH debe estar en el rango de 0 a 14.", Toast.LENGTH_SHORT).show()
            return
        }

        if (turbVal < 0.0) {
            Toast.makeText(this, "La turbidez debe ser mayor o igual a 0.", Toast.LENGTH_SHORT).show()
            return
        }

        val dt = util.parseStringToDateTimeModern(dateTimeStr)
        if (dt == null || dt.isAfter(LocalDateTime.now())) {
            Toast.makeText(this, "La fecha de monitoreo debe ser menor o igual a la actual.", Toast.LENGTH_SHORT).show()
            return
        }

        val contaminantsList = mutableListOf<String>()
        if (chkCloro.isChecked) contaminantsList.add("Cloro")
        if (chkPlomo.isChecked) contaminantsList.add("Plomo")
        if (chkBacterias.isChecked) contaminantsList.add("Bacterias")
        if (chkNitratos.isChecked) contaminantsList.add("Nitratos")

        val item = WatWaterMonitoring(
            id = id,
            name = name,
            firstName = txtFirstName.text.toString().trim(),
            lastName = txtLastName.text.toString().trim(),
            phone = txtPhone.text.toString().toIntOrNull() ?: 0,
            email = txtEmail.text.toString().trim(),
            address = txtAddress.text.toString().trim(),
            nisId = nis,
            ph = phVal,
            turbidity = turbVal,
            contaminants = contaminantsList.joinToString(", "),
            monitoringDateTime = dt
        )

        if (currentId.isNullOrEmpty()) {
            if (WatViewModel.getById(id) != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            WatViewModel.save(item)
        } else {
            WatViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            WatViewModel.delete(id)
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
