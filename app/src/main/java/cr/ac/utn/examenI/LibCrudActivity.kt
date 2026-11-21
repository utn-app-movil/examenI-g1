package cr.ac.utn.examenI

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.examenI.domain.model.LibReservation
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
    private lateinit var spnBranch: Spinner
    private lateinit var txtResDate: EditText
    private lateinit var txtRetDate: EditText
    private lateinit var btnDelete: Button

    private val viewModel: LibViewModel by viewModels()

    private val branches = arrayOf("Central Branch", "Atenas Branch", "San Carlos Branch", "Pacific Branch")
    private var resDateTime: LocalDateTime? = null
    private var retDateTime: LocalDateTime? = null
    private var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lib_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lib_main)) { v, insets ->
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
        chkBook1 = findViewById(R.id.lib_checkBook1)
        chkBook2 = findViewById(R.id.lib_checkBook2)
        chkBook3 = findViewById(R.id.lib_checkBook3)
        spnBranch = findViewById(R.id.lib_spnBranch)
        txtResDate = findViewById(R.id.lib_txtResDate)
        txtRetDate = findViewById(R.id.lib_txtRetDate)
        btnDelete = findViewById(R.id.lib_btnDelete)

        val branchAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, branches)
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnBranch.adapter = branchAdapter

        txtResDate.setOnClickListener {
            pickDateTime(resDateTime ?: LocalDateTime.now()) { setReservationDate(it) }
        }
        txtRetDate.setOnClickListener {
            pickDateTime(retDateTime ?: LocalDateTime.now().plusDays(7)) { setReturnDate(it) }
        }

        val id = intent.getStringExtra(EXTRA_ID)
        if (!id.isNullOrEmpty()) {
            loadExistingData(id)
        } else {
            btnDelete.visibility = View.GONE
            setReservationDate(LocalDateTime.now().withSecond(0).withNano(0))
            setReturnDate(LocalDateTime.now().plusDays(7).withSecond(0).withNano(0))
        }

        findViewById<Button>(R.id.lib_btnSave).setOnClickListener { doSave() }
        btnDelete.setOnClickListener { doDelete() }
        findViewById<Button>(R.id.lib_btnCancel).setOnClickListener { finish() }
    }

    private fun pickDateTime(initial: LocalDateTime, onPicked: (LocalDateTime) -> Unit) {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                TimePickerDialog(
                    this,
                    { _, hour, minute ->
                        onPicked(LocalDateTime.of(year, month + 1, day, hour, minute))
                    },
                    initial.hour, initial.minute, true
                ).show()
            },
            initial.year, initial.monthValue - 1, initial.dayOfMonth
        ).show()
    }

    private fun setReservationDate(value: LocalDateTime) {
        resDateTime = value
        txtResDate.setText(util.formatDateTime(value))
    }

    private fun setReturnDate(value: LocalDateTime) {
        retDateTime = value
        txtRetDate.setText(util.formatDateTime(value))
    }

    private fun loadExistingData(id: String) {
        val item = viewModel.getById(id)
        if (item == null) {
            showMessage(getString(R.string.TextDataNoFound))
            finish()
            return
        }
        isEditMode = true
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        chkBook1.isChecked = item.reservedBooks.contains(chkBook1.text.toString())
        chkBook2.isChecked = item.reservedBooks.contains(chkBook2.text.toString())
        chkBook3.isChecked = item.reservedBooks.contains(chkBook3.text.toString())
        for (i in branches.indices) {
            if (branches[i] == item.branch) {
                spnBranch.setSelection(i)
            }
        }
        setReservationDate(item.reservationDateTime)
        setReturnDate(item.returnDateTime)
    }

    private fun buildItem(): LibReservation? {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val firstName = txtFirstName.text.toString().trim()
        val lastName = txtLastName.text.toString().trim()
        val phoneText = txtPhone.text.toString().trim()
        val email = txtEmail.text.toString().trim()
        val address = txtAddress.text.toString().trim()

        if (id.isEmpty() || name.isEmpty() || firstName.isEmpty()
            || phoneText.isEmpty() || email.isEmpty()
        ) {
            showMessage(getString(R.string.TextSaveError))
            return null
        }

        val phone = phoneText.toIntOrNull()
        if (phone == null) {
            showMessage(getString(R.string.TextSaveError))
            return null
        }

        if (!email.contains("@") || !email.contains(".")) {
            showMessage(getString(R.string.TextSaveError))
            return null
        }

        val books = mutableListOf<String>()
        if (chkBook1.isChecked) books.add(chkBook1.text.toString())
        if (chkBook2.isChecked) books.add(chkBook2.text.toString())
        if (chkBook3.isChecked) books.add(chkBook3.text.toString())
        if (books.isEmpty()) {
            showMessage(getString(R.string.TextSaveError))
            return null
        }

        val res = resDateTime
        val ret = retDateTime
        if (res == null || ret == null) {
            showMessage(getString(R.string.TextSaveError))
            return null
        }

        if (!viewModel.isReturnAfterReservation(res, ret)) {
            showMessage(getString(R.string.TextSaveError))
            return null
        }

        val branch = spnBranch.selectedItem.toString()
        return LibReservation(
            id, name, firstName, lastName, phone, email, address,
            books, branch, res, ret
        )
    }

    private fun doSave() {
        val item = buildItem() ?: return
        util.showDialogCondition(this, getString(R.string.TextSaveQuestion)) {
            persist(item)
        }
    }

    private fun persist(item: LibReservation) {
        if (!isEditMode) {
            if (!viewModel.save(item)) {
                showMessage(getString(R.string.TextDuplicateData))
                return
            }
        } else {
            viewModel.update(item)
        }
        showMessage(getString(R.string.TextSaveSuccess))
        finish()
    }

    private fun doDelete() {
        if (!isEditMode) return
        val id = txtId.text.toString().trim()
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            viewModel.delete(id)
            showMessage(getString(R.string.TextDeleteSuccess))
            finish()
        }
    }

    private fun showMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.mnu_delete)?.isVisible = isEditMode
        return super.onPrepareOptionsMenu(menu)
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