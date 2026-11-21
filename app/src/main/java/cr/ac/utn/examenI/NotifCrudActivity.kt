package cr.ac.utn.examenI

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.examenI.domain.model.Notif
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.NotifViewModel
import java.time.LocalDate
import java.util.Calendar

class NotifCrudActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtEmailTitle: EditText
    private lateinit var txtMessage: EditText
    private lateinit var txtCC: EditText
    private lateinit var txtCCO: EditText
    private lateinit var chkStudents: CheckBox
    private lateinit var chkTeachers: CheckBox
    private lateinit var chkStaff: CheckBox
    private lateinit var txtSendDate: EditText
    private lateinit var btnDatePicker: ImageButton

    private var dueDay: Int = 0
    private var dueMonth: Int = 0
    private var dueYear: Int = 0
    private var currentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notif_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtId = findViewById(R.id.txtId)
        txtName = findViewById(R.id.txtName)
        txtFirstName = findViewById(R.id.txtFirstName)
        txtLastName = findViewById(R.id.txtLastName)
        txtPhone = findViewById(R.id.txtPhone)
        txtEmail = findViewById(R.id.txtEmail)
        txtAddress = findViewById(R.id.txtAddress)
        txtEmailTitle = findViewById(R.id.txtEmailTitle)
        txtMessage = findViewById(R.id.txtMessage)
        txtCC = findViewById(R.id.txtCC)
        txtCCO = findViewById(R.id.txtCCO)
        chkStudents = findViewById(R.id.chkStudents)
        chkTeachers = findViewById(R.id.chkTeachers)
        chkStaff = findViewById(R.id.chkStaff)
        txtSendDate = findViewById(R.id.txtSendDate)
        btnDatePicker = findViewById(R.id.btnDatePicker)

        resetDateToTomorrow()

        btnDatePicker.setOnClickListener { showDatePickerDialog() }
        txtSendDate.setOnClickListener { showDatePickerDialog() }

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtSendDate.setText(util.formatDate(LocalDate.now().plusDays(1)))
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { cleanScreen(); finish() }
    }

    private fun cleanScreen() {
        currentId = null
        txtId.setText("")
        txtId.isEnabled = true
        txtName.setText("")
        txtFirstName.setText("")
        txtLastName.setText("")
        txtPhone.setText("")
        txtEmail.setText("")
        txtAddress.setText("")
        txtEmailTitle.setText("")
        txtMessage.setText("")
        txtCC.setText("")
        txtCCO.setText("")
        chkStudents.isChecked = false
        chkTeachers.isChecked = false
        chkStaff.isChecked = false
        resetDateToTomorrow()
        txtSendDate.setText(util.formatDate(LocalDate.now().plusDays(1)))
    }

    private fun resetDateToTomorrow() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        dueYear = calendar.get(Calendar.YEAR)
        dueMonth = calendar.get(Calendar.MONTH)
        dueDay = calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            this,
            dueYear,
            dueMonth,
            dueDay
        )
        datePickerDialog.show()
    }

    override fun onDateSet(view: android.widget.DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dueYear = year
        dueMonth = month
        dueDay = dayOfMonth

        val monthStr = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
        val dayStr = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"

        val formattedDate = "$year-$monthStr-$dayStr"
        txtSendDate.setText(formattedDate)
    }

    private fun loadExistingData(id: String) {
        val item = NotifViewModel.getById(id) ?: return

        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)

        txtEmailTitle.setText(item.emailTitle)
        txtMessage.setText(item.message)
        txtCC.setText(item.cc)
        txtCCO.setText(item.cco)

        chkStudents.isChecked = item.targetRecipients.contains("Estudiantes")
        chkTeachers.isChecked = item.targetRecipients.contains("Profesorado")
        chkStaff.isChecked = item.targetRecipients.contains("Administrativos")

        txtSendDate.setText(util.formatDate(item.sendDate))

        dueYear = item.sendDate.year
        dueMonth = item.sendDate.monthValue - 1
        dueDay = item.sendDate.dayOfMonth
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val title = txtEmailTitle.text.toString().trim()
        val msg = txtMessage.text.toString().trim()
        val dateStr = txtSendDate.text.toString().trim()

        if (id.isEmpty() || title.isEmpty() || msg.isEmpty()) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val sendDateParsed = util.parseStringToDateModern(dateStr)
        // Validación de fecha: Debe ser posterior a la fecha actual (debe ser mayor al día actual)
        if (sendDateParsed == null || !sendDateParsed.isAfter(LocalDate.now())) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val targets = mutableListOf<String>()
        if (chkStudents.isChecked) targets.add("Estudiantes")
        if (chkTeachers.isChecked) targets.add("Profesorado")
        if (chkStaff.isChecked) targets.add("Administrativos")

        val phoneInt = txtPhone.text.toString().toIntOrNull() ?: 0

        val item = Notif(
            id = id,
            name = txtName.text.toString().trim(),
            firstName = txtFirstName.text.toString().trim(),
            lastName = txtLastName.text.toString().trim(),
            phone = phoneInt,
            email = txtEmail.text.toString().trim(),
            address = txtAddress.text.toString().trim(),
            emailTitle = title,
            message = msg,
            cc = txtCC.text.toString().trim(),
            cco = txtCCO.text.toString().trim(),
            targetRecipients = targets,
            sendDate = sendDateParsed
        )

        if (currentId.isNullOrEmpty()) {
            // Verificar registro duplicado
            if (NotifViewModel.getById(id) != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            NotifViewModel.save(item)
        } else {
            NotifViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            NotifViewModel.delete(id)
            Toast.makeText(this, getString(R.string.TextDeleteSuccess), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> { doSave(); true }
            R.id.mnu_delete -> { doDelete(); true }
            R.id.mnu_cancel -> { cleanScreen(); finish(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
