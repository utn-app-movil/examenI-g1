package cr.ac.utn.examenI.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

class ExchTransaction(
    override val id: String = "",
    override val name: String = "",
    override val firstName: String = "",
    override val lastName: String = "",
    override val phone: Int = 0,
    override val email: String = "",
    override val address: String = "",
    override val country: Country = Country.COSTA_RICA,
    override val birthDate: LocalDate = LocalDate.now(),
    val currencyType: String = "USD",
    val exchangeRate: Double = 0.0,
    val amountToExchange: Double = 0.0,
    val totalAmountToPay: Double = 0.0,
    val bank: String = "",
    val exchangeDateTime: LocalDateTime = LocalDateTime.now()
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate)
