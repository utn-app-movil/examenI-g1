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

class ShipCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtContainerNum: EditText
    private lateinit var spnContainerType: Spinner
    private lateinit var txtTemp: EditText
    private lateinit var txtWeight: EditText
    private lateinit var spnProduct: Spinner
    private lateinit var txtDateTime: EditText

    private var currentId: String? = null
    private val containerTypes = arrayOf("Húmedo", "Seco")
    private val products = arrayOf("Banano", "Piña", "Café", "Médico")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ship_crud)
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
        txtContainerNum = findViewById(R.id.txtContainerNum)
        spnContainerType = findViewById(R.id.spnContainerType)
        txtTemp = findViewById(R.id.txtTemp)
        txtWeight = findViewById(R.id.txtWeight)
        spnProduct = findViewById(R.id.spnProduct)
        txtDateTime = findViewById(R.id.txtDateTime)

        val adapterType = ArrayAdapter(this, android.R.layout.simple_spinner_item, containerTypes)
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnContainerType.adapter = adapterType

        val adapterProd = ArrayAdapter(this, android.R.layout.simple_spinner_item, products)
        adapterProd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnProduct.adapter = adapterProd

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtDateTime.setText(util.formatDateTime(LocalDateTime.now()))
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        /*val item = ShipViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        txtContainerNum.setText(item.containerNum)
        val posType = containerTypes.indexOf(item.containerType)
        if (posType >= 0) spnContainerType.setSelection(posType)
        txtTemp.setText(item.temperature.toString())
        txtWeight.setText(item.weight.toString())
        val posProd = products.indexOf(item.product)
        if (posProd >= 0) spnProduct.setSelection(posProd)
        txtDateTime.setText(util.formatDateTime(item.shipmentDateTime))*/
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val containerNum = txtContainerNum.text.toString().trim().uppercase()
        val dateTimeStr = txtDateTime.text.toString().trim()

        if (id.isEmpty() || name.isEmpty() || containerNum.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos requeridos.", Toast.LENGTH_SHORT).show()
            return
        }

        val alphaNumRegex = Regex("""^[A-Z0-9]+$""")
        if (!alphaNumRegex.matches(containerNum)) {
            Toast.makeText(this, "El número de contenedor debe ser alfanumérico.", Toast.LENGTH_SHORT).show()
            return
        }

        // Unique container num
        val duplicateContainer = false //ShipViewModel.getAll().any { it.containerNum == containerNum && it.id != id }
        if (duplicateContainer) {
            Toast.makeText(this, "El número de contenedor ya está registrado.", Toast.LENGTH_SHORT).show()
            return
        }

        val dt = util.parseStringToDateTimeModern(dateTimeStr)
        if (dt == null || dt.isAfter(LocalDateTime.now())) {
            Toast.makeText(this, "La fecha debe ser menor o igual a la actual.", Toast.LENGTH_SHORT).show()
            return
        }



        if (currentId.isNullOrEmpty()) {
            if (null != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            //ShipViewModel.save(item)
        } else {
            //ShipViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            //ShipViewModel.delete(id)
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
