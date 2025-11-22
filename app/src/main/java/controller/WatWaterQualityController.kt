package cr.ac.utn.movil.controllers

import cr.ac.utn.movil.data.MemoryDataManager
import cr.ac.utn.movil.identities.WatWaterQualityMonitoring

object WatWaterQualityController {

    fun add(waterQuality: WatWaterQualityMonitoring): Boolean {
        return try {
            if (exists(waterQuality.nisId)) {
                false // Duplicate data
            } else {
                waterQuality.updateId()
                MemoryDataManager.add(waterQuality)
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    fun update(waterQuality: WatWaterQualityMonitoring): Boolean {
        return try {
            waterQuality.updateId()
            MemoryDataManager.update(waterQuality)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun remove(nisId: String): Boolean {
        return try {
            MemoryDataManager.remove(nisId)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getAll(): List<WatWaterQualityMonitoring> {
        return try {
            MemoryDataManager.getAll()
                .filterIsInstance<WatWaterQualityMonitoring>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getById(nisId: String): WatWaterQualityMonitoring? {
        return try {
            val result = MemoryDataManager.getById(nisId)
            result as? WatWaterQualityMonitoring
        } catch (e: Exception) {
            null
        }
    }

    fun exists(nisId: String): Boolean {
        return getById(nisId) != null
    }
}