package cr.ac.utn.movil

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import controller.vehController
import identities.Vehicle

class vehActivity : AppCompatActivity() {

    private lateinit var vehController: vehController
    private lateinit var vehicleAdapter: VehicleAdapter
    private lateinit var recyclerViewVehicles: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veh)

        vehController = vehController()

        val editTextVehicleId: EditText = findViewById(R.id.editTextVehicleId)
        val editTextChargeDate: EditText = findViewById(R.id.editTextChargeDate)
        val editTextEstimatedAutonomy: EditText = findViewById(R.id.editTextEstimatedAutonomy)
        val editTextInitialBattery: EditText = findViewById(R.id.editTextInitialBattery)
        val editTextFinalBattery: EditText = findViewById(R.id.editTextFinalBattery)
        val spinnerVehicleBrand: Spinner = findViewById(R.id.spinnerVehicleBrand)
        val editTextOwnerName: EditText = findViewById(R.id.editTextOwnerName)
        val spinnerVehicleType: Spinner = findViewById(R.id.spinnerVehicleType)
        val buttonSave: Button = findViewById(R.id.buttonSave)
        recyclerViewVehicles = findViewById(R.id.recyclerViewVehicles)

        // Populate Spinners
        val vehicleBrands = arrayOf("Tesla", "BYD", "Ford", "Hyundai", "Kia")
        val vehicleBrandAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleBrands)
        spinnerVehicleBrand.adapter = vehicleBrandAdapter

        val vehicleTypes = arrayOf("sedan", "carga", "suv", "van")
        val vehicleTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleTypes)
        spinnerVehicleType.adapter = vehicleTypeAdapter

        // Initialize RecyclerView
        recyclerViewVehicles.layoutManager = LinearLayoutManager(this)
        updateVehicleList()

        buttonSave.setOnClickListener {
            val vehicleId = editTextVehicleId.text.toString()
            val chargeDate = editTextChargeDate.text.toString()
            val estimatedAutonomy = editTextEstimatedAutonomy.text.toString().toDoubleOrNull()
            val initialBattery = editTextInitialBattery.text.toString().toIntOrNull()
            val finalBattery = editTextFinalBattery.text.toString().toIntOrNull()
            val ownerName = editTextOwnerName.text.toString()

            if (vehicleId.isEmpty() || chargeDate.isEmpty() || estimatedAutonomy == null || initialBattery == null || finalBattery == null || ownerName.isEmpty()) {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (estimatedAutonomy <= 0) {
                Toast.makeText(this, "La autonomía debe ser mayor a 0 km", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (finalBattery > 100) {
                Toast.makeText(this, "El porcentaje final no puede exceder 100%", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (initialBattery > finalBattery) {
                Toast.makeText(this, "El porcentaje inicial no puede ser mayor al final", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val vehicle = Vehicle(
                vehicleId = vehicleId,
                chargeDate = chargeDate,
                estimatedAutonomy = estimatedAutonomy,
                initialBattery = initialBattery,
                finalBattery = finalBattery,
                brand = spinnerVehicleBrand.selectedItem.toString(),
                ownerName = ownerName,
                type = spinnerVehicleType.selectedItem.toString()
            )

            vehController.saveVehicle(this, vehicle)
            updateVehicleList()
        }
    }

    private fun updateVehicleList() {
        vehicleAdapter = VehicleAdapter(vehController.getVehicles())
        recyclerViewVehicles.adapter = vehicleAdapter
    }
}
