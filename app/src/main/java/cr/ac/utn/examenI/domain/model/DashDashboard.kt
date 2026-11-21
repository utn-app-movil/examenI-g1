package cr.ac.utn.examenI.domain.model

import java.time.LocalDate

class DashDashboard(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val contributionsTotal: Double = 0.0,
    val month: Int = 1,
    val year: Int = 2025
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "$name $firstName - Month: $month/$year Total: $$contributionsTotal".trim()
}
