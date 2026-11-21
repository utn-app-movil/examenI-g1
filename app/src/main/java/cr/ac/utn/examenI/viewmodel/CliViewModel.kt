package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.CliClient

/**
 * ViewModel for Corporate Clients.
 * Uses MemoryDataManager as required by exam guidelines.
 */
class CliViewModel : ViewModel() {
    companion object {
        private val repository = MemoryDataManager()

        fun getAll(): List<CliClient> {
            return repository.getAll().filterIsInstance<CliClient>()
        }

        fun getById(id: String): CliClient? {
            return repository.getById(id.trim()) as? CliClient
        }

        fun save(client: CliClient) {
            repository.add(client)
        }

        fun update(client: CliClient) {
            repository.update(client)
        }

        fun delete(id: String) {
            repository.delete(id.trim())
        }

        /**
         * Validates official Costa Rican Legal ID format:
         * 10 digits or hyphenated X-XXX-XXXXXX.
         */
        fun isValidLegalId(id: String): Boolean {
            val cleanId = id.trim()
            val regexHyphenated = Regex("""^\d{1}-\d{3}-\d{6}$""")
            val regexDigitsOnly = Regex("""^\d{10}$""")
            return cleanId.matches(regexHyphenated) || cleanId.matches(regexDigitsOnly)
        }

        /**
         * Verifies if legal ID already exists in repository.
         */
        fun isDuplicate(id: String): Boolean {
            return repository.getById(id.trim()) != null
        }
    }
}
