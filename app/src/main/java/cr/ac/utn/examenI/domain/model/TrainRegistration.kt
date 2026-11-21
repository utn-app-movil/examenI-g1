package cr.ac.utn.examenI.domain.model

import java.time.LocalDateTime

class TrainRegistration(
    id: String,
    name: String,
    firstName: String,
    lastName: String,
    phone: Int,
    email: String,
    address: String,
    val courses: List<TrainCourse>,
    val trainingDateTime: LocalDateTime
) : Person(
    id = id,
    name = name,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    email = email,
    address = address
)