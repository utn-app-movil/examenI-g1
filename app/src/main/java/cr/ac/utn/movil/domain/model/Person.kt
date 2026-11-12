package cr.ac.utn.movil.cr.ac.utn.movil.domain.model

import cr.ac.utn.movil.cr.ac.utn.movil.domain.model.Identifier
import cr.ac.utn.movil.domain.model.Country
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
): Identifier(id) {
    override val fullName = "$name $firstName $lastName"
}