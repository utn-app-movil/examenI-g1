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
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import java.time.LocalDate
import android.util.Patterns
import cr.ac.utn.examenI.domain.model.PayEmployee
import cr.ac.utn.examenI.viewmodel.PayViewModel
class PayCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtEmployeeNum: EditText
    private lateinit var txtPosition: EditText
    private lateinit var txtSalary: EditText
    private lateinit var txtIban: EditText
    private lateinit var txtMonth: EditText
    private lateinit var spnBank: Spinner

    private var currentId: String? = null
    private val banks = arrayOf("BAC", "Banco Nacional", "Banco de Costa Rica")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pay_crud)
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
        txtEmployeeNum = findViewById(R.id.txtEmployeeNum)
        txtPosition = findViewById(R.id.txtPosition)
        txtSalary = findViewById(R.id.txtSalary)
        txtIban = findViewById(R.id.txtIban)
        txtMonth = findViewById(R.id.txtMonth)
        spnBank = findViewById(R.id.spnBank)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, banks)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnBank.adapter = adapter

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtMonth.setText(LocalDate.now().monthValue.toString())
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        val item = PayViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        txtEmployeeNum.setText(item.employeeNum)
        txtPosition.setText(item.position)
        txtSalary.setText(item.salary.toString())
        txtIban.setText(item.iban)
        txtMonth.setText(item.month.toString())
        val pos = banks.indexOf(item.bank)
        if (pos >= 0) spnBank.setSelection(pos)
    }


    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val firstName = txtFirstName.text.toString().trim()
        val lastName = txtLastName.text.toString().trim()
        val phoneText = txtPhone.text.toString().trim()
        val email = txtEmail.text.toString().trim()
        val address = txtAddress.text.toString().trim()
        val employeeNum = txtEmployeeNum.text.toString().trim()
        val position = txtPosition.text.toString().trim()
        val salary = txtSalary.text.toString().trim().toDoubleOrNull()
        val iban = txtIban.text.toString().trim().uppercase()
        val month = txtMonth.text.toString().trim().toIntOrNull()
        val bank = spnBank.selectedItem?.toString()

        val errorMessage = getString(R.string.TextSaveError)

        val requiredFields = listOf(
            txtId, txtName, txtFirstName, txtLastName,
            txtPhone, txtEmail, txtAddress,
            txtEmployeeNum, txtPosition,
            txtSalary, txtIban, txtMonth
        )

        for (field in requiredFields) {
            if (field.text.toString().isBlank()) {
                field.error = errorMessage
                field.requestFocus()
                return
            }
        }

        val phone = phoneText.toIntOrNull()
        if (phone == null || phoneText.length != 8) {
            txtPhone.error = errorMessage
            txtPhone.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.error = errorMessage
            txtEmail.requestFocus()
            return
        }

        if (salary == null || !salary.isFinite() || salary <= 0.0) {
            txtSalary.error = errorMessage
            txtSalary.requestFocus()
            return
        }

        if (month == null ||
            month !in 1..LocalDate.now().monthValue) {
            txtMonth.error = errorMessage
            txtMonth.requestFocus()
            return
        }

        if (!isValidPayIban(iban)) {
            txtIban.error = errorMessage
            txtIban.requestFocus()
            return
        }

        if (bank.isNullOrBlank()) {
            Toast.makeText(
                this, errorMessage, Toast.LENGTH_SHORT
            ).show()
            return
        }

        val employee = PayEmployee(
            id = id,
            name = name,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            email = email,
            address = address,
            employeeNum = employeeNum,
            position = position,
            salary = salary,
            iban = iban,
            month = month,
            bank = bank
        )

        val saved = if (currentId.isNullOrEmpty()) {
            PayViewModel.save(employee)
        } else {
            if (currentId != id) {
                false
            } else {
                PayViewModel.update(employee)
            }
        }

        val message = when {
            saved -> R.string.TextSaveSuccess
            currentId.isNullOrEmpty() ->
                R.string.TextDuplicateData
            else -> R.string.TextSaveError
        }

        Toast.makeText(
            this, getString(message), Toast.LENGTH_SHORT
        ).show()

        if (saved) finish()
    }

    private fun isValidPayIban(iban: String): Boolean {
        if (!Regex("""^CR\d{20}$""").matches(iban)) {
            return false
        }

        val rearranged = iban.substring(4) + iban.substring(0, 4)
        var remainder = 0

        for (character in rearranged) {
            val digits = if (character.isLetter()) {
                (character - 'A' + 10).toString()
            } else {
                character.toString()
            }

            for (digit in digits) {
                remainder = (remainder * 10 + digit.digitToInt()) % 97
            }
        }

        return remainder == 1
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            PayViewModel.delete(id)
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
