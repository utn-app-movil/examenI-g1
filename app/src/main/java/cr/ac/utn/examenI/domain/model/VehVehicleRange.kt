package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class VehVehicleRange(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val vehicleId: String = "",
    val rangeKm: Double = 0.0,
    val initialBatteryPct: Double = 0.0,
    val finalBatteryPct: Double = 0.0,
    val brand: String = "",
    val vehicleType: String = "",
    val chargeDateTime: LocalDateTime = LocalDateTime.now()
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "$brand $vehicleType ($vehicleId) - Range: ${rangeKm}km".trim()
}
