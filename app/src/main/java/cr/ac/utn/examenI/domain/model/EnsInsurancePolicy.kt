package cr.ac.utn.examenI.domain.model

import java.time.LocalDate

class EnsInsurancePolicy(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val policyNum: String = "",
    val company: String = "",
    val insuranceType: String = "",
    val premium: Double = 0.0,
    val startDate: LocalDate = LocalDate.now(),
    val expirationDate: LocalDate = LocalDate.now().plusYears(1)
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "Policy #$policyNum ($company - $insuranceType)".trim()
}
