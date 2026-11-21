package cr.ac.utn.examenI.domain.model

import java.time.LocalDate

data class Notif(
    override val id: String = "",
    override val name: String = "",
    override val firstName: String = "",
    override val lastName: String = "",
    override val phone: Int = 0,
    override val email: String = "",
    override val address: String = "",
    val emailTitle: String = "",
    val message: String = "",
    val cc: String = "",
    val cco: String = "",
    val targetRecipients: List<String> = emptyList(),
    val sendDate: LocalDate = LocalDate.now().plusDays(1)
) : Person(id, name, firstName, lastName, phone, email, address) {
    override val fullName: String
        get() = "$emailTitle - Sender: ${super.fullName}".trim()
}
