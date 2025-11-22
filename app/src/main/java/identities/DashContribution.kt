package cr.ac.utn.movil.identities

import java.util.Objects

class DashContribution(
    val person: Person,
    val contributions: Int,
    val day: Int,
    val month: Int,
    val year: Int
) : Identifier() {

    override val FullDescription: String
        get() = "${person.FullName} - Contributions: $contributions, Date: $day/$month/$year"

    override val FullName: String
        get() = person.FullName

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DashContribution) return false
        return person.ID == other.person.ID && month == other.month && year == other.year
    }

    override fun hashCode(): Int {
        return Objects.hash(person.ID, month, year)
    }
}
