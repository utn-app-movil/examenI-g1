package cr.ac.utn.examenI

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import cr.ac.utn.examenI.domain.model.ExchExchangeRate
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.ExchViewModel
import java.time.LocalDateTime

class ExchCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtExchangeRate: EditText
    private lateinit var txtAmountToExchange: EditText
    private lateinit var spnCurrencyType: Spinner
    private lateinit var spnBank: Spinner
    private lateinit var txtTotalAmountToPay: EditText
    private lateinit var txtDateTime: EditText

    private var currentId: String? = null
    private val currencies = arrayOf("USD", "CRC")
    private val banks = arrayOf("BCR", "BNC", "BAC")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exch_crud)
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
        txtExchangeRate = findViewById(R.id.txtExchangeRate)
        txtAmountToExchange = findViewById(R.id.txtAmountToExchange)
        spnCurrencyType = findViewById(R.id.spnCurrencyType)
        spnBank = findViewById(R.id.spnBank)
        txtTotalAmountToPay = findViewById(R.id.txtTotalAmountToPay)
        txtDateTime = findViewById(R.id.txtDateTime)

        val adapterCurr = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapterCurr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnCurrencyType.adapter = adapterCurr

        val adapterBank = ArrayAdapter(this, android.R.layout.simple_spinner_item, banks)
        adapterBank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnBank.adapter = adapterBank

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { calculateTotal() }
            override fun afterTextChanged(s: Editable?) {}
        }
        txtExchangeRate.addTextChangedListener(watcher)
        txtAmountToExchange.addTextChangedListener(watcher)

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtDateTime.setText(util.formatDateTime(LocalDateTime.now()))
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }

    private fun calculateTotal() {
        val rate = txtExchangeRate.text.toString().toDoubleOrNull() ?: 0.0
        val amount = txtAmountToExchange.text.toString().toDoubleOrNull() ?: 0.0
        val total = rate * amount
        txtTotalAmountToPay.setText(total.toString())
    }

    private fun loadExistingData(id: String) {
        val item = ExchViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        txtExchangeRate.setText(item.exchangeRate.toString())
        txtAmountToExchange.setText(item.amountToExchange.toString())
        val posCurr = currencies.indexOf(item.currencyType)
        if (posCurr >= 0) spnCurrencyType.setSelection(posCurr)
        val posBank = banks.indexOf(item.bank)
        if (posBank >= 0) spnBank.setSelection(posBank)
        txtTotalAmountToPay.setText(item.totalAmountToPay.toString())
        txtDateTime.setText(util.formatDateTime(item.exchangeDateTime))
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val rate = txtExchangeRate.text.toString().toDoubleOrNull() ?: 0.0
        val amount = txtAmountToExchange.text.toString().toDoubleOrNull() ?: 0.0
        val dateTimeStr = txtDateTime.text.toString().trim()

        if (id.isEmpty() || name.isEmpty() || rate <= 0.0 || amount <= 0.0) {
            Toast.makeText(this, "Por favor complete los campos requeridos con valores válidos.", Toast.LENGTH_SHORT).show()
            return
        }

        val dt = util.parseStringToDateTimeModern(dateTimeStr)
        if (dt == null || dt.isAfter(LocalDateTime.now())) {
            Toast.makeText(this, "La fecha debe ser menor o igual a la actual.", Toast.LENGTH_SHORT).show()
            return
        }

        val total = rate * amount

        val item = ExchExchangeRate(
            id = id,
            name = name,
            firstName = txtFirstName.text.toString().trim(),
            lastName = txtLastName.text.toString().trim(),
            phone = txtPhone.text.toString().toIntOrNull() ?: 0,
            email = txtEmail.text.toString().trim(),
            address = txtAddress.text.toString().trim(),
            exchangeRate = rate,
            amountToExchange = amount,
            currencyType = spnCurrencyType.selectedItem?.toString() ?: "",
            bank = spnBank.selectedItem?.toString() ?: "",
            totalAmountToPay = total,
            exchangeDateTime = dt
        )

        if (currentId.isNullOrEmpty()) {
            if (ExchViewModel.getById(id) != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            ExchViewModel.save(item)
        } else {
            ExchViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            ExchViewModel.delete(id)
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
