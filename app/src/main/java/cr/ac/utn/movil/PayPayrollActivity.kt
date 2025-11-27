package cr.ac.utn.movil

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.movil.controller.PayPayrollController
import cr.ac.utn.movil.identities.PayPayroll
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class PayPayrollActivity : AppCompatActivity() {

    private lateinit var payEtName: EditText
    private lateinit var payEtFirstLastName: EditText
    private lateinit var payEtSecondLastName: EditText
    private lateinit var payEtPhone: EditText
    private lateinit var payEtEmail: EditText
    private lateinit var payEtAddress: EditText
    private lateinit var payEtCountry: EditText

    private lateinit var payEtEmployeeNumber: EditText
    private lateinit var payEtPosition: EditText
    private lateinit var payEtSalary: EditText
    private lateinit var payEtIban: EditText
    private lateinit var paySpMonth: Spinner
    private lateinit var paySpBank: Spinner

    private lateinit var payBtnAdd: Button
    private lateinit var payBtnUpdate: Button
    private lateinit var payBtnDelete: Button
    private lateinit var payBtnClear: Button

    private lateinit var payLvPayroll: ListView

    private val controller = PayPayrollController()
    private var selectedPayrollId: String? = null

    private lateinit var listAdapter: ArrayAdapter<String>
    private val listDisplay = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pay_activity_payroll)

        initViews()
        initSpinners()
        initList()
        initButtons()
        refreshList()
    }

    private fun initViews() {
        payEtName = findViewById(R.id.pay_etName)
        payEtFirstLastName = findViewById(R.id.pay_etFirstLastName)
        payEtSecondLastName = findViewById(R.id.pay_etSecondLastName)
        payEtPhone = findViewById(R.id.pay_etPhone)
        payEtEmail = findViewById(R.id.pay_etEmail)
        payEtAddress = findViewById(R.id.pay_etAddress)
        payEtCountry = findViewById(R.id.pay_etCountry)

        payEtEmployeeNumber = findViewById(R.id.pay_etEmployeeNumber)
        payEtPosition = findViewById(R.id.pay_etPosition)
        payEtSalary = findViewById(R.id.pay_etSalary)
        payEtIban = findViewById(R.id.pay_etIban)
        paySpMonth = findViewById(R.id.pay_spMonth)
        paySpBank = findViewById(R.id.pay_spBank)

        payBtnAdd = findViewById(R.id.pay_btnAdd)
        payBtnUpdate = findViewById(R.id.pay_btnUpdate)
        payBtnDelete = findViewById(R.id.pay_btnDelete)
        payBtnClear = findViewById(R.id.pay_btnClear)

        payLvPayroll = findViewById(R.id.pay_lvPayroll)
    }

    private fun initSpinners() {
        ArrayAdapter.createFromResource(
            this,
            R.array.pay_months_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            paySpMonth.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.pay_banks_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            paySpBank.adapter = adapter
        }
    }

    private fun initButtons() {
        payBtnAdd.setOnClickListener { handleSave() }
        payBtnUpdate.setOnClickListener { handleSave() }
        payBtnDelete.setOnClickListener { handleDelete() }
        payBtnClear.setOnClickListener { clearForm() }
    }

    private fun initList() {
        listAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listDisplay
        )
        payLvPayroll.adapter = listAdapter

        payLvPayroll.setOnItemClickListener { _, _, position, _ ->
            val all = controller.getAll()
            if (position in all.indices) {
                val payroll = all[position]
                selectedPayrollId = payroll.ID
                loadPayrollToForm(payroll)
            }
        }
    }

    // ---------  CRUD menu  ---------

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                handleSave()
                true
            }
            R.id.mnu_delete -> {
                handleDelete()
                true
            }
            R.id.mnu_cancel -> {
                clearForm()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // --------- Actions ---------

    private fun handleSave() {
        val existingId = selectedPayrollId
        val payroll = buildPayrollFromForm(existingId) ?: return

        val questionText = getString(R.string.TextSaveActionQuestion)
        showQuestionDialog(questionText) {
            try {
                if (existingId == null) {
                    controller.add(payroll)
                } else {
                    controller.update(payroll)
                }
                Toast.makeText(this, getString(R.string.MsgSaveSuccess), Toast.LENGTH_SHORT).show()
                clearForm()
                refreshList()
            } catch (ex: Exception) {
                Toast.makeText(this, getString(R.string.MsgDuplicateDate), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleDelete() {
        val id = selectedPayrollId
        if (id.isNullOrBlank()) {
            Toast.makeText(this, getString(R.string.MsgDataNoFound), Toast.LENGTH_SHORT).show()
            return
        }

        val questionText = getString(R.string.TextDeleteActionQuestion)
        showQuestionDialog(questionText) {
            controller.remove(id)
            Toast.makeText(this, getString(R.string.MsgDeleteSuccess), Toast.LENGTH_SHORT).show()
            clearForm()
            refreshList()
        }
    }

    private fun refreshList() {
        listDisplay.clear()
        val all = controller.getAll()

        all.forEach { payroll ->
            val fullName = "${payroll.Name} ${payroll.FLastName} ${payroll.SLastName}"
            val text = "${payroll.EmployeeNumber} - $fullName - ${payroll.SalaryAmount}"
            listDisplay.add(text)
        }

        listAdapter.notifyDataSetChanged()
    }

    private fun clearForm() {
        selectedPayrollId = null

        payEtName.text.clear()
        payEtFirstLastName.text.clear()
        payEtSecondLastName.text.clear()
        payEtPhone.text.clear()
        payEtEmail.text.clear()
        payEtAddress.text.clear()
        payEtCountry.text.clear()

        payEtEmployeeNumber.text.clear()
        payEtPosition.text.clear()
        payEtSalary.text.clear()
        payEtIban.text.clear()
        paySpMonth.setSelection(0)
        paySpBank.setSelection(0)
    }

    private fun loadPayrollToForm(payroll: PayPayroll) {
        payEtName.setText(payroll.Name)
        payEtFirstLastName.setText(payroll.FLastName)
        payEtSecondLastName.setText(payroll.SLastName)
        payEtPhone.setText(payroll.Phone.toString())
        payEtEmail.setText(payroll.Email)
        payEtAddress.setText(payroll.Address)
        payEtCountry.setText(payroll.Country)

        payEtEmployeeNumber.setText(payroll.EmployeeNumber)
        payEtPosition.setText(payroll.JobPosition)
        payEtSalary.setText(payroll.SalaryAmount.toString())
        payEtIban.setText(payroll.IbanAccount)

        val monthIndex = (payroll.PaymentMonth - 1).coerceIn(0, 11)
        paySpMonth.setSelection(monthIndex)

        val banks = resources.getStringArray(R.array.pay_banks_array)
        val bankIndex = banks.indexOfFirst { it.equals(payroll.BankName, ignoreCase = true) }
            .coerceAtLeast(0)
        paySpBank.setSelection(bankIndex)
    }

    // --------- Helpers / Validations ---------

    private fun buildPayrollFromForm(existingId: String?): PayPayroll? {
        val name = payEtName.text.toString().trim()
        val firstLastName = payEtFirstLastName.text.toString().trim()
        val secondLastName = payEtSecondLastName.text.toString().trim()
        val phoneText = payEtPhone.text.toString().trim()
        val email = payEtEmail.text.toString().trim()
        val address = payEtAddress.text.toString().trim()
        val country = payEtCountry.text.toString().trim()

        val employeeNumber = payEtEmployeeNumber.text.toString().trim()
        val position = payEtPosition.text.toString().trim()
        val salaryText = payEtSalary.text.toString().trim()
        val ibanRaw = payEtIban.text.toString().trim()
        val monthIndex = paySpMonth.selectedItemPosition
        val bank = paySpBank.selectedItem?.toString()?.trim().orEmpty()

        if (name.isEmpty() || firstLastName.isEmpty() || secondLastName.isEmpty() ||
            phoneText.isEmpty() || email.isEmpty() || address.isEmpty() || country.isEmpty() ||
            employeeNumber.isEmpty() || position.isEmpty() || salaryText.isEmpty() ||
            ibanRaw.isEmpty() || bank.isEmpty()
        ) {
            Toast.makeText(this, getString(R.string.pay_error_empty_fields), Toast.LENGTH_SHORT).show()
            return null
        }

        val phone = phoneText.toIntOrNull()
        if (phone == null || phone <= 0) {
            Toast.makeText(this, getString(R.string.pay_error_invalid_phone), Toast.LENGTH_SHORT).show()
            return null
        }

        val salary = salaryText.toDoubleOrNull()
        if (salary == null || salary <= 0) {
            Toast.makeText(this, getString(R.string.pay_error_invalid_salary), Toast.LENGTH_SHORT).show()
            return null
        }

        val iban = ibanRaw.replace(" ", "").uppercase(Locale.getDefault())
        if (!isValidCostaRicaIban(iban)) {
            Toast.makeText(this, getString(R.string.pay_error_invalid_iban), Toast.LENGTH_SHORT).show()
            return null
        }

        if (!isValidMonthIndex(monthIndex)) {
            Toast.makeText(this, getString(R.string.pay_error_invalid_month), Toast.LENGTH_SHORT).show()
            return null
        }

        val monthNumber = monthIndex + 1

        val payroll = PayPayroll()
        payroll.ID = existingId ?: UUID.randomUUID().toString()

        payroll.Name = name
        payroll.FLastName = firstLastName
        payroll.SLastName = secondLastName
        payroll.Phone = phone
        payroll.Email = email
        payroll.Address = address
        payroll.Country = country

        payroll.EmployeeNumber = employeeNumber
        payroll.JobPosition = position
        payroll.SalaryAmount = salary
        payroll.IbanAccount = iban
        payroll.PaymentMonth = monthNumber
        payroll.BankName = bank

        return payroll
    }

    private fun isValidCostaRicaIban(iban: String): Boolean {
        // official format CR + 20 digits (22 characters)
        val regex = Regex("^CR\\d{20}$")
        return regex.matches(iban)
    }

    private fun isValidMonthIndex(monthIndex: Int): Boolean {
        val calendar = Calendar.getInstance()
        val currentMonthIndex = calendar.get(Calendar.MONTH) // 0..11
        // Month not later than the current one
        return monthIndex in 0..currentMonthIndex
    }

    private fun showQuestionDialog(questionText: String, callback: () -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.TextTitleDialogQuestion))
            .setMessage(questionText)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.TextYes)) { dialog, _ ->
                dialog.dismiss()
                callback()
            }
            .setNegativeButton(getString(R.string.TextNo)) { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }
}