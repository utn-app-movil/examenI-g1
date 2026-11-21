package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.ProdOrder

class ProdViewModel : ViewModel() {
    companion object {
        private val repository = MemoryDataManager()

        fun getAll(): List<ProdOrder> {
            return repository.getAll().filterIsInstance<ProdOrder>()
        }

        fun getById(id: String): ProdOrder? {
            return repository.getById(id) as? ProdOrder
        }

        fun save(obj: ProdOrder) {
            repository.add(obj)
        }

        fun update(obj: ProdOrder) {
            repository.update(obj)
        }

        fun delete(id: String) {
            repository.delete(id)
        }
    }
}
