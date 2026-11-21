package cr.ac.utn.examenI.viewmodel

import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.Identifier
import cr.ac.utn.examenI.domain.model.Rent
import cr.ac.utn.examenI.domain.repository.IRepository
class RentViewModel {
    companion object{
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<Identifier> {
            return repository.getAll()
        }

        fun getById(id: String): Rent? {
            return repository.getById(id) as? Rent
        }

        fun existsById(id: String): Boolean {
            return repository.getById(id.trim()) != null
        }

        fun existsByPlate(plate: String, excludeId: String? = null): Boolean {
            return repository.getAll()
                .filterIsInstance<Rent>()
                .any { it.plate.equals(plate.trim(), ignoreCase = true) && it.id != excludeId }
        }

        fun save(rent: Rent): Boolean {
            if (existsById(rent.id) || existsByPlate(rent.plate)) {
                return false
            }
            repository.add(rent)
            return true
        }

        fun update(rent: Rent): Boolean {
            if (!existsById(rent.id) || existsByPlate(rent.plate, excludeId = rent.id)) {
                return false
            }
            repository.update(rent)
            return true
        }

        fun delete(id: String): Boolean {
            if (!existsById(id)) {
                return false
            }
            repository.delete(id)
            return true
        }
    }
}