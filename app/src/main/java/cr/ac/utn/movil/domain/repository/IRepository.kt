package cr.ac.utn.movil.cr.ac.utn.movil.domain.repository

import cr.ac.utn.movil.cr.ac.utn.movil.domain.model.Identifier

interface IRepository {
    fun getAll(): List<Identifier>
    fun getById(id: String): Identifier?
    fun add(obj: Identifier)
    fun update(obj: Identifier)
    fun delete(id: String)
}