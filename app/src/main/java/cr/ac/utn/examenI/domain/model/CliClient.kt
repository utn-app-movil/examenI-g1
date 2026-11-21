package cr.ac.utn.examenI.domain.model

/**
 * Entity representing a Corporate Client.
 * Inherits from the abstract class Identifier.
 */
class CliClient(
    override val id: String,
    val companyName: String,
    val province: String,
    val canton: String,
    val district: String,
    val services: List<String>
) : Identifier(id) {
    override val fullName: String
        get() = companyName
}
