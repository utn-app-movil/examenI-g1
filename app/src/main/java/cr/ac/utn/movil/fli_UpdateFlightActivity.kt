package cr.ac.utn.movil

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import controller.fli_FlightController
import model.fli_Flight
import util.getCountries
import java.util.Calendar

class fli_UpdateFlightActivity : AppCompatActivity() {

    private lateinit var fli_idInput: EditText
    private lateinit var fli_nameInput: EditText
    private lateinit var fli_lastNameInput: EditText
    private lateinit var fli_originCountrySpinner: Spinner
    private lateinit var fli_destinationCountrySpinner: Spinner
    private lateinit var fli_flightNumberInput: EditText
    private lateinit var fli_dateInput: TextView
    private lateinit var fli_timeInput: TextView

    private val fli_flightController = fli_FlightController()
    private var fli_flightId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }

        fli_flightId = intent.getStringExtra("fli_flight_id")

        fli_idInput = EditText(this).apply { hint = "Cédula" }
        fli_nameInput = EditText(this).apply { hint = "Nombre" }
        fli_lastNameInput = EditText(this).apply { hint = "Apellidos" }
        fli_flightNumberInput = EditText(this).apply { hint = "Número de Vuelo (ej. AB1234)" }

        val countries = getCountries()
        val countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        fli_originCountrySpinner = Spinner(this)
        fli_destinationCountrySpinner = Spinner(this)
        fli_originCountrySpinner.adapter = countryAdapter
        fli_destinationCountrySpinner.adapter = countryAdapter

        fli_dateInput = TextView(this).apply {
            text = "Seleccionar Fecha"
            setOnClickListener { showDatePickerDialog() }
        }

        fli_timeInput = TextView(this).apply {
            text = "Seleccionar Hora"
            setOnClickListener { showTimePickerDialog() }
        }

        val updateButton = Button(this).apply {
            text = "Actualizar Vuelo"
            setOnClickListener { updateFlight() }
        }
        
        layout.addView(TextView(this).apply { text = "Cédula:" })
        layout.addView(fli_idInput)
        layout.addView(TextView(this).apply { text = "Nombre:" })
        layout.addView(fli_nameInput)
        layout.addView(TextView(this).apply { text = "Apellidos:" })
        layout.addView(fli_lastNameInput)
        layout.addView(TextView(this).apply { text = "País de Origen:" })
        layout.addView(fli_originCountrySpinner)
        layout.addView(TextView(this).apply { text = "País de Destino:" })
        layout.addView(fli_destinationCountrySpinner)
        layout.addView(TextView(this).apply { text = "Número de Vuelo:" })
        layout.addView(fli_flightNumberInput)
        layout.addView(TextView(this).apply { text = "Fecha de Vuelo:" })
        layout.addView(fli_dateInput)
        layout.addView(TextView(this).apply { text = "Hora de Vuelo:" })
        layout.addView(fli_timeInput)
        layout.addView(updateButton)

        setContentView(layout)
        
        loadFlightData()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            fli_dateInput.text = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        }, year, month, day)

        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            fli_timeInput.text = String.format("%02d:%02d", selectedHour, selectedMinute)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun loadFlightData() {
        fli_flightId?.let { id ->
            val flight = fli_flightController.getFlights().find { it.fli_id == id }
            flight?.let {
                fli_idInput.setText(it.fli_id)
                fli_nameInput.setText(it.fli_name)
                fli_lastNameInput.setText(it.fli_lastName)
                fli_flightNumberInput.setText(it.fli_flightNumber)
                
                val countries = getCountries()
                fli_originCountrySpinner.setSelection(countries.indexOf(it.fli_originCountry))
                fli_destinationCountrySpinner.setSelection(countries.indexOf(it.fli_destinationCountry))

                val dateTimeParts = it.fli_dateTime.split(" ")
                fli_dateInput.text = dateTimeParts.getOrElse(0) { "" }
                fli_timeInput.text = dateTimeParts.getOrElse(1) { "" }
            }
        }
    }

    private fun updateFlight() {
        val fli_id = fli_idInput.text.toString()
        val fli_name = fli_nameInput.text.toString()
        val fli_lastName = fli_lastNameInput.text.toString()
        val fli_originCountry = fli_originCountrySpinner.selectedItem.toString()
        val fli_destinationCountry = fli_destinationCountrySpinner.selectedItem.toString()
        val fli_flightNumber = fli_flightNumberInput.text.toString()
        val fli_date = fli_dateInput.text.toString()
        val fli_time = fli_timeInput.text.toString()

        if (!fli_flightController.validateFlightNumber(fli_flightNumber)) {
            Toast.makeText(this, "Número de vuelo inválido.", Toast.LENGTH_SHORT).show()
            return
        }

        if (!fli_flightController.validateCountries(fli_originCountry, fli_destinationCountry)) {
            Toast.makeText(this, "País de origen y destino no pueden ser iguales.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedFlight = fli_Flight(fli_id, fli_name, fli_lastName, fli_originCountry, fli_destinationCountry, fli_flightNumber, "$fli_date $fli_time")
        fli_flightController.updateFlight(updatedFlight)

        Toast.makeText(this, "Vuelo actualizado exitosamente.", Toast.LENGTH_SHORT).show()
        finish()
    }
}