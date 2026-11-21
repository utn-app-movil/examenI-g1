package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.SinSinpe
import cr.ac.utn.examenI.domain.repository.IRepository

class SinViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<SinSinpe> = repository.getAll().filterIsInstance<SinSinpe>()
        fun getById(id: String): SinSinpe? = repository.getById(id) as? SinSinpe
        fun save(item: SinSinpe) = repository.add(item)
        fun update(item: SinSinpe) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
