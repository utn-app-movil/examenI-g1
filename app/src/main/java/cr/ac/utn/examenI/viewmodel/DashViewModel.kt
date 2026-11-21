package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.DashContribution

class DashViewModel : ViewModel() {

    companion object {
        // same repository for the list and the crud activity
        private val memoryManager = MemoryDataManager()
    }

    fun getAll(): List<DashContribution> {
        return memoryManager.getAll().filterIsInstance<DashContribution>()
    }

    fun getById(id: String): DashContribution? {
        return memoryManager.getById(id) as? DashContribution
    }

    fun isDuplicate(personId: String, month: Int, year: Int): Boolean {
        return getAll().any {
            it.person.id == personId && it.month == month && it.year == year
        }
    }

    fun save(item: DashContribution) {
        memoryManager.add(item)
    }

    fun update(item: DashContribution) {
        memoryManager.update(item)
    }

    fun delete(id: String) {
        memoryManager.delete(id)
    }
}