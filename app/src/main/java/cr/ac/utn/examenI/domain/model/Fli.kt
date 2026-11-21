package cr.ac.utn.examenI.domain.model

import java.time.LocalDateTime

class Fli(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    val originCountry: Country = Country.COSTA_RICA,
    val destCountry: Country = Country.UNITED_STATES,
    val flightNumber: String = "",
    val flightDateTime: LocalDateTime = LocalDateTime.now()
) : Person(id, name, firstName, lastName, phone, email, address, originCountry, flightDateTime.toLocalDate())
