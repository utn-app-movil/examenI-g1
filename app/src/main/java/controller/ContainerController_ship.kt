package controller

import android.content.Context
import cr.ac.utn.movil.data.MemoryDataManager
import cr.ac.utn.movil.identities.Identifier
import cr.ac.utn.movil.interfaces.IDataManager
import identities.Containers_ship

class ContainerController_ship {
    private var dataManager: IDataManager = MemoryDataManager
    private lateinit var context: Context

    constructor(context: Context){
        this.context = context
    }


    fun addContainer(container: Containers_ship){
        try {
            dataManager.add(container)
        }catch (e: Exception){
            throw Exception("Error during addign ")
        }
    }
    fun updateContainer(container: Containers_ship){
        try {
            dataManager.update(container)
        }catch (e: Exception){
            throw Exception("Error during update")
        }
    }
    fun removePerson(id: String){
        try {
            val result = dataManager.getById(id)
            if (result == null){
                throw Exception("Data mot found")
            }
            dataManager.remove(id)
        }catch (e: Exception){
            throw Exception("Error during deleting")
        }
    }
    fun GetById(id: String): Identifier? {
        try {
            return  dataManager.getById(id)

        }catch (e: Exception){
            throw Exception("Data not found")
        }
    }
}