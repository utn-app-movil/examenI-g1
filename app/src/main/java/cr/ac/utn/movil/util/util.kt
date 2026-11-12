package cr.ac.utn.movil.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import cr.ac.utn.examenI.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

const val EXTRA_ID = "cr.ac.utn.movil.Id"
class util {
    companion object{
        fun openActivity(context: Context
                         , objClass: Class<*>, extraName: String="", value: String?=null){
            val intent= Intent(context
                , objClass).apply { putExtra(extraName, value)}
            context.startActivity(intent)
        }

        fun parseStringToDateModern(dateString: String, pattern: String): LocalDate? {
            return try {
                val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())
                LocalDate.parse(dateString, formatter)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun parseStringToDateTimeModern(dateTimeString: String, pattern: String): LocalDateTime? {
            return try {
                val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
                LocalDateTime.parse(dateTimeString, formatter)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun showDialogCondition(context: Context, questionText: String, callback: () ->  Unit){
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setMessage(questionText)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.TextYes), DialogInterface.OnClickListener{
                        dialog, id -> callback()
                })
                .setNegativeButton(context.getString(R.string.TextNo), DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            val alert = dialogBuilder.create()
            alert.setTitle(context.getString(R.string.TextTitleDialogQuestion))
            alert.show()
        }
    }
}