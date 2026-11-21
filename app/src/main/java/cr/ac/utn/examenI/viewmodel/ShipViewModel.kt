package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.ShipMaintenance
import cr.ac.utn.examenI.domain.repository.IRepository

class ShipViewModel : ViewModel() {

    companion object {

        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<ShipMaintenance> {
            return repository.getAll().filterIsInstance<ShipMaintenance>()
        }

        fun getById(id: String): ShipMaintenance? {
            return repository.getById(id) as? ShipMaintenance
        }

        fun save(item: ShipMaintenance) {
            repository.add(item)
        }

        fun update(item: ShipMaintenance) {
            repository.update(item)
        }

        fun delete(id: String) {
            repository.delete(id)
        }
    }
}
