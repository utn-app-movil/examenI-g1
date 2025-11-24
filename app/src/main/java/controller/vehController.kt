package controller

import android.content.Context
import android.widget.Toast
import identities.Vehicle

class vehController {
    private val vehicles = mutableListOf<Vehicle>()

    fun saveVehicle(context: Context, vehicle: Vehicle) {
        vehicles.add(vehicle)
        Toast.makeText(context, "Vehículo guardado: ${vehicle.vehicleId}", Toast.LENGTH_LONG).show()
    }

    fun getVehicles(): List<Vehicle> {
        return vehicles
    }
}
