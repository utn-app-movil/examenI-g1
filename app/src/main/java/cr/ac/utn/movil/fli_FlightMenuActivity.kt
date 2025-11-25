package cr.ac.utn.movil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class fli_FlightMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }

        val createFlightButton = Button(this).apply {
            text = "Registrar Vuelo"
            setOnClickListener {
                val intent = Intent(this@fli_FlightMenuActivity, fli_FlightActivity::class.java)
                startActivity(intent)
            }
        }

        val viewFlightsButton = Button(this).apply {
            text = "Ver Vuelos Registrados"
            setOnClickListener {
                val intent = Intent(this@fli_FlightMenuActivity, fli_ViewFlightsActivity::class.java)
                startActivity(intent)
            }
        }

        layout.addView(createFlightButton)
        layout.addView(viewFlightsButton)

        setContentView(layout)
    }
}