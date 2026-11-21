package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.Identifier
import cr.ac.utn.examenI.domain.model.Person
import java.time.LocalDateTime

class PayenViewModel : ViewModel() {

    companion object {
        private val repository = MemoryDataManager()

        fun getAll(): List<Identifier> {
            return repository.getAll()
        }

        fun getById(id: String): PayenClaim? {
            return repository.getById(id) as? PayenClaim
        }

        fun save(claim: PayenClaim) {
            repository.add(claim)
        }

        fun update(claim: PayenClaim) {
            repository.update(claim)
        }

        fun delete(id: String) {
            repository.delete(id)
        }


        fun getPersonById(personId: String, excludeClaimId: String = ""): Person? {
            return getAll()
                .filterIsInstance<PayenClaim>()
                .firstOrNull { it.person.id == personId && it.id != excludeClaimId }
                ?.person
        }
    }
}


class PayenClaim(
    override val id: String,
    val person: Person,
    val specialty: String,
    val apptDateTime: LocalDateTime,
    val claimDateTime: LocalDateTime,
    val apptCost: Double,
    val medicineCost: Double,
    val medicines: List<String>
) : Identifier(id) {

    override val fullName: String
        get() = person.fullName

    /** Monto a reembolsar: el 80% de la suma de la cita y los medicamentos. */
    val reimbursementAmount: Double
        get() = (apptCost + medicineCost) * REIMBURSEMENT_RATE

    companion object {
        /** Porcentaje del total que cubre el seguro (80%). */
        const val REIMBURSEMENT_RATE = 0.80

        const val MEDICINE_ANALGESICS = "Analgésicos"
        const val MEDICINE_ANTIBIOTICS = "Antibióticos"
        const val MEDICINE_ANTI_INFLAMMATORIES = "Antiinflamatorios"
    }
}