package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class MedNursingCheck(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val bloodPressure: String = "",
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val oxygenation: Double = 0.0,
    val checkDateTime: LocalDateTime = LocalDateTime.now()
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "$name $firstName $lastName - BP: $bloodPressure W: $weight H: $height".trim()
}
