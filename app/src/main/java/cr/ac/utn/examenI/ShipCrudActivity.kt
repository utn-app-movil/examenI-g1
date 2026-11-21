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
import cr.ac.utn.examenI.domain.model.Person
import cr.ac.utn.examenI.domain.model.ShipMaintenance
import cr.ac.utn.examenI.viewmodel.ShipViewModel

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

        val item = ShipViewModel.getById(id)

        if (item == null) {
            Toast.makeText(
                this,
                R.string.TextSaveError,
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }

        txtId.setText(item.person.id)
        txtName.setText(item.person.name)
        txtFirstName.setText(item.person.firstName)
        txtLastName.setText(item.person.lastName)
        txtPhone.setText(item.person.phone.toString())
        txtEmail.setText(item.person.email)
        txtAddress.setText(item.person.address)
        txtContainerNum.setText(item.id)
        txtContainerNum.isEnabled = false

        val posType = containerTypes.indexOf(item.containerType)
        if (posType >= 0) {
            spnContainerType.setSelection(posType)
        }

        txtTemp.setText(item.temperature.toString())
        txtWeight.setText(item.weight.toString())

        val posProduct = products.indexOf(item.product)
        if (posProduct >= 0) {
            spnProduct.setSelection(posProduct)
        }
        txtDateTime.setText(
            util.formatDateTime(item.maintenanceDateTime)
        )
    }

    private fun doSave() {

        val personId = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val firstName = txtFirstName.text.toString().trim()
        val lastName = txtLastName.text.toString().trim()
        val phoneText = txtPhone.text.toString().trim()
        val email = txtEmail.text.toString().trim()
        val address = txtAddress.text.toString().trim()
        val containerNum = txtContainerNum.text.toString().trim().uppercase()
        val containerType = spnContainerType.selectedItem.toString()
        val temperatureText = txtTemp.text.toString().trim()
        val weightText = txtWeight.text.toString().trim()
        val product = spnProduct.selectedItem.toString()
        val dateTimeText = txtDateTime.text.toString().trim()

        if (personId.isEmpty() ||
            name.isEmpty() ||
            firstName.isEmpty() ||
            lastName.isEmpty() ||
            phoneText.isEmpty() ||
            email.isEmpty() ||
            address.isEmpty() ||
            containerNum.isEmpty() ||
            temperatureText.isEmpty() ||
            weightText.isEmpty() ||
            dateTimeText.isEmpty()
        ) {
            Toast.makeText(
                this,
                R.string.TextSaveError,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val alphaNumRegex = Regex("^[A-Z0-9]+$")

        if (!alphaNumRegex.matches(containerNum)) {
            Toast.makeText(
                this,
                R.string.TextSaveError,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Validate numeric values
        val phone = phoneText.toIntOrNull()
        val temperature = temperatureText.toDoubleOrNull()
        val weight = weightText.toDoubleOrNull()

        if (phone == null ||
            phone <= 0 ||
            temperature == null ||
            !temperature.isFinite() ||
            weight == null ||
            !weight.isFinite() ||
            weight <= 0
        ) {
            Toast.makeText(
                this,
                R.string.TextSaveError,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val maintenanceDateTime =
            util.parseStringToDateTimeModern(dateTimeText)

        if (maintenanceDateTime == null ||
            maintenanceDateTime.isAfter(LocalDateTime.now())
        ) {
            Toast.makeText(
                this,
                R.string.TextSaveError,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Validate duplicate container number
        val existingItem = ShipViewModel.getById(containerNum)

        if (existingItem != null && currentId != containerNum) {
            Toast.makeText(
                this,
                R.string.TextDuplicateData,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val person = Person(
            id = personId,
            name = name,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            email = email,
            address = address
        )

        val item = ShipMaintenance(
            id = containerNum,
            person = person,
            maintenanceDateTime = maintenanceDateTime,
            containerType = containerType,
            temperature = temperature,
            weight = weight,
            product = product
        )

        if (currentId.isNullOrEmpty()) {

            ShipViewModel.save(item)

            Toast.makeText(
                this,
                R.string.TextSaveSuccess,
                Toast.LENGTH_SHORT
            ).show()

            finish()

        } else {

            if (currentId != containerNum || existingItem == null) {
                Toast.makeText(
                    this,
                    R.string.TextSaveError,
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            util.showDialogCondition(
                this,
                getString(R.string.TextSaveQuestion)
            ) {
                ShipViewModel.update(item)

                Toast.makeText(
                    this,
                    R.string.TextSaveSuccess,
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
        }
    }



    private fun doDelete() {

        val containerId = currentId

        if (containerId.isNullOrEmpty()) {
            Toast.makeText(
                this,
                R.string.TextDeleteError,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val item = ShipViewModel.getById(containerId)

        if (item == null) {
            Toast.makeText(
                this,
                R.string.TextDataNoFound,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        util.showDialogCondition(
            this,
            getString(R.string.TextDeleteQuestion)
        ) {
            ShipViewModel.delete(containerId)

            Toast.makeText(
                this,
                R.string.TextDeleteSuccess,
                Toast.LENGTH_SHORT
            ).show()

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
