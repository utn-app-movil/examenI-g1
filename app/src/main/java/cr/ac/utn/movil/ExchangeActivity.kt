package cr.ac.utn.movil.exchange

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.movil.R
import cr.ac.utn.movil.controller.ExchangeController
import cr.ac.utn.movil.identities.Exchange
import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ExchangeActivity : AppCompatActivity() {

    private val controller = ExchangeController()
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange)

        // PERSON DATA
        val etName = findViewById<EditText>(R.id.exch_txtName)
        val etLast1 = findViewById<EditText>(R.id.exch_txtFirstLastName)
        val etLast2 = findViewById<EditText>(R.id.exch_txtSecondLastName)
        val etPhone = findViewById<EditText>(R.id.exch_txtPhone)
        val etEmail = findViewById<EditText>(R.id.exch_txtEmail)
        val etAddress = findViewById<EditText>(R.id.exch_txtAddress)
        val etCountry = findViewById<EditText>(R.id.exch_txtCountry)

        // DATE - TIME
        val etDate = findViewById<EditText>(R.id.exch_txtDate)
        val etTime = findViewById<EditText>(R.id.exch_txtTime)

        // AMOUNT & RATE
        val etAmount = findViewById<EditText>(R.id.exch_txtAmount)
        val etRate = findViewById<EditText>(R.id.exch_txtRate)

        // SPINNERS
        val spType = findViewById<Spinner>(R.id.exch_spType)
        val spBank = findViewById<Spinner>(R.id.exch_spBank)

        // RESULT
        val tvResult = findViewById<TextView>(R.id.exch_lblResult)

        // BUTTONS
        val btnCreate = findViewById<Button>(R.id.exch_btnCreate)
        val btnUpdate = findViewById<Button>(R.id.exch_btnUpdate)
        val btnDelete = findViewById<Button>(R.id.exch_btnDelete)
        val btnCalculate = findViewById<Button>(R.id.exch_btnCalculate)

        // Spinner type values
        val types = listOf(
            getString(R.string.exch_type_dollar_to_crc),
            getString(R.string.exch_type_crc_to_dollar)
        )
        spType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)

        // Banks
        val banks = resources.getStringArray(R.array.exch_bank_list).toList()
        spBank.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, banks)

        // Date Picker
        etDate.isFocusable = false
        etDate.setOnClickListener {
            val now = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, y, m, d -> etDate.setText("${y}-${m + 1}-${d}") },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Time Picker
        etTime.isFocusable = false
        etTime.setOnClickListener {
            val now = Calendar.getInstance()
            TimePickerDialog(
                this,
                { _, h, min -> etTime.setText(String.format("%02d:%02d", h, min)) },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
            ).show()
        }

        // CALCULATE
        btnCalculate.setOnClickListener {
            val amount = etAmount.text.toString().toDoubleOrNull()
            val rate = etRate.text.toString().toDoubleOrNull()
            val type = spType.selectedItemPosition

            if (amount == null || rate == null) {
                Toast.makeText(this, getString(R.string.exch_msg_fill_fields), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val result = if (type == 0) amount * rate else amount / rate
            tvResult.text = result.toString()
        }

        // CREATE
        btnCreate.setOnClickListener {
            if (!validateFields(etName, etLast1, etAmount, etRate, etDate, etTime)) return@setOnClickListener

            // Build datetime from two fields
            val date = LocalDate.parse(etDate.text.toString(), dateFormatter)
            val time = LocalTime.parse(etTime.text.toString(), timeFormatter)
            val dateTime = LocalDateTime.of(date, time)

            val entity = Exchange().apply {
                personName = etName.text.toString().trim()
                personFLastName = etLast1.text.toString().trim()
                personSLastName = etLast2.text.toString().trim()
                phone = etPhone.text.toString().toIntOrNull() ?: 0
                email = etEmail.text.toString().trim()
                address = etAddress.text.toString().trim()
                country = etCountry.text.toString().trim()
                exchangeType = spType.selectedItem.toString()
                rate = etRate.text.toString().toDoubleOrNull() ?: 0.0
                amount = etAmount.text.toString().toDoubleOrNull() ?: 0.0
                bankEntity = spBank.selectedItem.toString()
                this.dateTime = dateTime
                ID = UUID.randomUUID().toString()
            }

            if (entity.dateTime.isAfter(LocalDateTime.now())) {
                Toast.makeText(this, getString(R.string.exch_msg_invalid_date), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val ok = controller.addExchange(entity)
            if (!ok) {
                Toast.makeText(this, getString(R.string.exch_msg_duplicate), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.exch_msg_saved), Toast.LENGTH_LONG).show()
                clearFields(
                    etName, etLast1, etLast2, etPhone, etEmail,
                    etAddress, etCountry, etAmount, etRate, etDate, etTime, tvResult
                )
            }
        }

        btnUpdate.setOnClickListener {
            Toast.makeText(this, getString(R.string.exch_msg_not_found), Toast.LENGTH_LONG).show()
        }

        btnDelete.setOnClickListener {
            val id = etEmail.text.toString().trim()

            if (id.isEmpty()) {
                Toast.makeText(this, getString(R.string.exch_msg_fill_fields), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val ok = controller.deleteExchange(id)

            if (!ok) {
                Toast.makeText(this, getString(R.string.exch_msg_not_found), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.exch_msg_deleted), Toast.LENGTH_LONG).show()
                clearFields(
                    etName, etLast1, etLast2, etPhone, etEmail,
                    etAddress, etCountry, etAmount, etRate, etDate, etTime, tvResult
                )
            }
        }
    }

    private fun validateFields(
        name: EditText,
        last1: EditText,
        amount: EditText,
        rate: EditText,
        date: EditText,
        time: EditText
    ): Boolean {
        var ok = true

        listOf(name, last1, amount, rate, date, time).forEach { field ->
            if (field.text.toString().trim().isEmpty()) {
                field.error = getString(R.string.exch_msg_required)
                ok = false
            }
        }

        return ok
    }

    private fun clearFields(
        vararg fields: Any
    ) {
        fields.forEach {
            when (it) {
                is EditText -> it.text.clear()
                is TextView -> it.text = ""
            }
        }
    }
}

