package cr.ac.utn.examenI

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.examenI.domain.model.ProdProductionOrder
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.ProdViewModel
import java.time.LocalDateTime

class ProdCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtOrderNum: EditText
    private lateinit var chkProdA: CheckBox
    private lateinit var chkProdB: CheckBox
    private lateinit var chkProdC: CheckBox
    private lateinit var txtQuantity: EditText
    private lateinit var spnTargetCompany: Spinner
    private lateinit var txtStartDate: EditText
    private lateinit var txtDeliveryDate: EditText

    private var currentId: String? = null
    private val companies = arrayOf("Intel", "Amazon", "Walmart", "Dos Pinos")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_prod_crud)
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
        txtOrderNum = findViewById(R.id.txtOrderNum)
        chkProdA = findViewById(R.id.chkProdA)
        chkProdB = findViewById(R.id.chkProdB)
        chkProdC = findViewById(R.id.chkProdC)
        txtQuantity = findViewById(R.id.txtQuantity)
        spnTargetCompany = findViewById(R.id.spnTargetCompany)
        txtStartDate = findViewById(R.id.txtStartDate)
        txtDeliveryDate = findViewById(R.id.txtDeliveryDate)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, companies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnTargetCompany.adapter = adapter

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtStartDate.setText(util.formatDateTime(LocalDateTime.now()))
            txtDeliveryDate.setText(util.formatDateTime(LocalDateTime.now().plusDays(7)))
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener { doSave() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { doDelete() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        val item = ProdViewModel.getById(id) ?: return
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        txtOrderNum.setText(item.orderNum)
        chkProdA.isChecked = item.products.contains("Producto A")
        chkProdB.isChecked = item.products.contains("Producto B")
        chkProdC.isChecked = item.products.contains("Producto C")
        txtQuantity.setText(item.quantity.toString())
        val posComp = companies.indexOf(item.targetCompany)
        if (posComp >= 0) spnTargetCompany.setSelection(posComp)
        txtStartDate.setText(util.formatDateTime(item.startDate))
        txtDeliveryDate.setText(util.formatDateTime(item.deliveryDateTime))
    }

    private fun doSave() {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val orderNum = txtOrderNum.text.toString().trim()
        val qty = txtQuantity.text.toString().toIntOrNull() ?: 0

        if (id.isEmpty() || name.isEmpty() || orderNum.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos requeridos.", Toast.LENGTH_SHORT).show()
            return
        }

        if (qty <= 0) {
            Toast.makeText(this, "La cantidad debe ser mayor a 0.", Toast.LENGTH_SHORT).show()
            return
        }

        val startDt = util.parseStringToDateTimeModern(txtStartDate.text.toString().trim()) ?: LocalDateTime.now()
        val deliveryDt = util.parseStringToDateTimeModern(txtDeliveryDate.text.toString().trim()) ?: startDt.plusDays(7)

        if (!deliveryDt.isAfter(startDt)) {
            Toast.makeText(this, "La fecha de entrega debe ser posterior a la fecha de inicio.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedProds = mutableListOf<String>()
        if (chkProdA.isChecked) selectedProds.add("Producto A")
        if (chkProdB.isChecked) selectedProds.add("Producto B")
        if (chkProdC.isChecked) selectedProds.add("Producto C")

        val item = ProdProductionOrder(
            id = id,
            name = name,
            firstName = txtFirstName.text.toString().trim(),
            lastName = txtLastName.text.toString().trim(),
            phone = txtPhone.text.toString().toIntOrNull() ?: 0,
            email = txtEmail.text.toString().trim(),
            address = txtAddress.text.toString().trim(),
            orderNum = orderNum,
            products = selectedProds.joinToString(", "),
            quantity = qty,
            targetCompany = spnTargetCompany.selectedItem?.toString() ?: "",
            startDate = startDt,
            deliveryDateTime = deliveryDt
        )

        if (currentId.isNullOrEmpty()) {
            if (ProdViewModel.getById(id) != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            ProdViewModel.save(item)
        } else {
            ProdViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) return
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            ProdViewModel.delete(id)
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
