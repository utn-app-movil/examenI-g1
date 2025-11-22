package cr.ac.utn.movil

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import identities.Vehicle

class VehicleAdapter(private val vehicles: List<Vehicle>) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    // Generate unique IDs for the TextViews we will create in code
    private val idTextViewId = View.generateViewId()
    private val brandTextViewId = View.generateViewId()
    private val typeTextViewId = View.generateViewId()
    private val autonomyTextViewId = View.generateViewId()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        // Instead of inflating an XML file, create the layout programmatically
        val context = parent.context
        val container = LinearLayout(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            val padding = (8 * context.resources.displayMetrics.density).toInt()
            setPadding(padding, padding, padding, padding)
        }

        // Create TextView for Vehicle ID
        val idTextView = TextView(context).apply {
            id = idTextViewId
            textSize = 16f
            setTypeface(null, Typeface.BOLD)
        }

        // Create TextView for Brand
        val brandTextView = TextView(context).apply {
            id = brandTextViewId
        }

        // Create TextView for Type
        val typeTextView = TextView(context).apply {
            id = typeTextViewId
        }

        // Create TextView for Autonomy
        val autonomyTextView = TextView(context).apply {
            id = autonomyTextViewId
        }
        
        // Add the created TextViews to the container
        container.addView(idTextView)
        container.addView(brandTextView)
        container.addView(typeTextView)
        container.addView(autonomyTextView)

        return VehicleViewHolder(container)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicles[position]
        holder.textViewVehicleId.text = "ID: ${vehicle.vehicleId}"
        holder.textViewVehicleBrand.text = "Marca: ${vehicle.brand}"
        holder.textViewVehicleType.text = "Tipo: ${vehicle.type}"
        holder.textViewEstimatedAutonomy.text = "Autonomía: ${vehicle.estimatedAutonomy} km"
    }

    override fun getItemCount() = vehicles.size

    inner class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Find the TextViews using the generated IDs
        val textViewVehicleId: TextView = itemView.findViewById(idTextViewId)
        val textViewVehicleBrand: TextView = itemView.findViewById(brandTextViewId)
        val textViewVehicleType: TextView = itemView.findViewById(typeTextViewId)
        val textViewEstimatedAutonomy: TextView = itemView.findViewById(autonomyTextViewId)
    }
}
