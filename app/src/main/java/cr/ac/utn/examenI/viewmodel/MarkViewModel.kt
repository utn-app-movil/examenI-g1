package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.MarkMarketing
import cr.ac.utn.examenI.domain.repository.IRepository

class MarkViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<MarkMarketing> = repository.getAll().filterIsInstance<MarkMarketing>()
        fun getById(id: String): MarkMarketing? = repository.getById(id) as? MarkMarketing
        fun save(item: MarkMarketing) = repository.add(item)
        fun update(item: MarkMarketing) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
