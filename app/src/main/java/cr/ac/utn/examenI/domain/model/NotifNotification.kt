package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class NotifNotification(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val emailTitle: String = "",
    val message: String = "",
    val cc: String = "",
    val cco: String = "",
    val targetRecipients: String = "",
    val sendDateTime: LocalDateTime = LocalDateTime.now()
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "Email: $emailTitle -> $targetRecipients".trim()
}
