package cr.ac.utn.examenI.viewmodel

import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.Identifier
import cr.ac.utn.examenI.domain.repository.IRepository

object SinViewModel : IRepository {
    private val memoryManager = MemoryDataManager()

    override fun getAll(): List<Identifier> = memoryManager.getAll()

    override fun getById(id: String): Identifier? = memoryManager.getById(id)

    override fun add(obj: Identifier) {
        memoryManager.add(obj)
    }

    override fun update(obj: Identifier) {
        memoryManager.update(obj)
    }

    override fun delete(id: String) {
        memoryManager.delete(id)
    }
}
