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
import cr.ac.utn.examenI.domain.model.VacRecord
import cr.ac.utn.examenI.util.util
//these imports were add by Caguirret
import androidx.activity.viewModels
import cr.ac.utn.examenI.viewmodel.VacViewModel
import java.time.LocalDateTime

class VacCrudActivity : AppCompatActivity() {
    //to provide access to vaccination records through the viewmodel
    private val vacViewModel: VacViewModel by viewModels()
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var spnVaccineType: Spinner
    private lateinit var txtLocation: EditText
    private lateinit var txtDateTime: EditText

    private var currentId: String? = null
    private val vaccines = arrayOf("COVID-19", "Influenza", "Tétanos", "Hepatitis B")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vac_crud)
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
        spnVaccineType = findViewById(R.id.spnVaccineType)
        txtLocation = findViewById(R.id.txtLocation)
        txtDateTime = findViewById(R.id.txtDateTime)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vaccines)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnVaccineType.adapter = adapter

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

    //Loads an existing vaccination record into the form
    //the identifier remains read-only during editing to preserve record consistency
    private fun loadExistingData(id: String) {

        val record = vacViewModel.getById(id)

        if (record == null){
            Toast.makeText(
                this,
                R.string.TextDataNoFound,
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }
        //Load patient information
        txtName.setText(record.name)
        txtFirstName.setText(record.firstName)
        txtLastName.setText(record.lastName)
        txtPhone.setText(record.phone.toString())
        txtEmail.setText(record.email)
        txtAddress.setText(record.address)

        //Load vaccination information
        val vaccinePosition = vaccines.indexOf(record.vaccineType)

        if (vaccinePosition >= 0) {
            spnVaccineType.setSelection(vaccinePosition)
        }

        txtLocation.setText(record.location)
        txtDateTime.setText(util.formatDateTime(record.vaccinationDateTime))
    }

    //Validates and saves a vaccination record and prevents future vaccinations
    private fun doSave(){
        //validate required fields
        val requiredFields = listOf(
            txtId,
            txtName,
            txtFirstName,
            txtLastName,
            txtPhone,
            txtEmail,
            txtAddress,
            txtLocation,
            txtDateTime
        )

        val empyField = requiredFields.firstOrNull {
            it.text.toString().isBlank()
        }

        if (empyField != null) {
            empyField.error = getString(R.string.TextSaveError)
            empyField.requestFocus()
            return
        }

        //retrieve patient information
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val firstName = txtFirstName.text.toString().trim()
        val lastName = txtLastName.text.toString().trim()
        val email = txtEmail.text.toString().trim()
        val address = txtAddress.text.toString().trim()

        //validate phone number
        val phone = txtPhone.text.toString().toIntOrNull()

        if (phone == null  || phone <= 0){
            txtPhone.error = getString(R.string.TextSaveError)
            return
        }

        //Retrieve vaccination information
        val vaccinationType = spnVaccineType.selectedItem?.toString().orEmpty()
        val location = txtLocation.text.toString().trim()
        val dateTimeText = txtDateTime.text.toString().trim()

        if (vaccinationType.isBlank()){
            Toast.makeText(
                this,
                R.string.TextSaveError,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        //validate vaccionation date and time
        val vaccinationDateTime =
            util.parseStringToDateTimeModern(dateTimeText)

        if (vaccinationDateTime == null ||
            vaccinationDateTime.isAfter(LocalDateTime.now())
            ){
            txtDateTime.error = getString(R.string.TextSaveError)
            return
        }

        //Preserve the identifier during updates
        if (!currentId.isNullOrEmpty() && currentId != id){
            txtId.error = getString(R.string.TextSaveError)
            return
        }

        //Create the vaccination entity
        val record = VacRecord(
            id = id,
            name = name,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            email = email,
            address = address,
            vaccineType = vaccinationType,
            location = location,
            vaccinationDateTime = vaccinationDateTime
        )

        //save or update the record
        val successful = if (currentId.isNullOrEmpty()) {
            vacViewModel.save(record)
        }else{
            vacViewModel.update(record)
        }

        if (!successful){
            val messenRes = if (currentId.isNullOrEmpty()){
                R.string.TextDuplicateData
            }else{
                R.string.TextDataNoFound
            }
            Toast.makeText(this, messenRes, Toast.LENGTH_SHORT).show()
            return
        }

        //Show success only after a succesful operation
        Toast.makeText(
            this,
            R.string.TextSaveSuccess,
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    //Delete an existing vaccination record after confirmation
    //and prevents delection during record creation and displays success only when record
    //is actually removed
    private fun doDelete() {
        //Only an existing records can be deleted obviously
        val id = currentId

        if (id.isNullOrBlank()) {
            Toast.makeText(
                this,
                R.string.TextDataNoFound,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        //Request confirmation before a deleting
        util.showDialogCondition(
            this, getString(R.string.TextDeleteQuestion)
        ){
            val delete = vacViewModel.delete(id)

            if (delete){
                Toast.makeText(this,
                    R.string.TextDeleteSuccess,
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                Toast.makeText(
                    this,
                    R.string.TextDeleteError,
                    Toast.LENGTH_SHORT
                ).show()
            }
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
