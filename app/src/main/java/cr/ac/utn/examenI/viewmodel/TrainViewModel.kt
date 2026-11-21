package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.TrainRegistration
import cr.ac.utn.examenI.domain.repository.IRepository

class TrainViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<TrainRegistration> {
            return repository.getAll().filterIsInstance<TrainRegistration>()
        }

        fun getById(id: String): TrainRegistration? {
            return repository.getById(id) as? TrainRegistration
        }

        fun save(item: TrainRegistration) {
            repository.add(item)
        }

        fun update(item: TrainRegistration) {
            repository.update(item)
        }

        fun delete(id: String) {
            repository.delete(id)
        }
    }
}

