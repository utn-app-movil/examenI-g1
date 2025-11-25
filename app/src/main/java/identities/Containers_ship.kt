package identities

import cr.ac.utn.movil.identities.Identifier
import cr.ac.utn.movil.identities.Person
import java.time.LocalDate

class Containers_ship: Identifier {
    private lateinit var _person: Person
    private var _temperature: Int=0
    private var _weight: Int=0
    private lateinit var _dateTime: LocalDate
    private var _type = listOf("Humid", "Dry")
    private var _product = listOf("sand", "cement", "grain", "salt", "sugar", "oil", "solvents", "paints", "syrups")

    constructor()

    var temperature: Int
        get()= this._temperature
        set(value) {this._temperature = value}
    var weight: Int
        get() = this._weight
        set(value) {this._weight = value}

    override val FullName: String
        get() = TODO("Not yet implemented")
    override val FullDescription ="$temperature °C, $weight Kg"

    var dateTime: LocalDate
        get() = this._dateTime
        set(value) {this._dateTime = value}

    val type = _type
    val product = _product
}