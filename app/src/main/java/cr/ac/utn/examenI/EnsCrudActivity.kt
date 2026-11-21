package cr.ac.utn.examenI

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.examenI.domain.model.EnsInsurancePolicy
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.EnsViewModel
import java.time.LocalDate

class EnsCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtPolicyNum: EditText
    private lateinit var spnCompany: Spinner
    private lateinit var spnInsuranceType: Spinner
    private lateinit var txtPremium: EditText
    private lateinit var txtStartDate: EditText
    private lateinit var txtExpirationDate: EditText

    private var currentId: String? = null
    private val companies = arrayOf("INS", "ASSA", "MAPFRE", "BMI")
    private val insuranceTypes = arrayOf("Responsabilidad Civil", "Incendio", "Riesgos del Trabajo", "Flotilla")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ens_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtId = findViewById(R.id.ens_txtId)
        txtName = findViewById(R.id.ens_txtName)
        txtFirstName = findViewById(R.id.ens_txtFirstName)
        txtLastName = findViewById(R.id.ens_txtLastName)
        txtPhone = findViewById(R.id.ens_txtPhone)
        txtEmail = findViewById(R.id.ens_txtEmail)
        txtAddress = findViewById(R.id.ens_txtAddress)
        txtPolicyNum = findViewById(R.id.ens_txtPolicyNum)
        spnCompany = findViewById(R.id.ens_spnCompany)
        spnInsuranceType = findViewById(R.id.ens_spnInsuranceType)
        txtPremium = findViewById(R.id.ens_txtPremium)
        txtStartDate = findViewById(R.id.ens_txtStartDate)
        txtExpirationDate = findViewById(R.id.ens_txtExpirationDate)

        val adapterComp = ArrayAdapter(this, android.R.layout.simple_spinner_item, companies)
        adapterComp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnCompany.adapter = adapterComp

        val adapterType = ArrayAdapter(this, android.R.layout.simple_spinner_item, insuranceTypes)
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnInsuranceType.adapter = adapterType

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtStartDate.setText(util.formatDate(LocalDate.now()))
            txtExpirationDate.setText(util.formatDate(LocalDate.now().plusYears(1)))
        }

        findViewById<Button>(R.id.ens_btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.ens_btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.ens_btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        val item = EnsViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        txtPolicyNum.setText(item.policyNum)
        val posComp = companies.indexOf(item.company)
        if (posComp >= 0) spnCompany.setSelection(posComp)
        val posType = insuranceTypes.indexOf(item.insuranceType)
        if (posType >= 0) spnInsuranceType.setSelection(posType)
        txtPremium.setText(item.premium.toString())
        txtStartDate.setText(util.formatDate(item.startDate))
        txtExpirationDate.setText(util.formatDate(item.expirationDate))
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val policy = txtPolicyNum.text.toString().trim()

        if (id.isEmpty() || name.isEmpty() || policy.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos requeridos.", Toast.LENGTH_SHORT).show()
            return
        }

        val duplicatePolicy = EnsViewModel.getAll().any { it.policyNum.equals(policy, ignoreCase = true) && it.id != id }
        if (duplicatePolicy) {
            Toast.makeText(this, "El número de póliza ya existe.", Toast.LENGTH_SHORT).show()
            return
        }

        val startDt = util.parseStringToDateModern(txtStartDate.text.toString().trim()) ?: LocalDate.now()
        val expDt = util.parseStringToDateModern(txtExpirationDate.text.toString().trim()) ?: startDt.plusYears(1)

        if (!expDt.isAfter(startDt)) {
            Toast.makeText(this, "La fecha de vencimiento debe ser posterior a la fecha de inicio.", Toast.LENGTH_SHORT).show()
            return
        }

        val item = EnsInsurancePolicy(
            id = id,
            name = name,
            firstName = txtFirstName.text.toString().trim(),
            lastName = txtLastName.text.toString().trim(),
            phone = txtPhone.text.toString().toIntOrNull() ?: 0,
            email = txtEmail.text.toString().trim(),
            address = txtAddress.text.toString().trim(),
            policyNum = policy,
            company = spnCompany.selectedItem?.toString() ?: "",
            insuranceType = spnInsuranceType.selectedItem?.toString() ?: "",
            premium = txtPremium.text.toString().toDoubleOrNull() ?: 0.0,
            startDate = startDt,
            expirationDate = expDt
        )

        if (currentId.isNullOrEmpty()) {
            if (EnsViewModel.getById(id) != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            EnsViewModel.save(item)
        } else {
            EnsViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            EnsViewModel.delete(id)
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
