package cr.ac.utn.movil

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import cr.ac.utn.movil.controller.InventoryController
import cr.ac.utn.movil.identities.InventoryEntry
import cr.ac.utn.movil.identities.Person
import cr.ac.utn.movil.util.EXTRA_ID
import cr.ac.utn.movil.util.util
import java.time.LocalDateTime
import java.util.Calendar
import java.util.UUID
import kotlin.apply
import kotlin.collections.indexOf
import kotlin.let
import kotlin.text.format
import kotlin.text.isEmpty
import kotlin.text.toDouble
import kotlin.text.toDoubleOrNull
import kotlin.text.toInt
import kotlin.text.toIntOrNull
import kotlin.text.trim
import kotlin.toString

class InventoryDetailActivity : AppCompatActivity() {

    private lateinit var etPersonId: TextInputEditText
    private lateinit var etPersonName: TextInputEditText
    private lateinit var etPersonEmail: TextInputEditText
    private lateinit var etProductCode: TextInputEditText
    private lateinit var etProductName: TextInputEditText
    private lateinit var etQuantity: TextInputEditText
    private lateinit var etDate: TextInputEditText
    private lateinit var etTime: TextInputEditText
    private lateinit var spinnerSupplier: Spinner
    private lateinit var etUnitCost: TextInputEditText

