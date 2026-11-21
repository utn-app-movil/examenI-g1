package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class PayenReimbursement(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val specialty: String = "",
    val apptCost: Double = 0.0,
    val medicineCost: Double = 0.0,
    val reimbursementAmount: Double = 0.0,
    val medicines: String = "",
    val apptDateTime: LocalDateTime = LocalDateTime.now(),
    val claimDateTime: LocalDateTime = LocalDateTime.now()
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "$name $firstName - $specialty Reimbursement: $$reimbursementAmount".trim()
}
