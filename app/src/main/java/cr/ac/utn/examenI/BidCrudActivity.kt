
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
import cr.ac.utn.examenI.domain.model.Bid
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.BidViewModel
import java.time.LocalDateTime

class BidCrudActivity : AppCompatActivity() {

    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var spnArticleCode: Spinner
    private lateinit var txtDescription: EditText
    private lateinit var txtBidAmount: EditText
    private lateinit var chkAwarded: CheckBox
    private lateinit var txtDateTime: EditText

    private var currentId: String? = null

    private val articleCodes = arrayOf(
        "ART-001",
        "ART-002",
        "ART-003",
        "ART-004"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bid_crud)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { view, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        txtId = findViewById(R.id.txtId)
        txtName = findViewById(R.id.txtName)
        txtFirstName = findViewById(R.id.txtFirstName)
        txtLastName = findViewById(R.id.txtLastName)
        txtPhone = findViewById(R.id.txtPhone)
        txtEmail = findViewById(R.id.txtEmail)
        txtAddress = findViewById(R.id.txtAddress)
        spnArticleCode = findViewById(R.id.spnArticleCode)
        txtDescription = findViewById(R.id.txtDescription)
        txtBidAmount = findViewById(R.id.txtBidAmount)
        chkAwarded = findViewById(R.id.chkAwarded)
        txtDateTime = findViewById(R.id.txtDateTime)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            articleCodes
        )
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spnArticleCode.adapter = adapter

        currentId = intent.getStringExtra(EXTRA_ID)

        if (currentId.isNullOrEmpty()) {
            txtDateTime.setText(
                util.formatDateTime(LocalDateTime.now())
            )
        } else {
            loadExistingData(currentId!!)
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            doSave()
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            doDelete()
        }

        findViewById<Button>(R.id.btnCancel).setOnClickListener {
            finish()
        }
    }

    private fun loadExistingData(id: String) {
        val bid = BidViewModel.getById(id)

        if (bid == null) {
            showMessage(R.string.TextDataNoFound)
            finish()
            return
        }

        txtId.setText(bid.personId)
        txtName.setText(bid.name)
        txtFirstName.setText(bid.firstName)
        txtLastName.setText(bid.lastName)
        txtPhone.setText(bid.phone.toString())
        txtEmail.setText(bid.email)
        txtAddress.setText(bid.address)

        val position = articleCodes.indexOf(bid.articleCode)
        if (position >= 0) {
            spnArticleCode.setSelection(position)
        }

        txtDescription.setText(bid.description)
        txtBidAmount.setText(bid.bidAmount.toString())
        chkAwarded.isChecked = bid.awarded
        txtDateTime.setText(
            util.formatDateTime(bid.bidDateTime)
        )
    }

    private fun doSave() {
        val personId = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val firstName = txtFirstName.text.toString().trim()
        val lastName = txtLastName.text.toString().trim()
        val phone = txtPhone.text.toString().toIntOrNull()
        val email = txtEmail.text.toString().trim()
        val address = txtAddress.text.toString().trim()
        val articleCode = spnArticleCode.selectedItem?.toString() ?: ""
        val description = txtDescription.text.toString().trim()
        val bidAmount = txtBidAmount.text.toString().toDoubleOrNull()
        val dateTimeText = txtDateTime.text.toString().trim()

        // Validate required fields
        if (personId.isEmpty() ||
            name.isEmpty() ||
            firstName.isEmpty() ||
            lastName.isEmpty() ||
            phone == null ||
            phone <= 0 ||
            email.isEmpty() ||
            address.isEmpty() ||
            description.isEmpty() ||
            bidAmount == null ||
            !bidAmount.isFinite() ||
            bidAmount <= 0.0
        ) {
            showMessage(R.string.TextSaveError)
            return
        }

        val bidDateTime = try {
            util.parseStringToDateTimeModern(dateTimeText)
        } catch (e: Exception) {
            null
        }

        if (bidDateTime == null ||
            bidDateTime.isAfter(LocalDateTime.now())
        ) {
            showMessage(R.string.TextSaveError)
            return
        }

        val bidId = currentId ?: BidViewModel.generateId()

        val bid = Bid(
            id = bidId,
            personId = personId,
            name = name,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            email = email,
            address = address,
            articleCode = articleCode,
            description = description,
            bidAmount = bidAmount,
            awarded = chkAwarded.isChecked,
            bidDateTime = bidDateTime
        )

        val success = if (currentId == null) {
            BidViewModel.save(bid)
        } else {
            BidViewModel.update(bid)
        }

        if (success) {
            showMessage(R.string.TextSaveSuccess)
            finish()
        } else {
            showMessage(R.string.TextSaveError)
        }
    }

    private fun doDelete() {
        val id = currentId

        if (id.isNullOrEmpty()) {
            showMessage(R.string.TextDataNoFound)
            return
        }

        util.showDialogCondition(
            this,
            getString(R.string.TextDeleteQuestion)
        ) {
            val success = BidViewModel.delete(id)

            if (success) {
                showMessage(R.string.TextDeleteSuccess)
                finish()
            } else {
                showMessage(R.string.TextDeleteError)
            }
        }
    }

    private fun showMessage(messageId: Int) {
        Toast.makeText(
            this,
            getString(messageId),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                doSave()
                true
            }

            R.id.mnu_delete -> {
                doDelete()
                true
            }

            R.id.mnu_cancel -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
