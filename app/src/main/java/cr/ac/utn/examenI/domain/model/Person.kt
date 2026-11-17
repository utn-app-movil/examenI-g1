package cr.ac.utn.examenI.domain.model

import java.time.LocalDate

data class Person(
    override val id: String,
    val name: String,
    val firstName: String,
    val lastName: String,
    val phone: Int,
    val email: String,
    val address: String,
    val country: Country,
    val birthDate: LocalDate
): Identifier(id) {
    override val fullName = "$name $firstName $lastName"
}