package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.PayPayroll
import cr.ac.utn.examenI.domain.repository.IRepository

class PayViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<PayPayroll> = repository.getAll().filterIsInstance<PayPayroll>()
        fun getById(id: String): PayPayroll? = repository.getById(id) as? PayPayroll
        fun save(item: PayPayroll) = repository.add(item)
        fun update(item: PayPayroll) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
