package cr.ac.utn.movil.identities

import cr.ac.utn.movil.identities.Identifier

class rent_Rentals(
    var clientName: String,
    var lastName: String,
    var vehicleType: String,
    var plate: String,
    var mileage: Int,
    var driverLicense: String,
    override val FullDescription: String,
    override val FullName: String
) : Identifier()
