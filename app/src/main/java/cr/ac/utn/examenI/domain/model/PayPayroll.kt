package cr.ac.utn.examenI.domain.model

import java.time.LocalDate

class PayPayroll(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val employeeNum: String = "",
    val position: String = "",
    val salary: Double = 0.0,
    val iban: String = "",
    val month: Int = 1,
    val bank: String = ""
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "Emp #$employeeNum: $name $firstName - $position ($bank)".trim()
}
