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
import cr.ac.utn.examenI.domain.model.LibLibrary
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.LibViewModel
import java.time.LocalDateTime

class LibCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var chkBook1: CheckBox
    private lateinit var chkBook2: CheckBox
    private lateinit var chkBook3: CheckBox
    private lateinit var txtBranch: EditText
    private lateinit var txtResDate: EditText
    private lateinit var txtRetDate: EditText

    private var currentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lib_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtId = findViewById(R.id.lib_txtId)
        txtName = findViewById(R.id.lib_txtName)
        txtFirstName = findViewById(R.id.lib_txtFirstName)
        txtLastName = findViewById(R.id.lib_txtLastName)
        txtPhone = findViewById(R.id.lib_txtPhone)
        txtEmail = findViewById(R.id.lib_txtEmail)
        txtAddress = findViewById(R.id.lib_txtAddress)
        chkBook1 = findViewById(R.id.lib_chkBook1)
        chkBook2 = findViewById(R.id.lib_chkBook2)
        chkBook3 = findViewById(R.id.lib_chkBook3)
        txtBranch = findViewById(R.id.lib_txtBranch)
        txtResDate = findViewById(R.id.lib_txtResDate)
        txtRetDate = findViewById(R.id.lib_txtRetDate)

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtResDate.setText(util.formatDateTime(LocalDateTime.now()))
            txtRetDate.setText(util.formatDateTime(LocalDateTime.now().plusDays(7)))
        }

        findViewById<Button>(R.id.lib_btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.lib_btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.lib_btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        val item = LibViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        chkBook1.isChecked = item.reservedBooks.contains("Don Quijote")
        chkBook2.isChecked = item.reservedBooks.contains("Cien Años de Soledad")
        chkBook3.isChecked = item.reservedBooks.contains("El Principito")
        txtBranch.setText(item.branch)
        txtResDate.setText(util.formatDateTime(item.reservationDateTime))
        txtRetDate.setText(util.formatDateTime(item.returnDateTime))
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val resDateStr = txtResDate.text.toString().trim()
        val retDateStr = txtRetDate.text.toString().trim()

        if (id.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos requeridos.", Toast.LENGTH_SHORT).show()
            return
        }

        val resDt = util.parseStringToDateTimeModern(resDateStr) ?: LocalDateTime.now()
        val retDt = util.parseStringToDateTimeModern(retDateStr) ?: resDt.plusDays(7)

        if (!retDt.isAfter(resDt)) {
            Toast.makeText(this, "La fecha de devolución debe ser mayor a la fecha de reserva.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedBooks = mutableListOf<String>()
        if (chkBook1.isChecked) selectedBooks.add("Don Quijote")
        if (chkBook2.isChecked) selectedBooks.add("Cien Años de Soledad")
        if (chkBook3.isChecked) selectedBooks.add("El Principito")

        val item = LibLibrary(
            id = id,
            name = name,
            firstName = txtFirstName.text.toString().trim(),
            lastName = txtLastName.text.toString().trim(),
            phone = txtPhone.text.toString().toIntOrNull() ?: 0,
            email = txtEmail.text.toString().trim(),
            address = txtAddress.text.toString().trim(),
            reservedBooks = selectedBooks.joinToString(", "),
            branch = txtBranch.text.toString().trim(),
            reservationDateTime = resDt,
            returnDateTime = retDt
        )

        if (currentId.isNullOrEmpty()) {
            if (LibViewModel.getById(id) != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            LibViewModel.save(item)
        } else {
            LibViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            LibViewModel.delete(id)
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
