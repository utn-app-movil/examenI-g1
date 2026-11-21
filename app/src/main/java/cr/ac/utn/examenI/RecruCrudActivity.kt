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
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import java.time.LocalDateTime
import androidx.lifecycle.ViewModelProvider
import cr.ac.utn.examenI.domain.model.Person
import cr.ac.utn.examenI.domain.model.RecruApplication
import cr.ac.utn.examenI.domain.model.RecruRole
import cr.ac.utn.examenI.viewmodel.RecruSaveResult
import cr.ac.utn.examenI.viewmodel.RecruViewModel
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle
import java.util.UUID

class RecruCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtProvince: EditText
    private lateinit var txtCanton: EditText
    private lateinit var txtDistrict: EditText
    private lateinit var txtTargetCompany: EditText
    private lateinit var chkSoftware: CheckBox
    private lateinit var chkInfra: CheckBox
    private lateinit var chkNetworking: CheckBox
    private lateinit var chkSecurity: CheckBox
    private lateinit var chkQA: CheckBox
    private lateinit var txtSalaryExpectation: EditText
    private lateinit var txtExperienceYears: EditText
    private lateinit var txtDateTime: EditText

    private var currentId: String? = null

    private val recruViewModel: RecruViewModel by lazy {
        ViewModelProvider(this)[RecruViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recru_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recru_form_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtId = findViewById(R.id.recru_applicant_id)
        txtName = findViewById(R.id.recru_name)
        txtFirstName = findViewById(R.id.recru_first_name)
        txtLastName = findViewById(R.id.recru_last_name)
        txtPhone = findViewById(R.id.recru_phone)
        txtEmail = findViewById(R.id.recru_email)
        txtAddress = findViewById(R.id.recru_address)
        txtProvince = findViewById(R.id.recru_province)
        txtCanton = findViewById(R.id.recru_canton)
        txtDistrict = findViewById(R.id.recru_district)
        txtTargetCompany = findViewById(R.id.recru_target_company)
        chkSoftware = findViewById(R.id.recru_role_software)
        chkInfra = findViewById(R.id.recru_role_infrastructure)
        chkNetworking = findViewById(R.id.recru_role_networking)
        chkSecurity = findViewById(R.id.recru_role_security)
        chkQA = findViewById(R.id.recru_role_qa)
        txtSalaryExpectation = findViewById(R.id.recru_salary_expectation)
        txtExperienceYears = findViewById(R.id.recru_experience_years)
        txtDateTime = findViewById(R.id.recru_registration_datetime)

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtDateTime.setText(util.formatDateTime(LocalDateTime.now()))
        }

        findViewById<Button>(R.id.recru_btn_save).setOnClickListener { doSave() }
        findViewById<Button>(R.id.recru_btn_delete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.recru_btn_cancel).setOnClickListener { finish() }
    }
    /*se modifica el loadExistingData ya queda funcional para poder buscar y modificar una postulacion*/
    private fun loadExistingData(id: String) {
        val application = recruViewModel.getById(id)
        if (application == null) {
            Toast.makeText(this, R.string.TextDataNoFound, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val person = application.applicant
        txtId.setText(person.id)
        txtId.isEnabled = false
        txtName.setText(person.name)
        txtFirstName.setText(person.firstName)
        txtLastName.setText(person.lastName)
        txtPhone.setText(person.phone.toString())
        txtEmail.setText(person.email)
        txtAddress.setText(person.address)
        txtProvince.setText(application.province)
        txtCanton.setText(application.canton)
        txtDistrict.setText(application.district)
        txtTargetCompany.setText(application.targetCompany)
        chkSoftware.isChecked = RecruRole.SOFTWARE in application.roles
        chkInfra.isChecked = RecruRole.INFRASTRUCTURE in application.roles
        chkNetworking.isChecked = RecruRole.NETWORKING in application.roles
        chkSecurity.isChecked = RecruRole.SECURITY in application.roles
        chkQA.isChecked = RecruRole.QA in application.roles
        txtSalaryExpectation.setText(application.salaryExpectation.toPlainString())
        txtExperienceYears.setText(application.experienceYears.toString())
        txtDateTime.setText(util.formatDateTime(application.registrationDateTime))
    }

    private fun doSave() {
        val applicantId = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val firstName = txtFirstName.text.toString().trim()
        val lastName = txtLastName.text.toString().trim()
        val phone = txtPhone.text.toString().trim().toIntOrNull()
        val email = txtEmail.text.toString().trim()
        val address = txtAddress.text.toString().trim()
        val province = txtProvince.text.toString().trim()
        val canton = txtCanton.text.toString().trim()
        val district = txtDistrict.text.toString().trim()
        val company = txtTargetCompany.text.toString().trim()
        val salary = txtSalaryExpectation.text.toString().trim()
            .replace(',', '.')
            .toBigDecimalOrNull()
        val experienceYears = txtExperienceYears.text.toString().trim().toIntOrNull()

        val selectedRoles = mutableSetOf<RecruRole>()
        if (chkSoftware.isChecked) selectedRoles.add(RecruRole.SOFTWARE)
        if (chkInfra.isChecked) selectedRoles.add(RecruRole.INFRASTRUCTURE)
        if (chkNetworking.isChecked) selectedRoles.add(RecruRole.NETWORKING)
        if (chkSecurity.isChecked) selectedRoles.add(RecruRole.SECURITY)
        if (chkQA.isChecked) selectedRoles.add(RecruRole.QA)

        val dateTimeText = txtDateTime.text.toString().trim()
        val formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")
            .withResolverStyle(ResolverStyle.STRICT)
        val registrationDateTime = try {
            LocalDateTime.parse(dateTimeText, formatter)
        } catch (_: DateTimeParseException) {
            null
        }

        if (
            applicantId.isBlank() || name.isBlank() || firstName.isBlank() ||
            phone == null || phone <= 0 ||
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ||
            province.isBlank() || canton.isBlank() || district.isBlank() ||
            company.isBlank() || selectedRoles.isEmpty() ||
            salary == null || salary.signum() < 0 ||
            experienceYears == null || experienceYears < 0 ||
            registrationDateTime == null || registrationDateTime.isAfter(LocalDateTime.now())
        ) {
            Toast.makeText(this, R.string.TextSaveError, Toast.LENGTH_SHORT).show()
            return
        }

        val existingId = currentId?.takeIf { it.isNotBlank() }
        val application = RecruApplication(
            id = existingId ?: UUID.randomUUID().toString(),
            applicant = Person(
                id = applicantId,
                name = name,
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                email = email,
                address = address
            ),
            province = province,
            canton = canton,
            district = district,
            targetCompany = company,
            roles = selectedRoles.toSet(),
            salaryExpectation = salary,
            experienceYears = experienceYears,
            registrationDateTime = registrationDateTime
        )

        val result = if (existingId == null) {
            recruViewModel.save(application)
        } else {
            recruViewModel.update(application)
        }

        when (result) {
            RecruSaveResult.SUCCESS -> {
                Toast.makeText(this, R.string.TextSaveSuccess, Toast.LENGTH_SHORT).show()
                finish()
            }
            RecruSaveResult.DUPLICATE ->
                Toast.makeText(this, R.string.TextDuplicateData, Toast.LENGTH_SHORT).show()

            RecruSaveResult.NOT_FOUND ->
                Toast.makeText(this, R.string.TextDataNoFound, Toast.LENGTH_SHORT).show()
        }
    }

    private fun doDelete() {
        val applicationId = currentId?.takeIf { it.isNotBlank() } ?: return

        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            if (recruViewModel.delete(applicationId)) {
                Toast.makeText(this, R.string.TextDeleteSuccess, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, R.string.TextDeleteError, Toast.LENGTH_SHORT).show()
            }
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