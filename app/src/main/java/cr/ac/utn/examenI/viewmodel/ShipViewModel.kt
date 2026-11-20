package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.ShipShipping
import cr.ac.utn.examenI.domain.repository.IRepository

class ShipViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<ShipShipping> = repository.getAll().filterIsInstance<ShipShipping>()
        fun getById(id: String): ShipShipping? = repository.getById(id) as? ShipShipping
        fun save(item: ShipShipping) = repository.add(item)
        fun update(item: ShipShipping) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
