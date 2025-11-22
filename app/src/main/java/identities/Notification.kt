package identities

import cr.ac.utn.movil.identities.Identifier
import java.time.LocalDate

class Notification : Identifier {
    private lateinit var id: String
    private lateinit var senderName: String
    private lateinit var senderFLName: String
    private lateinit var emailTittle: String
    private lateinit var scheduleDate: LocalDate
    private var ccList: MutableList<String> = mutableListOf()
    private var ccoList: MutableList<String> = mutableListOf()


    constructor()

    constructor(
        id: String,
        senderName: String,
        senderFLName: String,
        emailTittle: String,
        scheduleDate: LocalDate,
        ccList: MutableList<String>,
        ccoList: MutableList<String>
    ) {
        this.id = id
        this.senderName = senderName
        this.senderFLName = senderFLName
        this.emailTittle = emailTittle
        this.scheduleDate = scheduleDate
        this.ccList = ccList
        this.ccoList = ccoList
    }

    var Id: String
        get() = this.id
        set(value) {this.id = value}

    var SenderName: String
        get() = this.senderName
        set(value) {this.senderName = value}

    var SenderFLName: String
        get() = this.senderFLName
        set(value) {this.senderFLName = value}

    var EmailTittle: String
        get() = this.emailTittle
        set(value) {this.emailTittle = value}

    var ScheduleDate: LocalDate
        get() = this.scheduleDate
        set(value) {this.scheduleDate = value}

    var CcList: MutableList<String>
        get() = this.ccList
        set(value) { this.ccList = value }

    var CcoList: MutableList<String>
        get() = this.ccoList
        set(value) { this.ccoList = value }

    // NO SÉ PARA QUE SE USAN PERO ES NECESARIO IMPLEMENTAR LAS SIGUIENTES VARIABLES
    override val FullDescription: String
        get() = TODO("Not yet implemented")

    override val FullName: String
        get() = TODO("Not yet implemented")

}