package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.EnsInsurancePolicy
import cr.ac.utn.examenI.domain.repository.IRepository

class EnsViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<EnsInsurancePolicy> = repository.getAll().filterIsInstance<EnsInsurancePolicy>()
        fun getById(id: String): EnsInsurancePolicy? = repository.getById(id) as? EnsInsurancePolicy
        fun save(item: EnsInsurancePolicy) = repository.add(item)
        fun update(item: EnsInsurancePolicy) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
