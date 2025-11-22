package identities

import cr.ac.utn.movil.identities.Identifier
import java.time.LocalDate
import java.util.Date

class Training: Identifier {

    override  var FullDescription: String = ""

    override var FullName: String = ""
    private var trainingId: String = ""
    private var userId: String = ""
    private var userName: String = ""
    private var userFLastName: String = ""
    private var userSLastName: String = ""
    private var  trainingList: String = ""
    private lateinit var trainingDate: LocalDate

    constructor()

    var TrainingId: String
        get() = this.trainingId
        set(value) {this.trainingId = value}

    var UserId: String
        get() = this.userId
        set(value) {this.userId = value}

    var UserName: String
        get() = this.userName
        set(value) {this.userName = value}

    var UserFLastName: String
        get() = this.userFLastName
        set(value) {this.userFLastName = value}

    var UserSLastname: String
        get() = this.userSLastName
        set(value) {this.userSLastName = value}

    var TrainingList: String
        get() = this.trainingList
        set(value) {this.trainingList = value}

    var TrainingDate: LocalDate
        get() = trainingDate
        set(value) { trainingDate = value }






}