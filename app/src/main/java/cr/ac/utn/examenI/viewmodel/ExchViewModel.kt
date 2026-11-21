package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.ExchExchangeRate
import cr.ac.utn.examenI.domain.repository.IRepository

class ExchViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<ExchExchangeRate> = repository.getAll().filterIsInstance<ExchExchangeRate>()
        fun getById(id: String): ExchExchangeRate? = repository.getById(id) as? ExchExchangeRate
        fun save(item: ExchExchangeRate) = repository.add(item)
        fun update(item: ExchExchangeRate) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
