package cr.ac.utn.movil.controller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.movil.data.entities.med_NursingControl
import cr.ac.utn.movil.databinding.ItemMedNursingRecordBinding
import java.text.SimpleDateFormat
import java.util.*

class med_NursingControlAdapter(
    private val onItemClick: (med_NursingControl) -> Unit,
    private val onItemLongClick: (med_NursingControl) -> Boolean
) : ListAdapter<med_NursingControl, med_NursingControlAdapter.VH>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemMedNursingRecordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position), onItemClick, onItemLongClick)
    }

    class VH(private val binding: ItemMedNursingRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        private val df = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        fun bind(
            item: med_NursingControl,
            clickListener: (med_NursingControl) -> Unit,
            longClickListener: (med_NursingControl) -> Boolean
        ) {
            binding.apply {
                medTvPaciente.text = item.patientName
                medTvPresion.text = item.bloodPressure
                medTvPeso.text = "${item.weightKg} kg"
                medTvAltura.text = "${item.heightCm} cm"
                medTvOxigeno.text = "${item.oxygenSaturation}%"
                medTvFecha.text = df.format(Date(item.dateTime))

                root.setOnClickListener { clickListener(item) }
                root.setOnLongClickListener { longClickListener(item) }
            }
        }
    }

    class Diff : DiffUtil.ItemCallback<med_NursingControl>() {
        override fun areItemsTheSame(old: med_NursingControl, new: med_NursingControl) = old.ID == new.ID
        override fun areContentsTheSame(old: med_NursingControl, new: med_NursingControl) = old == new
    }
}