package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class WatWaterMonitoring(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val nisId: String = "",
    val ph: Double = 0.0,
    val turbidity: Double = 0.0,
    val contaminants: String = "",
    val monitoringDateTime: LocalDateTime = LocalDateTime.now()
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "NIS: $nisId - pH: $ph, Turbidity: $turbidity".trim()
}
