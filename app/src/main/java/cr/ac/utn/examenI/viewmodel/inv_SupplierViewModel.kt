package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.inv_Supplier
import cr.ac.utn.examenI.domain.repository.IRepository
import java.util.UUID

class inv_SupplierViewModel : ViewModel() {

    companion object {
        private val inv_repository: IRepository = MemoryDataManager()
    }

    fun inv_getAll(): List<inv_Supplier> {
        return inv_repository.getAll()
            .filterIsInstance<inv_Supplier>()
            .sortedWith(
                compareBy(String.CASE_INSENSITIVE_ORDER) {
                        inv_supplier -> inv_supplier.inv_name
                }
            )
    }

    fun inv_getById(inv_id: String): inv_Supplier? {
        return inv_repository.getById(inv_id) as? inv_Supplier
    }

    fun inv_create(inv_name: String): inv_OperationResult {
        val inv_normalizedName = inv_name.trim()

        if (inv_normalizedName.isEmpty()) {
            return inv_OperationResult.inv_invalidData
        }

        val inv_duplicate = inv_getAll().any { inv_supplier ->
            inv_supplier.inv_name.equals(
                inv_normalizedName,
                ignoreCase = true
            )
        }

        if (inv_duplicate) {
            return inv_OperationResult.inv_duplicate
        }

        val inv_supplier = inv_Supplier(
            id = UUID.randomUUID().toString(),
            inv_name = inv_normalizedName
        )

        inv_repository.add(inv_supplier)
        return inv_OperationResult.inv_success
    }
}
