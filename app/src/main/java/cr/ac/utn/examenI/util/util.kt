package cr.ac.utn.examenI.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import cr.ac.utn.examenI.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

const val EXTRA_ID = "cr.ac.utn.movil.Id"

class util {
    companion object {
        fun openActivity(
            context: Context,
            objClass: Class<*>,
            extraName: String = "",
            value: String? = null
        ) {
            val intent = Intent(context, objClass).apply {
                if (extraName.isNotEmpty()) {
                    putExtra(extraName, value)
                }
            }
            context.startActivity(intent)
        }

        fun parseStringToDateModern(dateString: String, pattern: String = "yyyy-MM-dd"): LocalDate? {
            if (dateString.isBlank()) return null
            return try {
                val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
                LocalDate.parse(dateString.trim(), formatter)
            } catch (e: Exception) {
                try {
                    val altFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())
                    LocalDate.parse(dateString.trim(), altFormatter)
                } catch (e2: Exception) {
                    null
                }
            }
        }

        fun parseStringToDateTimeModern(dateTimeString: String, pattern: String = "yyyy-MM-dd HH:mm"): LocalDateTime? {
            if (dateTimeString.isBlank()) return null
            return try {
                val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
                LocalDateTime.parse(dateTimeString.trim(), formatter)
            } catch (e: Exception) {
                try {
                    val altFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
                    LocalDateTime.parse(dateTimeString.trim(), altFormatter)
                } catch (e2: Exception) {
                    null
                }
            }
        }

        fun formatDate(date: LocalDate?): String {
            if (date == null) return ""
            return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault()))
        }

        fun formatDateTime(dateTime: LocalDateTime?): String {
            if (dateTime == null) return ""
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault()))
        }

        fun showDialogCondition(context: Context, questionText: String, callback: () -> Unit) {
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setMessage(questionText)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.TextYes)) { _, _ -> callback() }
                .setNegativeButton(context.getString(R.string.TextNo)) { dialog, _ -> dialog.cancel() }

            val alert = dialogBuilder.create()
            alert.setTitle(context.getString(R.string.TextTitleDialogQuestion))
            alert.show()
        }

        fun showProgressDialog(context: Context, message: String): AlertDialog {
            val progressBar = ProgressBar(context).apply {
                isIndeterminate = true
                setPadding(40, 40, 40, 40)
            }
            val container = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(50, 40, 50, 40)
                gravity = Gravity.CENTER_VERTICAL
                addView(
                    progressBar,
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                )
                val tvMessage = TextView(context).apply {
                    text = message
                    textSize = 16f
                    setPadding(30, 0, 0, 0)
                }
                addView(tvMessage)
            }

            val builder = AlertDialog.Builder(context)
                .setView(container)
                .setCancelable(false)

            val dialog = builder.create()
            dialog.show()
            return dialog
        }
    }
}
