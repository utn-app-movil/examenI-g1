package cr.ac.utn.movil

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.movil.controller.ProdProductionOrderController
import cr.ac.utn.movil.data.identities.ProdProductionOrder
import java.util.Calendar

class ProdProductionOrderActivity : AppCompatActivity() {

    private lateinit var orderNumber: EditText
    private lateinit var quantity: EditText
    private lateinit var startDate: EditText
    private lateinit var endDate: EditText
    private lateinit var companySpinner: Spinner
    private lateinit var selectProductsButton: Button
    private lateinit var selectedProductsText: TextView

    private lateinit var loadButton: Button
    private lateinit var saveButton: Button
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var clearButton: Button

    private val selectedProducts = mutableListOf<String>()
    private val controller = ProdProductionOrderController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prod)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bindViews()
        setupDatePickers()
        setupCompanySpinner()
        setupProductSelection()
        setupButtons()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun bindViews() {
        orderNumber = findViewById(R.id.prod_et_order_number)
        quantity = findViewById(R.id.prod_et_quantity)
        startDate = findViewById(R.id.prod_et_start_date)
        endDate = findViewById(R.id.prod_et_end_date)
        companySpinner = findViewById(R.id.prod_sp_company)
        selectProductsButton = findViewById(R.id.prod_btn_select_products)
        selectedProductsText = findViewById(R.id.prod_tv_selected_products)

        loadButton = findViewById(R.id.prod_btn_load)
        saveButton = findViewById(R.id.prod_btn_save)
        updateButton = findViewById(R.id.prod_btn_update)
        deleteButton = findViewById(R.id.prod_btn_delete)
        clearButton = findViewById(R.id.prod_btn_clear)
    }

    private fun setupButtons() {
        loadButton.setOnClickListener { loadOrder() }

        saveButton.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Do you want to save the entered information?")
                .setPositiveButton("Yes") { _, _ ->
                    saveOrder()
                }
                .setNegativeButton("No", null)
                .show()
        }

        updateButton.setOnClickListener { updateOrder() }

        deleteButton.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Do you want to delete this order?")
                .setPositiveButton("Yes") { _, _ ->
                    deleteOrder()
                }
                .setNegativeButton("No", null)
                .show()
        }

        clearButton.setOnClickListener { clearForm() }
    }

    private fun loadOrder() {
        val num = orderNumber.text.toString()

        if (num.isBlank()) {
            orderNumber.error = "Enter order number"
            return
        }

        val order = controller.get(num)

        if (order == null) {
            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show()
            return
        }

        quantity.setText(order.quantity.toString())
        startDate.setText(order.startDate)
        endDate.setText(order.endDate)
        companySpinner.setSelection(
            (companySpinner.adapter as ArrayAdapter<String>).getPosition(order.company)
        )

        selectedProducts.clear()
        selectedProducts.addAll(order.products)
        selectedProductsText.text = order.products.joinToString(", ")

        Toast.makeText(this, "Order loaded", Toast.LENGTH_SHORT).show()
    }

    private fun saveOrder() {
        if (!validateInputs()) return

        val order = buildOrder()

        if (controller.create(order))
            Toast.makeText(this, "Order saved", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "Order number exists", Toast.LENGTH_SHORT).show()
    }

    private fun updateOrder() {
        if (!validateInputs()) return

        val order = buildOrder()

        if (controller.update(order))
            Toast.makeText(this, "Order updated", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show()
    }

    private fun deleteOrder() {
        val num = orderNumber.text.toString()

        if (num.isBlank()) {
            orderNumber.error = "Enter order number"
            return
        }

        if (controller.delete(num)) {
            Toast.makeText(this, "Order deleted", Toast.LENGTH_SHORT).show()
            clearForm()
        } else {
            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearForm() {
        orderNumber.setText("")
        quantity.setText("")
        startDate.setText("")
        endDate.setText("")
        companySpinner.setSelection(0)
        selectedProducts.clear()
        selectedProductsText.text = ""
    }

    private fun validateInputs(): Boolean {
        var ok = true

        if (orderNumber.text.isNullOrBlank()) {
            ok = false
            orderNumber.error = "Required"
        }

        if (quantity.text.isNullOrBlank()) {
            ok = false
            quantity.error = "Required"
        } else if (quantity.text.toString().toInt() <= 0) {
            ok = false
            quantity.error = "Quantity > 0"
        }

        if (startDate.text.isNullOrBlank()) {
            ok = false
            startDate.error = "Required"
        }

        if (endDate.text.isNullOrBlank()) {
            ok = false
            endDate.error = "Required"
        } else if (!isEndAfterStart(startDate.text.toString(), endDate.text.toString())) {
            ok = false
            endDate.error = "End > Start"
        }

        if (selectedProducts.isEmpty()) {
            ok = false
            Toast.makeText(this, "Select products", Toast.LENGTH_SHORT).show()
        }

        return ok
    }

    private fun isEndAfterStart(start: String, end: String): Boolean {
        fun parse(d: String): Calendar {
            val p = d.split("/")
            return Calendar.getInstance().apply {
                set(p[2].toInt(), p[1].toInt() - 1, p[0].toInt())
            }
        }
        return parse(end).after(parse(start))
    }

    private fun buildOrder(): ProdProductionOrder {
        return ProdProductionOrder(
            id = 0,
            orderNumber = orderNumber.text.toString(),
            products = selectedProducts.toList(),
            quantity = quantity.text.toString().toInt(),
            startDate = startDate.text.toString(),
            endDate = endDate.text.toString(),
            company = companySpinner.selectedItem.toString(),
            FullDescription = "Production order for $quantity units",
            FullName = "Order #${orderNumber.text}"
        )
    }

    private fun setupDatePickers() {
        startDate.setOnClickListener { showDatePicker { startDate.setText(it) } }
        endDate.setOnClickListener { showDatePicker { endDate.setText(it) } }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val dp = DatePickerDialog(this)
        dp.setOnDateSetListener { _, y, m, d -> onDateSelected("$d/${m + 1}/$y") }
        dp.show()
    }

    private fun setupCompanySpinner() {
        val companies = resources.getStringArray(R.array.prod_companies_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, companies)
        companySpinner.adapter = adapter
    }

    private fun setupProductSelection() {
        selectProductsButton.setOnClickListener {
            val products = resources.getStringArray(R.array.prod_products_array)
            val checkedItems = BooleanArray(products.size)

            android.app.AlertDialog.Builder(this)
                .setTitle("Select products")
                .setMultiChoiceItems(products, checkedItems) { _, index, isChecked ->
                    if (isChecked) selectedProducts.add(products[index])
                    else selectedProducts.remove(products[index])
                }
                .setPositiveButton("OK") { _, _ ->
                    selectedProductsText.text = selectedProducts.joinToString(", ")
                }
                .show()
        }
    }
}
