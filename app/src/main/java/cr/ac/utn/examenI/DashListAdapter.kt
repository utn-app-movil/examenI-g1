package cr.ac.utn.examenI

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.examenI.domain.model.DashContribution

class DashListAdapter(
    private val items: List<DashContribution>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_list, parent, false)
        return CustomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        // here the position is used directly
        val item = items[position]
        holder.bind(item, itemClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}