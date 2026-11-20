package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.WatWaterMonitoring
import cr.ac.utn.examenI.domain.repository.IRepository

class WatViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<WatWaterMonitoring> = repository.getAll().filterIsInstance<WatWaterMonitoring>()
        fun getById(id: String): WatWaterMonitoring? = repository.getById(id) as? WatWaterMonitoring
        fun save(item: WatWaterMonitoring) = repository.add(item)
        fun update(item: WatWaterMonitoring) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
