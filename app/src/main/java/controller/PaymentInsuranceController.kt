package controller

import android.content.Context
import cr.ac.utn.movil.R
import cr.ac.utn.movil.data.MemoryDataManager
import identities.PaymentInsurance


class PaymentInsuranceController {
    private var dataManager: MemoryDataManager = MemoryDataManager
    private var context: Context

    constructor(context: Context){
        this.context=context
    }

    fun addPayen(payen: PaymentInsurance){
        try {
            //smt
        }catch (e: Exception){
            throw Exception(context.getString(R.string.ErrorMsgAdd))
        }
    }
}