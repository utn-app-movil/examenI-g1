package cr.ac.utn.examenI

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.examenI.domain.model.ExchTransaction
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.ExchViewModel
import java.time.LocalDateTime
import java.util.Locale

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
    private lateinit var btnDelete: Button

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
        btnDelete = findViewById(R.id.btnDelete)

        txtDateTime.isFocusable = false
        txtDateTime.isClickable = true
        txtDateTime.setOnClickListener { showDateTimePicker() }

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
            btnDelete.visibility = View.GONE
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        btnDelete.setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }

    private fun showDateTimePicker() {
        val current = util.parseStringToDateTimeModern(txtDateTime.text.toString().trim()) ?: LocalDateTime.now()

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val timePicker = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        val selected = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute)
                        val now = LocalDateTime.now()
                        if (selected.isAfter(now)) {
                            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
                        } else {
                            txtDateTime.setText(util.formatDateTime(selected))
                        }
                    },
                    current.hour,
                    current.minute,
                    true
                )
                timePicker.show()
            },
            current.year,
            current.monthValue - 1,
            current.dayOfMonth
        )
        datePicker.datePicker.maxDate = System.currentTimeMillis()
        datePicker.show()
    }

    private fun calculateTotal() {
        val rate = txtExchangeRate.text.toString().toDoubleOrNull() ?: 0.0
        val amount = txtAmountToExchange.text.toString().toDoubleOrNull() ?: 0.0
        if (rate > 0.0 && amount > 0.0) {
            val total = rate * amount
            txtTotalAmountToPay.setText(String.format(Locale.US, "%.2f", total))
        } else {
            txtTotalAmountToPay.setText("")
        }
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
        txtTotalAmountToPay.setText(String.format(Locale.US, "%.2f", item.totalAmountToPay))
        txtDateTime.setText(util.formatDateTime(item.exchangeDateTime))
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val firstName = txtFirstName.text.toString().trim()
        val lastName = txtLastName.text.toString().trim()
        val phone = txtPhone.text.toString().toIntOrNull() ?: 0
        val email = txtEmail.text.toString().trim()
        val address = txtAddress.text.toString().trim()
        val rate = txtExchangeRate.text.toString().toDoubleOrNull() ?: 0.0
        val amount = txtAmountToExchange.text.toString().toDoubleOrNull() ?: 0.0
        val currency = spnCurrencyType.selectedItem?.toString() ?: "USD"
        val bank = spnBank.selectedItem?.toString() ?: "BCR"
        val dateTimeStr = txtDateTime.text.toString().trim()

        if (id.isEmpty() || name.isEmpty() || rate <= 0.0 || amount <= 0.0) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val dt = util.parseStringToDateTimeModern(dateTimeStr)
        if (dt == null || dt.isAfter(LocalDateTime.now())) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val total = rate * amount
        val transaction = ExchTransaction(
            id = id,
            name = name,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            email = email,
            address = address,
            currencyType = currency,
            exchangeRate = rate,
            amountToExchange = amount,
            totalAmountToPay = total,
            bank = bank,
            exchangeDateTime = dt
        )

        if (currentId.isNullOrEmpty()) {
            val saved = ExchViewModel.save(transaction)
            if (!saved) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            ExchViewModel.update(transaction)
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
        if (currentId.isNullOrEmpty()) {
            menu?.findItem(R.id.mnu_delete)?.isVisible = false
        }
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
