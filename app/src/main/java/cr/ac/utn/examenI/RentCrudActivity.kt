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
import cr.ac.utn.examenI.domain.model.Identifier
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util

class RentCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var spnVehicleType: Spinner
    private lateinit var txtPlate: EditText
    private lateinit var txtMileage: EditText
    private lateinit var txtDriverLicense: EditText

    private var currentId: String? = null
    private val vehicleTypes = arrayOf("Hatchback", "Sedan", "SUV", "Microbus")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rent_crud)
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
        spnVehicleType = findViewById(R.id.spnVehicleType)
        txtPlate = findViewById(R.id.txtPlate)
        txtMileage = findViewById(R.id.txtMileage)
        txtDriverLicense = findViewById(R.id.txtDriverLicense)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnVehicleType.adapter = adapter

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        /*val item = RentViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        val pos = vehicleTypes.indexOf(item.vehicleType)
        if (pos >= 0) spnVehicleType.setSelection(pos)
        txtPlate.setText(item.plate)
        txtMileage.setText(item.mileage.toString())
        txtDriverLicense.setText(item.driverLicense)*/
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val plate = txtPlate.text.toString().trim()
        val license = txtDriverLicense.text.toString().trim()

        if (id.isEmpty() || name.isEmpty() || plate.isEmpty() || license.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos requeridos.", Toast.LENGTH_SHORT).show()
            return
        }



        if (currentId.isNullOrEmpty()) {
            if ( null != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            //RentViewModel.save(item)
        } else {
            //RentViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            //RentViewModel.delete(id)
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
