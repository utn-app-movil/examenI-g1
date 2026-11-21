package cr.ac.utn.examenI

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import cr.ac.utn.examenI.domain.model.Person
import cr.ac.utn.examenI.domain.model.inv_InventoryEntry
import cr.ac.utn.examenI.domain.model.inv_Supplier
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.inv_InventoryViewModel
import cr.ac.utn.examenI.viewmodel.inv_OperationResult
import cr.ac.utn.examenI.viewmodel.inv_SupplierViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle
import java.util.Locale
import java.util.UUID

class InvCrudActivity : AppCompatActivity() {

    companion object {
        private const val inv_supplierStateKey =
            "inv_selected_supplier_id"
    }

    private lateinit var inv_responsibleId: EditText
    private lateinit var inv_responsibleName: EditText
    private lateinit var inv_responsibleEmail: EditText
    private lateinit var inv_productCode: EditText
    private lateinit var inv_productName: EditText
    private lateinit var inv_quantity: EditText
    private lateinit var inv_entryDateTime: EditText
    private lateinit var inv_supplier: Spinner
    private lateinit var inv_unitCost: EditText

    private lateinit var inv_viewModel: inv_InventoryViewModel
    private lateinit var inv_supplierViewModel: inv_SupplierViewModel

    private var inv_currentId: String? = null

