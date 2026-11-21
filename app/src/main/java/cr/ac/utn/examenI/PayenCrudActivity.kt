package cr.ac.utn.examenI

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.examenI.domain.model.PayenReimbursement
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.PayenViewModel
import java.time.LocalDateTime

class PayenCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtSpecialty: EditText
    private lateinit var txtApptCost: EditText
    private lateinit var txtMedicineCost: EditText
    private lateinit var chkMed1: CheckBox
    private lateinit var chkMed2: CheckBox
    private lateinit var chkMed3: CheckBox
    private lateinit var txtReimbursement: EditText
    private lateinit var txtApptDate: EditText
    private lateinit var txtClaimDate: EditText

    private var currentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payen_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtId = findViewById(R.id.payen_txtId)
        txtName = findViewById(R.id.payen_txtName)
        txtFirstName = findViewById(R.id.payen_txtFirstName)
        txtLastName = findViewById(R.id.payen_txtLastName)
        txtPhone = findViewById(R.id.payen_txtPhone)
        txtEmail = findViewById(R.id.payen_txtEmail)
        txtAddress = findViewById(R.id.payen_txtAddress)
        txtSpecialty = findViewById(R.id.payen_txtSpecialty)
        txtApptCost = findViewById(R.id.payen_txtApptCost)
        txtMedicineCost = findViewById(R.id.payen_txtMedicineCost)
        chkMed1 = findViewById(R.id.payen_chkMed1)
        chkMed2 = findViewById(R.id.payen_chkMed2)
        chkMed3 = findViewById(R.id.payen_chkMed3)
        txtReimbursement = findViewById(R.id.payen_txtReimbursement)
        txtApptDate = findViewById(R.id.payen_txtApptDate)
        txtClaimDate = findViewById(R.id.payen_txtClaimDate)

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { calculateReimbursement() }
            override fun afterTextChanged(s: Editable?) {}
        }
        txtApptCost.addTextChangedListener(watcher)
        txtMedicineCost.addTextChangedListener(watcher)

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtApptDate.setText(util.formatDateTime(LocalDateTime.now()))
            txtClaimDate.setText(util.formatDateTime(LocalDateTime.now()))
        }

        findViewById<Button>(R.id.payen_btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.payen_btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.payen_btnCancel).setOnClickListener { finish() }
    }

    private fun calculateReimbursement() {
        val appt = txtApptCost.text.toString().toDoubleOrNull() ?: 0.0
        val med = txtMedicineCost.text.toString().toDoubleOrNull() ?: 0.0
        val reimbursement = (appt + med) * 0.80
        txtReimbursement.setText(reimbursement.toString())
    }

    private fun loadExistingData(id: String) {
        val item = PayenViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        txtSpecialty.setText(item.specialty)
        txtApptCost.setText(item.apptCost.toString())
        txtMedicineCost.setText(item.medicineCost.toString())
        chkMed1.isChecked = item.medicines.contains("Analgésicos")
        chkMed2.isChecked = item.medicines.contains("Antibióticos")
        chkMed3.isChecked = item.medicines.contains("Antiinflamatorios")
        txtReimbursement.setText(item.reimbursementAmount.toString())
        txtApptDate.setText(util.formatDateTime(item.apptDateTime))
        txtClaimDate.setText(util.formatDateTime(item.claimDateTime))
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val apptCostVal = txtApptCost.text.toString().toDoubleOrNull() ?: 0.0
        val medCostVal = txtMedicineCost.text.toString().toDoubleOrNull() ?: 0.0

        if (id.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos requeridos.", Toast.LENGTH_SHORT).show()
            return
        }

        val apptDt = util.parseStringToDateTimeModern(txtApptDate.text.toString().trim()) ?: LocalDateTime.now()
        val claimDt = util.parseStringToDateTimeModern(txtClaimDate.text.toString().trim()) ?: LocalDateTime.now()

        val selectedMeds = mutableListOf<String>()
        if (chkMed1.isChecked) selectedMeds.add("Analgésicos")
        if (chkMed2.isChecked) selectedMeds.add("Antibióticos")
        if (chkMed3.isChecked) selectedMeds.add("Antiinflamatorios")

        val reimbursement = (apptCostVal + medCostVal) * 0.80

        val item = PayenReimbursement(
            id = id,
            name = name,
            firstName = txtFirstName.text.toString().trim(),
            lastName = txtLastName.text.toString().trim(),
            phone = txtPhone.text.toString().toIntOrNull() ?: 0,
            email = txtEmail.text.toString().trim(),
            address = txtAddress.text.toString().trim(),
            specialty = txtSpecialty.text.toString().trim(),
            apptCost = apptCostVal,
            medicineCost = medCostVal,
            reimbursementAmount = reimbursement,
            medicines = selectedMeds.joinToString(", "),
            apptDateTime = apptDt,
            claimDateTime = claimDt
        )

        if (currentId.isNullOrEmpty()) {
            if (PayenViewModel.getById(id) != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            PayenViewModel.save(item)
        } else {
            PayenViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            PayenViewModel.delete(id)
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
