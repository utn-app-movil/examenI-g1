package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.DashDashboard
import cr.ac.utn.examenI.domain.repository.IRepository

class DashViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<DashDashboard> = repository.getAll().filterIsInstance<DashDashboard>()
        fun getById(id: String): DashDashboard? = repository.getById(id) as? DashDashboard
        fun save(item: DashDashboard) = repository.add(item)
        fun update(item: DashDashboard) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
