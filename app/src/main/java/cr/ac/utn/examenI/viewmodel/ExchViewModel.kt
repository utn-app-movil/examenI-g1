package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.ExchTransaction

class ExchViewModel : ViewModel() {
    companion object {
        private val exchMemoryManager = MemoryDataManager()

        fun getAll(): List<ExchTransaction> {
            return exchMemoryManager.getAll().filterIsInstance<ExchTransaction>()
        }

        fun getById(id: String): ExchTransaction? {
            return exchMemoryManager.getById(id) as? ExchTransaction
        }

        fun save(transaction: ExchTransaction): Boolean {
            if (exchMemoryManager.getById(transaction.id) != null) {
                return false
            }
            exchMemoryManager.add(transaction)
            return true
        }

        fun insert(transaction: ExchTransaction): Boolean {
            return save(transaction)
        }

        fun update(transaction: ExchTransaction) {
            exchMemoryManager.update(transaction)
        }

        fun delete(id: String) {
            exchMemoryManager.delete(id)
        }
    }
}
