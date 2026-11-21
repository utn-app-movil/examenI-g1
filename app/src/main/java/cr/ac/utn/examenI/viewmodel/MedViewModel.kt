package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.MedNursingCheck
import cr.ac.utn.examenI.domain.repository.IRepository

class MedViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<MedNursingCheck> = repository.getAll().filterIsInstance<MedNursingCheck>()
        fun getById(id: String): MedNursingCheck? = repository.getById(id) as? MedNursingCheck
        fun save(item: MedNursingCheck) = repository.add(item)
        fun update(item: MedNursingCheck) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
