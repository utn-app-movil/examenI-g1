package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class SinSinpe(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val originPhone: String = "",
    val recipientPhone: String = "",
    val recipientName: String = "",
    val amount: Double = 0.0,
    val description: String = "",
    val sinpeDateTime: LocalDateTime = LocalDateTime.now()
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "SINPE -> $recipientPhone ($recipientName) - $$amount".trim()
}
