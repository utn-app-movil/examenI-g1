package cr.ac.utn.movil.entities

import cr.ac.utn.movil.identities.Identifier
import java.util.*
import kotlin.text.format

data class InsurancePolicy(
    var policyNumber_ens: String = "",
    var company_ens: String = "",
    var insuranceType_ens: String = "",
    var startDate_ens: Date = Date(),
    var expirationDate_ens: Date = Date(),
    var premium_ens: Double = 0.0
) : Identifier() {

    override var ID: String
        get() = policyNumber_ens
        set(value) { policyNumber_ens = value }

    override val FullDescription: String
        get() = "$company_ens - $insuranceType_ens ($${String.format("%,.2f", premium_ens)})"

    override val FullName: String
        get() = "Policy $policyNumber_ens"
}
