package cr.ac.utn.movil.controller

import cr.ac.utn.movil.data.MemoryDataManager
import cr.ac.utn.movil.identities.DashContribution

class DashController {

    fun addContribution(contribution: DashContribution) {
        MemoryDataManager.add(contribution)
    }

    fun getContributions(): List<DashContribution> {
        return MemoryDataManager.getAll().filterIsInstance<DashContribution>()
    }

    fun updateContribution(contribution: DashContribution) {
        MemoryDataManager.update(contribution)
    }

    fun deleteContribution(contribution: DashContribution) {
        MemoryDataManager.remove(contribution.ID)
    }
}
