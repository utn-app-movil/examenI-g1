package cr.ac.utn.examenI.domain.model

import java.time.LocalDateTime

data class ShipMaintenance(
    override val id: String,
    val person: Person,
    val maintenanceDateTime: LocalDateTime,
    val containerType: String,
    val temperature: Double,
    val weight: Double,
    val product: String
) : Identifier(id) {

    override val fullName: String
        get() = person.fullName
}
