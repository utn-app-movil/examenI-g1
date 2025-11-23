package identities

import cr.ac.utn.movil.identities.Identifier
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class PharmacySale : Identifier() {

    var PatientId: String = ""
    var PatientName: String = ""
    var MedicineTypes: String = ""   // e.g.: "Cream, Pills"
    var TotalAmount: Double = 0.0
    var PurchaseDateTime: LocalDateTime = LocalDateTime.now()

    override val FullName: String
        get() = "$PatientName ($PatientId)"

    override val FullDescription: String
        get() {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault())
            val dateTimeText = PurchaseDateTime.format(formatter)
            return "$FullName - $MedicineTypes - $${"%.2f".format(TotalAmount)} - $dateTimeText"
        }
}
