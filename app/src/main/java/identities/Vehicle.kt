package identities

data class Vehicle(
    val vehicleId: String,
    val chargeDate: String,
    val estimatedAutonomy: Double,
    val initialBattery: Int,
    val finalBattery: Int,
    val brand: String,
    val ownerName: String,
    val type: String
)
