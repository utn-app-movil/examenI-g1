package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class InvInventory(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val productCode: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val supplier: String = "",
    val unitCost: Double = 0.0,
    val entryDateTime: LocalDateTime = LocalDateTime.now()
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "$productCode: $productName (Qty: $quantity, Supplier: $supplier)".trim()
}
