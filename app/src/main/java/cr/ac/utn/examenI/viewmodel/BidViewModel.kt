package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.BidAuction
import cr.ac.utn.examenI.domain.repository.IRepository

class BidViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<BidAuction> = repository.getAll().filterIsInstance<BidAuction>()
        fun getById(id: String): BidAuction? = repository.getById(id) as? BidAuction
        fun save(item: BidAuction) = repository.add(item)
        fun update(item: BidAuction) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
