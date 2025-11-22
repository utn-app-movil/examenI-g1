package cr.ac.utn.movil

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DataRegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dataregister)

        val editTextVehicleId: EditText = findViewById(R.id.editTextVehicleId)
        val editTextChargeDate: EditText = findViewById(R.id.editTextChargeDate)
        val editTextEstimatedAutonomy: EditText = findViewById(R.id.editTextEstimatedAutonomy)
        val editTextInitialBattery: EditText = findViewById(R.id.editTextInitialBattery)
        val editTextFinalBattery: EditText = findViewById(R.id.editTextFinalBattery)
        val spinnerVehicleBrand: Spinner = findViewById(R.id.spinnerVehicleBrand)
        val editTextOwnerName: EditText = findViewById(R.id.editTextOwnerName)
        val spinnerVehicleType: Spinner = findViewById(R.id.spinnerVehicleType)
        val buttonSave: Button = findViewById(R.id.buttonSave)

        // Populate Spinners
        val vehicleBrands = arrayOf("Tesla", "BYD", "Ford", "Hyundai", "Kia")
        val vehicleBrandAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleBrands)
        spinnerVehicleBrand.adapter = vehicleBrandAdapter

        val vehicleTypes = arrayOf("sedan", "carga", "suv", "van")
        val vehicleTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleTypes)
        spinnerVehicleType.adapter = vehicleTypeAdapter

        buttonSave.setOnClickListener {
            val autonomy = editTextEstimatedAutonomy.text.toString().toDoubleOrNull() ?: 0.0
            val finalBattery = editTextFinalBattery.text.toString().toIntOrNull() ?: 0

            if (autonomy <= 0) {
                Toast.makeText(this, "La autonomía debe ser mayor a 0 km", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (finalBattery > 100) {
                Toast.makeText(this, "El porcentaje final no puede exceder 100%", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: Save data
            Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
        }
    }
}
