package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.RentRental
import cr.ac.utn.examenI.domain.repository.IRepository

class RentViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<RentRental> = repository.getAll().filterIsInstance<RentRental>()
        fun getById(id: String): RentRental? = repository.getById(id) as? RentRental
        fun save(item: RentRental) = repository.add(item)
        fun update(item: RentRental) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
