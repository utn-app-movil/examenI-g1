package cr.ac.utn.examenI.domain.model

class PayEmployee(
    id: String,
    name: String,
    firstName: String,
    lastName: String,
    phone: Int,
    email: String,
    address: String,
    val employeeNum: String,
    val position: String,
    val salary: Double,
    val iban: String,
    val month: Int,
    val bank: String
) : Person(
    id = id,
    name = name,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    email = email,
    address = address
)