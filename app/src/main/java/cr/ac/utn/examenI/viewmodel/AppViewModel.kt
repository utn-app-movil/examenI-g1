package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.AppAppointment
import cr.ac.utn.examenI.domain.repository.IRepository

class AppViewModel : ViewModel() {
    fun getAppointments(): List<AppAppointment> = repository.getAll().map { it as AppAppointment }

    fun getAppointment(id: String): AppAppointment? =
        repository.getById(id) as? AppAppointment

    fun addAppointment(appointment: AppAppointment): Boolean {
        if (getAppointment(appointment.id) != null) return false
        repository.add(appointment)
        return true
    }

    fun updateAppointment(appointment: AppAppointment): Boolean {
        if (getAppointment(appointment.id) == null) return false
        repository.update(appointment)
        return true
    }

    fun deleteAppointment(id: String): Boolean {
        val appointment = getAppointment(id) ?: return false
        repository.delete(appointment.id)
        return true
    }

    companion object {
        private val repository: IRepository = MemoryDataManager()
    }
}
