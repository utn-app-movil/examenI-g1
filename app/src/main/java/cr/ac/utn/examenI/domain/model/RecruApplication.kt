package cr.ac.utn.examenI.domain.model
import java.math.BigDecimal
import java.time.LocalDateTime

data class RecruApplication(
    override val id: String,
    val applicant: Person,
    val province: String,
    val canton: String,
    val district: String,
    val targetCompany: String,
    val roles: Set<RecruRole>,
    val salaryExpectation: BigDecimal,
    val experienceYears: Int,
    val registrationDateTime: LocalDateTime
) : Identifier(id) {
    override val fullName: String
        get() = applicant.fullName
}

enum class RecruRole {
    SOFTWARE,
    INFRASTRUCTURE,
    NETWORKING,
    SECURITY,
    QA
}