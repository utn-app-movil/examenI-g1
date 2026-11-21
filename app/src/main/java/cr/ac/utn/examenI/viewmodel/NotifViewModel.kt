package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.NotifNotification
import cr.ac.utn.examenI.domain.repository.IRepository

class NotifViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<NotifNotification> = repository.getAll().filterIsInstance<NotifNotification>()
        fun getById(id: String): NotifNotification? = repository.getById(id) as? NotifNotification
        fun save(item: NotifNotification) = repository.add(item)
        fun update(item: NotifNotification) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
