package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.Notif
import cr.ac.utn.examenI.domain.repository.IRepository

class NotifViewModel : ViewModel() {
    companion object {
        private var repository: IRepository

        init {
            repository = MemoryDataManager()
        }

        fun getAll(): List<Notif> {
            return repository.getAll().filterIsInstance<Notif>()
        }

        fun getById(id: String): Notif? {
            return repository.getById(id) as? Notif
        }

        fun save(item: Notif) {
            repository.add(item)
        }

        fun update(item: Notif) {
            repository.update(item)
        }

        fun delete(id: String) {
            repository.delete(id)
        }
    }
}