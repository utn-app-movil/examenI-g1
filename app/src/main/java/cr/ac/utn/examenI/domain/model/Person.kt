package cr.ac.utn.examenI.domain.model

import java.time.LocalDate

open class Person(
    override val id: String = "",
    open val name: String = "",
    open val firstName: String = "",
    open val lastName: String = "",
    open val phone: Int = 0,
    open val email: String = "",
    open val address: String = "",
    open val country: Country = Country.COSTA_RICA,
    open val birthDate: LocalDate = LocalDate.now()
) : Identifier(id) {
    override val fullName: String
        get() = "$name $firstName $lastName".trim()
}
