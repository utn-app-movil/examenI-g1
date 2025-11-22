package cr.ac.utn.movil.data

import cr.ac.utn.movil.interfaces.IDataManager
import cr.ac.utn.movil.identities.Identifier
import cr.ac.utn.movil.identities.rent_Rentals

object MemoryDataManager : IDataManager {

    // Lista global de objetos (todas las entidades)
    private var objectList = mutableListOf<Identifier>()

    // Métodos genéricos
    override fun add(obj: Identifier) {
        objectList.add(obj)
    }

    override fun remove(id: String) {
        objectList.removeIf { it.ID.trim() == id.trim() }
    }

    override fun update(obj: Identifier) {
        remove(obj.ID)
        add(obj)
    }

    override fun getAll() = objectList

    override fun getById(id: String): Identifier? {
        val result = objectList.filter { it.ID.trim() == id.trim() }
        return if (result.any()) result[0] else null
    }


    fun getAllRentals(): MutableList<rent_Rentals> {
        return objectList.filterIsInstance<rent_Rentals>().toMutableList()
    }


    fun findRentalByPlate(plate: String): rent_Rentals? {
        return getAllRentals().find { it.plate.equals(plate, ignoreCase = true) }
    }


    fun addRental(rental: rent_Rentals): Boolean {
        val exists = findRentalByPlate(rental.plate)
        if (exists != null) return false
        add(rental)
        return true
    }


    fun updateRental(rental: rent_Rentals) {
        update(rental)
    }


    fun deleteRental(rental: rent_Rentals) {
        remove(rental.ID)
    }
}
