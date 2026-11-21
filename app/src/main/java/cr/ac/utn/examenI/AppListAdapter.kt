package cr.ac.utn.examenI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.examenI.domain.model.AppAppointment
import cr.ac.utn.examenI.util.util

class AppListAdapter(
    private val appointments: List<AppAppointment>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<AppListAdapter.AppViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val appointment = appointments[position]
        val specialty = appointment.specialty.name.replace('_', ' ')
        val date = util.formatDateTime(appointment.dateTime)
        holder.name.text = appointment.fullName
        holder.details.text = "${appointment.id} · ${appointment.doctorName} · $specialty · $date"
        holder.itemView.setOnClickListener { itemClickListener.onItemClick(appointment) }
    }

    override fun getItemCount(): Int = appointments.size

    class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(android.R.id.text1)
        val details: TextView = view.findViewById(android.R.id.text2)
    }
}
