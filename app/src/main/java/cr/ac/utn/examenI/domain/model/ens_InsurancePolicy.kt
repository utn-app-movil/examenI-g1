
package cr.ac.utn.examenI.domain.model

class ens_InsurancePolicy(
    override val id: String,
    val company: String,
    val insuranceType: String,
    val startDate: String,
    val expirationDate: String,
    val premium: Double
) : Identifier(id) {

    override val fullName: String
        get() = company
}
