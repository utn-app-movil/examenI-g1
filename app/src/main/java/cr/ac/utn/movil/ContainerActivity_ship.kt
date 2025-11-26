package cr.ac.utn.movil

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import controller.ContainerController_ship

class ContainerActivity_ship : AppCompatActivity() {

    private lateinit var txtId: TextInputEditText
    private lateinit var txtdate : TextView
    private lateinit var txttepm: TextInputEditText
    private lateinit var txtweight: TextInputEditText
    private lateinit var txttype: TextView
    private lateinit var txtproduct: TextView


    private  var day :Int=0
    private  var month: Int=0
    private  var year: Int =0
    private lateinit var menuItemDelete: MenuItem
    private lateinit var Controller: ContainerController_ship

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_container_ship)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Controller = ContainerController_ship(this)
        txtId = findViewById<TextInputEditText>(R.id.idtxt_ship)
        txtdate = findViewById<TextView>(R.id.container_date_ship)
        txttepm = findViewById<TextInputEditText>(R.id.temptxt_ship)
        txtweight = findViewById<TextInputEditText>(R.id.container_weight_ship)
        txttype = findViewById<TextView>(R.id.container_type)
        txtproduct = findViewById<TextView>(R.id.container_products)

        val SelectType = findViewById<Button>(R.id.btnType_ship)
        SelectType.setOnClickListener(View.OnClickListener{ view ->
            Window()
        })

    }
    fun Window(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle("I am the title")
            .setPositiveButton("Positive") { dialog, which ->
                // Do something.
            }
            .setNegativeButton("Negative") { dialog, which ->
                // Do something else.
            }
            .setSingleChoiceItems(
                arrayOf("Item One", "Item Two", "Item Three"), 0
            ) { dialog, which ->
                // Do something.
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}