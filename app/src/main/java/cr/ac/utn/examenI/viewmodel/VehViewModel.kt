package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.VehVehicleRange
import cr.ac.utn.examenI.domain.repository.IRepository

class VehViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<VehVehicleRange> = repository.getAll().filterIsInstance<VehVehicleRange>()
        fun getById(id: String): VehVehicleRange? = repository.getById(id) as? VehVehicleRange
        fun save(item: VehVehicleRange) = repository.add(item)
        fun update(item: VehVehicleRange) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
