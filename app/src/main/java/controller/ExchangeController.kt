package cr.ac.utn.movil.controller

import cr.ac.utn.movil.identities.Exchange
import data.MemoryDataManager
import java.time.LocalDateTime

class ExchangeController {

    fun addExchange(ex: Exchange): Boolean {
        // Validate ID
        if (ex.ID.trim().isEmpty()) return false

        // Prevent duplicates
        if (MemoryDataManager.getById(ex.ID) != null) return false

        // Validate date is not in the future
        if (ex.dateTime.isAfter(LocalDateTime.now())) return false

        MemoryDataManager.add(ex)
        return true
    }

    fun updateExchange(ex: Exchange): Boolean {
        if (ex.ID.trim().isEmpty()) return false

        if (ex.dateTime.isAfter(LocalDateTime.now())) return false

        MemoryDataManager.update(ex)
        return true
    }

    fun deleteExchange(id: String): Boolean {
        val exists = MemoryDataManager.getById(id)
            ?: return false

        MemoryDataManager.remove(id)
        return true
    }

    fun getAllExchanges(): List<Exchange> {
        return MemoryDataManager.getAll().filterIsInstance<Exchange>()
    }

    fun getExchange(id: String): Exchange? {
        return MemoryDataManager.getById(id) as? Exchange
    }
}