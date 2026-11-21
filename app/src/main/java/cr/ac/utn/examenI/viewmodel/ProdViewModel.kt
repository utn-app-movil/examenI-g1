package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.ProdProductionOrder
import cr.ac.utn.examenI.domain.repository.IRepository

class ProdViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<ProdProductionOrder> = repository.getAll().filterIsInstance<ProdProductionOrder>()
        fun getById(id: String): ProdProductionOrder? = repository.getById(id) as? ProdProductionOrder
        fun save(item: ProdProductionOrder) = repository.add(item)
        fun update(item: ProdProductionOrder) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
