package cr.ac.utn.movil.controller

import cr.ac.utn.movil.data.MemoryDataManager
import cr.ac.utn.movil.identities.InventoryEntry
import java.time.LocalDateTime
import kotlin.collections.any
import kotlin.collections.filterIsInstance
import kotlin.text.equals
import kotlin.text.matches
import kotlin.text.toRegex
import kotlin.text.trim

class InventoryController {

    private val dataManager = MemoryDataManager

    fun addInventoryEntry(entry: InventoryEntry): Boolean {
        return try {
            if (isDuplicate(entry)) {
                false
            } else {
                dataManager.add(entry)
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun updateInventoryEntry(entry: InventoryEntry): Boolean {
        return try {
            dataManager.update(entry)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun removeInventoryEntry(id: String): Boolean {
        return try {
            dataManager.remove(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getAllInventoryEntries(): List<InventoryEntry> {
        return try {
            dataManager.getAll().filterIsInstance<InventoryEntry>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getInventoryEntryById(id: String): InventoryEntry? {
        return try {
            dataManager.getById(id) as? InventoryEntry
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Check if product code already exists (excluding current entry when updating)
    fun isDuplicate(entry: InventoryEntry): Boolean {
        val allEntries = getAllInventoryEntries()
        return allEntries.any {
            it.ID != entry.ID &&
                    it.ProductCode.trim().equals(entry.ProductCode.trim(), ignoreCase = true)
        }
    }

    // Validate that date and time are not in the future
    fun validateDateTime(dateTime: LocalDateTime?): Boolean {
        if (dateTime == null) return false
        return !dateTime.isAfter(LocalDateTime.now())
    }

    // Validate email format
    fun validateEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    // Validate quantity
    fun validateQuantity(quantity: Int): Boolean {
        return quantity > 0
    }

    // Validate unit cost
    fun validateUnitCost(cost: Double): Boolean {
        return cost > 0.0
    }
}