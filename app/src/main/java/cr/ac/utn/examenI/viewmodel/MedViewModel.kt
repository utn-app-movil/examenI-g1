package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.Med

class MedViewModel : ViewModel() {

    companion object{
        private val repository = MemoryDataManager()

        fun getAll(): List<Med> {
            return repository.getAll().filterIsInstance<Med>()
        }

        fun getById(id: String): Med? {
            return repository .getById(id) as? Med
        }

        fun save(item: Med) {
            repository.add(item)
        }

        fun update(item: Med) {
            repository.update(item)
        }

        fun delete(id: String) {
            repository.delete(id)
        }

    }
}