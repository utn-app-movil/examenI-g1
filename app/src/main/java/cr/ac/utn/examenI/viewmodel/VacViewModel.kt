package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.VacRecord

/*Manage the vaccination records using memory repository*/
class VacViewModel : ViewModel() {

    companion object{
        //Shared repository across vaccination screens
        private val memoryManager = MemoryDataManager()
    }

    //returns all vacinations records
    fun getAll(): List<VacRecord> {
        return memoryManager.getAll().filterIsInstance<VacRecord>()
    }

    //Finds a vaccination record by its identifier
    fun getById(id: String): VacRecord? {
        return memoryManager.getById(id) as? VacRecord
    }

    //save a new vaccination record and returns false when it identifier already exist
    fun save(record: VacRecord): Boolean {
        if (getById(record.id) != null){
            return false
        }
        memoryManager.add(record)
        return false
    }

    //Updates and exist vaccination record
    fun update(record: VacRecord): Boolean {
        if (getById(record.id)== null){
            return false
        }
        memoryManager.update(record)
        return true
    }

    //Delete a vaccination record by identifier
    fun delete(id: String): Boolean{
        if (getById(id) == null){
            return false
        }
        memoryManager.delete(id)
        return true
    }


}