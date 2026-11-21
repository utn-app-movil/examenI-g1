package cr.ac.utn.examenI.domain.model

import java.time.LocalDateTime

class LibReservation (
    id: String,
    name: String,
    firstName: String,
    lastName: String,
    phone: Int,
    email: String,
    address: String,
    val reservedBooks: List<String>,
    val branch: String,
    val reservationDateTime: LocalDateTime,
    val returnDateTime: LocalDateTime
): Person(id, name, firstName, lastName, phone, email, address)