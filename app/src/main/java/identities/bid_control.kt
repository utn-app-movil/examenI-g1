package identities

import cr.ac.utn.movil.identities.Person
import java.time.LocalDateTime

    open class bid_control: Person {

    private lateinit var date: LocalDateTime
    private var amount: Double=0.0
    private var codeArt: Int=0
    private var descriptionArt: String=""

    private var adjudicated: Boolean = false

        constructor(date: LocalDateTime, amount: Double, codeArt: Int, descriptionArt: String, adjudicated: Boolean)

        var Date: LocalDateTime
        get() = this.date
        set(value){this.date = value}

        var Amount: Double
        get() = this.amount
        set(value){this.amount = value}

        var Code: Int
        get() = this.codeArt
        set(value){this.codeArt = value}

        var Description: String
        get() = this.descriptionArt
        set(value){this.descriptionArt = value}

        var Adjudicated: Boolean
        get() = this.adjudicated
        set(value){this.adjudicated = value}

        override val FullDescription: String
        get() = "Puja de '${this.Name} ${this.FLastName}' " +
                "por el articulo '${this.descriptionArt}' " +
                "por el monto de = '${this.amount}'"


}