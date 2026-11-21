package cr.ac.utn.examenI

import android.os.Bundle
import android.util.Patterns
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
import cr.ac.utn.examenI.domain.model.Rent
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.RentViewModel

class RentCrudActivity : AppCompatActivity() {

    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var spnVehicleType: Spinner
    private lateinit var txtPlate: EditText
    private lateinit var txtMileage: EditText
    private lateinit var txtDriverLicense: EditText
    private lateinit var btnDelete: Button

    private var currentId: String? = null
    private val vehicleTypes = arrayOf(TYPE_HATCHBACK, TYPE_SEDAN, TYPE_SUV, TYPE_MICROBUS)

    companion object {
        private const val TYPE_HATCHBACK = "Hatchback"
        private const val TYPE_SEDAN = "Sedan"
        private const val TYPE_SUV = "SUV"
        private const val TYPE_MICROBUS = "Microbus"

        private const val MIN_PHONE_LENGTH = 8
        private const val MIN_LICENSE_LENGTH = 2

        private const val ERR_REQUIRED_FIELD = "This field is required"
        private const val ERR_INVALID_PHONE = "Enter a valid numeric phone (min 8 digits)"
        private const val ERR_INVALID_EMAIL = "Enter a valid email address"
        private const val ERR_INVALID_MILEAGE = "Enter a valid mileage (0 or greater)"
        private const val ERR_INVALID_LICENSE = "Enter a valid driver license (min 2 chars)"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rent_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        title = getString(R.string.rent_title)

        bindViews()
        setupSpinner()

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            btnDelete.visibility = View.GONE
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        btnDelete.setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }

    private fun bindViews() {
        txtId = findViewById(R.id.txtId)
        txtName = findViewById(R.id.txtName)
        txtFirstName = findViewById(R.id.txtFirstName)
        txtLastName = findViewById(R.id.txtLastName)
        txtPhone = findViewById(R.id.txtPhone)
        txtEmail = findViewById(R.id.txtEmail)
        txtAddress = findViewById(R.id.txtAddress)
        spnVehicleType = findViewById(R.id.spnVehicleType)
        txtPlate = findViewById(R.id.txtPlate)
        txtMileage = findViewById(R.id.txtMileage)
        txtDriverLicense = findViewById(R.id.txtDriverLicense)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnVehicleType.adapter = adapter
    }

    private fun loadExistingData(id: String) {
        val item = RentViewModel.getById(id)
        if (item == null) {
            Toast.makeText(this, getString(R.string.TextDataNoFound), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)

        val vehiclePosition = vehicleTypes.indexOf(item.vehicleType)
        if (vehiclePosition >= 0) {
            spnVehicleType.setSelection(vehiclePosition)
        }

        txtPlate.setText(item.plate)
        txtMileage.setText(item.mileage.toString())
        txtDriverLicense.setText(item.driverLicense)
        btnDelete.visibility = View.VISIBLE
    }

    private fun validateForm(): Rent? {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val firstName = txtFirstName.text.toString().trim()
        val lastName = txtLastName.text.toString().trim()
        val phoneText = txtPhone.text.toString().trim()
        val email = txtEmail.text.toString().trim()
        val address = txtAddress.text.toString().trim()
        val vehicleType = spnVehicleType.selectedItem?.toString().orEmpty()
        val plate = txtPlate.text.toString().trim().uppercase()
        val mileageText = txtMileage.text.toString().trim()
        val driverLicense = txtDriverLicense.text.toString().trim()

        if (id.isEmpty()) {
            txtId.error = ERR_REQUIRED_FIELD
            txtId.requestFocus()
            return null
        }

        if (name.isEmpty()) {
            txtName.error = ERR_REQUIRED_FIELD
            txtName.requestFocus()
            return null
        }

        if (firstName.isEmpty()) {
            txtFirstName.error = ERR_REQUIRED_FIELD
            txtFirstName.requestFocus()
            return null
        }

        if (lastName.isEmpty()) {
            txtLastName.error = ERR_REQUIRED_FIELD
            txtLastName.requestFocus()
            return null
        }

        val phoneNumber = phoneText.toIntOrNull()
        if (phoneText.length < MIN_PHONE_LENGTH || phoneNumber == null || phoneNumber <= 0) {
            txtPhone.error = ERR_INVALID_PHONE
            txtPhone.requestFocus()
            return null
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.error = ERR_INVALID_EMAIL
            txtEmail.requestFocus()
            return null
        }

        if (address.isEmpty()) {
            txtAddress.error = ERR_REQUIRED_FIELD
            txtAddress.requestFocus()
            return null
        }

        if (plate.isEmpty()) {
            txtPlate.error = ERR_REQUIRED_FIELD
            txtPlate.requestFocus()
            return null
        }

        val mileageValue = mileageText.toIntOrNull()
        if (mileageValue == null || mileageValue < 0) {
            txtMileage.error = ERR_INVALID_MILEAGE
            txtMileage.requestFocus()
            return null
        }

        if (driverLicense.length < MIN_LICENSE_LENGTH) {
            txtDriverLicense.error = ERR_INVALID_LICENSE
            txtDriverLicense.requestFocus()
            return null
        }

        return Rent(
            id = id,
            name = name,
            firstName = firstName,
            lastName = lastName,
            phone = phoneNumber,
            email = email,
            address = address,
            vehicleType = vehicleType,
            plate = plate,
            mileage = mileageValue,
            driverLicense = driverLicense
        )
    }

    private fun doSave() {
        val rentItem = validateForm() ?: return

        if (currentId.isNullOrEmpty()) {
            if (RentViewModel.existsById(rentItem.id) || RentViewModel.existsByPlate(rentItem.plate)) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }

            util.showDialogCondition(this, getString(R.string.TextSaveQuestion)) {
                val saved = RentViewModel.save(rentItem)
                if (saved) {
                    Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            if (RentViewModel.existsByPlate(rentItem.plate, excludeId = rentItem.id)) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }

            util.showDialogCondition(this, getString(R.string.TextSaveQuestion)) {
                val updated = RentViewModel.update(rentItem)
                if (updated) {
                    Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, getString(R.string.TextDataNoFound), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun doDelete() {
        val id = currentId ?: txtId.text.toString().trim()
        if (id.isEmpty()) {
            Toast.makeText(this, getString(R.string.TextDataNoFound), Toast.LENGTH_SHORT).show()
            return
        }

        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            val deleted = RentViewModel.delete(id)
            if (deleted) {
                Toast.makeText(this, getString(R.string.TextDeleteSuccess), Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.TextDeleteError), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.mnu_delete)?.isVisible = !currentId.isNullOrEmpty()
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                doSave()
                true
            }
            R.id.mnu_delete -> {
                doDelete()
                true
            }
            R.id.mnu_cancel -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}