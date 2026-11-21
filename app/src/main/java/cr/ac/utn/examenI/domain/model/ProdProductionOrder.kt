package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class ProdProductionOrder(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val orderNum: String = "",
    val products: String = "",
    val quantity: Int = 0,
    val targetCompany: String = "",
    val startDate: LocalDateTime = LocalDateTime.now(),
    val deliveryDateTime: LocalDateTime = LocalDateTime.now().plusDays(7)
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "Order #$orderNum: $targetCompany - Qty: $quantity ($products)".trim()
}
