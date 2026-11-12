package cr.ac.utn.examenI.domain.model

import cr.ac.utn.examenI.domain.model.Country

data class Person(
    override val id: String,
    val name: String,
    val firstName: String,
    val lastName: String,
    val phone: Int,
    val email: String,
    val address: String,
    val country: Country,
): Identifier(id) {
    override val fullName = "$name $firstName $lastName"
}