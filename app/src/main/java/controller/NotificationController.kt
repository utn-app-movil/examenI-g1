package controller

import android.content.Context
import cr.ac.utn.movil.data.MemoryDataManager
import cr.ac.utn.movil.R
import identities.Notification

class NotificationController(private val context: Context) {
    private val dataManager = MemoryDataManager

    fun addNotification(notification: Notification) {
        try {
            dataManager.add(notification)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgAdd))
        }
    }

    fun updateNotification(notification: Notification) {
        try {
            dataManager.update(notification)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgUpdate))
        }
    }

    fun getById(id: String): Notification? {
        try {
            val obj = dataManager.getById(id)
            return if (obj is Notification) obj else null
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgGetById))
        }
    }

    fun removeNotification(id: String) {
        try {
            val result = getById(id)
            if (result == null) {
                throw Exception(context.getString(R.string.MsgDataNoFound))
            }
            dataManager.remove(id)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgRemove))
        }
    }

    fun getNotifications(): List<Notification> {
        return dataManager.getAll().mapNotNull { it as? Notification }
    }
}
