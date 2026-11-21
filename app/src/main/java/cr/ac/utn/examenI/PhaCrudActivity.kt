package cr.ac.utn.examenI

import android.os.Bundle
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
import cr.ac.utn.examenI.domain.model.PhaPharmacy
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.PhaViewModel
import java.time.LocalDateTime

class PhaCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var chkCream: CheckBox
    private lateinit var chkPills: CheckBox
    private lateinit var chkSyrup: CheckBox
    private lateinit var txtTotalAmount: EditText
    private lateinit var txtDateTime: EditText

    private var currentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pha_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtId = findViewById(R.id.pha_txtId)
        txtName = findViewById(R.id.pha_txtName)
        txtFirstName = findViewById(R.id.pha_txtFirstName)
        txtLastName = findViewById(R.id.pha_txtLastName)
        txtPhone = findViewById(R.id.pha_txtPhone)
        txtEmail = findViewById(R.id.pha_txtEmail)
        txtAddress = findViewById(R.id.pha_txtAddress)
        chkCream = findViewById(R.id.pha_chkCream)
        chkPills = findViewById(R.id.pha_chkPills)
        chkSyrup = findViewById(R.id.pha_chkSyrup)
        txtTotalAmount = findViewById(R.id.pha_txtTotalAmount)
        txtDateTime = findViewById(R.id.pha_txtDateTime)

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtDateTime.setText(util.formatDateTime(LocalDateTime.now()))
        }

        findViewById<Button>(R.id.pha_btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.pha_btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.pha_btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        val item = PhaViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        chkCream.isChecked = item.medicines.contains("Crema")
        chkPills.isChecked = item.medicines.contains("Pastillas")
        chkSyrup.isChecked = item.medicines.contains("Jarabe")
        txtTotalAmount.setText(item.totalAmount.toString())
        txtDateTime.setText(util.formatDateTime(item.purchaseDateTime))
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val totalAmt = txtTotalAmount.text.toString().toDoubleOrNull()

        if (totalAmt == null || !totalAmt.isFinite() || totalAmt <= 0.0) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val dateTimeStr = txtDateTime.text.toString().trim()

        if (id.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val email = txtEmail.text.toString().trim()
        if (email.isNotEmpty() && (!email.contains("@") || !email.contains("."))) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val phoneText = txtPhone.text.toString().trim()
        val phone = phoneText.toIntOrNull()
        if (phone == null || phone <= 0 || phoneText.length < 8) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val dt = util.parseStringToDateTimeModern(dateTimeStr)
        if (dt == null || dt.isAfter(LocalDateTime.now())) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val selectedMeds = mutableListOf<String>()
        if (chkCream.isChecked) selectedMeds.add("Crema")
        if (chkPills.isChecked) selectedMeds.add("Pastillas")
        if (chkSyrup.isChecked) selectedMeds.add("Jarabe")

        if (selectedMeds.isEmpty()) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val item = PhaPharmacy(
            id = id,
            name = name,
            firstName = txtFirstName.text.toString().trim(),
            lastName = txtLastName.text.toString().trim(),
            phone = phone,
            email = email,
            address = txtAddress.text.toString().trim(),
            medicines = selectedMeds.joinToString(", "),
            totalAmount = totalAmt,
            purchaseDateTime = dt
        )

        if (currentId.isNullOrEmpty()) {
            if (PhaViewModel.getById(id) != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            PhaViewModel.save(item)
        } else {
            PhaViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty() || PhaViewModel.getById(id) == null) {
            Toast.makeText(this, getString(R.string.TextDataNoFound), Toast.LENGTH_SHORT).show()
            return
        }
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            PhaViewModel.delete(id)
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
