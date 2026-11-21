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
import cr.ac.utn.examenI.domain.model.CliClient
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.CliViewModel

/**
 * Activity for Corporate Clients CRUD operations.
 * Implements validations:
 * - Unique corporate legal ID.
 * - Official Costa Rican legal ID format.
 * - At least one contracted service selected.
 */
class CliCrudActivity : AppCompatActivity() {

    private lateinit var txtCorporateId: EditText
    private lateinit var txtCompanyName: EditText
    private lateinit var txtProvince: EditText
    private lateinit var txtCanton: EditText
    private lateinit var txtDistrict: EditText

    private lateinit var chkInternet: CheckBox
    private lateinit var chkTelephony: CheckBox
    private lateinit var chkHosting: CheckBox
    private lateinit var chkSupport: CheckBox

    private var currentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cli_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }

    private fun initViews() {
        txtCorporateId = findViewById(R.id.txtCorporateId)
        txtCompanyName = findViewById(R.id.txtCompanyName)
        txtProvince = findViewById(R.id.txtProvince)
        txtCanton = findViewById(R.id.txtCanton)
        txtDistrict = findViewById(R.id.txtDistrict)

        chkInternet = findViewById(R.id.chkInternet)
        chkTelephony = findViewById(R.id.chkTelephony)
        chkHosting = findViewById(R.id.chkHosting)
        chkSupport = findViewById(R.id.chkSupport)
    }

    private fun getSelectedServices(): List<String> {
        val services = mutableListOf<String>()
        if (chkInternet.isChecked) services.add("Internet empresarial")
        if (chkTelephony.isChecked) services.add("Telefonía IP")
        if (chkHosting.isChecked) services.add("Hosting")
        if (chkSupport.isChecked) services.add("Soporte técnico")
        return services
    }

    private fun loadExistingData(id: String) {
        val item = CliViewModel.getById(id) ?: return
        txtCorporateId.setText(item.id)
        txtCorporateId.isEnabled = false
        txtCompanyName.setText(item.companyName)
        txtProvince.setText(item.province)
        txtCanton.setText(item.canton)
        txtDistrict.setText(item.district)

        chkInternet.isChecked = item.services.contains("Internet empresarial")
        chkTelephony.isChecked = item.services.contains("Telefonía IP")
        chkHosting.isChecked = item.services.contains("Hosting")
        chkSupport.isChecked = item.services.contains("Soporte técnico")
    }

    private fun doSave() {
        val corpId = txtCorporateId.text.toString().trim()
        val companyName = txtCompanyName.text.toString().trim()
        val province = txtProvince.text.toString().trim()
        val canton = txtCanton.text.toString().trim()
        val district = txtDistrict.text.toString().trim()
        val services = getSelectedServices()

        // Validation 1: Required fields
        if (corpId.isEmpty() || companyName.isEmpty()) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        // Validation 2: Official format
        if (!CliViewModel.isValidLegalId(corpId)) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        // Validation 3: At least one contracted service
        if (services.isEmpty()) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val client = CliClient(corpId, companyName, province, canton, district, services)

        if (currentId.isNullOrEmpty()) {
            // Check duplicate
            if (CliViewModel.isDuplicate(corpId)) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            CliViewModel.save(client)
        } else {
            CliViewModel.update(client)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtCorporateId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            CliViewModel.delete(id)
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
