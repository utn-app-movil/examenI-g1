package cr.ac.utn.examenI.domain.model

import java.time.LocalDate

class RentRental(
    id: String = "",
    name: String = "",
    firstName: String = "",
    lastName: String = "",
    phone: Int = 0,
    email: String = "",
    address: String = "",
    country: Country = Country.COSTA_RICA,
    birthDate: LocalDate = LocalDate.now(),
    val vehicleType: String = "",
    val plate: String = "",
    val mileage: Double = 0.0,
    val driverLicense: String = ""
) : Person(id, name, firstName, lastName, phone, email, address, country, birthDate) {
    override val fullName: String
        get() = "$name $firstName - $vehicleType ($plate)".trim()
}
