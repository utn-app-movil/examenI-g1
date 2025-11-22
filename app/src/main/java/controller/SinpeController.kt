package cr.ac.utn.movil.controller

import cr.ac.utn.movil.data.MemoryDataManager
import cr.ac.utn.movil.entity.SinpeEntity

object SinpeController {

    fun getAll(): List<SinpeEntity> {
        return MemoryDataManager.getAll().filterIsInstance<SinpeEntity>()
    }

    fun add(entity: SinpeEntity): Boolean {
        val exists = getAll().any {
            it.sinOriginPhone == entity.sinOriginPhone &&
                    it.sinDestPhone == entity.sinDestPhone &&
                    it.sinAmount == entity.sinAmount &&
                    it.sinDateTime.time == entity.sinDateTime.time
        }
        if (exists) return false
        MemoryDataManager.add(entity)
        return true
    }

    fun update(entity: SinpeEntity): Boolean {
        val exists = getAll().any {
            it.ID != entity.ID &&
                    it.sinOriginPhone == entity.sinOriginPhone &&
                    it.sinDestPhone == entity.sinDestPhone &&
                    it.sinAmount == entity.sinAmount &&
                    it.sinDateTime.time == entity.sinDateTime.time
        }
        if (exists) return false
        MemoryDataManager.update(entity)
        return true
    }

    fun delete(entity: SinpeEntity) {
        MemoryDataManager.remove(entity.ID)
    }

    fun getById(id: String): SinpeEntity? {
        return MemoryDataManager.getById(id) as? SinpeEntity
    }
}