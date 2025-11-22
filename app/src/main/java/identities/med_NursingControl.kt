package cr.ac.utn.movil.data.entities

import cr.ac.utn.movil.identities.Identifier

class med_NursingControl : Identifier() {

    // NO toques ID → ya existe en la clase padre y funciona perfecto
    // Solo usa: registro.ID = "123" o registro.ID

    var patientName: String = ""
    var bloodPressure: String = ""
    var weightKg: Double = 0.0
    var heightCm: Double = 0.0
    var oxygenSaturation: Int = 0
    var dateTime: Long = 0L

    override val FullDescription: String
        get() = "$patientName | $bloodPressure | $weightKg kg | $heightCm cm | $oxygenSaturation% | ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(java.util.Date(dateTime))}"

    override val FullName: String
        get() = patientName
}