package cr.ac.utn.examenI.domain.model

import java.time.LocalDate

class CliClient(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val companyName: String = "",
    val corporateId: String = "",
    val province: String = "",
    val canton: String = "",
    val district: String = "",
    val services: String = ""
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "$companyName ($corporateId) - $name $firstName".trim()
}
