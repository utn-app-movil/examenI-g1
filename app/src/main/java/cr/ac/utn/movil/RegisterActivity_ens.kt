package cr.ac.utn.movil

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.movil.controllers.InsurancePolicyController_ens
import cr.ac.utn.movil.entities.InsurancePolicy
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity_ens : AppCompatActivity() {

    private lateinit var etPolicyNumber: EditText
    private lateinit var spinnerCompany: AutoCompleteTextView
    private lateinit var spinnerType: AutoCompleteTextView
    private lateinit var etStartDate: EditText
    private lateinit var etExpirationDate: EditText
    private lateinit var etPremium: EditText
    private lateinit var btnSave: Button
    private lateinit var tvTitle: TextView

    private lateinit var controller: InsurancePolicyController_ens
    private var isEditMode = false
    private var currentPolicy: InsurancePolicy? = null

    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_ens)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_ens)!!) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // === Vistas ===
        etPolicyNumber = findViewById(R.id.etPolicyNumber)
        spinnerCompany = findViewById(R.id.spinnerCompany)
        spinnerType = findViewById(R.id.spinnerType)
        etStartDate = findViewById(R.id.etStartDate)
        etExpirationDate = findViewById(R.id.etExpirationDate)
        etPremium = findViewById(R.id.etPremium)
        btnSave = findViewById(R.id.btnSave)
        tvTitle = findViewById(R.id.tvTitle)

        controller = InsurancePolicyController_ens(this)

        setupSpinners()
        setupDatePickers()

        intent.getStringExtra("POLICY_NUMBER")?.let { number ->
            isEditMode = true
            currentPolicy = controller.getPolicyByNumber(number)
            currentPolicy?.let { fillForm(it) }
            tvTitle.text = "Edit Policy"
            btnSave.text = "Update Policy"
            etPolicyNumber.isEnabled = false
        }

        btnSave.setOnClickListener { savePolicy() }
    }

    private fun setupSpinners() {
        spinnerCompany.setAdapter(ArrayAdapter(this,
            android.R.layout.simple_dropdown_item_1line,
            controller.getCompaniesList()))

        spinnerType.setAdapter(ArrayAdapter(this,
            android.R.layout.simple_dropdown_item_1line,
            controller.getCoverageTypesList()))
    }

    private fun setupDatePickers() {
        val listener = DatePickerDialog.OnDateSetListener { _, y, m, d ->
            calendar.set(y, m, d)
            val date = dateFormat.format(calendar.time)
            if (etStartDate.hasFocus()) etStartDate.setText(date)
            else etExpirationDate.setText(date)
        }

        etStartDate.setOnClickListener {
            DatePickerDialog(this, listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        etExpirationDate.setOnClickListener {
            DatePickerDialog(this, listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun fillForm(p: InsurancePolicy) {
        etPolicyNumber.setText(p.policyNumber)
        spinnerCompany.setText(p.company, false)
        spinnerType.setText(p.insuranceType, false)
        etStartDate.setText(dateFormat.format(p.startDate))
        etExpirationDate.setText(dateFormat.format(p.expirationDate))
        etPremium.setText(p.premium.toString())
    }

    private fun savePolicy() {
        if (etPolicyNumber.text.isEmpty() || spinnerCompany.text.isEmpty() ||
            spinnerType.text.isEmpty() || etStartDate.text.isEmpty() ||
            etExpirationDate.text.isEmpty() || etPremium.text.isEmpty()) {
            Toast.makeText(this, "Complete all fields", Toast.LENGTH_LONG).show()
            return
        }

        val premium = etPremium.text.toString().toDoubleOrNull() ?: run {
            Toast.makeText(this, "Invalid premium", Toast.LENGTH_SHORT).show()
            return
        }

        val policy = InsurancePolicy().apply {
            policyNumber = etPolicyNumber.text.toString().trim().uppercase()
            company = spinnerCompany.text.toString()
            insuranceType = spinnerType.text.toString()
            startDate = dateFormat.parse(etStartDate.text.toString())!!
            expirationDate = dateFormat.parse(etExpirationDate.text.toString())!!
            this.premium = premium
        }

        val success = if (isEditMode) controller.updatePolicy(policy).isSuccess
        else controller.addPolicy(policy).isSuccess

        Toast.makeText(this,
            if (success) "Saved successfully!" else "Error saving policy",
            Toast.LENGTH_SHORT).show()

        if (success) finish()
    }
}