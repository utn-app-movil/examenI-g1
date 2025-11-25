package cr.ac.utn.movil.identities


open class mark_Campaign : Identifier {

    // 1. Campaign Properties
    var Code: String = ""
    var Name: String = ""
    var Budget: Double = 0.0
    var StartDate: Long = 0L
    var EndDate: Long = 0L
    var Channel: String = ""
    var Leader: String = ""
    var Province: String = ""

    // 2. Identifier Implementations

    override val FullName: String
        get() = "Campaign: $Name (Code: $Code)"

    override val FullDescription: String
        get() = "Budget: $$Budget | Channel: $Channel | Led by: $Leader | Province: $Province"


    constructor() : super()

    constructor(
        code: String,
        name: String,
        budget: Double,
        startDate: Long,
        endDate: Long,
        channel: String,
        leader: String,
        province: String
    ) : this() {
        this.Code = code
        this.Name = name
        this.Budget = budget
        this.StartDate = startDate
        this.EndDate = endDate
        this.Channel = channel
        this.Leader = leader
        this.Province = province


        this.ID = code
    }
}