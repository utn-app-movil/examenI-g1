package cr.ac.utn.movil.controller

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import cr.ac.utn.movil.data.entities.med_NursingControl
import cr.ac.utn.movil.databinding.ActivityMedNursingControlBinding

class med_NursingControlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedNursingControlBinding
    private val controller = med_NursingControlController()
    private lateinit var adapter: med_NursingControlAdapter
    private var itemEditando: med_NursingControl? = null  // Para saber si estamos editando

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedNursingControlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Nursing Control"

        // Adapter con funciones de editar y eliminar
        adapter = med_NursingControlAdapter(
            onItemClick = { item -> editar(item) },
            onItemLongClick = { item -> eliminar(item); true }
        )

        binding.medRvRecords.layoutManager = LinearLayoutManager(this)
        binding.medRvRecords.adapter = adapter

        binding.medBtnSave.setOnClickListener { guardar() }
        cargar()
    }

    private fun editar(item: med_NursingControl) {
        itemEditando = item
        binding.medEtPatientName.setText(item.patientName)
        binding.medEtBloodPressure.setText(item.bloodPressure)
        binding.medEtWeight.setText(item.weightKg.toString())
        binding.medEtHeight.setText(item.heightCm.toString())
        binding.medEtOxygen.setText(item.oxygenSaturation.toString())
        binding.medBtnSave.text = "Actualizar"
    }

    private fun eliminar(item: med_NursingControl) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Eliminar registro de ${item.patientName}?")
            .setPositiveButton("Sí") { _, _ ->
                controller.delete(item.ID)
                cargar()
                Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun guardar() {
        val nombre = binding.medEtPatientName.text.toString().trim()
        val pa = binding.medEtBloodPressure.text.toString().trim()
        val peso = binding.medEtWeight.text.toString().toDoubleOrNull()
        val altura = binding.medEtHeight.text.toString().toDoubleOrNull()
        val o2 = binding.medEtOxygen.text.toString().toIntOrNull()

        if (nombre.isEmpty() || pa.isEmpty() || peso == null || altura == null || o2 == null) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show()
            return
        }

        if (!validarPresion(pa)) {
            Toast.makeText(this, "Formato inválido. Usa: 120/80", Toast.LENGTH_LONG).show()
            return
        }

        if (itemEditando != null) {
            // ACTUALIZAR
            itemEditando!!.apply {
                patientName = nombre
                bloodPressure = pa
                weightKg = peso
                heightCm = altura
                oxygenSaturation = o2
                dateTime = System.currentTimeMillis()
            }
            controller.update(itemEditando!!)
            Toast.makeText(this, "Registro actualizado", Toast.LENGTH_SHORT).show()
        } else {
            // NUEVO
            val nuevo = med_NursingControl().apply {
                patientName = nombre
                bloodPressure = pa
                weightKg = peso
                heightCm = altura
                oxygenSaturation = o2
            }
            controller.add(nuevo)
            Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show()
        }

        itemEditando = null
        binding.medBtnSave.text = "Guardar"
        limpiar()
        cargar()
    }

    private fun validarPresion(pa: String): Boolean {
        val regex = Regex("""^\d{2,3}/\d{2,3}$""")
        if (!pa.matches(regex)) return false
        return try {
            val (s, d) = pa.split("/").map { it.toInt() }
            s in 70..250 && d in 40..150
        } catch (e: Exception) { false }
    }

    private fun limpiar() {
        binding.medEtPatientName.text?.clear()
        binding.medEtBloodPressure.text?.clear()
        binding.medEtWeight.text?.clear()
        binding.medEtHeight.text?.clear()
        binding.medEtOxygen.text?.clear()
    }

    private fun cargar() {
        adapter.submitList(controller.getAll())
    }
}