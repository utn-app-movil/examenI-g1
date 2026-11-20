package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.InvInventory
import cr.ac.utn.examenI.domain.repository.IRepository

class InvViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<InvInventory> = repository.getAll().filterIsInstance<InvInventory>()
        fun getById(id: String): InvInventory? = repository.getById(id) as? InvInventory
        fun save(item: InvInventory) = repository.add(item)
        fun update(item: InvInventory) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
