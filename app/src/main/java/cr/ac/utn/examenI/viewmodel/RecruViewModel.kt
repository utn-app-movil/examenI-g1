package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.RecruApplication

class RecruViewModel : ViewModel() {

    companion object {
        private val memoryManager = MemoryDataManager()
    }

    fun getAll(): List<RecruApplication> =
        memoryManager.getAll().filterIsInstance<RecruApplication>()

    fun getById(id: String): RecruApplication? =
        memoryManager.getById(id) as? RecruApplication

    fun save(application: RecruApplication): RecruSaveResult {
        if (getById(application.id) != null || hasDuplicate(application)) {
            return RecruSaveResult.DUPLICATE
        }

        memoryManager.add(application)
        return RecruSaveResult.SUCCESS
    }

    fun update(application: RecruApplication): RecruSaveResult {
        if (getById(application.id) == null) {
            return RecruSaveResult.NOT_FOUND
        }

        if (hasDuplicate(application)) {
            return RecruSaveResult.DUPLICATE
        }

        memoryManager.update(application)
        return RecruSaveResult.SUCCESS
    }

    fun delete(id: String): Boolean {
        val application = getById(id) ?: return false
        memoryManager.delete(application.id)
        return true
    }

    private fun hasDuplicate(application: RecruApplication): Boolean =
        getAll().any { existing ->
            existing.id != application.id &&
                    existing.applicant.id.trim() == application.applicant.id.trim() &&
                    existing.targetCompany.trim().equals(
                        application.targetCompany.trim(),
                        ignoreCase = true
                    )
        }
}

enum class RecruSaveResult {
    SUCCESS,
    DUPLICATE,
    NOT_FOUND
}