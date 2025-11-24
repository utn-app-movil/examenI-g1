package cr.ac.utn.movil

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Lic_ControlActivity : AppCompatActivity() {

    private lateinit var etMedicalCode: EditText
    private lateinit var spLicenseType: Spinner
    private lateinit var etCurrentScore: EditText
    private lateinit var etRenewalDate: EditText
    private lateinit var etRenewalTime: EditText
    private lateinit var btnRenew: Button
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lic_control)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // === FIND VIEWS ===
        etMedicalCode = findViewById(R.id.LIC_ET_MEDICAL_CODE)
        spLicenseType = findViewById(R.id.LIC_SP_LICENSE_TYPE)
        etCurrentScore = findViewById(R.id.LIC_ET_CURRENT_SCORE)
        etRenewalDate = findViewById(R.id.LIC_ET_RENEWAL_DATE)
        etRenewalTime = findViewById(R.id.LIC_ET_RENEWAL_TIME)
        btnRenew = findViewById(R.id.LIC_BTN_RENEW)
        tvResult = findViewById(R.id.LIC_TV_RESULT)

        // === SPINNER CON STRING-ARRAY ===
        spLicenseType.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.lic_license_types)
        )

        // === CALENDARIO Y RELOJ ===
        etRenewalDate.setOnClickListener { showDatePicker() }
        findViewById<Button>(R.id.LIC_BTN_CALENDAR).setOnClickListener { showDatePicker() }

        etRenewalTime.setOnClickListener { showTimePicker() }
        findViewById<Button>(R.id.LIC_BTN_CLOCK).setOnClickListener { showTimePicker() }

        btnRenew.setOnClickListener { validarYRenovar() }
    }

    // === INFLAR EL MENÚ (ESTO FALTABA) ===
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        return true
    }

    // === ACCIONES DEL MENÚ ===
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                validarYRenovar()
                true
            }
            R.id.mnu_delete -> {
                limpiarCampos()
                tvResult.text = getString(R.string.lic_error_required) // o un mensaje personalizado
                tvResult.setTextColor(resources.getColor(android.R.color.holo_orange_dark))
                true
            }
            R.id.mnu_cancel -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDatePicker() {
        val hoy = LocalDate.now()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                val fecha = LocalDate.of(year, month + 1, day)
                if (fecha.isAfter(hoy)) {
                    mostrarError(getString(R.string.lic_error_future_date))
                } else {
                    etRenewalDate.setText(fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                }
            },
            hoy.year, hoy.monthValue - 1, hoy.dayOfMonth
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }.show()
    }

    private fun showTimePicker() {
        val ahora = LocalTime.now()
        TimePickerDialog(
            this,
            { _, hour, minute ->
                etRenewalTime.setText(String.format("%02d:%02d", hour, minute))
            },
            ahora.hour, ahora.minute, true
        ).show()
    }

    private fun validarYRenovar() {
        val codigo = etMedicalCode.text.toString().trim()
        val tipo = spLicenseType.selectedItem.toString()
        val puntajeStr = etCurrentScore.text.toString()
        val fechaStr = etRenewalDate.text.toString()
        val horaStr = etRenewalTime.text.toString()

        when {
            codigo.isEmpty() || puntajeStr.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty() -> {
                mostrarError(getString(R.string.lic_error_required))
                return
            }
            puntajeStr.toIntOrNull() == null -> {
                mostrarError(getString(R.string.lic_error_invalid_score))
                return
            }
            puntajeStr.toInt() < 65 -> {
                mostrarError(getString(R.string.lic_error_low_score, puntajeStr.toInt()))
                return
            }
            else -> {
                val fechaSeleccionada = LocalDate.parse(fechaStr)
                val horaSeleccionada = LocalTime.parse(horaStr)
                val fechaHoraSeleccionada = LocalDateTime.of(fechaSeleccionada, horaSeleccionada)
                val ahora = LocalDateTime.now()

                if (fechaHoraSeleccionada.isAfter(ahora)) {
                    mostrarError(getString(R.string.lic_error_future_date))
                } else {
                    val fechaFormateada = fechaHoraSeleccionada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    mostrarExito(getString(R.string.lic_success, tipo, codigo, puntajeStr.toInt(), fechaFormateada))
                    limpiarCampos()
                }
            }
        }
    }

    private fun mostrarError(mensaje: String) {
        tvResult.text = mensaje
        tvResult.setTextColor(resources.getColor(android.R.color.holo_red_dark))
    }

    private fun mostrarExito(mensaje: String) {
        tvResult.text = mensaje
        tvResult.setTextColor(resources.getColor(android.R.color.holo_green_dark))
    }

    private fun limpiarCampos() {
        etMedicalCode.text.clear()
        spLicenseType.setSelection(0)
        etCurrentScore.text.clear()
        etRenewalDate.text.clear()
        etRenewalTime.text.clear()
    }
}