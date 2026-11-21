package cr.ac.utn.examenI

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EveCrudActivity : AppCompatActivity() {

    internal data class EventRecord(
        val id: String,
        val name: String,
        val firstName: String,
        val lastName: String,
        val phone: Int,
        val email: String,
        val address: String,
        val institution: String,
        val venue: String,
        val seatNumber: String,
        val eventType: String,
        val eventDateTime: LocalDateTime
    ) {
        fun summaryText(): String {
            val formattedDate = eventDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            return "$id - $name $firstName $lastName | $eventType | $venue | $institution | Asiento: $seatNumber | $formattedDate"
        }
    }

    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtInstitution: EditText
    private lateinit var txtVenue: EditText
    private lateinit var txtSeatNum: EditText
    private lateinit var spnEventType: Spinner
    private lateinit var txtDateTime: EditText

    private var currentId: String? = null
    private var selectedDateTime: LocalDateTime = LocalDateTime.now().plusDays(1).withSecond(0).withNano(0)
    private val eventTypes = arrayOf("Teatro", "Cine", "Concierto")
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_eve_crud)
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
        txtInstitution = findViewById(R.id.txtInstitution)
        txtVenue = findViewById(R.id.txtVenue)
        txtSeatNum = findViewById(R.id.txtSeatNum)
        spnEventType = findViewById(R.id.spnEventType)
        txtDateTime = findViewById(R.id.txtDateTime)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, eventTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnEventType.adapter = adapter

        findViewById<Button>(R.id.btnPickDate).setOnClickListener { openCalendarPicker() }
        findViewById<Button>(R.id.btnPickTime).setOnClickListener { openTimePicker() }

        val incomingId = intent.getStringExtra("EXTRA_ID")
        if (!incomingId.isNullOrEmpty()) {
            loadExistingData(incomingId)
        } else {
            refreshDateTimeText()
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }

    private fun openCalendarPicker() {
        val currentDate = selectedDateTime.toLocalDate()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val pickedDate = LocalDate.of(year, month + 1, dayOfMonth)
                selectedDateTime = LocalDateTime.of(pickedDate, selectedDateTime.toLocalTime())
                refreshDateTimeText()
            },
            currentDate.year,
            currentDate.monthValue - 1,
            currentDate.dayOfMonth
        ).show()
    }

    private fun openTimePicker() {
        val currentTime = selectedDateTime.toLocalTime()
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val pickedTime = LocalTime.of(hourOfDay, minute)
                selectedDateTime = LocalDateTime.of(selectedDateTime.toLocalDate(), pickedTime)
                refreshDateTimeText()
            },
            currentTime.hour,
            currentTime.minute,
            true
        ).show()
    }

    private fun refreshDateTimeText() {
        txtDateTime.setText(selectedDateTime.format(formatter))
    }

    private fun parseDateTimeOrNull(raw: String): LocalDateTime? {
        return try {
            LocalDateTime.parse(raw, formatter)
        } catch (_: Exception) {
            null
        }
    }

    private fun loadExistingData(id: String) {
        val item = records.find { it.id.equals(id.trim(), ignoreCase = true) } ?: return
        currentId = item.id
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        txtInstitution.setText(item.institution)
        txtVenue.setText(item.venue)
        txtSeatNum.setText(item.seatNumber)

        val pos = eventTypes.indexOfFirst { it.equals(item.eventType, ignoreCase = true) }
        if (pos >= 0) spnEventType.setSelection(pos)

        selectedDateTime = item.eventDateTime
        refreshDateTimeText()
    }

    private fun isSeatTakenByOther(
        eventType: String,
        venue: String,
        eventDateTime: LocalDateTime,
        seatNumber: String,
        currentStudentId: String
    ): Boolean {
        return records.any { existing ->
            !existing.id.equals(currentStudentId.trim(), ignoreCase = true) &&
                    existing.eventType.equals(eventType.trim(), ignoreCase = true) &&
                    existing.venue.equals(venue.trim(), ignoreCase = true) &&
                    existing.eventDateTime == eventDateTime &&
                    existing.seatNumber.equals(seatNumber.trim(), ignoreCase = true)
        }
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val firstName = txtFirstName.text.toString().trim()
        val lastName = txtLastName.text.toString().trim()
        val phoneText = txtPhone.text.toString().trim()
        val email = txtEmail.text.toString().trim()
        val address = txtAddress.text.toString().trim()
        val institution = txtInstitution.text.toString().trim()
        val venue = txtVenue.text.toString().trim()
        val seat = txtSeatNum.text.toString().trim()
        val eventType = spnEventType.selectedItem?.toString()?.trim() ?: ""

        if (
            id.isEmpty() || name.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
            phoneText.isEmpty() || email.isEmpty() || address.isEmpty() ||
            institution.isEmpty() || venue.isEmpty() || seat.isEmpty() || eventType.isEmpty()
        ) {
            Toast.makeText(this, "Por favor complete todos los campos requeridos.", Toast.LENGTH_SHORT).show()
            return
        }

        val phone = phoneText.toIntOrNull()
        if (phone == null) {
            Toast.makeText(this, "Telefono invalido.", Toast.LENGTH_SHORT).show()
            return
        }

        val finalDateTime = parseDateTimeOrNull(txtDateTime.text.toString().trim()) ?: selectedDateTime

        val duplicateId = records.any { it.id.equals(id, ignoreCase = true) }
        if (currentId.isNullOrEmpty() && duplicateId) {
            Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
            return
        }

        if (isSeatTakenByOther(eventType, venue, finalDateTime, seat, id)) {
            Toast.makeText(
                this,
                "El numero de asiento ya esta ocupado para este evento/lugar.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val record = EventRecord(
            id = id,
            name = name,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            email = email,
            address = address,
            institution = institution,
            venue = venue,
            seatNumber = seat,
            eventType = eventType,
            eventDateTime = finalDateTime
        )

        if (currentId.isNullOrEmpty()) {
            records.add(record)
        } else {
            val idx = records.indexOfFirst { it.id.equals(currentId, ignoreCase = true) }
            if (idx >= 0) {
                records[idx] = record
            } else {
                records.add(record)
            }
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return

        AlertDialog.Builder(this)
            .setMessage(getString(R.string.TextDeleteQuestion))
            .setPositiveButton(android.R.string.ok) { _, _ ->
                records.removeAll { it.id.equals(id, ignoreCase = true) }
                Toast.makeText(this, getString(R.string.TextDeleteSuccess), Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                doSave()
                true
            }
            R.id.mnu_delete -> {
                doDelete()
                true
            }
            R.id.mnu_cancel -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val records = mutableListOf<EventRecord>()

        internal fun getAllRecords(): List<EventRecord> = records.toList()
    }
}