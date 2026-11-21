package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.LibReservation
import cr.ac.utn.examenI.domain.repository.IRepository
import java.time.LocalDateTime

class LibViewModel: ViewModel() {
    companion object {
        private val sharedRepository: IRepository = MemoryDataManager()
    }
    private val repository: IRepository = sharedRepository

    fun getAll(): List<LibReservation>{
        val result = mutableListOf<LibReservation>()
        for (item in repository.getAll()){
            result.add(item as LibReservation)
        }
        return result
    }

    fun getById(id: String): LibReservation?{
        return repository.getById(id) as? LibReservation
    }

    fun save(item: LibReservation): Boolean {
        if (getById(item.id) != null ) return false
        repository.add(item)
        return true
    }

    fun update(item: LibReservation){
        repository.update(item)
    }

    fun delete(id: String){
        repository.delete(id)
    }

    fun isReturnAfterReservation(reservation: LocalDateTime, returnDate: LocalDateTime): Boolean {
        return returnDate.isAfter(reservation)
    }

}