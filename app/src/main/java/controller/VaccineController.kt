package controller

import android.content.Context
import cr.ac.utn.movil.data.MemoryDataManager
import cr.ac.utn.movil.interfaces.IDataManager
import cr.ac.utn.movil.R
import identities.Vaccine
import java.time.LocalDate
import java.time.LocalTime

class VaccineController(context: Context) {

    private var dataManager: IDataManager = MemoryDataManager
    private var context: Context = context

    fun getVaccines() = dataManager.getAll()

    fun addVaccine(vaccine: Vaccine) {
        try {
            validateDate(vaccine.Date)
            validateTime(vaccine.Date, vaccine.Time)
            dataManager.add(vaccine)

        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgAdd))
        }
    }

    fun updateVaccine(vaccine: Vaccine) {
        try {
            validateDate(vaccine.Date)
            validateTime(vaccine.Date, vaccine.Time)
            dataManager.update(vaccine)

        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgUpdate))
        }
    }

    fun getById(id: String): Vaccine? {
        try {
            return dataManager.getById(id) as Vaccine?

        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgGetById))
        }
    }

    fun removeVaccine(id: String) {
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

    // -------------------------------------------------------------------------
    // VALIDACIONES
    // -------------------------------------------------------------------------

    private fun validateDate(date: LocalDate) {
        val today = LocalDate.now()
        if (date.isBefore(today)) {
            throw Exception("La fecha no puede ser menor a la actual")
        }
    }

    private fun validateTime(date: LocalDate, time: LocalTime) {
        val today = LocalDate.now()
        val now = LocalTime.now()

        if (date.isEqual(today) && time.isBefore(now)) {
            throw Exception("La hora no puede ser menor a la hora actual")
        }
    }
}
