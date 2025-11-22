package cr.ac.utn.movil

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.movil.controllers.WatWaterQualityController
import cr.ac.utn.movil.identities.WatOwner
import cr.ac.utn.movil.identities.WatWaterQualityMonitoring
import java.text.SimpleDateFormat
import java.util.*

class WatMainActivity : AppCompatActivity() {

    private lateinit var etNisId: EditText
    private lateinit var etDate: EditText
    private lateinit var etPhLevel: EditText
    private lateinit var etTurbidity: EditText
    private lateinit var btnSelectContaminants: Button
    private lateinit var tvSelectedContaminants: TextView
    private lateinit var etOwnerName: EditText
    private lateinit var etOwnerId: EditText
    private lateinit var etOwnerPhone: EditText
    private lateinit var etOwnerAddress: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnClear: Button
    private lateinit var btnList: Button
    private lateinit var btnSelectDate: Button

    private var selectedDate: Calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var selectedContaminants = mutableListOf<String>()
    private var isEditMode = false
    private var currentNisId = ""

    private val contaminantsList = listOf(
        "Bacteria",
        "Heavy Metals",
        "Pesticides",
        "Nitrates",
        "Phosphates",
        "Chlorine",
        "Lead",
        "Arsenic"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wat_activity_main)

        supportActionBar?.title = getString(R.string.wat_app_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        etNisId = findViewById(R.id.wat_etNisId)
        etDate = findViewById(R.id.wat_etDate)
        etPhLevel = findViewById(R.id.wat_etPhLevel)
        etTurbidity = findViewById(R.id.wat_etTurbidity)
        btnSelectContaminants = findViewById(R.id.wat_btnSelectContaminants)
        tvSelectedContaminants = findViewById(R.id.wat_tvSelectedContaminants)
        etOwnerName = findViewById(R.id.wat_etOwnerName)
        etOwnerId = findViewById(R.id.wat_etOwnerId)
        etOwnerPhone = findViewById(R.id.wat_etOwnerPhone)
        etOwnerAddress = findViewById(R.id.wat_etOwnerAddress)
        btnSearch = findViewById(R.id.wat_btnSearch)
        btnClear = findViewById(R.id.wat_btnClear)
        btnList = findViewById(R.id.wat_btnList)
        btnSelectDate = findViewById(R.id.wat_btnSelectDate)

        // Set current date as default
        etDate.setText(dateFormat.format(selectedDate.time))
    }

