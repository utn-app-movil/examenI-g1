package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.Identifier
import cr.ac.utn.examenI.domain.model.VehCharge
import cr.ac.utn.examenI.domain.repository.IRepository

class VehViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<Identifier> {
            return repository.getAll()
        }

        fun getById(id: String): VehCharge? {
            return repository.getById(id) as VehCharge?
        }

        fun save(item: VehCharge) {
            repository.add(item)
        }

        fun update(item: VehCharge) {
            repository.update(item)
        }

        fun delete(id: String) {
            repository.delete(id)
        }
    }
}