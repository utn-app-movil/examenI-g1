package cr.ac.utn.examenI.domain.model

class DashContribution(
    val person: Person,
    val contributionsTotal: Double,
    val month: Int,
    val year: Int
) : Identifier("${person.id}-$month-$year") {

    // the list adapter shows fullName, so we put the dashboard info here
    override val fullName: String
        get() = "${person.fullName} - $contributionsTotal - $month/$year"
}