package cr.ac.utn.examenI

import android.app.DatePickerDialog
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
import cr.ac.utn.examenI.domain.model.MarkCampaign
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.MarkViewModel
import java.time.LocalDate

class MarkCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtCode: EditText
    private lateinit var txtCampaignName: EditText
    private lateinit var txtBudget: EditText
    private lateinit var spnChannel: Spinner
    private lateinit var txtLeader: EditText
    private lateinit var spnProvince: Spinner
    private lateinit var txtStartDate: EditText
    private lateinit var txtEndDate: EditText

    private val markViewModel = MarkViewModel()
    private var currentId: String? = null
    private val channels = arrayOf("Digital", "TV", "Radio")
    private val provinces = arrayOf("San Jose", "Alajuela", "Cartago", "Heredia", "Guanacaste", "Puntarenas", "Limon")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mark_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtId = findViewById(R.id.mark_txtId)
        txtName = findViewById(R.id.mark_txtName)
        txtFirstName = findViewById(R.id.mark_txtFirstName)
        txtLastName = findViewById(R.id.mark_txtLastName)
        txtPhone = findViewById(R.id.mark_txtPhone)
        txtEmail = findViewById(R.id.mark_txtEmail)
        txtAddress = findViewById(R.id.mark_txtAddress)
        txtCode = findViewById(R.id.mark_txtCode)
        txtCampaignName = findViewById(R.id.mark_txtCampaignName)
        txtBudget = findViewById(R.id.mark_txtBudget)
        spnChannel = findViewById(R.id.mark_spnChannel)
        txtLeader = findViewById(R.id.mark_txtLeader)
        spnProvince = findViewById(R.id.mark_spnProvince)
        txtStartDate = findViewById(R.id.mark_txtStartDate)
        txtEndDate = findViewById(R.id.mark_txtEndDate)

        val adapterChan = ArrayAdapter(this, android.R.layout.simple_spinner_item, channels)
        adapterChan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnChannel.adapter = adapterChan

        val adapterProv = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        adapterProv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnProvince.adapter = adapterProv

        txtStartDate.setOnClickListener { showDatePicker(txtStartDate) }
        txtEndDate.setOnClickListener { showDatePicker(txtEndDate) }

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtStartDate.setText(util.formatDate(LocalDate.now()))
            txtEndDate.setText(util.formatDate(LocalDate.now().plusMonths(6)))
        }

        findViewById<Button>(R.id.mark_btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.mark_btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.mark_btnCancel).setOnClickListener { finish() }
    }

    private fun showDatePicker(editText: EditText) {
        val date = util.parseStringToDateModern(editText.text.toString()) ?: LocalDate.now()
        DatePickerDialog(this, { _, year, month, day ->
            editText.setText(util.formatDate(LocalDate.of(year, month + 1, day)))
        }, date.year, date.monthValue - 1, date.dayOfMonth).show()
    }

    private fun loadExistingData(id: String) {
        val item = markViewModel.getById(id)
        if (item == null) {
            Toast.makeText(this, getString(R.string.TextDataNoFound), Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        txtCode.setText(item.code)
        txtCode.isEnabled = false
        txtCampaignName.setText(item.campaignName)
        txtBudget.setText(item.budget.toString())
        val posChan = channels.indexOf(item.channel)
        if (posChan >= 0) spnChannel.setSelection(posChan)
        txtLeader.setText(item.leader)
        val posProv = provinces.indexOf(item.province)
        if (posProv >= 0) spnProvince.setSelection(posProv)
        txtStartDate.setText(util.formatDate(item.startDate))
        txtEndDate.setText(util.formatDate(item.endDate))
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val codeVal = txtCode.text.toString().trim()
        val campaign = txtCampaignName.text.toString().trim()
        val leader = txtLeader.text.toString().trim()
        val budgetVal = txtBudget.text.toString().toDoubleOrNull() ?: 0.0
        val phoneVal = txtPhone.text.toString().toIntOrNull() ?: 0
        val channel = spnChannel.selectedItem.toString()
        val province = spnProvince.selectedItem.toString()

        if (id.isEmpty() || name.isEmpty() || codeVal.isEmpty() || campaign.isEmpty() || leader.isEmpty()) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        if (budgetVal <= 0.0) {
            txtBudget.error = getString(R.string.TextSaveError)
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        if (!channels.contains(channel) || !provinces.contains(province)) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val startDt = util.parseStringToDateModern(txtStartDate.text.toString().trim())
        val endDt = util.parseStringToDateModern(txtEndDate.text.toString().trim())

        if (startDt == null || endDt == null || startDt.isAfter(endDt)) {
            txtEndDate.error = getString(R.string.TextSaveError)
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val item = MarkCampaign(
            id, name,
            txtFirstName.text.toString().trim(),
            txtLastName.text.toString().trim(),
            phoneVal,
            txtEmail.text.toString().trim(),
            txtAddress.text.toString().trim(),
            codeVal, campaign, budgetVal, channel, leader, province, startDt, endDt
        )

        if (currentId.isNullOrEmpty()) {
            if (markViewModel.getById(id) != null || markViewModel.existsCode(codeVal)) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            markViewModel.save(item)
        } else {
            markViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = currentId
        if (id.isNullOrEmpty() || markViewModel.getById(id) == null) {
            Toast.makeText(this, getString(R.string.TextDataNoFound), Toast.LENGTH_SHORT).show()
            return
        }
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            markViewModel.delete(id)
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