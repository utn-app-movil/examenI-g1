package cr.ac.utn.examenI.domain.model

import java.time.LocalDateTime

class VehCharge(
    id: String,
    name: String,
    firstName: String,
    lastName: String,
    phone: Int,
    email: String,
    address: String,
    val vehicleId: String,
    val rangeKm: Double,
    val initialBatteryPct: Double,
    val finalBatteryPct: Double,
    val brand: String,
    val vehicleType: String,
    val chargeDateTime: LocalDateTime
) : Person(id, name, firstName, lastName, phone, email, address)