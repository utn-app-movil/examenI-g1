package identities

import cr.ac.utn.movil.identities.Person
import java.util.Date

class recru_Roles : Person {

    // Location data
    private var _province: String = ""
    private var _canton: String = ""
    private var _district: String = ""

    // Additional data for recruitment
    private var _company: String = ""
    private var _rolesOfInterest: MutableList<String> = mutableListOf()
    private var _salaryExpectation: Double = 0.0
    private var _yearsOfExperience: Int = 0
    private var _registrationDate: Date = Date()

    // Empty constructor
    constructor() : super()

    // Constructor with all attributes
    constructor(
        id: String,
        name: String,
        fLastName: String,
        sLastName: String,
        phone: Int,
        email: String,
        address: String,
        country: String,
        province: String,
        canton: String,
        district: String,
        company: String,
        rolesOfInterest: MutableList<String>,
        salaryExpectation: Double,
        yearsOfExperience: Int,
        registrationDate: Date
    ) : super() {
        this.ID = id
        this.Name = name
        this.FLastName = fLastName
        this.SLastName = sLastName
        this.Phone = phone
        this.Email = email
        this.Address = address
        this.Country = country
        this._province = province
        this._canton = canton
        this._district = district
        this._company = company
        this._rolesOfInterest = rolesOfInterest
        this._salaryExpectation = salaryExpectation
        this._yearsOfExperience = yearsOfExperience
        this._registrationDate = registrationDate
    }

    // Getters and Setters
    var province: String
        get() = this._province
        set(value) { this._province = value }

    var canton: String
        get() = this._canton
        set(value) { this._canton = value }

    var district: String
        get() = this._district
        set(value) { this._district = value }

    var company: String
        get() = this._company
        set(value) { this._company = value }

    var rolesOfInterest: MutableList<String>
        get() = this._rolesOfInterest
        set(value) { this._rolesOfInterest = value }

    var salaryExpectation: Double
        get() = this._salaryExpectation
        set(value) { this._salaryExpectation = value }

    var yearsOfExperience: Int
        get() = this._yearsOfExperience
        set(value) { this._yearsOfExperience = value }

    var registrationDate: Date
        get() = this._registrationDate
        set(value) { this._registrationDate = value }

    override val FullDescription: String
        get() = "Registration of $FullName for $company in roles: ${rolesOfInterest.joinToString(", ")}"
}
