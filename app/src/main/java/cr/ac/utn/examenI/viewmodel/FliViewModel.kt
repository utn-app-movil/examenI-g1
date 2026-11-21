package cr.ac.utn.examenI.viewmodel

import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.Fli

object FliViewModel {
    private val memory = MemoryDataManager()

    fun getAll(): List<Fli> {
        return memory.getAll().filterIsInstance<Fli>()
    }

    fun getById(id: String): Fli? {
        return memory.getById(id) as? Fli
    }

    fun save(item: Fli) {
        memory.add(item)
    }

    fun update(item: Fli) {
        memory.update(item)
    }

    fun delete(id: String) {
        memory.delete(id)
    }
}
