package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.EveEvent
import cr.ac.utn.examenI.domain.repository.IRepository

class EveViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<EveEvent> = repository.getAll().filterIsInstance<EveEvent>()
        fun getById(id: String): EveEvent? = repository.getById(id) as? EveEvent
        fun save(item: EveEvent) = repository.add(item)
        fun update(item: EveEvent) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
