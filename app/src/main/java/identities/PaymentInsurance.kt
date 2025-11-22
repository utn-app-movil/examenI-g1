package identities

import cr.ac.utn.movil.identities.Person
import java.time.LocalDate

class PaymentInsurance {
    private var personId: String = ""
    private var personName: String = ""
    private var personEmail: String = ""
    private var speciality: String = ""
    private lateinit var appointmentDate: LocalDate
    private lateinit var requestDate: LocalDate
    private var dateCost: Float = 0.0F
    private var medsCost: Float = 0.0F
    private var meds: MutableList<String> = mutableListOf()

    constructor()

    constructor(
        personId: String,
        personName: String,
        personEmail: String,
        speciality: String,
        appointmentDate: LocalDate,
        requestDate: LocalDate,
        dateCost: Float,
        medsCost: Float,
        meds: MutableList<String>
    ){
        this.personId = personId
        this.personName = personName
        this.personEmail = personEmail
        this.speciality = speciality
        this.appointmentDate = appointmentDate
        this.requestDate = requestDate
        this.dateCost = dateCost
        this.medsCost = medsCost
        this.meds = meds
    }

    var PersonId: String
        get() = this.personId
        set(value) {this.personId = value}

    var PersonName: String
        get() = this.personName
        set(value) {this.personName = value}

    var PersonEmail: String
        get() = this.personEmail
        set(value) {this.personEmail = value}

    var Speciality: String
        get() = this.speciality
        set(value) {this.speciality = value}

    var AppointmentDate: LocalDate
        get() = this.appointmentDate
        set(value) {this.appointmentDate = value}

    var RequestDate: LocalDate
        get() = this.requestDate
        set(value) {this.requestDate = value}

    var DateCost: Float
        get() = this.dateCost
        set(value) {this.dateCost = value}

    var MedsCost: Float
        get() = this.medsCost
        set(value) {this.medsCost = value}

    var Meds: MutableList<String>
        get() = this.meds
        set(value) {this.meds = value}

}