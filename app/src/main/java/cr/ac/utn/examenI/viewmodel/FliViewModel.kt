package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.FliFlight
import cr.ac.utn.examenI.domain.repository.IRepository

class FliViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<FliFlight> = repository.getAll().filterIsInstance<FliFlight>()
        fun getById(id: String): FliFlight? = repository.getById(id) as? FliFlight
        fun save(item: FliFlight) = repository.add(item)
        fun update(item: FliFlight) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
