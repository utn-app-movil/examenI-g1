package cr.ac.utn.movil.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.movil.R
import cr.ac.utn.movil.controller.SinpeController
import cr.ac.utn.movil.databinding.ActivitySinpeBinding
import cr.ac.utn.movil.entity.SinpeEntity
import java.text.SimpleDateFormat
import java.util.*

class SinpeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySinpeBinding
    private var editing: SinpeEntity? = null
    private val fmt = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinpeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.sin_title)

        val id = intent.getStringExtra("SINPE_ID")
        if (id != null) {
            editing = SinpeController.getById(id)
            editing?.let { fill(it) }
        } else {
            binding.sinEtDatetime.setText(fmt.format(Date()))
        }

        binding.sinEtDatetime.setOnClickListener { showDatePicker() }
        binding.sinBtnSave.setOnClickListener { save() }
        binding.sinBtnCancel.setOnClickListener { finish() }
    }

    private fun fill(e: SinpeEntity) = with(binding) {
        sinEtOriginName.setText(e.sinOriginName)
        sinEtOriginPhone.setText(e.sinOriginPhone)
        sinEtDestName.setText(e.sinDestName)
        sinEtDestPhone.setText(e.sinDestPhone)
        sinEtAmount.setText(e.sinAmount.toString())
        sinEtDescription.setText(e.sinDescription)
        sinEtDatetime.setText(fmt.format(e.sinDateTime))
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        DatePickerDialog(this, { _, y, m, d ->
            c.set(y, m, d)
            binding.sinEtDatetime.setText(fmt.format(c.time))
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).apply {
            datePicker.maxDate = System.currentTimeMillis()
            show()
        }
    }

    private fun save() = with(binding) {
        val oName = sinEtOriginName.text.toString().trim()
        val oPhone = sinEtOriginPhone.text.toString().trim()
        val dName = sinEtDestName.text.toString().trim()
        val dPhone = sinEtDestPhone.text.toString().trim()
        val amountStr = sinEtAmount.text.toString().trim()
        val desc = sinEtDescription.text.toString().trim()
        val dateStr = sinEtDatetime.text.toString().trim()



        if (oPhone.length != 8 || dPhone.length != 8 || oPhone == dPhone ||
            !oPhone.all { it.isDigit() } || !dPhone.all { it.isDigit() }) {
            Toast.makeText(this@SinpeActivity, "Los teléfonos deben tener 8 dígitos y ser diferentes", Toast.LENGTH_LONG).show()
            return
        }

        val amount = amountStr.toDoubleOrNull() ?: run {
            Toast.makeText(this@SinpeActivity, "Monto inválido", Toast.LENGTH_LONG).show()
            return
        }

        val date = try {
            fmt.parse(dateStr)!!
        } catch (e: Exception) {
            Toast.makeText(this@SinpeActivity, "Fecha inválida", Toast.LENGTH_LONG).show()
            return
        }

        if (date.after(Date())) {
            Toast.makeText(this@SinpeActivity, "No se permiten fechas futuras", Toast.LENGTH_LONG).show()
            return
        }

        val entity = editing?.apply {
            sinOriginName = oName
            sinOriginPhone = oPhone
            sinDestName = dName
            sinDestPhone = dPhone
            sinAmount = amount
            sinDescription = desc
            sinDateTime = date
            FullName = "$oName → $dName"
            FullDescription = "De: $oPhone → Para: $dPhone\n₡${amount}\n$desc"
        } ?: SinpeEntity(
            FullName = "$oName → $dName",
            FullDescription = "De: $oPhone → Para: $dPhone\n₡${amount}\n$desc"
        ).apply {
            sinOriginName = oName
            sinOriginPhone = oPhone
            sinDestName = dName
            sinDestPhone = dPhone
            sinAmount = amount
            sinDescription = desc
            sinDateTime = date
        }

        val success = if (editing == null) SinpeController.add(entity)
        else SinpeController.update(entity)

        Toast.makeText(
            this@SinpeActivity,
            if (success) "SINPE guardado correctamente" else "Ya existe un SINPE con estos datos",
            Toast.LENGTH_LONG
        ).show()

        if (success) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}