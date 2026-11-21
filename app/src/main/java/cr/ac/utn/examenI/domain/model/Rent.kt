package cr.ac.utn.examenI.domain.model

import java.time.LocalDate

class Rent (
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
    val mileage: Int = 0,
    val driverLicense: String = ""
): Person (
    id=id,
    name=name,
    firstName=firstName,
    lastName=lastName,
    phone=phone,
    email=email,
    address=address,
    country=country,
    birthDate=birthDate
){
    override val fullName: String
        get() = "${super.fullName} - $vehicleType ($plate)".trim()
}