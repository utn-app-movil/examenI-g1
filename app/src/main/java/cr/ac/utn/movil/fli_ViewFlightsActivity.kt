package cr.ac.utn.movil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import controller.fli_FlightController
import model.fli_Flight

class fli_ViewFlightsActivity : AppCompatActivity() {

    private lateinit var fli_recyclerView: RecyclerView
    private lateinit var fli_flightAdapter: fli_FlightAdapter
    private val fli_flightController = fli_FlightController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fli_recyclerView = RecyclerView(this)
        fli_recyclerView.layoutManager = LinearLayoutManager(this)

        setContentView(fli_recyclerView)
    }

    override fun onResume() {
        super.onResume()
        updateFlightList()
    }

    private fun updateFlightList() {
        val flights = fli_flightController.getFlights().toMutableList()
        fli_flightAdapter = fli_FlightAdapter(flights) {
            val intent = Intent(this, fli_UpdateFlightActivity::class.java)
            intent.putExtra("fli_flight_id", it.fli_id)
            startActivity(intent)
        }
        fli_recyclerView.adapter = fli_flightAdapter
    }
}

class fli_FlightAdapter(
    private val flights: MutableList<fli_Flight>,
    private val onUpdateClick: (fli_Flight) -> Unit
) : RecyclerView.Adapter<fli_FlightAdapter.FlightViewHolder>() {

    private val fli_flightController = fli_FlightController()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val context = parent.context
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16,16,16,16)
        }
        return FlightViewHolder(layout)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        holder.bind(flights[position], position)
    }

    override fun getItemCount() = flights.size

    inner class FlightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val flightInfo: TextView
        private val updateButton: Button
        private val deleteButton: Button

        init {
            val container = itemView as LinearLayout
            flightInfo = TextView(container.context)
            updateButton = Button(container.context).apply { text = "Actualizar" }
            deleteButton = Button(container.context).apply { text = "Eliminar" }
            
            val buttonLayout = LinearLayout(container.context).apply {
                orientation = LinearLayout.HORIZONTAL
                addView(updateButton)
                addView(deleteButton)
            }

            container.addView(flightInfo)
            container.addView(buttonLayout)
        }

        fun bind(flight: fli_Flight, position: Int) {
            flightInfo.text = "Vuelo: ${flight.fli_flightNumber} - ${flight.fli_originCountry} a ${flight.fli_destinationCountry}"
            
            updateButton.setOnClickListener {
                onUpdateClick(flight)
            }
            
            deleteButton.setOnClickListener { 
                fli_flightController.deleteFlight(flight.fli_id)
                flights.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, flights.size)
            }
        }
    }
}