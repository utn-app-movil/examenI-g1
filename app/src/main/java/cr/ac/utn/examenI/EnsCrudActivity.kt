
package cr.ac.utn.examenI

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cr.ac.utn.examenI.domain.model.ens_InsurancePolicy
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.ens_InsuranceViewModel
import java.time.LocalDate

class EnsCrudActivity : AppCompatActivity() {

    private lateinit var txtPolicyNum: EditText
    private lateinit var spnCompany: Spinner
    private lateinit var spnInsuranceType: Spinner
    private lateinit var txtPremium: EditText
    private lateinit var txtStartDate: EditText
    private lateinit var txtExpirationDate: EditText

    private lateinit var viewModel: ens_InsuranceViewModel

    private var currentId: String? = null

    private val companies = arrayOf(
        "INS",
        "ASSA",
        "MAPFRE",
        "BMI"
    )

    private val insuranceTypes = arrayOf(
        "Civil Liability",
        "Fire Insurance",
        "Workers Compensation",
        "Fleet Insurance"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ens_crud)

        viewModel = ViewModelProvider(this).get(
            ens_InsuranceViewModel::class.java
        )

        txtPolicyNum = findViewById(R.id.txtPolicyNum)
        spnCompany = findViewById(R.id.spnCompany)
        spnInsuranceType = findViewById(R.id.spnInsuranceType)
        txtPremium = findViewById(R.id.txtPremium)
        txtStartDate = findViewById(R.id.txtStartDate)
        txtExpirationDate = findViewById(R.id.txtExpirationDate)

        setupSpinners()
        setupDatePickers()

        currentId = intent.getStringExtra(EXTRA_ID)

        if (currentId.isNullOrEmpty()) {
            txtStartDate.setText(
                LocalDate.now().toString()
            )

            txtExpirationDate.setText(
                LocalDate.now().plusYears(1).toString()
            )

            findViewById<Button>(R.id.btnDelete)
                .isEnabled = false
        } else {
            loadExistingData(currentId!!)
        }

        findViewById<Button>(R.id.btnSave)
            .setOnClickListener {
                doSave()
            }

        findViewById<Button>(R.id.btnDelete)
            .setOnClickListener {
                doDelete()
            }

        findViewById<Button>(R.id.btnCancel)
            .setOnClickListener {
                finish()
            }
    }

    private fun setupSpinners() {

        val companyAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            companies
        )

        companyAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spnCompany.adapter = companyAdapter

        val typeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            insuranceTypes
        )

        typeAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spnInsuranceType.adapter = typeAdapter
    }

    private fun setupDatePickers() {

        txtStartDate.setOnClickListener {
            showDatePicker(txtStartDate)
        }

        txtExpirationDate.setOnClickListener {
            showDatePicker(txtExpirationDate)
        }
    }

    private fun showDatePicker(field: EditText) {

        val date = try {
            LocalDate.parse(field.text.toString())
        } catch (e: Exception) {
            LocalDate.now()
        }

        DatePickerDialog(
            this,
            { _, year, month, day ->

                val selectedDate = LocalDate.of(
                    year,
                    month + 1,
                    day
                )

                field.setText(selectedDate.toString())
            },
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        ).show()
    }

    private fun loadExistingData(id: String) {

        val policy = viewModel.getPolicyById(id)

        if (policy == null) {
            Toast.makeText(
                this,
                getString(R.string.TextDataNoFound),
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        txtPolicyNum.setText(policy.id)
        txtPolicyNum.isEnabled = false

        val companyPosition = companies.indexOf(
            policy.company
        )

        if (companyPosition >= 0) {
            spnCompany.setSelection(companyPosition)
        }

        val typePosition = insuranceTypes.indexOf(
            policy.insuranceType
        )

        if (typePosition >= 0) {
            spnInsuranceType.setSelection(typePosition)
        }

        txtStartDate.setText(policy.startDate)

        txtExpirationDate.setText(
            policy.expirationDate
        )

        txtPremium.setText(
            policy.premium.toString()
        )
    }

    private fun doSave() {

        val policyNumber = txtPolicyNum.text
            .toString()
            .trim()

        val company = spnCompany.selectedItem.toString()

        val insuranceType = spnInsuranceType
            .selectedItem.toString()

        val startDate = txtStartDate.text
            .toString()
            .trim()

        val expirationDate = txtExpirationDate.text
            .toString()
            .trim()

        val premium = txtPremium.text
            .toString()
            .trim()
            .toDoubleOrNull()

        if (policyNumber.isEmpty()) {
            txtPolicyNum.error = getString(
                R.string.TextSaveError
            )
            return
        }

        if (premium == null) {
            txtPremium.error = getString(
                R.string.TextSaveError
            )
            return
        }

        val policy = ens_InsurancePolicy(
            id = policyNumber,
            company = company,
            insuranceType = insuranceType,
            startDate = startDate,
            expirationDate = expirationDate,
            premium = premium
        )

        val result = if (currentId.isNullOrEmpty()) {
            viewModel.addPolicy(policy)
        } else {
            viewModel.updatePolicy(policy)
        }

        val messageId = when (result) {

            ens_InsuranceViewModel.SaveResult.SUCCESS ->
                R.string.TextSaveSuccess

            ens_InsuranceViewModel.SaveResult.DUPLICATE ->
                R.string.TextDuplicateData

            ens_InsuranceViewModel.SaveResult.NOT_FOUND ->
                R.string.TextDataNoFound

            ens_InsuranceViewModel.SaveResult.INVALID_DATA ->
                R.string.TextSaveError
        }

        Toast.makeText(
            this,
            getString(messageId),
            Toast.LENGTH_LONG
        ).show()

        if (result == ens_InsuranceViewModel.SaveResult.SUCCESS) {
            finish()
        }
    }

    private fun doDelete() {

        val id = currentId ?: return

        util.showDialogCondition(
            this,
            getString(R.string.TextDeleteQuestion)
        ) {

            val deleted = viewModel.deletePolicy(id)

            if (deleted) {
                Toast.makeText(
                    this,
                    getString(R.string.TextDeleteSuccess),
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.TextDataNoFound),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.mnu_save -> {
                doSave()
                true
            }

            R.id.mnu_delete -> {
                doDelete()
                true
            }

            R.id.mnu_cancel -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
