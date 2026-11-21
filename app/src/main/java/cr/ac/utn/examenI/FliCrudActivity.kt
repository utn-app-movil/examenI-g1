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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.examenI.domain.model.Country
import cr.ac.utn.examenI.domain.model.FliFlight
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.FliViewModel
import java.time.LocalDateTime

class FliCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var spnOriginCountry: Spinner
    private lateinit var spnDestCountry: Spinner
    private lateinit var txtFlightNum: EditText
    private lateinit var txtDateTime: EditText

    private var currentId: String? = null
    private val countries = Country.values()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fli_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtId = findViewById(R.id.fli_txtId)
        txtName = findViewById(R.id.fli_txtName)
        txtFirstName = findViewById(R.id.fli_txtFirstName)
        txtLastName = findViewById(R.id.fli_txtLastName)
        txtPhone = findViewById(R.id.fli_txtPhone)
        txtEmail = findViewById(R.id.fli_txtEmail)
        txtAddress = findViewById(R.id.fli_txtAddress)
        spnOriginCountry = findViewById(R.id.fli_spnOriginCountry)
        spnDestCountry = findViewById(R.id.fli_spnDestCountry)
        txtFlightNum = findViewById(R.id.fli_txtFlightNum)
        txtDateTime = findViewById(R.id.fli_txtDateTime)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnOriginCountry.adapter = adapter
        spnDestCountry.adapter = adapter

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtDateTime.setText(util.formatDateTime(LocalDateTime.now().plusDays(2)))
        }

        findViewById<Button>(R.id.fli_btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.fli_btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.fli_btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        val item = FliViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        spnOriginCountry.setSelection(countries.indexOf(item.originCountry))
        spnDestCountry.setSelection(countries.indexOf(item.destCountry))
        txtFlightNum.setText(item.flightNumber)
        txtDateTime.setText(util.formatDateTime(item.flightDateTime))
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val flightNum = txtFlightNum.text.toString().trim().uppercase()
        val dateTimeStr = txtDateTime.text.toString().trim()

        if (id.isEmpty() || name.isEmpty() || flightNum.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos requeridos.", Toast.LENGTH_SHORT).show()
            return
        }

        val origin = spnOriginCountry.selectedItem as Country
        val dest = spnDestCountry.selectedItem as Country

        if (origin == dest) {
            Toast.makeText(this, "El país destino debe ser diferente al país de origen.", Toast.LENGTH_SHORT).show()
            return
        }

        val flightRegex = Regex("""^[A-Z]{2}\d{4}$""")
        if (!flightRegex.matches(flightNum)) {
            Toast.makeText(this, "Número de vuelo debe ser de formato AB1234.", Toast.LENGTH_SHORT).show()
            return
        }

        val dt = util.parseStringToDateTimeModern(dateTimeStr)
        if (dt == null || !dt.isAfter(LocalDateTime.now())) {
            Toast.makeText(this, "La fecha de vuelo debe ser mayor a la fecha actual.", Toast.LENGTH_SHORT).show()
            return
        }

        val item = FliFlight(
            id = id,
            name = name,
            firstName = txtFirstName.text.toString().trim(),
            lastName = txtLastName.text.toString().trim(),
            phone = txtPhone.text.toString().toIntOrNull() ?: 0,
            email = txtEmail.text.toString().trim(),
            address = txtAddress.text.toString().trim(),
            originCountry = origin,
            destCountry = dest,
            flightNumber = flightNum,
            flightDateTime = dt
        )

        if (currentId.isNullOrEmpty()) {
            if (FliViewModel.getById(id) != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            FliViewModel.save(item)
        } else {
            FliViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            FliViewModel.delete(id)
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
            R.id.mnu_cancel -> { finish(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
