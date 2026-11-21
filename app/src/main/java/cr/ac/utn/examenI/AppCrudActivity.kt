package cr.ac.utn.examenI

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.examenI.domain.model.AppAppointment
import cr.ac.utn.examenI.domain.model.AppSpecialty
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.AppViewModel
import java.time.LocalDateTime

class AppCrudActivity : AppCompatActivity() {
    private lateinit var appointmentId: EditText
    private lateinit var patientName: EditText
    private lateinit var firstSurname: EditText
    private lateinit var secondSurname: EditText
    private lateinit var phone: EditText
    private lateinit var email: EditText
    private lateinit var address: EditText
    private lateinit var doctorName: EditText
    private lateinit var specialty: Spinner
    private lateinit var appointmentDate: EditText

    private val viewModel: AppViewModel by viewModels()
    private var currentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_app_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_crud_root)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        appointmentId = findViewById(R.id.app_id)
        patientName = findViewById(R.id.app_patient_name)
        firstSurname = findViewById(R.id.app_first_surname)
        secondSurname = findViewById(R.id.app_second_surname)
        phone = findViewById(R.id.app_phone)
        email = findViewById(R.id.app_email)
        address = findViewById(R.id.app_address)
        doctorName = findViewById(R.id.app_doctor)
        specialty = findViewById(R.id.app_specialty)
        appointmentDate = findViewById(R.id.app_date_time)

        val adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, AppSpecialty.entries.toTypedArray()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        specialty.adapter = adapter

        val id = intent.getStringExtra(EXTRA_ID)
        if (id == null) {
            appointmentDate.setText(util.formatDateTime(LocalDateTime.now().plusDays(1)))
        } else {
            val appointment = viewModel.getAppointment(id)
            if (appointment == null) {
                showMessage(R.string.TextDataNoFound)
                finish()
                return
            }
            loadAppointment(appointment)
        }

        findViewById<Button>(R.id.app_save).setOnClickListener { requestSave() }
        findViewById<Button>(R.id.app_delete).apply {
            isEnabled = currentId != null
            setOnClickListener { requestDelete() }
        }
        findViewById<Button>(R.id.app_cancel).setOnClickListener { finish() }
    }

    private fun loadAppointment(appointment: AppAppointment) {
        currentId = appointment.id
        appointmentId.setText(appointment.id)
        appointmentId.isEnabled = false
        patientName.setText(appointment.patientName)
        firstSurname.setText(appointment.firstSurname)
        secondSurname.setText(appointment.secondSurname)
        phone.setText(appointment.phone)
        email.setText(appointment.email)
        address.setText(appointment.address)
        doctorName.setText(appointment.doctorName)
        specialty.setSelection(appointment.specialty.ordinal)
        appointmentDate.setText(util.formatDateTime(appointment.dateTime))
    }

    private fun requestSave() {
        val appointment = readForm() ?: return
        if (currentId == null) {
            saveAppointment(appointment, editing = false)
        } else {
            util.showDialogCondition(this, getString(R.string.TextSaveQuestion)) {
                saveAppointment(appointment, editing = true)
            }
        }
    }

    private fun readForm(): AppAppointment? {
        val requiredField = listOf(appointmentId, patientName, doctorName)
            .firstOrNull { it.text.isBlank() }
        if (requiredField != null) {
            showFieldError(requiredField, R.string.TextSaveError)
            return null
        }

        val phoneText = phone.text.toString().trim()
        val emailText = email.text.toString().trim()
        if (phoneText.isBlank() && emailText.isBlank()) {
            showFieldError(phone, R.string.TextSaveError)
            return null
        }

        val dateText = appointmentDate.text.toString().trim()
        val date = util.parseStringToDateTimeModern(dateText)
        // Comparing with the formatted value catches invalid dates corrected by the parser.
        if (date == null || util.formatDateTime(date) != dateText || !date.isAfter(LocalDateTime.now())) {
            showFieldError(appointmentDate, R.string.TextSaveError)
            return null
        }

        val selectedSpecialty = specialty.selectedItem as? AppSpecialty
        if (selectedSpecialty == null) {
            showMessage(R.string.TextSaveError)
            return null
        }
        return AppAppointment(
            id = currentId ?: appointmentId.text.toString().trim(),
            patientName = patientName.text.toString().trim(),
            firstSurname = firstSurname.text.toString().trim(),
            secondSurname = secondSurname.text.toString().trim(),
            phone = phoneText,
            email = emailText,
            address = address.text.toString().trim(),
            doctorName = doctorName.text.toString().trim(),
            specialty = selectedSpecialty,
            dateTime = date
        )
    }

    private fun saveAppointment(appointment: AppAppointment, editing: Boolean) {
        if (!appointment.dateTime.isAfter(LocalDateTime.now())) {
            showFieldError(appointmentDate, R.string.TextSaveError)
            return
        }

        val saved = if (editing) {
            viewModel.updateAppointment(appointment)
        } else {
            viewModel.addAppointment(appointment)
        }
        if (saved) {
            showMessage(R.string.TextSaveSuccess)
            finish()
        } else if (editing) {
            showMessage(R.string.TextDataNoFound)
        } else {
            showFieldError(appointmentId, R.string.TextDuplicateData)
        }
    }

    private fun requestDelete() {
        val id = currentId ?: return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            if (viewModel.deleteAppointment(id)) {
                showMessage(R.string.TextDeleteSuccess)
                finish()
            } else {
                showMessage(R.string.TextDataNoFound)
            }
        }
    }

    private fun showFieldError(field: EditText, message: Int) {
        field.requestFocus()
        field.error = getString(message)
    }

    private fun showMessage(message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        menu?.findItem(R.id.mnu_delete)?.isVisible = currentId != null
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                requestSave()
                true
            }
            R.id.mnu_delete -> {
                requestDelete()
                true
            }
            R.id.mnu_cancel -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
