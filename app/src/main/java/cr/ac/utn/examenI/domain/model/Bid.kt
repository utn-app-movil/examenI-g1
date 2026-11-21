
package cr.ac.utn.examenI.domain.model

import java.time.LocalDateTime

data class Bid(
    override val id: String,
    val personId: String,
    override val name: String,
    override val firstName: String,
    override val lastName: String,
    override val phone: Int,
    override val email: String,
    override val address: String,
    val articleCode: String,
    val description: String,
    val bidAmount: Double,
    val awarded: Boolean,
    val bidDateTime: LocalDateTime
) : Person(
    id = id,
    name = name,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    email = email,
    address = address
)
