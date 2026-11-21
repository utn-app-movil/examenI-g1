package cr.ac.utn.examenI.domain.model

import java.time.LocalDateTime

class Med (
    id: String,
    name: String,
    firstName: String,
    lastName: String,
    phone: Int,
    email: String,
    address: String,
    val bloodPressure: String,
    val weight: Double,
    val height: Double,
    val oxygenation: Int,
    val checkDateTime: LocalDateTime
) : Person (id,name, firstName, lastName, phone, email, address)