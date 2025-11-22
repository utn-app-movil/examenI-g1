package cr.ac.utn.movil

import identities.Vaccine
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import controller.VaccineController
import java.time.LocalDate
import java.time.LocalTime

class ActivityVaccine : AppCompatActivity() {

    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtType: TextView
    private lateinit var txtSite: EditText
    private lateinit var txtDate: EditText
    private lateinit var txtTime: EditText
    private lateinit var vaccineController: VaccineController

    private var isEditMode = false
    private lateinit var deleteMenu: MenuItem

    private var selectedDate: LocalDate = LocalDate.now()
    private var selectedTime: LocalTime = LocalTime.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vaccine)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        vaccineController = VaccineController(this)

        txtId = findViewById(R.id.txtId_vac)
        txtName = findViewById(R.id.txtName_vac)
        txtFirstName = findViewById(R.id.txtFirstName_vac)
        txtType = findViewById(R.id.txtVaccineType_vac)
        txtSite = findViewById(R.id.txtSite_vac)
        txtDate = findViewById(R.id.txtDate_vac)
        txtTime = findViewById(R.id.txtTime_vac)

        val btnSearch = findViewById<ImageButton>(R.id.btnVaccineSearch_vac)
        btnSearch.setOnClickListener {
            searchVaccine(txtId.text.toString().trim())
        }

        txtDate.setOnClickListener { openDatePicker() }
        txtTime.setOnClickListener { openTimePicker() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        deleteMenu = menu.findItem(R.id.mnu_delete)
        deleteMenu.isVisible = isEditMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> { saveVaccine(); true }
            R.id.mnu_delete -> { deleteVaccine(); true }
            R.id.mnu_cancel -> { cleanScreen(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isValid(): Boolean {
        return txtId.text.isNotBlank()
                && txtName.text.isNotBlank()
                && txtFirstName.text.isNotBlank()
                && txtType.text.isNotBlank()
                && txtSite.text.isNotBlank()
                && txtDate.text.isNotBlank()
                && txtTime.text.isNotBlank()
    }

    private fun searchVaccine(id: String) {
        try {
            val vaccine = vaccineController.getById(id)
            if (vaccine != null) {
                isEditMode = true

                txtId.setText(vaccine.ID)
                txtId.isEnabled = false

                txtName.setText(vaccine.Name)
                txtFirstName.setText(vaccine.FirstName)
                txtType.setText(vaccine.VaccineType)
                txtSite.setText(vaccine.Site)

                selectedDate = vaccine.Date
                selectedTime = vaccine.Time

                txtDate.setText(selectedDate.toString())
                txtTime.setText(selectedTime.toString())

                invalidateOptionsMenu()
            } else {
                Toast.makeText(this, R.string.MsgDataNoFound, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun saveVaccine() {
        try {
            if (!isValid()) {
                Toast.makeText(this, R.string.MsgMissingInfo_vac, Toast.LENGTH_LONG).show()
                return
            }

            val existing = vaccineController.getById(txtId.text.toString())
            if (!isEditMode && existing != null) {
                Toast.makeText(this, R.string.MsgDuplicateInfo_vac, Toast.LENGTH_LONG).show()
                return
            }

            val vaccine = Vaccine(
                id = txtId.text.toString(),
                name = txtName.text.toString(),
                firstName = txtFirstName.text.toString(),
                vaccineType = txtType.text.toString(),
                site = txtSite.text.toString(),
                date = selectedDate,
                time = selectedTime
            )

            if (!isEditMode) vaccineController.addVaccine(vaccine)
            else vaccineController.updateVaccine(vaccine)

            cleanScreen()
            Toast.makeText(this, R.string.MsgSaveSuccess, Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteVaccine() {
        try {
            vaccineController.removeVaccine(txtId.text.toString())
            cleanScreen()
            Toast.makeText(this, R.string.MsgDeleteSucess_vac, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun cleanScreen() {
        isEditMode = false

        txtId.isEnabled = true
        txtId.setText("")
        txtName.setText("")
        txtFirstName.setText("")
        txtType.setText("")
        txtSite.setText("")
        txtDate.setText("")
        txtTime.setText("")

        selectedDate = LocalDate.now()
        selectedTime = LocalTime.now()

        invalidateOptionsMenu()
    }

    private fun openDatePicker() {
        val picker = DatePickerDialog(
            this,
            { _, y, m, d ->
                selectedDate = LocalDate.of(y, m + 1, d)
                txtDate.setText(selectedDate.toString())
            },
            selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth
        )
        picker.show()
    }

    private fun openTimePicker() {
        val picker = TimePickerDialog(
            this,
            { _, h, min ->
                selectedTime = LocalTime.of(h, min)
                txtTime.setText(selectedTime.toString())
            },
            selectedTime.hour, selectedTime.minute, true
        )
        picker.show()
    }
}
