package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class LibLibrary(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val reservedBooks: String = "",
    val branch: String = "",
    val reservationDateTime: LocalDateTime = LocalDateTime.now(),
    val returnDateTime: LocalDateTime = LocalDateTime.now().plusDays(7)
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "$name $firstName - Books: $reservedBooks ($branch)".trim()
}
