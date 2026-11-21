package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class FliFlight(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val originCountry: Country = Country.COSTA_RICA,
    val destCountry: Country = Country.UNITED_STATES,
    val flightNumber: String = "",
    val flightDateTime: LocalDateTime = LocalDateTime.now()
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "$flightNumber: ${originCountry.code} -> ${destCountry.code} ($name $firstName)".trim()
}
