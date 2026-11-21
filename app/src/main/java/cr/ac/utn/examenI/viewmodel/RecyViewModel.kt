package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.RecyRecycling
import cr.ac.utn.examenI.domain.repository.IRepository

class RecyViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<RecyRecycling> = repository.getAll().filterIsInstance<RecyRecycling>()
        fun getById(id: String): RecyRecycling? = repository.getById(id) as? RecyRecycling
        fun save(item: RecyRecycling) = repository.add(item)
        fun update(item: RecyRecycling) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
