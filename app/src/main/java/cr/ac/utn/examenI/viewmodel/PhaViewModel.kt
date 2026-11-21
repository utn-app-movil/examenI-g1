package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.PhaPharmacy
import cr.ac.utn.examenI.domain.repository.IRepository

class PhaViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<PhaPharmacy> = repository.getAll().filterIsInstance<PhaPharmacy>()
        fun getById(id: String): PhaPharmacy? = repository.getById(id) as? PhaPharmacy
        fun save(item: PhaPharmacy) = repository.add(item)
        fun update(item: PhaPharmacy) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
