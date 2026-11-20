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
import cr.ac.utn.examenI.domain.model.PayPayroll
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.PayViewModel
import java.time.LocalDate

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

        txtId = findViewById(R.id.pay_txtId)
        txtName = findViewById(R.id.pay_txtName)
        txtFirstName = findViewById(R.id.pay_txtFirstName)
        txtLastName = findViewById(R.id.pay_txtLastName)
        txtPhone = findViewById(R.id.pay_txtPhone)
        txtEmail = findViewById(R.id.pay_txtEmail)
        txtAddress = findViewById(R.id.pay_txtAddress)
        txtEmployeeNum = findViewById(R.id.pay_txtEmployeeNum)
        txtPosition = findViewById(R.id.pay_txtPosition)
        txtSalary = findViewById(R.id.pay_txtSalary)
        txtIban = findViewById(R.id.pay_txtIban)
        txtMonth = findViewById(R.id.pay_txtMonth)
        spnBank = findViewById(R.id.pay_spnBank)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, banks)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnBank.adapter = adapter

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtMonth.setText(LocalDate.now().monthValue.toString())
        }

        findViewById<Button>(R.id.pay_btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.pay_btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.pay_btnCancel).setOnClickListener { finish() }
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
        val empNum = txtEmployeeNum.text.toString().trim()
        val ibanStr = txtIban.text.toString().trim().uppercase()
        val monthVal = txtMonth.text.toString().toIntOrNull() ?: 1

        if (id.isEmpty() || name.isEmpty() || empNum.isEmpty() || ibanStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos requeridos.", Toast.LENGTH_SHORT).show()
            return
        }

        val ibanRegex = Regex("""^[A-Z]{2}\d{15,28}$""")
        if (!ibanRegex.matches(ibanStr)) {
            Toast.makeText(this, "Formato de IBAN inválido (debe iniciar con código de país y dígitos).", Toast.LENGTH_SHORT).show()
            return
        }

        val currentMonth = LocalDate.now().monthValue
        if (monthVal < 1 || monthVal > currentMonth) {
            Toast.makeText(this, "El mes debe ser menor o igual al mes actual ($currentMonth).", Toast.LENGTH_SHORT).show()
            return
        }

        val item = PayPayroll(
            id = id,
            name = name,
            firstName = txtFirstName.text.toString().trim(),
            lastName = txtLastName.text.toString().trim(),
            phone = txtPhone.text.toString().toIntOrNull() ?: 0,
            email = txtEmail.text.toString().trim(),
            address = txtAddress.text.toString().trim(),
            employeeNum = empNum,
            position = txtPosition.text.toString().trim(),
            salary = txtSalary.text.toString().toDoubleOrNull() ?: 0.0,
            iban = ibanStr,
            month = monthVal,
            bank = spnBank.selectedItem?.toString() ?: ""
        )

        if (currentId.isNullOrEmpty()) {
            if (PayViewModel.getById(id) != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            PayViewModel.save(item)
        } else {
            PayViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
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
