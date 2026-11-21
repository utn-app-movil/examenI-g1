package cr.ac.utn.examenI.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.inv_InventoryEntry
import cr.ac.utn.examenI.domain.repository.IRepository
import java.math.BigDecimal
import java.time.LocalDateTime

enum class inv_OperationResult {
    inv_success,
    inv_invalidData,
    inv_duplicate,
    inv_notFound
}

class inv_InventoryViewModel : ViewModel() {

    companion object {
        private val inv_repository: IRepository = MemoryDataManager()
    }

    fun inv_getAll(): List<inv_InventoryEntry> {
        return inv_repository.getAll()
            .filterIsInstance<inv_InventoryEntry>()
            .sortedByDescending { inv_entry -> inv_entry.inv_entryDateTime }
    }

    fun inv_getById(inv_id: String): inv_InventoryEntry? {
        return inv_repository.getById(inv_id) as? inv_InventoryEntry
    }

    fun inv_create(
        inv_entry: inv_InventoryEntry
    ): inv_OperationResult {
        if (!inv_isValid(inv_entry)) {
            return inv_OperationResult.inv_invalidData
        }

        if (
            inv_getById(inv_entry.id) != null ||
            inv_isDuplicate(inv_entry)
        ) {
            return inv_OperationResult.inv_duplicate
        }

        inv_repository.add(inv_entry)
        return inv_OperationResult.inv_success
    }

    fun inv_update(
        inv_entry: inv_InventoryEntry
    ): inv_OperationResult {
        if (inv_getById(inv_entry.id) == null) {
            return inv_OperationResult.inv_notFound
        }

        if (!inv_isValid(inv_entry)) {
            return inv_OperationResult.inv_invalidData
        }

        if (inv_isDuplicate(inv_entry)) {
            return inv_OperationResult.inv_duplicate
        }

        inv_repository.update(inv_entry)
        return inv_OperationResult.inv_success
    }

    fun inv_delete(inv_id: String): inv_OperationResult {
        if (inv_getById(inv_id) == null) {
            return inv_OperationResult.inv_notFound
        }

        inv_repository.delete(inv_id)
        return inv_OperationResult.inv_success
    }

    private fun inv_isValid(
        inv_entry: inv_InventoryEntry
    ): Boolean {
        val inv_person = inv_entry.inv_responsible

        return inv_entry.id.isNotBlank() &&
                inv_entry.id == inv_entry.id.trim() &&
                inv_person.id.isNotBlank() &&
                inv_person.name.isNotBlank() &&
                Patterns.EMAIL_ADDRESS.matcher(
                    inv_person.email.trim()
                ).matches() &&
                inv_entry.inv_productCode.isNotBlank() &&
                inv_entry.inv_productName.isNotBlank() &&
                inv_entry.inv_quantity > 0 &&
                inv_entry.inv_supplier.isNotBlank() &&
                inv_entry.inv_unitCost.compareTo(BigDecimal.ZERO) >= 0 &&
                !inv_entry.inv_entryDateTime.isAfter(LocalDateTime.now())
    }

    private fun inv_isDuplicate(
        inv_entry: inv_InventoryEntry
    ): Boolean {
        return inv_getAll().any { inv_existing ->
            inv_existing.id != inv_entry.id &&
                    inv_sameText(
                        inv_existing.inv_responsible.id,
                        inv_entry.inv_responsible.id
                    ) &&
                    inv_sameText(
                        inv_existing.inv_responsible.name,
                        inv_entry.inv_responsible.name
                    ) &&
                    inv_sameText(
                        inv_existing.inv_responsible.email,
                        inv_entry.inv_responsible.email
                    ) &&
                    inv_sameText(
                        inv_existing.inv_productCode,
                        inv_entry.inv_productCode
                    ) &&
                    inv_sameText(
                        inv_existing.inv_productName,
                        inv_entry.inv_productName
                    ) &&
                    inv_existing.inv_quantity == inv_entry.inv_quantity &&
                    inv_existing.inv_entryDateTime ==
                    inv_entry.inv_entryDateTime &&
                    inv_sameText(
                        inv_existing.inv_supplier,
                        inv_entry.inv_supplier
                    ) &&
                    inv_existing.inv_unitCost.compareTo(
                        inv_entry.inv_unitCost
                    ) == 0
        }
    }

    private fun inv_sameText(
        inv_first: String,
        inv_second: String
    ): Boolean {
        return inv_first.trim().equals(
            inv_second.trim(),
            ignoreCase = true
        )
    }
}