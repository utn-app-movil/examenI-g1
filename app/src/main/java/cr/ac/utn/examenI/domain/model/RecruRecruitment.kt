package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class RecruRecruitment(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val province: String = "",
    val canton: String = "",
    val district: String = "",
    val targetCompany: String = "",
    val roles: String = "",
    val salaryExpectation: Double = 0.0,
    val experienceYears: Int = 0,
    val registrationDateTime: LocalDateTime = LocalDateTime.now()
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "$name $firstName - $targetCompany ($roles)".trim()
}
