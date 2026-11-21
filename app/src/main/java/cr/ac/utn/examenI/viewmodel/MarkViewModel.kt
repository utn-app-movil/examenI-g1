package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.MarkCampaign

class MarkViewModel : ViewModel() {

    companion object {
        private val memoryManager = MemoryDataManager()
    }

    fun getAll(): List<MarkCampaign> {
        return memoryManager.getAll().filterIsInstance<MarkCampaign>()
    }

    fun getById(id: String): MarkCampaign? {
        return memoryManager.getById(id) as? MarkCampaign
    }

    fun existsCode(code: String): Boolean {
        return getAll().any { it.code.equals(code.trim(), ignoreCase = true) }
    }

    fun save(campaign: MarkCampaign) {
        memoryManager.add(campaign)
    }

    fun update(campaign: MarkCampaign) {
        memoryManager.update(campaign)
    }

    fun delete(id: String) {
        memoryManager.delete(id)
    }
}