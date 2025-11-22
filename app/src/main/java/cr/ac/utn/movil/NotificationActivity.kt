package cr.ac.utn.movil

import android.app.DatePickerDialog
import android.app.Notification
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import controller.NotificationController
import cr.ac.utn.movil.util.util
import java.time.LocalDate
import java.util.Calendar

class NotificationActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var idNotification_notif: EditText
    private lateinit var senderName_notif: EditText
    private lateinit var senderFLName_notif: EditText
    private lateinit var emailTittle_notif: EditText
    private lateinit var selectedDate_notif: TextView
    private lateinit var CB_CC1: CheckBox
    private lateinit var CB_CC2: CheckBox
    private lateinit var CB_CC3: CheckBox
    private lateinit var CB_CCO1: CheckBox
    private lateinit var CB_CCO2: CheckBox
    private lateinit var CB_CCO3: CheckBox
    private var isEditMode: Boolean=false
    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var notificationController: NotificationController
    private lateinit var menuitemDelete: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        notificationController = NotificationController(this)

        idNotification_notif = findViewById<EditText>(R.id.idNotification_notif)
        senderName_notif = findViewById<EditText>(R.id.senderName_notif)
        senderFLName_notif = findViewById<EditText>(R.id.senderFLName_notif)
        emailTittle_notif = findViewById<EditText>(R.id.emailTittle_notif)
        selectedDate_notif = findViewById<TextView>(R.id.selectedDate_notif)

        CB_CC1 = findViewById<CheckBox>(R.id.CB_CC1)
        CB_CC2 = findViewById<CheckBox>(R.id.CB_CC2)
        CB_CC3 = findViewById<CheckBox>(R.id.CB_CC3)
        CB_CCO1 = findViewById<CheckBox>(R.id.CB_CCO1)
        CB_CCO2 = findViewById<CheckBox>(R.id.CB_CCO2)
        CB_CCO3 = findViewById<CheckBox>(R.id.CB_CCO3)

        val btnSelectDate = findViewById<ImageButton>(R.id.btnSelectDate_notif)
        btnSelectDate.setOnClickListener{
            showDatePickerDialog()
        }

        val btnSearch_notif = findViewById<Button>(R.id.btnSearch_notif)
        btnSearch_notif.setOnClickListener{
            val id = idNotification_notif.text.toString()
            searchNotification(id)
        }

        ResetDate()

        val defaultCcList = listOf("cc1@example.com", "cc2@example.com", "cc3@example.com")
        val defaultCcoList = listOf("cco1@example.com", "cco2@example.com", "cco3@example.com")

        cargarCheckBox(defaultCcList, defaultCcoList)
    }

    fun searchNotification(id: String) {
        try {
            val notification = notificationController.getById(id)
            if (notification != null) {
                isEditMode = true
                idNotification_notif.setText(notification.Id)
                idNotification_notif.isEnabled = false
                senderName_notif.setText(notification.SenderName)
                senderFLName_notif.setText(notification.SenderFLName)
                emailTittle_notif.setText(notification.EmailTittle)
                selectedDate_notif.text = getDateFormatString(
                    notification.ScheduleDate.dayOfMonth,
                    notification.ScheduleDate.monthValue,
                    notification.ScheduleDate.year
                )

                cargarCheckBox(notification.CcList, notification.CcoList)

            } else {
                Toast.makeText(this, "Notificación not found", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            cleanScreen()
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun cleanScreen() {
        ResetDate()
        isEditMode = false
        idNotification_notif.isEnabled = true
        idNotification_notif.setText("")
        senderName_notif.setText("")
        senderFLName_notif.setText("")
        emailTittle_notif.setText("")
        selectedDate_notif.setText("")

        CB_CC1.isChecked = false
        CB_CC2.isChecked = false
        CB_CC3.isChecked = false
        CB_CCO1.isChecked = false
        CB_CCO2.isChecked = false
        CB_CCO3.isChecked = false
    }

    private fun ResetDate() {
        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(this, this, year, month, day)
        datePickerDialog.show()
    }

    fun getDateFormatString(dayOfMonth: Int, monthValue: Int, yearValue: Int): String {
        return "${if (dayOfMonth < 10) "0" else ""}$dayOfMonth/${if (monthValue < 10) "0" else ""}$monthValue/$yearValue"
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        selectedDate_notif.text = getDateFormatString(dayOfMonth, month + 1, year)
    }

    fun isValidatedData(): Boolean {
        val dateParsed = util.parseStringToDateModern(selectedDate_notif.text.toString(), "dd/MM/yyyy")
        val today = LocalDate.now()
        val isCCSelected = CB_CC1.isChecked || CB_CC2.isChecked || CB_CC3.isChecked
        val isCCOSelected = CB_CCO1.isChecked || CB_CCO2.isChecked || CB_CCO3.isChecked

        return idNotification_notif.text.trim().isNotEmpty() &&
                senderName_notif.text.trim().isNotEmpty() &&
                senderFLName_notif.text.trim().isNotEmpty() &&
                emailTittle_notif.text.trim().isNotEmpty() &&
                selectedDate_notif.text.trim().isNotEmpty() &&
                dateParsed != null &&
                dateParsed.isAfter(today.minusDays(1)) &&
                isCCSelected && isCCOSelected
    }

    fun saveNotification() {
        try {
            if (isValidatedData()) {
                val notification = identities.Notification()
                notification.Id = idNotification_notif.text.toString()
                notification.SenderName = senderName_notif.text.toString()
                notification.SenderFLName = senderFLName_notif.text.toString()
                notification.EmailTittle = emailTittle_notif.text.toString()

                val dateParsed = util.parseStringToDateModern(
                    selectedDate_notif.text.toString(),
                    "dd/MM/yyyy"
                )
                notification.ScheduleDate = dateParsed!!

                notification.CcList = mutableListOf<String>().apply {
                    if (CB_CC1.isChecked) add(CB_CC1.text.toString())
                    if (CB_CC2.isChecked) add(CB_CC2.text.toString())
                    if (CB_CC3.isChecked) add(CB_CC3.text.toString())
                }

                notification.CcoList = mutableListOf<String>().apply {
                    if (CB_CCO1.isChecked) add(CB_CCO1.text.toString())
                    if (CB_CCO2.isChecked) add(CB_CCO2.text.toString())
                    if (CB_CCO3.isChecked) add(CB_CCO3.text.toString())
                }

                if (!isEditMode) {
                    notificationController.addNotification(notification)
                } else {
                    notificationController.updateNotification(notification)
                }

                cleanScreen()
                Toast.makeText(this, "Notificación guardada con éxito", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(
                    this,
                    "Todos los campos son obligatorios, la fecha debe ser futura y al menos un CC y CCO debe estar seleccionado.",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }


    fun deleteNotification() {
        try {
            notificationController.removeNotification(idNotification_notif.text.toString())
            cleanScreen()
            Toast.makeText(this, "Notificación eliminada con éxito", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun cargarCheckBox(ccList: List<String>, ccoList: List<String>) {
        val ccBoxes = listOf(CB_CC1, CB_CC2, CB_CC3)
        val ccoBoxes = listOf(CB_CCO1, CB_CCO2, CB_CCO3)

        ccBoxes.forEachIndexed { index, checkBox ->
            checkBox.text = ccList.getOrNull(index) ?: ""
            checkBox.isChecked = false
        }

        ccoBoxes.forEachIndexed { index, checkBox ->
            checkBox.text = ccoList.getOrNull(index) ?: ""
            checkBox.isChecked = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_crud, menu)
        menuitemDelete = menu!!.findItem(R.id.mnu_delete)
        if (isEditMode)
            menuitemDelete.isVisible = true
        else
            menuitemDelete.isVisible =false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mnu_save -> {
                if (isEditMode){
                    util.showDialogCondition(this
                        , getString(R.string.TextSaveActionQuestion)
                        , { saveNotification() })
                }else
                    saveNotification()
                true
            }
            R.id.mnu_delete -> {
                util.showDialogCondition(this
                    , getString(R.string.TextDeleteActionQuestion)
                    ,{ deleteNotification() })
                return true
            }
            R.id.mnu_cancel -> {
                cleanScreen()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}