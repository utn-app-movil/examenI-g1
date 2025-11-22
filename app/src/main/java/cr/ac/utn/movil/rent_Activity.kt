package cr.ac.utn.movil

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import controller.rent_Controller
import cr.ac.utn.movil.identities.rent_Rentals

class rent_Activity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPlate: EditText
    private lateinit var etMileage: EditText
    private lateinit var etDriverLicense: EditText
    private lateinit var spVehicleType: Spinner

    private lateinit var btnSave: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    private val controller = rent_Controller()
    private var selectedItem: rent_Rentals? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_rent)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupSpinner()
        setupList()
        setupEvents()
    }

    private fun initViews() {
        etName = findViewById(R.id.et_rent_nidia_name)
        etLastName = findViewById(R.id.et_rent_nidia_lastname)
        etPlate = findViewById(R.id.et_rent_nidia_plate)
        etMileage = findViewById(R.id.et_rent_nidia_mileage)
        etDriverLicense = findViewById(R.id.et_rent_nidia_driver_license)

        spVehicleType = findViewById(R.id.sp_rent_nidia_vehicle_type)

        btnSave = findViewById(R.id.btn_rent_nidia_save)
        btnUpdate = findViewById(R.id.btn_rent_nidia_update)
        btnDelete = findViewById(R.id.btn_rent_nidia_delete)

        listView = findViewById(R.id.lv_rent_nidia_list)
    }

    private fun setupSpinner() {
        val vehicleTypes = resources.getStringArray(R.array.rent_nidia_vehicle_types)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleTypes)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spVehicleType.adapter = spinnerAdapter
    }

    private fun setupList() {
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            controller.getAllRentals().map { formatItem(it) }
        )
        listView.adapter = adapter
    }

    private fun setupEvents() {

        listView.setOnItemClickListener { _, _, index, _ ->
            selectedItem = controller.getAllRentals()[index]

            selectedItem?.let { r ->
                etName.setText(r.clientName)
                etLastName.setText(r.lastName)
                etPlate.setText(r.plate)
                etMileage.setText(r.mileage.toString())
                etDriverLicense.setText(r.driverLicense)

                val pos = (spVehicleType.adapter as ArrayAdapter<String>)
                    .getPosition(r.vehicleType)
                spVehicleType.setSelection(pos)
            }

            Toast.makeText(this, "Item selected", Toast.LENGTH_SHORT).show()
        }

        btnSave.setOnClickListener {
            val rental = getFormData() ?: return@setOnClickListener

            val success = controller.addRental(rental)
            if (!success) {
                Toast.makeText(this, "Plate already exists", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            clearForm()
            refreshList()
            Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()
        }

        btnUpdate.setOnClickListener {
            if (selectedItem == null) {
                Toast.makeText(this, "Select an item first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rental = getFormData() ?: return@setOnClickListener

            rental.ID = selectedItem!!.ID
            controller.updateRental(rental)

            clearForm()
            refreshList()
            selectedItem = null

            Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show()
        }

        btnDelete.setOnClickListener {
            if (selectedItem == null) {
                Toast.makeText(this, "Select an item first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            controller.deleteRental(selectedItem!!.ID)

            clearForm()
            refreshList()
            selectedItem = null

            Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFormData(): rent_Rentals? {
        val name = etName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val plate = etPlate.text.toString().trim()
        val mileageText = etMileage.text.toString().trim()
        val license = etDriverLicense.text.toString().trim()
        val vehicleType = spVehicleType.selectedItem.toString()

        if (name.isEmpty() || lastName.isEmpty() || plate.isEmpty() ||
            mileageText.isEmpty() || license.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return null
        }

        val mileage = mileageText.toIntOrNull()
        if (mileage == null) {
            Toast.makeText(this, "Mileage must be numeric", Toast.LENGTH_SHORT).show()
            return null
        }

        return rent_Rentals(
            name,
            lastName,
            vehicleType,
            plate,
            mileage,
            license
        )
    }

    private fun formatItem(r: rent_Rentals): String {
        return "${r.clientName} - ${r.vehicleType} - ${r.plate}"
    }

    private fun refreshList() {
        adapter.clear()
        adapter.addAll(controller.getAllRentals().map { formatItem(it) })
        adapter.notifyDataSetChanged()
    }

    private fun clearForm() {
        etName.text.clear()
        etLastName.text.clear()
        etPlate.text.clear()
        etMileage.text.clear()
        etDriverLicense.text.clear()
        spVehicleType.setSelection(0)
    }
}

