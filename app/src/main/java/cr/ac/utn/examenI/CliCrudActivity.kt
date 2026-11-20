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
import cr.ac.utn.examenI.domain.model.CliClient
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.CliViewModel

class CliCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtCompanyName: EditText
    private lateinit var txtCorporateId: EditText
    private lateinit var txtProvince: EditText
    private lateinit var txtCanton: EditText
    private lateinit var txtDistrict: EditText
    private lateinit var chkConsulting: CheckBox
    private lateinit var chkSupport: CheckBox
    private lateinit var chkDev: CheckBox
    private lateinit var chkMaint: CheckBox

    private var currentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cli_crud)
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
        txtCompanyName = findViewById(R.id.txtCompanyName)
        txtCorporateId = findViewById(R.id.txtCorporateId)
        txtProvince = findViewById(R.id.txtProvince)
        txtCanton = findViewById(R.id.txtCanton)
        txtDistrict = findViewById(R.id.txtDistrict)
        chkConsulting = findViewById(R.id.chkConsulting)
        chkSupport = findViewById(R.id.chkSupport)
        chkDev = findViewById(R.id.chkDev)
        chkMaint = findViewById(R.id.chkMaint)

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        val item = CliViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        txtCompanyName.setText(item.companyName)
        txtCorporateId.setText(item.corporateId)
        txtProvince.setText(item.province)
        txtCanton.setText(item.canton)
        txtDistrict.setText(item.district)

        chkConsulting.isChecked = item.services.contains("Consultoría")
        chkSupport.isChecked = item.services.contains("Soporte")
        chkDev.isChecked = item.services.contains("Desarrollo")
        chkMaint.isChecked = item.services.contains("Mantenimiento")
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val companyName = txtCompanyName.text.toString().trim()
        val corpId = txtCorporateId.text.toString().trim()

        if (id.isEmpty() || companyName.isEmpty() || corpId.isEmpty()) {
            Toast.makeText(this, "Por favor complete la identificación y datos de la empresa.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedServices = mutableListOf<String>()
        if (chkConsulting.isChecked) selectedServices.add("Consultoría")
        if (chkSupport.isChecked) selectedServices.add("Soporte")
        if (chkDev.isChecked) selectedServices.add("Desarrollo")
        if (chkMaint.isChecked) selectedServices.add("Mantenimiento")

        val item = CliClient(
            id = id,
            name = txtName.text.toString().trim(),
            firstName = txtFirstName.text.toString().trim(),
            lastName = txtLastName.text.toString().trim(),
            phone = txtPhone.text.toString().toIntOrNull() ?: 0,
            email = txtEmail.text.toString().trim(),
            address = txtAddress.text.toString().trim(),
            companyName = companyName,
            corporateId = corpId,
            province = txtProvince.text.toString().trim(),
            canton = txtCanton.text.toString().trim(),
            district = txtDistrict.text.toString().trim(),
            services = selectedServices.joinToString(", ")
        )

        if (currentId.isNullOrEmpty()) {
            if (CliViewModel.getById(id) != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            CliViewModel.save(item)
        } else {
            CliViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            CliViewModel.delete(id)
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
