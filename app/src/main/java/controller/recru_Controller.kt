package controller

import cr.ac.utn.movil.data.MemoryDataManager
import cr.ac.utn.movil.interfaces.IDataManager
import cr.ac.utn.movil.identities.Identifier
import android.content.Context
import cr.ac.utn.movil.R

class RecruController {

    private val dataManager: IDataManager = MemoryDataManager
    private var context: Context

    constructor(context: Context){
        this.context = context
    }

    fun addIdentifier(obj: Identifier) {
        try {
            dataManager.add(obj)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgAdd))
        }
    }

    fun updateIdentifier(obj: Identifier) {
        try {
            dataManager.update(obj)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgUpdate))
        }
    }

    fun removeIdentifier(id: String) {
        try {
            val result = dataManager.getById(id)
            if (result == null) {
                throw Exception(context.getString(R.string.MsgDataNoFound))
            }
            dataManager.remove(id)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgRemove))
        }
    }

    fun getById(id: String): Identifier? {
        try {
            return dataManager.getById(id)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgGetById))
        }
    }

    fun getAllIdentifiers(): List<Identifier> {
        try {
            return dataManager.getAll()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgGetAll))
        }
    }
}
