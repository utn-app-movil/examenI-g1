package cr.ac.utn.examenI

import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.examenI.domain.model.DashContribution
import cr.ac.utn.examenI.domain.model.Person
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.DashViewModel
import java.time.LocalDate

class DashCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtContributionsTotal: EditText
    private lateinit var txtMonth: EditText
    private lateinit var txtYear: EditText

    private val viewModel = DashViewModel()
    private var currentId: String? = null

    companion object {
        private const val PHONE_LENGTH = 8
        private const val MIN_MONTH = 1
        private const val MAX_MONTH = 12
        private const val MIN_YEAR = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dash_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dash_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtId = findViewById(R.id.dash_txtId)
        txtName = findViewById(R.id.dash_txtName)
        txtFirstName = findViewById(R.id.dash_txtFirstName)
        txtLastName = findViewById(R.id.dash_txtLastName)
        txtPhone = findViewById(R.id.dash_txtPhone)
        txtEmail = findViewById(R.id.dash_txtEmail)
        txtAddress = findViewById(R.id.dash_txtAddress)
        txtContributionsTotal = findViewById(R.id.dash_txtContributionsTotal)
        txtMonth = findViewById(R.id.dash_txtMonth)
        txtYear = findViewById(R.id.dash_txtYear)

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        }

        findViewById<Button>(R.id.dash_btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.dash_btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.dash_btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        val item = viewModel.getById(id)
        if (item == null) {
            Toast.makeText(this, getString(R.string.TextDataNoFound), Toast.LENGTH_SHORT).show()
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
        txtContributionsTotal.setText(item.contributionsTotal.toString())
        txtMonth.setText(item.month.toString())
        txtYear.setText(item.year.toString())

        // these 3 fields make the id of the record, so they can not change
        txtId.isEnabled = false
        txtMonth.isEnabled = false
        txtYear.isEnabled = false
    }

    private fun showError(field: EditText) {
        field.requestFocus()
        Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val firstName = txtFirstName.text.toString().trim()
        val lastName = txtLastName.text.toString().trim()
        val phoneText = txtPhone.text.toString().trim()
        val email = txtEmail.text.toString().trim()
        val address = txtAddress.text.toString().trim()
        val total = txtContributionsTotal.text.toString().trim().toDoubleOrNull()
        val month = txtMonth.text.toString().trim().toIntOrNull()
        val year = txtYear.text.toString().trim().toIntOrNull()
        val phone = phoneText.toIntOrNull()

        if (id.isEmpty()) { showError(txtId); return }
        if (name.isEmpty()) { showError(txtName); return }
        if (firstName.isEmpty()) { showError(txtFirstName); return }
        if (lastName.isEmpty()) { showError(txtLastName); return }
        if (phone == null || phoneText.length != PHONE_LENGTH) { showError(txtPhone); return }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { showError(txtEmail); return }
        if (address.isEmpty()) { showError(txtAddress); return }
        if (total == null || total <= 0.0) { showError(txtContributionsTotal); return }
        if (month == null || month < MIN_MONTH || month > MAX_MONTH) { showError(txtMonth); return }
        if (year == null || year < MIN_YEAR || year > LocalDate.now().year) { showError(txtYear); return }

        val person = Person(
            id = id,
            name = name,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            email = email,
            address = address
        )
        val item = DashContribution(person, total, month, year)

        if (currentId.isNullOrEmpty()) {
            // same person + month + year can not be repeated
            if (viewModel.isDuplicate(id, month, year)) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            viewModel.save(item)
            saveFinished()
        } else {
            util.showDialogCondition(this, getString(R.string.TextSaveQuestion)) {
                viewModel.update(item)
                saveFinished()
            }
        }
    }

    private fun saveFinished() {
        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val idToDelete = currentId
        if (idToDelete.isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.TextDataNoFound), Toast.LENGTH_SHORT).show()
            return
        }
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            viewModel.delete(idToDelete)
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