    private val inv_dateTimeFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern(
            "uuuu-MM-dd HH:mm",
            Locale.ROOT
        ).withResolverStyle(ResolverStyle.STRICT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inv_crud)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.inv_crud_root)
        ) { inv_view, inv_insets ->
            val inv_systemBars = inv_insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            inv_view.setPadding(
                inv_systemBars.left,
                inv_systemBars.top,
                inv_systemBars.right,
                inv_systemBars.bottom
            )

            inv_insets
        }

        inv_viewModel = ViewModelProvider(this)[
            inv_InventoryViewModel::class.java
        ]

        inv_supplierViewModel = ViewModelProvider(this)[
            inv_SupplierViewModel::class.java
        ]

        inv_responsibleId = findViewById(R.id.inv_responsible_id)
        inv_responsibleName = findViewById(R.id.inv_responsible_name)
        inv_responsibleEmail = findViewById(R.id.inv_responsible_email)
        inv_productCode = findViewById(R.id.inv_product_code)
        inv_productName = findViewById(R.id.inv_product_name)
        inv_quantity = findViewById(R.id.inv_quantity)
        inv_entryDateTime = findViewById(R.id.inv_entry_date_time)
        inv_supplier = findViewById(R.id.inv_supplier)
        inv_unitCost = findViewById(R.id.inv_unit_cost)

        inv_currentId = intent.getStringExtra(EXTRA_ID)

        inv_loadSuppliers()

        if (inv_currentId != null) {
            if (!inv_loadEntry()) {
                return
            }
        } else {
            inv_entryDateTime.setText(
                LocalDateTime.now().format(inv_dateTimeFormatter)
            )
        }

        findViewById<Button>(R.id.inv_delete_button).visibility =
            if (inv_currentId == null) View.GONE else View.VISIBLE

        inv_entryDateTime.setOnClickListener {
            inv_selectDateTime()
        }

        findViewById<Button>(R.id.inv_add_supplier).setOnClickListener {
            inv_showSupplierDialog()
        }

        findViewById<Button>(R.id.inv_save_button).setOnClickListener {
            inv_save()
        }

        findViewById<Button>(R.id.inv_delete_button).setOnClickListener {
            inv_delete()
        }

        findViewById<Button>(R.id.inv_cancel_button).setOnClickListener {
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val inv_selectedSupplier =
            inv_supplier.selectedItem as? inv_Supplier

        outState.putString(
            inv_supplierStateKey,
            inv_selectedSupplier?.id
        )

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val inv_supplierId = savedInstanceState.getString(
            inv_supplierStateKey
        )

        inv_loadSuppliers(inv_supplierId)
    }

    private fun inv_loadSuppliers(
        inv_selectedId: String? = null
    ) {
        val inv_suppliers = inv_supplierViewModel.inv_getAll()

        val inv_adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            inv_suppliers
        )

        inv_adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        inv_supplier.adapter = inv_adapter

        val inv_position = inv_suppliers.indexOfFirst {
                inv_item -> inv_item.id == inv_selectedId
        }

        if (inv_position >= 0) {
            inv_supplier.setSelection(inv_position)
        }
    }

    private fun inv_loadEntry(): Boolean {
        val inv_id = inv_currentId ?: return false
        val inv_entry = inv_viewModel.inv_getById(inv_id)

        if (inv_entry == null) {
            inv_showMessage(R.string.TextDataNoFound)
            finish()
            return false
        }

        inv_responsibleId.setText(inv_entry.inv_responsible.id)
        inv_responsibleName.setText(inv_entry.inv_responsible.name)
        inv_responsibleEmail.setText(inv_entry.inv_responsible.email)
        inv_productCode.setText(inv_entry.inv_productCode)
        inv_productName.setText(inv_entry.inv_productName)
        inv_quantity.setText(inv_entry.inv_quantity.toString())
        inv_unitCost.setText(inv_entry.inv_unitCost.toPlainString())

        inv_entryDateTime.setText(
            inv_entry.inv_entryDateTime.format(inv_dateTimeFormatter)
        )

        val inv_selectedSupplier =
            inv_supplierViewModel.inv_getAll().firstOrNull {
                    inv_item ->
                inv_item.inv_name == inv_entry.inv_supplier
            }

        inv_loadSuppliers(inv_selectedSupplier?.id)

        return true
    }

    private fun inv_showSupplierDialog() {
        val inv_input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            setSingleLine(true)
        }

        val inv_dialog = AlertDialog.Builder(this)
            .setTitle(R.string.text_add_new)
            .setView(inv_input)
            .setPositiveButton(R.string.TextSave, null)
            .setNegativeButton(R.string.TextCancel, null)
            .create()

        inv_dialog.setOnShowListener {
            inv_dialog.getButton(
                AlertDialog.BUTTON_POSITIVE
            ).setOnClickListener {
                val inv_name = inv_input.text.toString().trim()

                when (
                    inv_supplierViewModel.inv_create(inv_name)
                ) {
                    inv_OperationResult.inv_success -> {
                        val inv_createdSupplier =
                            inv_supplierViewModel.inv_getAll()
                                .firstOrNull {
                                        inv_item ->
                                    inv_item.inv_name.equals(
                                        inv_name,
                                        ignoreCase = true
                                    )
                                }

                        inv_loadSuppliers(inv_createdSupplier?.id)
                        inv_dialog.dismiss()
                    }

                    inv_OperationResult.inv_duplicate -> {
                        inv_input.error = getString(
                            R.string.TextDuplicateData
                        )
                    }

                    else -> {
                        inv_input.error = getString(
                            R.string.TextSaveError
                        )
                    }
                }
            }
        }

        inv_dialog.show()
    }

    private fun inv_selectDateTime() {
        val inv_initial = inv_parseDateTime()
            ?: LocalDateTime.now()

        DatePickerDialog(
            this,
            { _, inv_year, inv_month, inv_day ->
                TimePickerDialog(
                    this,
                    { _, inv_hour, inv_minute ->
                        val inv_selected = LocalDateTime.of(
                            inv_year,
                            inv_month + 1,
                            inv_day,
                            inv_hour,
                            inv_minute
                        )

                        inv_entryDateTime.setText(
                            inv_selected.format(inv_dateTimeFormatter)
                        )
                    },
                    inv_initial.hour,
                    inv_initial.minute,
                    true
                ).show()
            },
            inv_initial.year,
            inv_initial.monthValue - 1,
            inv_initial.dayOfMonth
        ).show()
    }

    private fun inv_parseDateTime(): LocalDateTime? {
        return try {
            LocalDateTime.parse(
                inv_entryDateTime.text.toString().trim(),
                inv_dateTimeFormatter
            )
        } catch (_: DateTimeParseException) {
            null
        }
    }

    private fun inv_save() {
        val inv_quantityValue =
            inv_quantity.text.toString().trim().toIntOrNull()

        val inv_costValue =
            inv_unitCost.text.toString().trim().toBigDecimalOrNull()

        val inv_dateTimeValue = inv_parseDateTime()

        val inv_selectedSupplier =
            inv_supplier.selectedItem as? inv_Supplier

        if (
            inv_quantityValue == null ||
            inv_costValue == null ||
            inv_dateTimeValue == null ||
            inv_selectedSupplier == null
        ) {
            inv_showMessage(R.string.TextSaveError)
            return
        }

        val inv_storedSupplier = inv_supplierViewModel.inv_getById(
            inv_selectedSupplier.id
        )

        if (inv_storedSupplier == null) {
            inv_showMessage(R.string.TextDataNoFound)
            return
        }

        val inv_person = Person(
            id = inv_responsibleId.text.toString().trim(),
            name = inv_responsibleName.text.toString().trim(),
            email = inv_responsibleEmail.text.toString().trim()
        )

        val inv_entry = inv_InventoryEntry(
            id = inv_currentId ?: UUID.randomUUID().toString(),
            inv_responsible = inv_person,
            inv_productCode = inv_productCode.text.toString().trim(),
            inv_productName = inv_productName.text.toString().trim(),
            inv_quantity = inv_quantityValue,
            inv_entryDateTime = inv_dateTimeValue,
            inv_supplier = inv_storedSupplier.inv_name,
            inv_unitCost = inv_costValue
        )

        util.showDialogCondition(
            this,
            getString(R.string.TextSaveQuestion)
        ) {
            val inv_result =
                if (inv_currentId == null) {
                    inv_viewModel.inv_create(inv_entry)
                } else {
                    inv_viewModel.inv_update(inv_entry)
                }

            when (inv_result) {
                inv_OperationResult.inv_success -> {
                    inv_showMessage(R.string.TextSaveSuccess)
                    finish()
                }

                inv_OperationResult.inv_duplicate -> {
                    inv_showMessage(R.string.TextDuplicateData)
                }

                inv_OperationResult.inv_notFound -> {
                    inv_showMessage(R.string.TextDataNoFound)
                }

                inv_OperationResult.inv_invalidData -> {
                    inv_showMessage(R.string.TextSaveError)
                }
            }
        }
    }

    private fun inv_delete() {
        val inv_id = inv_currentId ?: return

        util.showDialogCondition(
            this,
            getString(R.string.TextDeleteQuestion)
        ) {
            when (inv_viewModel.inv_delete(inv_id)) {
                inv_OperationResult.inv_success -> {
                    inv_showMessage(R.string.TextDeleteSuccess)
                    finish()
                }

                else -> {
                    inv_showMessage(R.string.TextDataNoFound)
                }
            }
        }
    }

    private fun inv_showMessage(inv_stringId: Int) {
        Toast.makeText(
            this,
            getString(inv_stringId),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.mnu_delete)?.isVisible =
            inv_currentId != null

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                inv_save()
                true
            }

            R.id.mnu_delete -> {
                inv_delete()
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