package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.AppAppointment
import cr.ac.utn.examenI.domain.repository.IRepository

class AppViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<AppAppointment> = repository.getAll().filterIsInstance<AppAppointment>()
        fun getById(id: String): AppAppointment? = repository.getById(id) as? AppAppointment
        fun save(item: AppAppointment) = repository.add(item)
        fun update(item: AppAppointment) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
