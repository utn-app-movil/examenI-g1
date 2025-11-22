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
            Toast.makeText(this, "Please enter an ID to search", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val registration = controller.getById(id)
            if (registration == null) {
                Toast.makeText(this, "No registration found for ID: $id", Toast.LENGTH_SHORT).show()
            } else {
                loadRegistration(registration as recru_Roles)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error searching registration: ${e.message}", Toast.LENGTH_SHORT).show()
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

        Toast.makeText(this, "Registration loaded successfully", Toast.LENGTH_SHORT).show()
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
            .setTitle("Select Roles of Interest")
            .setMultiChoiceItems(availableRoles, checkedItems) { _, which, isChecked ->
                if (isChecked) {
                    if (!selectedRoles.contains(availableRoles[which])) {
                        selectedRoles.add(availableRoles[which])
                    }
                } else {
                    selectedRoles.remove(availableRoles[which])
                }
            }
            .setPositiveButton("OK") { _, _ ->
                updateSelectedRolesText()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateSelectedRolesText() {
        tvSelectedRoles.text = if (selectedRoles.isEmpty()) {
            "No roles selected"
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
                    Toast.makeText(this, "Registration date cannot be in the past", Toast.LENGTH_SHORT).show()
                    selectedDate = null
                    tvSelectedDate.text = "No date selected"
                }
                selectedDate!!.after(today.time) -> {
                    Toast.makeText(this, "Registration date cannot be in the future", Toast.LENGTH_SHORT).show()
                    selectedDate = null
                    tvSelectedDate.text = "No date selected"
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

        // Create registration object
        val registration = recru_Roles(
            id = etId.text.toString().trim(),
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

        // Add registration
        try {
            controller.addIdentifier(registration)
            Toast.makeText(this, "Registration saved successfully", Toast.LENGTH_SHORT).show()
            clearForm()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to save registration. Check validations.", Toast.LENGTH_LONG).show()
        }
    }

    private fun validateFields(): Boolean {
        if (etId.text.isNullOrBlank()) {
            Toast.makeText(this, "ID is required", Toast.LENGTH_SHORT).show()
            etId.requestFocus()
            return false
        }

        if (etName.text.isNullOrBlank()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show()
            etName.requestFocus()
            return false
        }

        if (etFirstLastName.text.isNullOrBlank()) {
            Toast.makeText(this, "First last name is required", Toast.LENGTH_SHORT).show()
            etFirstLastName.requestFocus()
            return false
        }

        if (etCompany.text.isNullOrBlank()) {
            Toast.makeText(this, "Company is required", Toast.LENGTH_SHORT).show()
            etCompany.requestFocus()
            return false
        }

        if (selectedRoles.isEmpty()) {
            Toast.makeText(this, "Please select at least one role", Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedDate == null) {
            Toast.makeText(this, "Please select a registration date", Toast.LENGTH_SHORT).show()
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
        tvSelectedRoles.text = "No roles selected"
        etSalaryExpectation.text?.clear()
        etYearsOfExperience.text?.clear()
        selectedDate = null
        tvSelectedDate.text = "No date selected"

        Toast.makeText(this, "Form cleared", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                clearForm()
                Toast.makeText(this, "Ready to create new registration", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.mnu_delete -> {
                Toast.makeText(this, "Delete feature - coming soon", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.mnu_cancel -> {
                Toast.makeText(this, "Update feature - coming soon", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
