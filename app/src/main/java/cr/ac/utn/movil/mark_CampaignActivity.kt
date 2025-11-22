package cr.ac.utn.movil.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.movil.controllers.mark_CampaignController
import cr.ac.utn.movil.identities.mark_Campaign
import cr.ac.utn.movil.R
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale // This is for giving format to dates and hours


class mark_CampaignActivity : AppCompatActivity() {


    private val controller = mark_CampaignController()

    // Temporaly storage of the time values
    private var startDateTimestamp: Long = 0L
    private var endDateTimestamp: Long = 0L


    private lateinit var codeEditText: android.widget.EditText
    private lateinit var nameEditText: android.widget.EditText
    private lateinit var budgetEditText: android.widget.EditText
    private lateinit var startDateButton: android.widget.Button
    private lateinit var endDateButton: android.widget.Button
    private lateinit var channelSpinner: android.widget.Spinner
    private lateinit var leaderEditText: android.widget.EditText
    private lateinit var provinceSpinner: android.widget.Spinner
    private lateinit var saveButton: android.widget.Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_mark_campaign)

        initializeViews()

        setupSpinners()

        setupDatePickers()

        setupSaveButton()
    }
    private fun formatToCurrency(amount: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        return formatter.format(amount)
    }
    private fun initializeViews() {
        codeEditText = findViewById(R.id.mark_et_code)
        nameEditText = findViewById(R.id.mark_et_name)
        budgetEditText = findViewById(R.id.mark_et_budget)
        startDateButton = findViewById(R.id.mark_btn_start_date)
        endDateButton = findViewById(R.id.mark_btn_end_date)
        channelSpinner = findViewById(R.id.mark_sp_channel)
        leaderEditText = findViewById(R.id.mark_et_leader)
        provinceSpinner = findViewById(R.id.mark_sp_province)
        saveButton = findViewById(R.id.mark_btn_save)
    }
    private fun setupSpinners() {

        val provinceOptions = listOf("Alajuela", "Cartago", "Guanacaste", "Heredia", "Limón", "Puntarenas", "San José")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            provinceOptions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        provinceSpinner.adapter = adapter
    }

    private fun setupDatePickers() {
        startDateButton.setOnClickListener {
            showDatePicker(true)
        }

        endDateButton.setOnClickListener {
            showDatePicker(false)
        }
    }
    private fun showDatePicker(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()

        val listener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(year, monthOfYear, dayOfMonth)
            val selectedTimestamp = calendar.timeInMillis
            val formattedDate = "$dayOfMonth/${monthOfYear + 1}/$year"

            if (isStartDate) {
                startDateTimestamp = selectedTimestamp
                startDateButton.text = formattedDate
            } else {
                endDateTimestamp = selectedTimestamp
                endDateButton.text = formattedDate
            }
        }

        DatePickerDialog(
            this,
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
        //
    }

    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            saveCampaign()
        }
    }

    private fun saveCampaign() {
        val code = codeEditText.text.toString().trim()
        val name = nameEditText.text.toString().trim()
        val budgetString = budgetEditText.text.toString().trim()
        val channel = channelSpinner.selectedItem.toString()
        val leader = leaderEditText.text.toString().trim()
        val province = provinceSpinner.selectedItem.toString()

        // 2. Validación de Campos Vacíos (Mejorada)
        if (code.isEmpty()) {
            Toast.makeText(this, "Error: Campaign Code cannot be empty.", Toast.LENGTH_LONG).show()
            return
        }
        if (name.isEmpty()) {
            Toast.makeText(this, "Error: Campaign Name cannot be empty.", Toast.LENGTH_LONG).show()
            return
        }
        if (budgetString.isEmpty()) {
            Toast.makeText(this, "Error: Budget cannot be empty.", Toast.LENGTH_LONG).show()
            return
        }
        if (leader.isEmpty()) {
            Toast.makeText(this, "Error: Campaign Leader cannot be empty.", Toast.LENGTH_LONG).show()
            return
        }
        if (startDateTimestamp == 0L) {
            Toast.makeText(this, "Error: Start Date must be selected.", Toast.LENGTH_LONG).show()
            return
        }
        if (endDateTimestamp == 0L) {
            Toast.makeText(this, "Error: End Date must be selected.", Toast.LENGTH_LONG).show()
            return
        }


        val budget = budgetString.toDoubleOrNull()
        if (budget == null) {
            Toast.makeText(this, "Budget must be a valid number.", Toast.LENGTH_LONG).show()
            return
        }

        val newCampaign = mark_Campaign(
            code = code,
            name = name,
            budget = budget,
            startDate = startDateTimestamp,
            endDate = endDateTimestamp,
            channel = channel,
            leader = leader,
            province = province
        )

        val success = controller.processCampaign(newCampaign)

        if (success) {
            val formattedBudget = formatToCurrency(budget)

            Toast.makeText(
                this,
                "Campaign saved: ${newCampaign.FullName} with Budget: $formattedBudget",
                Toast.LENGTH_LONG
            ).show()

            finish()
        } else {
            Toast.makeText(this, "Error saving campaign. Check budget (> 0) and dates (Start <= End).", Toast.LENGTH_LONG).show()
        }
    }
    }
