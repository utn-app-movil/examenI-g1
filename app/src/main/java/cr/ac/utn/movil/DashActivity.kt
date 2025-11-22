package cr.ac.utn.movil

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.movil.controller.DashController
import cr.ac.utn.movil.identities.DashContribution
import cr.ac.utn.movil.identities.Person
import java.util.UUID

class DashActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var contributionsInput: EditText
    private lateinit var dayInput: EditText
    private lateinit var monthInput: EditText
    private lateinit var yearInput: EditText
    private lateinit var addButton: Button
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var listView: ListView

    private lateinit var dashController: DashController
    private lateinit var listAdapter: ArrayAdapter<String>
    private var allContributions = mutableListOf<DashContribution>()
    private var selectedContribution: DashContribution? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_dash)

        dashController = DashController()

        nameInput = findViewById(R.id.dash_name_input)
        contributionsInput = findViewById(R.id.dash_contributions_input)
        dayInput = findViewById(R.id.dash_day_input)
        monthInput = findViewById(R.id.dash_month_input)
        yearInput = findViewById(R.id.dash_year_input)
        addButton = findViewById(R.id.dash_add_button)
        updateButton = findViewById(R.id.dash_update_button)
        deleteButton = findViewById(R.id.dash_delete_button)
        listView = findViewById(R.id.dash_list_view)

        setupListView()
        setupButtonListeners()
    }

    private fun setupListView() {
        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = listAdapter
        updateListView()

        listView.setOnItemClickListener { _, _, position, _ ->
            selectedContribution = allContributions[position]
            selectedContribution?.let {
                nameInput.setText(it.person.Name)
                contributionsInput.setText(it.contributions.toString())
                dayInput.setText(it.day.toString())
                monthInput.setText(it.month.toString())
                yearInput.setText(it.year.toString())
            }
        }
    }

    private fun updateListView() {
        allContributions = dashController.getContributions().toMutableList()
        val contributionDescriptions = allContributions.map { it.FullDescription }
        listAdapter.clear()
        listAdapter.addAll(contributionDescriptions)
        listAdapter.notifyDataSetChanged()
    }

    private fun setupButtonListeners() {
        addButton.setOnClickListener { addContribution() }
        updateButton.setOnClickListener { updateContribution() }
        deleteButton.setOnClickListener { deleteContribution() }
    }

    private fun addContribution() {
        val name = nameInput.text.toString()
        val contributions = contributionsInput.text.toString().toIntOrNull()
        val day = dayInput.text.toString().toIntOrNull()
        val month = monthInput.text.toString().toIntOrNull()
        val year = yearInput.text.toString().toIntOrNull()

        if (name.isBlank() || contributions == null || day == null || month == null || year == null) {
            Toast.makeText(this, R.string.dash_toast_fill_fields, Toast.LENGTH_SHORT).show()
            return
        }

        val alreadyExists = allContributions.any {
            it.person.Name.equals(name, ignoreCase = true) && it.month == month && it.year == year
        }

        if (alreadyExists) {
            Toast.makeText(this, R.string.dash_toast_already_exists, Toast.LENGTH_LONG).show()
            return
        }

        val existingPerson = allContributions.firstOrNull { it.person.Name.equals(name, ignoreCase = true) }?.person
        val person = existingPerson ?: Person().apply {
            this.Name = name
            this.ID = UUID.randomUUID().toString()
        }

        val newContribution = DashContribution(person, contributions, day, month, year).apply {
            this.ID = UUID.randomUUID().toString()
        }

        dashController.addContribution(newContribution)
        updateListView()
        clearInputFields()
        Toast.makeText(this, R.string.dash_toast_added_success, Toast.LENGTH_SHORT).show()
    }

    private fun updateContribution() {
        if (selectedContribution == null) {
            Toast.makeText(this, R.string.dash_toast_select_to_update, Toast.LENGTH_SHORT).show()
            return
        }

        val name = nameInput.text.toString()
        val contributions = contributionsInput.text.toString().toIntOrNull()
        val day = dayInput.text.toString().toIntOrNull()
        val month = monthInput.text.toString().toIntOrNull()
        val year = yearInput.text.toString().toIntOrNull()

        if (name.isBlank() || contributions == null || day == null || month == null || year == null) {
            Toast.makeText(this, R.string.dash_toast_fill_fields, Toast.LENGTH_SHORT).show()
            return
        }

        val alreadyExists = allContributions.any {
            it.ID != selectedContribution!!.ID &&
            it.person.Name.equals(name, ignoreCase = true) &&
            it.month == month &&
            it.year == year
        }

        if (alreadyExists) {
            Toast.makeText(this, R.string.dash_toast_update_conflict, Toast.LENGTH_LONG).show()
            return
        }

        val updatedContribution = DashContribution(selectedContribution!!.person, contributions, day, month, year).apply {
            this.ID = selectedContribution!!.ID
            this.person.Name = name
        }

        dashController.updateContribution(updatedContribution)
        updateListView()
        clearInputFields()
        Toast.makeText(this, R.string.dash_toast_updated_success, Toast.LENGTH_SHORT).show()
    }

    private fun deleteContribution() {
        if (selectedContribution == null) {
            Toast.makeText(this, R.string.dash_toast_select_to_delete, Toast.LENGTH_SHORT).show()
            return
        }

        dashController.deleteContribution(selectedContribution!!)
        updateListView()
        clearInputFields()
        Toast.makeText(this, R.string.dash_toast_deleted_success, Toast.LENGTH_SHORT).show()
    }

    private fun clearInputFields() {
        nameInput.text.clear()
        contributionsInput.text.clear()
        dayInput.text.clear()
        monthInput.text.clear()
        yearInput.text.clear()
        nameInput.requestFocus()
        selectedContribution = null
    }
}
