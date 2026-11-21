package cr.ac.utn.examenI.domain.model

import android.location.Location
import java.time.LocalDateTime

/**represents a vaccination record asociated with a client to her personal informacion
 * like vaccination personal history*/
class VacRecord (
    id: String,
    name: String,
    firstName: String,
    lastName: String,
    phone: Int,
    email: String,
    address: String,

    val vaccineType: String,
    val location: String,
    val vaccinationDateTime: LocalDateTime


    /*The VacRecord class inherits the attributes of Person*/
): Person(
    id = id,
    name = name,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    email = email,
    address = address
)
