package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.VacVaccination
import cr.ac.utn.examenI.domain.repository.IRepository

class VacViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<VacVaccination> = repository.getAll().filterIsInstance<VacVaccination>()
        fun getById(id: String): VacVaccination? = repository.getById(id) as? VacVaccination
        fun save(item: VacVaccination) = repository.add(item)
        fun update(item: VacVaccination) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
