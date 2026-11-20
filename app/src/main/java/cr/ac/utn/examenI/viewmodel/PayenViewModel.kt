package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.PayenReimbursement
import cr.ac.utn.examenI.domain.repository.IRepository

class PayenViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<PayenReimbursement> = repository.getAll().filterIsInstance<PayenReimbursement>()
        fun getById(id: String): PayenReimbursement? = repository.getById(id) as? PayenReimbursement
        fun save(item: PayenReimbursement) = repository.add(item)
        fun update(item: PayenReimbursement) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
