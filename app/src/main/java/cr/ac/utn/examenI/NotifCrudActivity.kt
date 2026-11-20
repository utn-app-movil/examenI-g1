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
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import java.time.LocalDateTime

class NotifCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtEmailTitle: EditText
    private lateinit var txtMessage: EditText
    private lateinit var txtCC: EditText
    private lateinit var txtCCO: EditText
    private lateinit var chkStudents: CheckBox
    private lateinit var chkTeachers: CheckBox
    private lateinit var chkStaff: CheckBox
    private lateinit var txtSendDateTime: EditText

    private var currentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notif_crud)
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
        txtEmailTitle = findViewById(R.id.txtEmailTitle)
        txtMessage = findViewById(R.id.txtMessage)
        txtCC = findViewById(R.id.txtCC)
        txtCCO = findViewById(R.id.txtCCO)
        chkStudents = findViewById(R.id.chkStudents)
        chkTeachers = findViewById(R.id.chkTeachers)
        chkStaff = findViewById(R.id.chkStaff)
        txtSendDateTime = findViewById(R.id.txtSendDateTime)

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtSendDateTime.setText(util.formatDateTime(LocalDateTime.now().plusDays(1)))
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        /*val item = NotifViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        txtEmailTitle.setText(item.emailTitle)
        txtMessage.setText(item.message)
        txtCC.setText(item.cc)
        txtCCO.setText(item.cco)
        chkStudents.isChecked = item.targetRecipients.contains("Estudiantes")
        chkTeachers.isChecked = item.targetRecipients.contains("Profesorado")
        chkStaff.isChecked = item.targetRecipients.contains("Administrativos")
        txtSendDateTime.setText(util.formatDateTime(item.sendDateTime))*/
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val title = txtEmailTitle.text.toString().trim()
        val msg = txtMessage.text.toString().trim()
        val dateTimeStr = txtSendDateTime.text.toString().trim()

        if (id.isEmpty() || title.isEmpty() || msg.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos requeridos.", Toast.LENGTH_SHORT).show()
            return
        }

        val dt = util.parseStringToDateTimeModern(dateTimeStr)
        if (dt == null || !dt.isAfter(LocalDateTime.now())) {
            Toast.makeText(this, "La fecha de envío debe ser posterior a la actual.", Toast.LENGTH_SHORT).show()
            return
        }

        val targets = mutableListOf<String>()
        if (chkStudents.isChecked) targets.add("Estudiantes")
        if (chkTeachers.isChecked) targets.add("Profesorado")
        if (chkStaff.isChecked) targets.add("Administrativos")



        if (currentId.isNullOrEmpty()) {
            if (null != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            //NotifViewModel.save(item)
        } else {
            //NotifViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            //NotifViewModel.delete(id)
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
