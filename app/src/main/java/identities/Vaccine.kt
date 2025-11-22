package identities

import java.time.LocalDate
import java.time.LocalTime

class Vaccine {

    private var id: String=""
    private var name: String=""
    private var firstName: String=""
    private var vaccineType: String=""
    private var site: String=""
    private lateinit var date: LocalDate
    private lateinit var time: LocalTime

    constructor()

    constructor(
        id: String,
        name: String,
        firstName: String,
        vaccineType: String,
        site: String,
        date: LocalDate,
        time: LocalTime
    ) {
        this.id = id
        this.name = name
        this.firstName = firstName
        this.vaccineType = vaccineType
        this.site = site
        this.date = date
        this.time = time
    }

    var ID: String
        get() = id
        set(value) { id = value }

    var Name: String
        get() = name
        set(value) { name = value }

    var FirstName: String
        get() = firstName
        set(value) { firstName = value }

    var VaccineType: String
        get() = vaccineType
        set(value) { vaccineType = value }

    var Site: String
        get() = site
        set(value) { site = value }

    var Date: LocalDate
        get() = date
        set(value) { date = value }

    var Time: LocalTime
        get() = time
        set(value) { time = value }
}
