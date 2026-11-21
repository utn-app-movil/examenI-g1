package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class VacVaccination(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val vaccineType: String = "",
    val location: String = "",
    val vaccinationDateTime: LocalDateTime = LocalDateTime.now()
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "$name $firstName $lastName - $vaccineType ($location)".trim()
}