    private fun setupListeners() {
        btnSelectDate.setOnClickListener { showDatePicker() }
        btnSelectContaminants.setOnClickListener { showContaminantsDialog() }
        btnSearch.setOnClickListener { searchRecord() }
        btnClear.setOnClickListener { clearForm() }
        btnList.setOnClickListener { showList() }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate.set(year, month, dayOfMonth)
                etDate.setText(dateFormat.format(selectedDate.time))
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showContaminantsDialog() {
        val checkedItems = BooleanArray(contaminantsList.size) { index ->
            selectedContaminants.contains(contaminantsList[index])
        }

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.wat_msg_select_contaminants))
            .setMultiChoiceItems(
                contaminantsList.toTypedArray(),
                checkedItems
            ) { _, which, isChecked ->
                if (isChecked) {
                    if (!selectedContaminants.contains(contaminantsList[which])) {
                        selectedContaminants.add(contaminantsList[which])
                    }
                } else {
                    selectedContaminants.remove(contaminantsList[which])
                }
            }
            .setPositiveButton(getString(R.string.TextYes)) { _, _ ->
                updateContaminantsDisplay()
            }
            .setNegativeButton(getString(R.string.TextNo), null)
            .show()
    }

    private fun updateContaminantsDisplay() {
        tvSelectedContaminants.text = if (selectedContaminants.isEmpty()) {
            ""
        } else {
            selectedContaminants.joinToString(", ")
        }
    }

    private fun searchRecord() {
        val nisId = etNisId.text.toString().trim()

        if (nisId.isEmpty()) {
            Toast.makeText(this, getString(R.string.wat_error_empty_nis), Toast.LENGTH_SHORT).show()
            return
        }

        val record = WatWaterQualityController.getById(nisId)
        if (record != null) {
            loadRecordToForm(record)
            isEditMode = true
            currentNisId = nisId
            Toast.makeText(this, getString(R.string.MsgSaveSuccess), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.MsgDataNoFound), Toast.LENGTH_SHORT).show()
            clearFormExceptNisId()
        }
    }

    private fun loadRecordToForm(record: WatWaterQualityMonitoring) {
        etNisId.setText(record.nisId)
        etDate.setText(dateFormat.format(record.measurementDate))
        etPhLevel.setText(record.phLevel.toString())
        etTurbidity.setText(record.turbidity.toString())

        selectedContaminants.clear()
        selectedContaminants.addAll(record.detectedContaminants)
        updateContaminantsDisplay()

        etOwnerName.setText(record.owner.fullName)
        etOwnerId.setText(record.owner.identificationNumber)
        etOwnerPhone.setText(record.owner.phone)
        etOwnerAddress.setText(record.owner.address)

        selectedDate.time = record.measurementDate
    }

    private fun clearForm() {
        etNisId.setText("")
        clearFormExceptNisId()
    }

    private fun clearFormExceptNisId() {
        selectedDate = Calendar.getInstance()
        etDate.setText(dateFormat.format(selectedDate.time))
        etPhLevel.setText("")
        etTurbidity.setText("")
        selectedContaminants.clear()
        updateContaminantsDisplay()
        etOwnerName.setText("")
        etOwnerId.setText("")
        etOwnerPhone.setText("")
        etOwnerAddress.setText("")
        isEditMode = false
        currentNisId = ""
    }

    private fun showList() {
        val intent = Intent(this, WatListActivity::class.java)
        startActivity(intent)
    }

    private fun validateData(): Boolean {
        val nisId = etNisId.text.toString().trim()
        val phLevelStr = etPhLevel.text.toString().trim()
        val turbidityStr = etTurbidity.text.toString().trim()
        val ownerName = etOwnerName.text.toString().trim()
        val ownerIdNum = etOwnerId.text.toString().trim()

        if (nisId.isEmpty()) {
            Toast.makeText(this, getString(R.string.wat_error_empty_nis), Toast.LENGTH_SHORT).show()
            return false
        }

        if (ownerName.isEmpty()) {
            Toast.makeText(this, getString(R.string.wat_error_empty_owner_name), Toast.LENGTH_SHORT).show()
            return false
        }

        if (ownerIdNum.isEmpty()) {
            Toast.makeText(this, getString(R.string.wat_error_empty_owner_id), Toast.LENGTH_SHORT).show()
            return false
        }

        if (phLevelStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.wat_error_invalid_ph), Toast.LENGTH_SHORT).show()
            return false
        }

        val phLevel = phLevelStr.toDoubleOrNull()
        if (phLevel == null || phLevel < 0 || phLevel > 14) {
            Toast.makeText(this, getString(R.string.wat_error_invalid_ph), Toast.LENGTH_SHORT).show()
            return false
        }

        if (turbidityStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.wat_error_invalid_turbidity), Toast.LENGTH_SHORT).show()
            return false
        }

        val turbidity = turbidityStr.toDoubleOrNull()
        if (turbidity == null || turbidity < 0) {
            Toast.makeText(this, getString(R.string.wat_error_invalid_turbidity), Toast.LENGTH_SHORT).show()
            return false
        }

        // Validate date is not in the future
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 23)
        today.set(Calendar.MINUTE, 59)
        today.set(Calendar.SECOND, 59)

        if (selectedDate.after(today)) {
            Toast.makeText(this, getString(R.string.wat_error_invalid_date), Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedContaminants.isEmpty()) {
            Toast.makeText(this, getString(R.string.wat_error_no_contaminants), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveRecord() {
        if (!validateData()) {
            return
        }

        val waterQuality = WatWaterQualityMonitoring().apply {
            nisId = etNisId.text.toString().trim()
            measurementDate = selectedDate.time
            phLevel = etPhLevel.text.toString().toDouble()
            turbidity = etTurbidity.text.toString().toDouble()
            detectedContaminants = selectedContaminants.toMutableList()
            owner = WatOwner().apply {
                fullName = etOwnerName.text.toString().trim()
                identificationNumber = etOwnerId.text.toString().trim()
                phone = etOwnerPhone.text.toString().trim()
                address = etOwnerAddress.text.toString().trim()
            }
        }

        val success = if (isEditMode) {
            WatWaterQualityController.update(waterQuality)
        } else {
            WatWaterQualityController.add(waterQuality)
        }

        if (success) {
            Toast.makeText(this, getString(R.string.MsgSaveSuccess), Toast.LENGTH_SHORT).show()
            clearForm()
        } else {
            val errorMsg = if (isEditMode) {
                getString(R.string.ErrorMsgUpdate)
            } else {
                getString(R.string.MsgDuplicateDate)
            }
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteRecord() {
        val nisId = etNisId.text.toString().trim()

        if (nisId.isEmpty()) {
            Toast.makeText(this, getString(R.string.wat_error_empty_nis), Toast.LENGTH_SHORT).show()
            return
        }

        if (!WatWaterQualityController.exists(nisId)) {
            Toast.makeText(this, getString(R.string.MsgDataNoFound), Toast.LENGTH_SHORT).show()
            return
        }

        val success = WatWaterQualityController.remove(nisId)
        if (success) {
            Toast.makeText(this, getString(R.string.MsgDeleteSuccess), Toast.LENGTH_SHORT).show()
            clearForm()
        } else {
            Toast.makeText(this, getString(R.string.ErrorMsgRemove), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.wat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.wat_menu_save -> {
                showSaveConfirmationDialog()
                true
            }
            R.id.wat_menu_delete -> {
                showDeleteConfirmationDialog()
                true
            }
            R.id.wat_menu_cancel -> {
                clearForm()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSaveConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.TextTitleDialogQuestion))
            .setMessage(getString(R.string.TextSaveActionQuestion))
            .setPositiveButton(getString(R.string.TextYes)) { _, _ ->
                saveRecord()
            }
            .setNegativeButton(getString(R.string.TextNo), null)
            .show()
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.TextTitleDialogQuestion))
            .setMessage(getString(R.string.TextDeleteActionQuestion))
            .setPositiveButton(getString(R.string.TextYes)) { _, _ ->
                deleteRecord()
            }
            .setNegativeButton(getString(R.string.TextNo), null)
            .show()
    }
}