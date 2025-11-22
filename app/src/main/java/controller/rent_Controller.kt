package controller

import cr.ac.utn.movil.data.MemoryDataManager
import cr.ac.utn.movil.identities.rent_Rentals

class rent_Controller {


    fun addRental(rental: rent_Rentals): Boolean {


        val exists = MemoryDataManager.getAll()
            .filterIsInstance<rent_Rentals>()
            .any { it.plate.trim() == rental.plate.trim() }

        if (exists) return false

        MemoryDataManager.add(rental)
        return true
    }


    fun updateRental( rental: rent_Rentals) {

        MemoryDataManager.update(rental)
    }

    fun deleteRental(id: String) {
        MemoryDataManager.remove(id)
    }

    fun getAllRentals(): MutableList<rent_Rentals> {
        return MemoryDataManager.getAll()
            .filterIsInstance<rent_Rentals>()
            .toMutableList()
    }


    fun findByPlate(plate: String): rent_Rentals? {
        return getAllRentals().find { it.plate.trim() == plate.trim() }
    }
}
