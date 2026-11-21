package cr.ac.utn.examenI.domain.model

import java.time.LocalDateTime

class ProdOrder(
    override val id: String = "",
    val orderNumber: String = "",
    val products: List<String> = emptyList(),
    val quantity: Int = 0,
    val targetCompany: String = "",
    val startDate: LocalDateTime = LocalDateTime.now(),
    val deliveryDateTime: LocalDateTime = LocalDateTime.now()
) : Identifier(id) {
    override val fullName: String
        get() = "Order #$orderNumber - Company: $targetCompany"
}
