package cr.ac.utn.examenI.domain.model

import java.time.LocalDate

class MarkCampaign(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    val code: String = "",
    val campaignName: String = "",
    val budget: Double = 0.0,
    val channel: String = "",
    val leader: String = "",
    val province: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now()
) : Person(id, name, firstName, lastName, phone, email, address) {

    override val fullName: String
        get() = "$code - $campaignName"
}