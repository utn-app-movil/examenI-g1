package cr.ac.utn.movil.identities

import java.util.Date

class WatWaterQualityMonitoring : Identifier() {
    var nisId: String = ""
    var measurementDate: Date = Date()
    var phLevel: Double = 0.0
    var turbidity: Double = 0.0
    var detectedContaminants: MutableList<String> = mutableListOf()
    var owner: WatOwner = WatOwner()

    override val FullDescription: String
        get() = "NIS: $nisId - Date: $measurementDate - pH: $phLevel - Turbidity: $turbidity - Owner: ${owner.fullName}"

    override val FullName: String
        get() = "Water Quality Monitoring - NIS: $nisId"

    init {
        this.ID = nisId
    }

    fun updateId() {
        this.ID = nisId
    }
}

class WatOwner {
    var fullName: String = ""
    var identificationNumber: String = ""
    var phone: String = ""
    var address: String = ""

    override fun toString(): String {
        return "$fullName - ID: $identificationNumber"
    }
}