    private val controller = InventoryController()
    private var currentEntry: InventoryEntry? = null
    private var selectedDateTime: LocalDateTime? = null
    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0
    private var selectedHour = 0
    private var selectedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inv_detail)

        initializeViews()
        setupSupplierSpinner()
        setupDateTimePickers()
        loadEntryData()
    }

    private fun initializeViews() {
        etPersonId = findViewById(R.id.inv_et_person_id)
        etPersonName = findViewById(R.id.inv_et_person_name)
        etPersonEmail = findViewById(R.id.inv_et_person_email)
        etProductCode = findViewById(R.id.inv_et_product_code)
        etProductName = findViewById(R.id.inv_et_product_name)
        etQuantity = findViewById(R.id.inv_et_quantity)
        etDate = findViewById(R.id.inv_et_date)
        etTime = findViewById(R.id.inv_et_time)
        spinnerSupplier = findViewById(R.id.inv_spinner_supplier)
        etUnitCost = findViewById(R.id.inv_et_unit_cost)
    }

    private fun setupSupplierSpinner() {
        val suppliers = resources.getStringArray(R.array.inv_suppliers_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, suppliers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSupplier.adapter = adapter
    }

    private fun setupDateTimePickers() {
        val calendar = Calendar.getInstance()
        selectedYear = calendar.get(Calendar.YEAR)
        selectedMonth = calendar.get(Calendar.MONTH)
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH)
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY)
        selectedMinute = calendar.get(Calendar.MINUTE)

        etDate.setOnClickListener {
            showDatePicker()
        }

        etTime.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedYear = year
                selectedMonth = month
                selectedDay = dayOfMonth
                updateDateTimeDisplay()
            },
            selectedYear,
            selectedMonth,
            selectedDay
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedHour = hourOfDay
                selectedMinute = minute
                updateDateTimeDisplay()
            },
            selectedHour,
            selectedMinute,
            true
        ).show()
    }

    private fun updateDateTimeDisplay() {
        etDate.setText(String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear))
        etTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute))

        selectedDateTime = LocalDateTime.of(
            selectedYear,
            selectedMonth + 1,
            selectedDay,
            selectedHour,
            selectedMinute
        )
    }

    private fun loadEntryData() {
        val entryId = intent.getStringExtra(EXTRA_ID)

        if (entryId != null) {
            supportActionBar?.title = getString(R.string.inv_edit_entry)
            currentEntry = controller.getInventoryEntryById(entryId)

            currentEntry?.let { entry ->
                etPersonId.setText(entry.Person.ID)
                etPersonName.setText(entry.Person.Name)
                etPersonEmail.setText(entry.Person.Email)
                etProductCode.setText(entry.ProductCode)
                etProductName.setText(entry.ProductName)
                etQuantity.setText(entry.Quantity.toString())

                entry.EntryDateTime?.let { dateTime ->
                    selectedDateTime = dateTime
                    selectedYear = dateTime.year
                    selectedMonth = dateTime.monthValue - 1
                    selectedDay = dateTime.dayOfMonth
                    selectedHour = dateTime.hour
                    selectedMinute = dateTime.minute
                    updateDateTimeDisplay()
                }

                val suppliers = resources.getStringArray(R.array.inv_suppliers_array)
                val supplierPosition = suppliers.indexOf(entry.Supplier)
                if (supplierPosition >= 0) {
                    spinnerSupplier.setSelection(supplierPosition)
                }

                etUnitCost.setText(entry.UnitCost.toString())
            }
        } else {
            supportActionBar?.title = getString(R.string.inv_add_entry)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)

        if (currentEntry == null) {
            menu?.findItem(R.id.mnu_delete)?.isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                util.showDialogCondition(
                    this,
                    getString(R.string.TextSaveActionQuestion)
                ) {
                    saveEntry()
                }
                true
            }
            R.id.mnu_delete -> {
                util.showDialogCondition(
                    this,
                    getString(R.string.TextDeleteActionQuestion)
                ) {
                    deleteEntry()
                }
                true
            }
            R.id.mnu_cancel -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true

        if (etPersonId.text.toString().trim().isEmpty()) {
            etPersonId.error = getString(R.string.inv_error_empty_person_id)
            isValid = false
        }

        if (etPersonName.text.toString().trim().isEmpty()) {
            etPersonName.error = getString(R.string.inv_error_empty_person_name)
            isValid = false
        }

        val email = etPersonEmail.text.toString().trim()
        if (email.isEmpty()) {
            etPersonEmail.error = getString(R.string.inv_error_invalid_email)
            isValid = false
        } else if (!controller.validateEmail(email)) {
            etPersonEmail.error = getString(R.string.inv_error_invalid_email)
            isValid = false
        }

        if (etProductCode.text.toString().trim().isEmpty()) {
            etProductCode.error = getString(R.string.inv_error_empty_product_code)
            isValid = false
        }

        if (etProductName.text.toString().trim().isEmpty()) {
            etProductName.error = getString(R.string.inv_error_empty_product_name)
            isValid = false
        }

        val quantityStr = etQuantity.text.toString().trim()
        if (quantityStr.isEmpty()) {
            etQuantity.error = getString(R.string.inv_error_invalid_quantity)
            isValid = false
        } else {
            val quantity = quantityStr.toIntOrNull() ?: 0
            if (!controller.validateQuantity(quantity)) {
                etQuantity.error = getString(R.string.inv_error_invalid_quantity)
                isValid = false
            }
        }

        if (etDate.text.toString().trim().isEmpty()) {
            etDate.error = getString(R.string.inv_error_empty_date)
            isValid = false
        }

        if (etTime.text.toString().trim().isEmpty()) {
            etTime.error = getString(R.string.inv_error_empty_time)
            isValid = false
        }

        if (selectedDateTime != null && !controller.validateDateTime(selectedDateTime)) {
            Toast.makeText(this, R.string.inv_error_future_datetime, Toast.LENGTH_LONG).show()
            isValid = false
        }

        val unitCostStr = etUnitCost.text.toString().trim()
        if (unitCostStr.isEmpty()) {
            etUnitCost.error = getString(R.string.inv_error_invalid_cost)
            isValid = false
        } else {
            val unitCost = unitCostStr.toDoubleOrNull() ?: 0.0
            if (!controller.validateUnitCost(unitCost)) {
                etUnitCost.error = getString(R.string.inv_error_invalid_cost)
                isValid = false
            }
        }

        return isValid
    }

    private fun saveEntry() {
        if (!validateFields()) {
            return
        }

        try {
            val person = Person().apply {
                ID = etPersonId.text.toString().trim()
                Name = etPersonName.text.toString().trim()
                Email = etPersonEmail.text.toString().trim()
            }

            val entry = currentEntry ?: InventoryEntry()

            if (currentEntry == null) {
                entry.ID = UUID.randomUUID().toString()
            }

            entry.Person = person
            entry.ProductCode = etProductCode.text.toString().trim()
            entry.ProductName = etProductName.text.toString().trim()
            entry.Quantity = etQuantity.text.toString().trim().toInt()
            entry.EntryDateTime = selectedDateTime
            entry.Supplier = spinnerSupplier.selectedItem.toString()
            entry.UnitCost = etUnitCost.text.toString().trim().toDouble()

            val success = if (currentEntry == null) {
                controller.addInventoryEntry(entry)
            } else {
                controller.updateInventoryEntry(entry)
            }

            if (success) {
                val message = if (currentEntry == null) {
                    getString(R.string.MsgSaveSuccess)
                } else {
                    getString(R.string.inv_success_updated)
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, R.string.MsgDuplicateDate, Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, R.string.ErrorMsgAdd, Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEntry() {
        currentEntry?.let { entry ->
            if (controller.removeInventoryEntry(entry.ID)) {
                Toast.makeText(this, R.string.MsgDeleteSuccess, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, R.string.ErrorMsgRemove, Toast.LENGTH_LONG).show()
            }
        }
    }
}