package cr.ac.utn.examenI.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class inv_InventoryEntry(
    override val id: String,
    val inv_responsible: Person,
    val inv_productCode: String,
    val inv_productName: String,
    val inv_quantity: Int,
    val inv_entryDateTime: LocalDateTime,
    val inv_supplier: String,
    val inv_unitCost: BigDecimal
) : Identifier(id) {

    override val fullName: String
        get() = inv_productName
}