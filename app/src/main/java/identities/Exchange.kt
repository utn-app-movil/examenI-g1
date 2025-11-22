package cr.ac.utn.movil.identities

import cr.ac.utn.movil.identities.Identifier
import java.time.LocalDateTime

class Exchange : Identifier() {

    var personName: String = ""
    var personFLastName: String = ""
    var personSLastName: String = ""
    var phone: Int = 0
    var email: String = ""
    var address: String = ""
    var country: String = ""

    var exchangeType: String = ""   // USD→CRC or CRC→USD
    var rate: Double = 0.0          // Exchange rate value
    var amount: Double = 0.0        // Amount to be exchanged
    var bankEntity: String = ""     // Selected bank
    var dateTime: LocalDateTime = LocalDateTime.now()

    override val FullDescription: String
        get() = "$FullName - $exchangeType - $amount at rate $rate"

    override val FullName: String
        get() = "$personName $personFLastName $personSLastName"
}

