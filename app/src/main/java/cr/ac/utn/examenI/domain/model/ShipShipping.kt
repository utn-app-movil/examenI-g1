package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class ShipShipping(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val containerNum: String = "",
    val containerType: String = "",
    val temperature: Double = 0.0,
    val weight: Double = 0.0,
    val product: String = "",
    val shipmentDateTime: LocalDateTime = LocalDateTime.now()
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "Container #$containerNum ($containerType, $product)".trim()
}
