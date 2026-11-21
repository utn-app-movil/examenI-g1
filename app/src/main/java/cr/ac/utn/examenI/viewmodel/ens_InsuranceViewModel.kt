
package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.ens_InsurancePolicy
import cr.ac.utn.examenI.domain.repository.IRepository
import java.text.SimpleDateFormat
import java.util.Locale

class ens_InsuranceViewModel : ViewModel() {

    enum class SaveResult {
        SUCCESS,
        DUPLICATE,
        NOT_FOUND,
        INVALID_DATA
    }

    companion object {
        private val repository: IRepository = MemoryDataManager()
    }

    fun getPolicies(): List<ens_InsurancePolicy> {
        return repository.getAll()
            .filterIsInstance<ens_InsurancePolicy>()
    }

    fun getPolicyById(id: String): ens_InsurancePolicy? {
        return repository.getById(id) as? ens_InsurancePolicy
    }

    fun addPolicy(policy: ens_InsurancePolicy): SaveResult {

        if (!validatePolicy(policy)) {
            return SaveResult.INVALID_DATA
        }

        val exists = repository.getAll().any {
            it.id.trim().equals(
                policy.id.trim(),
                ignoreCase = true
            )
        }

        if (exists) {
            return SaveResult.DUPLICATE
        }

        repository.add(policy)
        return SaveResult.SUCCESS
    }

    fun updatePolicy(policy: ens_InsurancePolicy): SaveResult {

        if (!validatePolicy(policy)) {
            return SaveResult.INVALID_DATA
        }

        val current = getPolicyById(policy.id)
            ?: return SaveResult.NOT_FOUND

        val duplicate = repository.getAll().any {
            it.id.trim().equals(
                policy.id.trim(),
                ignoreCase = true
            ) && it !== current
        }

        if (duplicate) {
            return SaveResult.DUPLICATE
        }

        repository.update(policy)
        return SaveResult.SUCCESS
    }

    fun deletePolicy(id: String): Boolean {
        val policy = getPolicyById(id) ?: return false

        repository.delete(policy.id)
        return true
    }

    private fun validatePolicy(policy: ens_InsurancePolicy): Boolean {

        if (policy.id.isBlank()) {
            return false
        }

        if (policy.id != policy.id.trim()) {
            return false
        }

        if (policy.company.isBlank()) {
            return false
        }

        if (policy.insuranceType.isBlank()) {
            return false
        }

        if (policy.premium <= 0.0 || !policy.premium.isFinite()) {
            return false
        }

        val format = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        )

        format.isLenient = false

        val start = try {
            format.parse(policy.startDate)
        } catch (e: Exception) {
            null
        }

        val expiration = try {
            format.parse(policy.expirationDate)
        } catch (e: Exception) {
            null
        }

        if (start == null || expiration == null) {
            return false
        }

        return expiration.after(start)
    }
}
