package cr.ac.utn.examenI

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.examenI.domain.model.Sinpe
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.SinViewModel
import java.time.LocalDateTime

class SinCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtOriginPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtRecipientPhone: EditText
    private lateinit var txtRecipientName: EditText
    private lateinit var txtAmount: EditText
    private lateinit var txtDescription: EditText
    private lateinit var txtDateTime: EditText

    private var currentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sin_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtId = findViewById(R.id.txtId)
        txtName = findViewById(R.id.txtName)
        txtFirstName = findViewById(R.id.txtFirstName)
        txtLastName = findViewById(R.id.txtLastName)
        txtOriginPhone = findViewById(R.id.txtOriginPhone)
        txtEmail = findViewById(R.id.txtEmail)
        txtAddress = findViewById(R.id.txtAddress)
        txtRecipientPhone = findViewById(R.id.txtRecipientPhone)
        txtRecipientName = findViewById(R.id.txtRecipientName)
        txtAmount = findViewById(R.id.txtAmount)
        txtDescription = findViewById(R.id.txtDescription)
        txtDateTime = findViewById(R.id.txtDateTime)

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtDateTime.setText("")
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        val item = SinViewModel.getById(id) as? Sinpe ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.originName)
        txtFirstName.setText(item.originFirstName)
        txtLastName.setText(item.originLastName)
        txtOriginPhone.setText(item.originPhone)
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        txtRecipientPhone.setText(item.recipientPhone)
        txtRecipientName.setText(item.recipientName)
        txtAmount.setText(item.amount.toString())
        txtDescription.setText(item.description)
        txtDateTime.setText(util.formatDateTime(item.sinpeDateTime))
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val firstName = txtFirstName.text.toString().trim()
        val lastName = txtLastName.text.toString().trim()
        val originPhone = txtOriginPhone.text.toString().trim()
        val email = txtEmail.text.toString().trim()
        val address = txtAddress.text.toString().trim()
        val recipientPhone = txtRecipientPhone.text.toString().trim()
        val recipientName = txtRecipientName.text.toString().trim()
        val amountVal = txtAmount.text.toString().toDoubleOrNull() ?: 0.0
        val description = txtDescription.text.toString().trim()
        val dateTimeStr = txtDateTime.text.toString().trim()

        if (id.isEmpty() || name.isEmpty() || recipientPhone.isEmpty() || recipientName.isEmpty() || dateTimeStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val phoneRegex = Regex("""^\d{8}$""")
        if (!phoneRegex.matches(recipientPhone)) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        if (originPhone.isNotEmpty() && !phoneRegex.matches(originPhone)) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        if (originPhone.isNotEmpty() && originPhone == recipientPhone) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val dt = util.parseStringToDateTimeModern(dateTimeStr)
        if (dt == null || dt.isAfter(LocalDateTime.now())) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val sinpe = Sinpe(
            id = id,
            originName = name,
            originFirstName = firstName,
            originLastName = lastName,
            originPhone = originPhone,
            email = email,
            address = address,
            recipientPhone = recipientPhone,
            recipientName = recipientName,
            amount = amountVal,
            description = description,
            sinpeDateTime = dt
        )

        if (currentId.isNullOrEmpty()) {
            if (SinViewModel.getById(id) != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            SinViewModel.add(sinpe)
        } else {
            SinViewModel.update(sinpe)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            SinViewModel.delete(id)
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
