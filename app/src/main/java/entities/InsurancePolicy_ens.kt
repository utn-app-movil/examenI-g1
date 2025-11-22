package cr.ac.utn.movil.entities

import cr.ac.utn.movil.identities.Identifier
import java.util.*

data class InsurancePolicy(
    var policyNumber: String = "",
    var company: String = "",
    var insuranceType: String = "",
    var startDate: Date = Date(),
    var expirationDate: Date = Date(),
    var premium: Double = 0.0
) : Identifier() {

    override var ID: String
        get() = policyNumber
        set(value) { policyNumber = value }

    override val FullDescription: String
        get() = "$company - $insuranceType ($${String.format("%,.2f", premium)})"

    override val FullName: String
        get() = "Policy $policyNumber"

    override fun toString(): String {
        val fmt = java.text.SimpleDateFormat("MM/dd/yyyy", java.util.Locale.getDefault())
        return "$policyNumber\n$company - $insuranceType\n${fmt.format(startDate)} → ${fmt.format(expirationDate)}\nPremium: $${String.format("%,.2f", premium)}"
    }
}