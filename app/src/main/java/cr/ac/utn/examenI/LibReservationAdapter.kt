package cr.ac.utn.examenI

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.examenI.domain.model.LibReservation
import cr.ac.utn.examenI.util.util

class LibReservationAdapter(
    private val items: List<LibReservation>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_list, parent, false)
        return CustomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, itemClickListener)
        holder.tvText3.text = item.reservedBooks.joinToString(", ")
        holder.tvText4.text = item.branch + " | " +
                util.formatDateTime(item.reservationDateTime) + " | " +
                util.formatDateTime(item.returnDateTime)

    }

    override fun getItemCount(): Int = items.size
}