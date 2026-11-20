package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.LicLicenseRenewal
import cr.ac.utn.examenI.domain.repository.IRepository

class LicViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<LicLicenseRenewal> = repository.getAll().filterIsInstance<LicLicenseRenewal>()
        fun getById(id: String): LicLicenseRenewal? = repository.getById(id) as? LicLicenseRenewal
        fun save(item: LicLicenseRenewal) = repository.add(item)
        fun update(item: LicLicenseRenewal) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
