package cr.ac.utn.movil

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.movil.controllers.WatWaterQualityController
import java.text.SimpleDateFormat
import java.util.*

class WatListActivity : AppCompatActivity() {

    private lateinit var lvRecords: ListView
    private lateinit var tvNoData: TextView
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wat_activity_list)

        supportActionBar?.title = getString(R.string.wat_list_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeViews()
        loadRecords()
    }

    private fun initializeViews() {
        lvRecords = findViewById(R.id.wat_lvRecords)
        tvNoData = findViewById(R.id.wat_tvNoData)
    }

    private fun loadRecords() {
        try {
            val records = WatWaterQualityController.getAll()

            if (records.isEmpty()) {
                tvNoData.visibility = TextView.VISIBLE
                lvRecords.visibility = ListView.INVISIBLE
            } else {
                tvNoData.visibility = TextView.INVISIBLE
                lvRecords.visibility = ListView.VISIBLE

                val displayList = records.map { record ->
                    buildString {
                        append("NIS: ${record.nisId}\n")
                        append("Date: ${dateFormat.format(record.measurementDate)}\n")
                        append("pH: ${record.phLevel} | Turbidity: ${record.turbidity} NTU\n")
                        append("Owner: ${record.owner.fullName}\n")
                        append("ID: ${record.owner.identificationNumber}\n")
                        if (record.detectedContaminants.isNotEmpty()) {
                            append("Contaminants: ${record.detectedContaminants.joinToString(", ")}")
                        }
                    }
                }

                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    displayList
                )
                lvRecords.adapter = adapter
            }
        } catch (e: Exception) {
            tvNoData.visibility = TextView.VISIBLE
            lvRecords.visibility = ListView.INVISIBLE
            tvNoData.text = "Error loading records: ${e.message}"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}