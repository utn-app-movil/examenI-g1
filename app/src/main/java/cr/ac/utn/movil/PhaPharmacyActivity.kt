package cr.ac.utn.movil

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.movil.data.MemoryDataManager
import identities.PharmacySale
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class PhaPharmacyActivity : AppCompatActivity() {

    private lateinit var edtPatientId: EditText
    private lateinit var edtPatientName: EditText
    private lateinit var edtTotalAmount: EditText
    private lateinit var edtPurchaseDateTime: EditText
    private lateinit var lstMedicines: ListView
    private lateinit var lstPurchases: ListView
    private lateinit var btnSave: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var btnClear: Button

    private val pharmacySales = mutableListOf<PharmacySale>()
    private lateinit var purchasesAdapter: ArrayAdapter<String>

    private var selectedSaleId: String? = null
    private val dateTimePattern = "yyyy-MM-dd HH:mm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pha_activity_pharmacy)

        initControls()
        initMedicinesList()
        initPurchasesList()
        setCurrentDateTime()
        initEvents()
    }

    private fun initControls() {
        edtPatientId = findViewById(R.id.pha_edt_patient_id)
        edtPatientName = findViewById(R.id.pha_edt_patient_name)
        edtTotalAmount = findViewById(R.id.pha_edt_total_amount)
        edtPurchaseDateTime = findViewById(R.id.pha_edt_purchase_datetime)

        lstMedicines = findViewById(R.id.pha_lst_medicines)
        lstPurchases = findViewById(R.id.pha_lst_purchases)

        btnSave = findViewById(R.id.pha_btn_save)
        btnUpdate = findViewById(R.id.pha_btn_update)
        btnDelete = findViewById(R.id.pha_btn_delete)
        btnClear = findViewById(R.id.pha_btn_clear)
    }

    private fun initMedicinesList() {
        val medicines = listOf(
            getString(R.string.pha_medicine_cream),
            getString(R.string.pha_medicine_pills),
            getString(R.string.pha_medicine_syrup)
        )

        val medicinesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            medicines
        )
        lstMedicines.adapter = medicinesAdapter
        lstMedicines.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    private fun initPurchasesList() {
        purchasesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf()
        )
        lstPurchases.adapter = purchasesAdapter

        loadSalesFromMemory()

        lstPurchases.setOnItemClickListener { _, _, position, _ ->
            if (position >= 0 && position < pharmacySales.size) {
                val sale = pharmacySales[position]
                selectedSaleId = sale.ID
                fillFormWithSale(sale)
            }
        }
    }

    private fun initEvents() {
        edtPurchaseDateTime.setOnClickListener {
            openDateTimePicker()
        }

        btnSave.setOnClickListener {
            val sale = buildSaleFromForm(isNew = true)
            if (sale != null) {
                MemoryDataManager.add(sale)
                selectedSaleId = sale.ID
                loadSalesFromMemory()
                Toast.makeText(this, R.string.pha_toast_saved, Toast.LENGTH_LONG).show()
            }
        }

        btnUpdate.setOnClickListener {
            if (selectedSaleId == null) {
                Toast.makeText(this, R.string.pha_error_select_item, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val sale = buildSaleFromForm(isNew = false)
            if (sale != null) {
                sale.ID = selectedSaleId!!.trim()
                MemoryDataManager.update(sale)
                loadSalesFromMemory()
                Toast.makeText(this, R.string.pha_toast_updated, Toast.LENGTH_LONG).show()
            }
        }

        btnDelete.setOnClickListener {
            if (selectedSaleId == null) {
                Toast.makeText(this, R.string.pha_error_select_item, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            showDeleteDialog()
        }

        btnClear.setOnClickListener {
            clearForm()
        }
    }

    private fun getFormatter(): DateTimeFormatter {
        return DateTimeFormatter.ofPattern(dateTimePattern, Locale.getDefault())
    }

    private fun setCurrentDateTime() {
        val now = LocalDateTime.now()
        edtPurchaseDateTime.setText(now.format(getFormatter()))
    }

    private fun openDateTimePicker() {
        val now = LocalDateTime.now()

        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val selectedDateTime = LocalDateTime.of(
                    year,
                    month + 1,
                    dayOfMonth,
                    hourOfDay,
                    minute
                )
                edtPurchaseDateTime.setText(selectedDateTime.format(getFormatter()))
            }

            TimePickerDialog(
                this,
                timeListener,
                now.hour,
                now.minute,
                true
            ).show()
        }

        DatePickerDialog(
            this,
            dateListener,
            now.year,
            now.monthValue - 1,
            now.dayOfMonth
        ).show()
    }

    private fun getSelectedMedicines(): String {
        val result = mutableListOf<String>()
        val checked = lstMedicines.checkedItemPositions
        val count = lstMedicines.count

        for (i in 0 until count) {
            if (checked.get(i)) {
                val value = lstMedicines.getItemAtPosition(i) as String
                result.add(value)
            }
        }
        return result.joinToString(", ")
    }

    private fun setMedicinesSelected(medicines: String) {
        val selectedList = medicines.split(",").map { it.trim() }.filter { it.isNotEmpty() }

        val count = lstMedicines.count
        for (i in 0 until count) {
            val value = lstMedicines.getItemAtPosition(i) as String
            lstMedicines.setItemChecked(i, selectedList.contains(value))
        }
    }

    private fun buildSaleFromForm(isNew: Boolean): PharmacySale? {
        val patientId = edtPatientId.text.toString().trim()
        val patientName = edtPatientName.text.toString().trim()
        val totalAmountText = edtTotalAmount.text.toString().trim()
        val dateTimeText = edtPurchaseDateTime.text.toString().trim()
        val medicines = getSelectedMedicines()

        if (patientId.isEmpty() ||
            patientName.isEmpty() ||
            totalAmountText.isEmpty() ||
            dateTimeText.isEmpty()
        ) {
            Toast.makeText(this, R.string.pha_error_required_fields, Toast.LENGTH_LONG).show()
            return null
        }

        if (medicines.isEmpty()) {
            Toast.makeText(this, R.string.pha_error_no_medicine, Toast.LENGTH_LONG).show()
            return null
        }

        val totalAmount = totalAmountText.toDoubleOrNull()
        if (totalAmount == null || totalAmount <= 0.0) {
            Toast.makeText(this, R.string.pha_error_invalid_amount, Toast.LENGTH_LONG).show()
            return null
        }

        val purchaseDateTime: LocalDateTime = try {
            LocalDateTime.parse(dateTimeText, getFormatter())
        } catch (ex: Exception) {
            Toast.makeText(this, R.string.pha_error_invalid_datetime, Toast.LENGTH_LONG).show()
            return null
        }

        // Validation: date and time cannot be greater than current
        if (purchaseDateTime.isAfter(LocalDateTime.now())) {
            Toast.makeText(this, R.string.pha_error_future_datetime, Toast.LENGTH_LONG).show()
            return null
        }

        if (isNew) {
            val existing = MemoryDataManager.getById(patientId)
            if (existing != null && existing is PharmacySale) {
                Toast.makeText(this, R.string.pha_error_duplicated_id, Toast.LENGTH_LONG).show()
                return null
            }
        }

        val sale = PharmacySale()
        sale.ID = patientId
        sale.PatientId = patientId
        sale.PatientName = patientName
        sale.MedicineTypes = medicines
        sale.TotalAmount = totalAmount
        sale.PurchaseDateTime = purchaseDateTime

        return sale
    }

    private fun loadSalesFromMemory() {
        pharmacySales.clear()
        val all = MemoryDataManager.getAll()
        for (item in all) {
            if (item is PharmacySale) {
                pharmacySales.add(item)
            }
        }
        refreshPurchasesList()
    }

    private fun refreshPurchasesList() {
        val descriptions = pharmacySales.map { it.FullDescription }
        purchasesAdapter.clear()
        purchasesAdapter.addAll(descriptions)
        purchasesAdapter.notifyDataSetChanged()
    }

    private fun fillFormWithSale(sale: PharmacySale) {
        edtPatientId.setText(sale.PatientId)
        edtPatientName.setText(sale.PatientName)
        edtTotalAmount.setText(sale.TotalAmount.toString())
        edtPurchaseDateTime.setText(sale.PurchaseDateTime.format(getFormatter()))
        setMedicinesSelected(sale.MedicineTypes)
    }

    private fun clearForm() {
        edtPatientId.text.clear()
        edtPatientName.text.clear()
        edtTotalAmount.text.clear()
        for (i in 0 until lstMedicines.count) {
            lstMedicines.setItemChecked(i, false)
        }
        selectedSaleId = null
        setCurrentDateTime()
    }

    private fun showDeleteDialog() {
        val idToDelete = selectedSaleId ?: return

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.pha_dialog_delete_title))
            .setMessage(getString(R.string.pha_dialog_delete_message))
            .setPositiveButton(R.string.TextYes) { _, _ ->
                MemoryDataManager.remove(idToDelete)
                selectedSaleId = null
                clearForm()
                loadSalesFromMemory()
                Toast.makeText(this, R.string.pha_toast_deleted, Toast.LENGTH_LONG).show()
            }
            .setNegativeButton(R.string.TextNo, null)
            .show()
    }
}
