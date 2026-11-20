package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class ExchExchangeRate(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val exchangeRate: Double = 0.0,
    val amountToExchange: Double = 0.0,
    val currencyType: String = "",
    val bank: String = "",
    val totalAmountToPay: Double = 0.0,
    val exchangeDateTime: LocalDateTime = LocalDateTime.now()
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "$bank: $amountToExchange $currencyType -> Total: $$totalAmountToPay".trim()
}
