package cr.ac.utn.movil

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.movil.controllers.InsurancePolicyController_ens
import cr.ac.utn.movil.entities.InsurancePolicy
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity_ens : AppCompatActivity() {

    private lateinit var etPolicyNumber_ens: EditText
    private lateinit var spinnerCompany_ens: Spinner
    private lateinit var spinnerType_ens: Spinner
    private lateinit var etStartDate_ens: EditText
    private lateinit var etExpirationDate_ens: EditText
    private lateinit var etPremium_ens: EditText
    private lateinit var btnSave_ens: Button
    private lateinit var btnDelete_ens: Button
    private lateinit var btnView_ens: Button
    private lateinit var tvTitle_ens: TextView

    private lateinit var controller_ens: InsurancePolicyController_ens
    private var isEditMode_ens = false
    private var currentPolicy_ens: InsurancePolicy? = null

    private val calendar_ens = Calendar.getInstance()
    private val dateFormat_ens = SimpleDateFormat("MM/dd/yyyy", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_ens)

        supportActionBar?.apply {
            title = "Insurance policy"
            setDisplayHomeAsUpEnabled(true)
        }

        etPolicyNumber_ens = findViewById(R.id.etPolicyNumber_ens)
        spinnerCompany_ens = findViewById(R.id.spinnerCompany_ens)
        spinnerType_ens = findViewById(R.id.spinnerType_ens)
        etStartDate_ens = findViewById(R.id.etStartDate_ens)
        etExpirationDate_ens = findViewById(R.id.etExpirationDate_ens)
        etPremium_ens = findViewById(R.id.etPremium_ens)
        btnSave_ens = findViewById(R.id.btnSave_ens)
        btnDelete_ens = findViewById(R.id.btnDelete_ens)
        btnView_ens = findViewById(R.id.btnView_ens)
        tvTitle_ens = findViewById(R.id.tvTitle_ens)

        controller_ens = InsurancePolicyController_ens(this)

        val companyAdapter_ens = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            controller_ens.getCompaniesList_ens()
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        spinnerCompany_ens.adapter = companyAdapter_ens

        val typeAdapter_ens = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            controller_ens.getCoverageTypesList_ens()
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        spinnerType_ens.adapter = typeAdapter_ens

        etStartDate_ens.setOnClickListener { showDatePicker_ens(etStartDate_ens) }
        etExpirationDate_ens.setOnClickListener { showDatePicker_ens(etExpirationDate_ens) }

        intent.getStringExtra("POLICY_NUMBER")?.let { number ->
            isEditMode_ens = true
            currentPolicy_ens = controller_ens.getPolicyByNumber_ens(number)
            currentPolicy_ens?.let { fillForm_ens(it) }

            tvTitle_ens.text = getString(R.string.title_edit_policy_ens)
            btnSave_ens.text = getString(R.string.btn_update_policy_ens)
            btnDelete_ens.visibility = Button.VISIBLE
            btnView_ens.visibility = Button.VISIBLE
            etPolicyNumber_ens.isEnabled = false
        }

        btnSave_ens.setOnClickListener { confirmSave_ens() }
        btnDelete_ens.setOnClickListener { confirmDelete_ens() }
        btnView_ens.setOnClickListener { showDetails_ens() }
    }

    private fun showDatePicker_ens(target: EditText) {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar_ens.set(year, month, day)
                target.setText(dateFormat_ens.format(calendar_ens.time))
            },
            calendar_ens.get(Calendar.YEAR),
            calendar_ens.get(Calendar.MONTH),
            calendar_ens.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun fillForm_ens(p: InsurancePolicy) {
        etPolicyNumber_ens.setText(p.policyNumber_ens)
        spinnerCompany_ens.setSelection(controller_ens.getCompaniesList_ens().indexOf(p.company_ens))
        spinnerType_ens.setSelection(controller_ens.getCoverageTypesList_ens().indexOf(p.insuranceType_ens))
        etStartDate_ens.setText(dateFormat_ens.format(p.startDate_ens))
        etExpirationDate_ens.setText(dateFormat_ens.format(p.expirationDate_ens))
        etPremium_ens.setText(p.premium_ens.toString())
    }

    private fun confirmSave_ens() {
        val title = if (isEditMode_ens) getString(R.string.confirm_update_title_ens) else getString(R.string.confirm_save_title_ens)
        val message = if (isEditMode_ens) getString(R.string.confirm_update_message_ens) else getString(R.string.confirm_save_message_ens)

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yes") { _, _ -> savePolicy_ens() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun savePolicy_ens() {
        if (etPolicyNumber_ens.text.isNullOrBlank()) { etPolicyNumber_ens.error = "Required"; etPolicyNumber_ens.requestFocus(); return }
        if (etStartDate_ens.text.isNullOrBlank()) { etStartDate_ens.error = "Required"; etStartDate_ens.requestFocus(); return }
        if (etExpirationDate_ens.text.isNullOrBlank()) { etExpirationDate_ens.error = "Required"; etExpirationDate_ens.requestFocus(); return }
        if (etPremium_ens.text.isNullOrBlank()) { etPremium_ens.error = "Required"; etPremium_ens.requestFocus(); return }

        val startDate: Date
        val expirationDate: Date
        val premiumValue: Double

        try {
            startDate = dateFormat_ens.parse(etStartDate_ens.text.toString())!!
            expirationDate = dateFormat_ens.parse(etExpirationDate_ens.text.toString())!!
        } catch (e: ParseException) {
            Toast.makeText(this, "Invalid date format (MM/dd/yyyy)", Toast.LENGTH_LONG).show()
            return
        }

        if (expirationDate.before(startDate)) {
            Toast.makeText(this, getString(R.string.error_expiration_before_start_ens), Toast.LENGTH_LONG).show()
            return
        }

        try {
            premiumValue = etPremium_ens.text.toString().toDouble()
            if (premiumValue <= 0) { etPremium_ens.error = "Premium must be greater than 0"; etPremium_ens.requestFocus(); return }
        } catch (e: NumberFormatException) {
            etPremium_ens.error = "Invalid amount"; etPremium_ens.requestFocus(); return
        }

        val policy = InsurancePolicy(
            policyNumber_ens = etPolicyNumber_ens.text.toString().trim().uppercase(),
            company_ens = spinnerCompany_ens.selectedItem.toString(),
            insuranceType_ens = spinnerType_ens.selectedItem.toString(),
            startDate_ens = startDate,
            expirationDate_ens = expirationDate,
            premium_ens = premiumValue
        )

        val result = if (isEditMode_ens) {
            controller_ens.updatePolicy_ens(policy)
        } else {
            controller_ens.addPolicy_ens(policy)
        }

        if (result.isSuccess) {
            currentPolicy_ens = policy
            Toast.makeText(this, result.getOrNull() ?: "Operation successful", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, result.exceptionOrNull()?.message ?: "Unexpected error", Toast.LENGTH_LONG).show()
        }
    }

    private fun confirmDelete_ens() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm_delete_title_ens))
            .setMessage(getString(R.string.confirm_delete_message_ens))
            .setPositiveButton("Delete") { _, _ ->
                currentPolicy_ens?.policyNumber_ens?.let { controller_ens.deletePolicy_ens(it) }
                Toast.makeText(this, getString(R.string.success_policy_deleted_ens), Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDetails_ens() {
        val p = currentPolicy_ens ?: return
        val msg = """
            Number: ${p.policyNumber_ens}
            Company: ${p.company_ens}
            Type: ${p.insuranceType_ens}
            Premium: $${p.premium_ens}
            Start: ${dateFormat_ens.format(p.startDate_ens)}
            Expiration: ${dateFormat_ens.format(p.expirationDate_ens)}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.policy_details_title_ens))
            .setMessage(msg)
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}