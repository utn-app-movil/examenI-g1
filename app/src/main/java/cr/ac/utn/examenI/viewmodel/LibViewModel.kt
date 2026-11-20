package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.LibLibrary
import cr.ac.utn.examenI.domain.repository.IRepository

class LibViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<LibLibrary> = repository.getAll().filterIsInstance<LibLibrary>()
        fun getById(id: String): LibLibrary? = repository.getById(id) as? LibLibrary
        fun save(item: LibLibrary) = repository.add(item)
        fun update(item: LibLibrary) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
