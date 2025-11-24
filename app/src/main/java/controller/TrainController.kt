package controller

import android.content.Context
import cr.ac.utn.movil.R
import cr.ac.utn.movil.data.MemoryDataManager
import cr.ac.utn.movil.interfaces.IDataManager
import identities.Training

class TrainController {

    private var dataManager: IDataManager = MemoryDataManager
    private  var context: Context

    constructor(context: Context){
        this.context=context
    }

    fun addTraining(training: Training){
        try {
            dataManager.add(training)
        }catch (e: Exception){
            throw Exception(context
                .getString(R.string.ErrorMsgAdd))
        }
    }

    fun updateTraining(training: Training){
        try {
            dataManager.update(training)
        }catch (e: Exception){
            throw Exception(context
                .getString(R.string.ErrorMsgUpdate))
        }
    }

    fun getTrain(){}

    fun getById(id: String): Training?{
        try {
            return dataManager.getById(id) as Training?
        }catch (e: Exception){
            throw Exception(context
                .getString(R.string.ErrorMsgGetById))
        }
    }


    fun removeTraining(id: String){
        try{
            val result = dataManager.getById(id)
            if (result == null){
                throw Exception(context
                    .getString(R.string.MsgDataNoFound))
            }
            dataManager.remove(id)
        }catch (e: Exception){
            throw Exception(context
                .getString(R.string.ErrorMsgRemove))
        }
    }
}