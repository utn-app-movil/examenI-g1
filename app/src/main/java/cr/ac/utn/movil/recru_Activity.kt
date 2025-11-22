package cr.ac.utn.movil

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import controller.RecruController
import identities.recru_Roles
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.toString

class recru_Activity : AppCompatActivity() {

    // UI Components - Personal Information
    private lateinit var etId: TextInputEditText
    private lateinit var etName: TextInputEditText
    private lateinit var etFirstLastName: TextInputEditText
    private lateinit var etSecondLastName: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etEmail: TextInputEditText

    // UI Components - Location
    private lateinit var etAddress: TextInputEditText
    private lateinit var etCountry: TextInputEditText
    private lateinit var etProvince: TextInputEditText
    private lateinit var etCanton: TextInputEditText
    private lateinit var etDistrict: TextInputEditText

    // UI Components - Recruitment Information
    private lateinit var etCompany: TextInputEditText
    private lateinit var btnSelectRoles: Button
    private lateinit var tvSelectedRoles: TextView
    private lateinit var etSalaryExpectation: TextInputEditText
    private lateinit var etYearsOfExperience: TextInputEditText
    private lateinit var btnSelectDate: Button
    private lateinit var tvSelectedDate: TextView

    // UI Components - Action Buttons
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    // Controller
    private val controller = RecruController(this)

    // Data
    private var selectedRoles = mutableListOf<String>()
    private var selectedDate: Date? = null
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Available roles
    private val availableRoles = arrayOf(
        "Software Developer",
        "Backend Developer",
        "Frontend Developer",
        "Full Stack Developer",
        "Mobile Developer",
        "DevOps Engineer",
        "QA Engineer",
        "Data Scientist",
        "Product Manager",
        "UI/UX Designer"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recru)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recru_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupListeners()

