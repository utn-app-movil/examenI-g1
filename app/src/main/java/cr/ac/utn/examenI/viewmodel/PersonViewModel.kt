package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.Person
import cr.ac.utn.examenI.domain.repository.IRepository

class PersonViewModel: ViewModel() {
    companion object{
        private var repository: IRepository
        init {
            repository = MemoryDataManager()
        }

        fun getPeople(): List<Person>{
            return repository.getAll() as MutableList<Person>
        }

        fun getPersonById(id: String): Person?{
            return repository.getById(id) as? Person
        }

        fun savePerson(person: Person){
            repository.add(person)
        }

        fun updatePerson(person: Person){
            repository.update(person)
        }

        fun removePerson(id: String){
            repository.delete(id)
        }
    }
}