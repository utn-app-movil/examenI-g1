package cr.ac.utn.examenI.domain.model

import java.time.LocalDateTime

data class Sinpe(
    override val id: String = "",
    val originName: String = "",
    val originFirstName: String = "",
    val originLastName: String = "",
    val originPhone: String = "",
    val email: String = "",
    val address: String = "",
    val recipientPhone: String = "",
    val recipientName: String = "",
    val amount: Double = 0.0,
    val description: String = "",
    val sinpeDateTime: LocalDateTime = LocalDateTime.now()
) : Identifier(id) {
    override val fullName: String
        get() = "$originName $originFirstName $originLastName".trim()
}
