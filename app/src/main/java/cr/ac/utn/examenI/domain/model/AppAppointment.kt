package cr.ac.utn.examenI.domain.model

import java.time.LocalDateTime

data class AppAppointment(
    override val id: String,
    val patientName: String,
    val firstSurname: String,
    val secondSurname: String,
    val phone: String,
    val email: String,
    val address: String,
    val doctorName: String,
    val specialty: AppSpecialty,
    val dateTime: LocalDateTime
) : Identifier(id) {
    override val fullName: String
        get() = listOf(patientName, firstSurname, secondSurname)
            .filter { it.isNotBlank() }
            .joinToString(" ")
}
