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
import java.time.LocalDateTime

class VehCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtVehicleId: EditText
    private lateinit var txtRangeKm: EditText
    private lateinit var txtInitialBattery: EditText
    private lateinit var txtFinalBattery: EditText
    private lateinit var spnBrand: Spinner
    private lateinit var spnVehicleType: Spinner
    private lateinit var txtChargeDate: EditText

    private var currentId: String? = null
    private val brands = arrayOf("Tesla", "BYD", "Nissan", "Hyundai")
    private val vehicleTypes = arrayOf("Sedan", "SUV", "Hatchback")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_veh_crud)
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
        txtVehicleId = findViewById(R.id.txtVehicleId)
        txtRangeKm = findViewById(R.id.txtRangeKm)
        txtInitialBattery = findViewById(R.id.txtInitialBattery)
        txtFinalBattery = findViewById(R.id.txtFinalBattery)
        spnBrand = findViewById(R.id.spnBrand)
        spnVehicleType = findViewById(R.id.spnVehicleType)
        txtChargeDate = findViewById(R.id.txtChargeDate)

        val adapterBrand = ArrayAdapter(this, android.R.layout.simple_spinner_item, brands)
        adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnBrand.adapter = adapterBrand

        val adapterType = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleTypes)
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnVehicleType.adapter = adapterType

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtChargeDate.setText(util.formatDateTime(LocalDateTime.now()))
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        /*val item = VehViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        txtVehicleId.setText(item.vehicleId)
        txtRangeKm.setText(item.rangeKm.toString())
        txtInitialBattery.setText(item.initialBatteryPct.toString())
        txtFinalBattery.setText(item.finalBatteryPct.toString())
        val posBrand = brands.indexOf(item.brand)
        if (posBrand >= 0) spnBrand.setSelection(posBrand)
        val posType = vehicleTypes.indexOf(item.vehicleType)
        if (posType >= 0) spnVehicleType.setSelection(posType)
        txtChargeDate.setText(util.formatDateTime(item.chargeDateTime))*/
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val vehId = txtVehicleId.text.toString().trim()
        val range = txtRangeKm.text.toString().toDoubleOrNull() ?: 0.0
        val initBat = txtInitialBattery.text.toString().toDoubleOrNull() ?: 0.0
        val finalBat = txtFinalBattery.text.toString().toDoubleOrNull() ?: 0.0

        if (id.isEmpty() || vehId.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos requeridos.", Toast.LENGTH_SHORT).show()
            return
        }

        if (range <= 0) {
            Toast.makeText(this, "El rango en Km debe ser mayor a 0.", Toast.LENGTH_SHORT).show()
            return
        }

        if (initBat > 100 || finalBat > 100 || initBat < 0 || finalBat < 0) {
            Toast.makeText(this, "El porcentaje de batería debe estar entre 0% y 100%.", Toast.LENGTH_SHORT).show()
            return
        }

        val dt = util.parseStringToDateTimeModern(txtChargeDate.text.toString().trim()) ?: LocalDateTime.now()



        if (currentId.isNullOrEmpty()) {
            if (null != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            //VehViewModel.save(item)
        } else {
            //VehViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            //VehViewModel.delete(id)
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