        // Search button
        val btnSearchId = findViewById<ImageButton>(R.id.btnSearchId)
        btnSearchId.setOnClickListener {
            searchById()
        }
    }

    private fun searchById() {
        val id = etId.text.toString().trim()
        if (id.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_enter_id_search), Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val registration = controller.getById(id)
            if (registration == null) {
                Toast.makeText(this, getString(R.string.no_registration_found, id), Toast.LENGTH_SHORT).show()
            } else {
                loadRegistration(registration as recru_Roles)
            }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                getString(R.string.error_searching_registration, e.message ?: ""),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadRegistration(registration: recru_Roles) {
        etId.setText(registration.ID)
        etName.setText(registration.Name)
        etFirstLastName.setText(registration.FLastName)
        etSecondLastName.setText(registration.SLastName)
        etPhone.setText(registration.Phone.toString())
        etEmail.setText(registration.Email)
        etAddress.setText(registration.Address)
        etCountry.setText(registration.Country)
        etProvince.setText(registration.province)
        etCanton.setText(registration.canton)
        etDistrict.setText(registration.district)
        etCompany.setText(registration.company)
        selectedRoles = registration.rolesOfInterest.toMutableList()
        updateSelectedRolesText()
        etSalaryExpectation.setText(registration.salaryExpectation.toString())
        etYearsOfExperience.setText(registration.yearsOfExperience.toString())
        selectedDate = registration.registrationDate
        tvSelectedDate.text = dateFormat.format(selectedDate!!)

        Toast.makeText(this, getString(R.string.registration_loaded_successfully), Toast.LENGTH_SHORT).show()
    }

    private fun initializeViews() {
        // Personal Information
        etId = findViewById(R.id.etId)
        etName = findViewById(R.id.etName)
        etFirstLastName = findViewById(R.id.etFirstLastName)
        etSecondLastName = findViewById(R.id.etSecondLastName)
        etPhone = findViewById(R.id.etPhone)
        etEmail = findViewById(R.id.etEmail)

        // Location
        etAddress = findViewById(R.id.etAddress)
        etCountry = findViewById(R.id.etCountry)
        etProvince = findViewById(R.id.etProvince)
        etCanton = findViewById(R.id.etCanton)
        etDistrict = findViewById(R.id.etDistrict)

        // Recruitment Information
        etCompany = findViewById(R.id.etCompany)
        btnSelectRoles = findViewById(R.id.btnSelectRoles)
        tvSelectedRoles = findViewById(R.id.tvSelectedRoles)
        etSalaryExpectation = findViewById(R.id.etSalaryExpectation)
        etYearsOfExperience = findViewById(R.id.etYearsOfExperience)
        btnSelectDate = findViewById(R.id.btnSelectDate)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)

        // Action Buttons
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
    }

    private fun setupListeners() {
        btnSelectRoles.setOnClickListener {
            showRolesDialog()
        }

        btnSelectDate.setOnClickListener {
            showDatePicker()
        }

        btnSave.setOnClickListener {
            saveRegistration()
        }

        btnCancel.setOnClickListener {
            clearForm()
        }
    }

    private fun showRolesDialog() {
        val checkedItems = BooleanArray(availableRoles.size) { index ->
            selectedRoles.contains(availableRoles[index])
        }

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.select_roles_interest))
            .setMultiChoiceItems(availableRoles, checkedItems) { _, which, isChecked ->
                if (isChecked) {
                    if (!selectedRoles.contains(availableRoles[which])) {
                        selectedRoles.add(availableRoles[which])
                    }
                } else {
                    selectedRoles.remove(availableRoles[which])
                }
            }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                updateSelectedRolesText()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun updateSelectedRolesText() {
        tvSelectedRoles.text = if (selectedRoles.isEmpty()) {
            getString(R.string.no_roles_selected)
        } else {
            selectedRoles.joinToString(", ")
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            calendar.set(selectedYear, selectedMonth, selectedDay)
            // Reset time to midnight for comparison
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            selectedDate = calendar.time

            // Get today's date at midnight
            val today = Calendar.getInstance()
            today.set(Calendar.HOUR_OF_DAY, 0)
            today.set(Calendar.MINUTE, 0)
            today.set(Calendar.SECOND, 0)
            today.set(Calendar.MILLISECOND, 0)

            // Validate date is not in the past or future - must be today
            when {
                selectedDate!!.before(today.time) -> {
                    Toast.makeText(this, getString(R.string.registration_date_past), Toast.LENGTH_SHORT).show()
                    selectedDate = null
                    tvSelectedDate.text = getString(R.string.no_date_selected)
                }
                selectedDate!!.after(today.time) -> {
                    Toast.makeText(this, getString(R.string.registration_date_future), Toast.LENGTH_SHORT).show()
                    selectedDate = null
                    tvSelectedDate.text = getString(R.string.no_date_selected)
                }
                else -> {
                    tvSelectedDate.text = dateFormat.format(selectedDate!!)
                }
            }
        }, year, month, day).show()
    }

    private fun saveRegistration() {
        // Validate required fields
        if (!validateFields()) {
            return
        }

        val id = etId.text.toString().trim()

        // Create registration object
        val registration = recru_Roles(
            id = id,
            name = etName.text.toString().trim(),
            fLastName = etFirstLastName.text.toString().trim(),
            sLastName = etSecondLastName.text.toString().trim(),
            phone = etPhone.text.toString().trim().toIntOrNull() ?: 0,
            email = etEmail.text.toString().trim(),
            address = etAddress.text.toString().trim(),
            country = etCountry.text.toString().trim(),
            province = etProvince.text.toString().trim(),
            canton = etCanton.text.toString().trim(),
            district = etDistrict.text.toString().trim(),
            company = etCompany.text.toString().trim(),
            rolesOfInterest = selectedRoles.toMutableList(),
            salaryExpectation = etSalaryExpectation.text.toString().trim().toDoubleOrNull() ?: 0.0,
            yearsOfExperience = etYearsOfExperience.text.toString().trim().toIntOrNull() ?: 0,
            registrationDate = selectedDate ?: Date()
        )

        try {
            // Check if registration exists
            val existingRegistration = controller.getById(id)

            if (existingRegistration != null) {
                // Update existing registration
                controller.updateIdentifier(registration)
                Toast.makeText(this, getString(R.string.registration_updated_successfully), Toast.LENGTH_SHORT).show()
            } else {
                // Add new registration
                controller.addIdentifier(registration)
                Toast.makeText(this, getString(R.string.registration_saved_successfully), Toast.LENGTH_SHORT).show()
            }

            clearForm()
        } catch (e: Exception) {
            Toast.makeText(
                this,
                getString(R.string.error_saving, e.message ?: ""),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun deleteRegistration() {
        val id = etId.text.toString().trim()
        if (id.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_enter_id_delete), Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm_delete))
            .setMessage(getString(R.string.confirm_delete_message))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                try {
                    controller.removeIdentifier(id)
                    Toast.makeText(this, getString(R.string.registration_deleted_successfully), Toast.LENGTH_SHORT).show()
                    clearForm()
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        getString(R.string.error_deleting, e.message ?: ""),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun validateFields(): Boolean {
        if (etId.text.isNullOrBlank()) {
            Toast.makeText(this, getString(R.string.id_required), Toast.LENGTH_SHORT).show()
            etId.requestFocus()
            return false
        }

        if (etName.text.isNullOrBlank()) {
            Toast.makeText(this, getString(R.string.name_required), Toast.LENGTH_SHORT).show()
            etName.requestFocus()
            return false
        }

        if (etFirstLastName.text.isNullOrBlank()) {
            Toast.makeText(this, getString(R.string.first_last_name_required), Toast.LENGTH_SHORT).show()
            etFirstLastName.requestFocus()
            return false
        }

        if (etCompany.text.isNullOrBlank()) {
            Toast.makeText(this, getString(R.string.company_required), Toast.LENGTH_SHORT).show()
            etCompany.requestFocus()
            return false
        }

        if (selectedRoles.isEmpty()) {
            Toast.makeText(this, getString(R.string.select_at_least_one_role), Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedDate == null) {
            Toast.makeText(this, getString(R.string.select_registration_date), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun clearForm() {
        // Personal Information
        etId.text?.clear()
        etName.text?.clear()
        etFirstLastName.text?.clear()
        etSecondLastName.text?.clear()
        etPhone.text?.clear()
        etEmail.text?.clear()

        // Location
        etAddress.text?.clear()
        etCountry.text?.clear()
        etProvince.text?.clear()
        etCanton.text?.clear()
        etDistrict.text?.clear()

        // Recruitment Information
        etCompany.text?.clear()
        selectedRoles.clear()
        tvSelectedRoles.text = getString(R.string.no_roles_selected)
        etSalaryExpectation.text?.clear()
        etYearsOfExperience.text?.clear()
        selectedDate = null
        tvSelectedDate.text = getString(R.string.no_date_selected)

        Toast.makeText(this, getString(R.string.form_cleared), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                clearForm()
                Toast.makeText(this, getString(R.string.ready_create_new_registration), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.mnu_delete -> {
                deleteRegistration()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
