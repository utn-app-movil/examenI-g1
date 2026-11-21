package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.CliClient
import cr.ac.utn.examenI.domain.repository.IRepository

class CliViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<CliClient> = repository.getAll().filterIsInstance<CliClient>()
        fun getById(id: String): CliClient? = repository.getById(id) as? CliClient
        fun save(item: CliClient) = repository.add(item)
        fun update(item: CliClient) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
