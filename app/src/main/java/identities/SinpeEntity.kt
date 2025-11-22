package cr.ac.utn.movil.entity

import cr.ac.utn.movil.identities.Identifier
import java.util.Date

class SinpeEntity(
    override var FullDescription: String,
    override var FullName: String
) : Identifier() {
    var sinOriginName: String = ""
    var sinOriginPhone: String = ""
    var sinDestName: String = ""
    var sinDestPhone: String = ""
    var sinAmount: Double = 0.0
    var sinDescription: String = ""
    var sinDateTime: Date = Date()
}