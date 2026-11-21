package cr.ac.utn.examenI.data.memory

import cr.ac.utn.examenI.domain.model.Identifier
import cr.ac.utn.examenI.domain.repository.IRepository

class MemoryDataManager: IRepository {
    private val objList = mutableListOf<Identifier>()
    override fun add(obj: Identifier){
        objList.add(obj)
    }

    override fun delete(id: String) {
        objList.removeIf { it.id == id }
    }

    override fun update(obj: Identifier) {
        delete(obj.id)
        add(obj)
    }

    override fun getAll() = objList

    override fun getById(id: String): Identifier? {
        val result = objList.filter { it.id.trim() == id.trim() }
        return if(result.any()) result[0] else null
    }
}