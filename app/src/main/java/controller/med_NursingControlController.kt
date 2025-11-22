package cr.ac.utn.movil.controller

import cr.ac.utn.movil.data.MemoryDataManager
import cr.ac.utn.movil.data.entities.med_NursingControl

class med_NursingControlController {

    fun add(record: med_NursingControl) {
        record.ID = java.util.UUID.randomUUID().toString()
        record.dateTime = System.currentTimeMillis()
        MemoryDataManager.add(record)
    }

    fun getAll(): List<med_NursingControl> {
        return MemoryDataManager.getAll()
            .filterIsInstance<med_NursingControl>()
            .sortedByDescending { it.dateTime }
    }

    fun update(record: med_NursingControl) {
        MemoryDataManager.update(record)
    }

    fun delete(id: String) {
        MemoryDataManager.remove(id)
    }
}