package identities

import cr.ac.utn.movil.identities.Identifier
import java.time.LocalDate
import java.time.LocalTime

class Vaccine() : Identifier() {

    var Name: String = ""
    var FirstName: String = ""
    var VaccineType: String = ""
    var Site: String = ""
    var Date: LocalDate = LocalDate.now()
    var Time: LocalTime = LocalTime.now()

    constructor(
        id: String,
        name: String,
        firstName: String,
        vaccineType: String,
        site: String,
        date: LocalDate,
        time: LocalTime
    ) : this() {
        this.ID = id
        this.Name = name
        this.FirstName = firstName
        this.VaccineType = vaccineType
        this.Site = site
        this.Date = date
        this.Time = time
    }

    // Obligatorio por Identifier
    override val FullName: String
        get() = "$Name $FirstName"

    // Obligatorio por Identifier
    override val FullDescription: String
        get() = "Vacuna: $VaccineType - Paciente: $Name $FirstName - Sitio: $Site"
}
