package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.RecruRecruitment
import cr.ac.utn.examenI.domain.repository.IRepository

class RecruViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<RecruRecruitment> = repository.getAll().filterIsInstance<RecruRecruitment>()
        fun getById(id: String): RecruRecruitment? = repository.getById(id) as? RecruRecruitment
        fun save(item: RecruRecruitment) = repository.add(item)
        fun update(item: RecruRecruitment) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
