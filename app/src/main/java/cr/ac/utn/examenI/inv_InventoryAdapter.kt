package cr.ac.utn.examenI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.examenI.domain.model.inv_InventoryEntry
import java.time.format.DateTimeFormatter

class inv_InventoryAdapter(
    private val inv_entries: List<inv_InventoryEntry>,
    private val inv_onClick: (inv_InventoryEntry) -> Unit
) : RecyclerView.Adapter<inv_InventoryAdapter.inv_InventoryViewHolder>() {

    override fun onCreateViewHolder(
        inv_parent: ViewGroup,
        inv_viewType: Int
    ): inv_InventoryViewHolder {
        val inv_view = LayoutInflater.from(inv_parent.context).inflate(
            R.layout.inv_item_inventory,
            inv_parent,
            false
        )

        return inv_InventoryViewHolder(inv_view)
    }

    override fun onBindViewHolder(
        inv_holder: inv_InventoryViewHolder,
        inv_position: Int
    ) {
        inv_holder.inv_bind(inv_entries[inv_position])
    }

    override fun getItemCount(): Int {
        return inv_entries.size
    }

    inner class inv_InventoryViewHolder(
        inv_view: View
    ) : RecyclerView.ViewHolder(inv_view) {

        private val inv_productName: TextView =
            inv_view.findViewById(R.id.inv_item_product_name)

        private val inv_productCode: TextView =
            inv_view.findViewById(R.id.inv_item_product_code)

        private val inv_responsibleName: TextView =
            inv_view.findViewById(R.id.inv_item_responsible_name)

        private val inv_entryDateTime: TextView =
            inv_view.findViewById(R.id.inv_item_entry_date_time)

        private val inv_dateTimeFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        fun inv_bind(inv_entry: inv_InventoryEntry) {
            inv_productName.text = inv_entry.inv_productName
            inv_productCode.text = inv_entry.inv_productCode
            inv_responsibleName.text = inv_entry.inv_responsible.name
            inv_entryDateTime.text = inv_entry.inv_entryDateTime.format(
                inv_dateTimeFormatter
            )

            itemView.setOnClickListener {
                inv_onClick(inv_entry)
            }
        }
    }
}