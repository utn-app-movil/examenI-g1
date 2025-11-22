package cr.ac.utn.movil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.movil.identities.InventoryEntry
import kotlin.text.format

class InventoryAdapter(
    private var inventoryList: List<InventoryEntry>,
    private val onItemClick: (InventoryEntry) -> Unit
) : RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {

    class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProductName: TextView = itemView.findViewById(R.id.inv_item_tv_product_name)
        val tvProductCode: TextView = itemView.findViewById(R.id.inv_item_tv_product_code)
        val tvQuantity: TextView = itemView.findViewById(R.id.inv_item_tv_quantity)
        val tvUnitCost: TextView = itemView.findViewById(R.id.inv_item_tv_unit_cost)
        val tvTotalCost: TextView = itemView.findViewById(R.id.inv_item_tv_total_cost)
        val tvSupplier: TextView = itemView.findViewById(R.id.inv_item_tv_supplier)
        val tvDate: TextView = itemView.findViewById(R.id.inv_item_tv_date)
        val tvPerson: TextView = itemView.findViewById(R.id.inv_item_tv_person)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inv_list, parent, false)
        return InventoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val entry = inventoryList[position]

        holder.tvProductName.text = entry.ProductName
        holder.tvProductCode.text = "Code: ${entry.ProductCode}"
        holder.tvQuantity.text = "Qty: ${entry.Quantity}"
        holder.tvUnitCost.text = "Unit Cost: $${String.format("%.2f", entry.UnitCost)}"
        holder.tvTotalCost.text = "Total: $${String.format("%.2f", entry.TotalCost)}"
        holder.tvSupplier.text = "Supplier: ${entry.Supplier}"
        holder.tvDate.text = "Date: ${entry.FormattedDateTime}"
        holder.tvPerson.text = "By: ${entry.Person.FullName}"

        holder.itemView.setOnClickListener {
            onItemClick(entry)
        }
    }

    override fun getItemCount(): Int = inventoryList.size

    fun updateData(newList: List<InventoryEntry>) {
        inventoryList = newList
        notifyDataSetChanged()
    }
}