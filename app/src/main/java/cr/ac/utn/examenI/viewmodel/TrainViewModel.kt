package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.TrainTraining
import cr.ac.utn.examenI.domain.repository.IRepository

class TrainViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<TrainTraining> = repository.getAll().filterIsInstance<TrainTraining>()
        fun getById(id: String): TrainTraining? = repository.getById(id) as? TrainTraining
        fun save(item: TrainTraining) = repository.add(item)
        fun update(item: TrainTraining) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
