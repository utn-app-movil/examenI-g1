package cr.ac.utn.examenI

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.inv_InventoryViewModel

class InvListActivity : AppCompatActivity() {

    private lateinit var inv_entriesList: RecyclerView
    private lateinit var inv_viewModel: inv_InventoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inv_list)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.inv_list_root)
        ) { inv_view, inv_insets ->
            val inv_systemBars = inv_insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            inv_view.setPadding(
                inv_systemBars.left,
                inv_systemBars.top,
                inv_systemBars.right,
                inv_systemBars.bottom
            )

            inv_insets
        }

        inv_viewModel = ViewModelProvider(this)[
            inv_InventoryViewModel::class.java
        ]

        inv_entriesList = findViewById(R.id.inv_entries_list)
        inv_entriesList.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.inv_add_button).setOnClickListener {
            util.openActivity(
                this,
                InvCrudActivity::class.java
            )
        }
    }

    override fun onResume() {
        super.onResume()
        inv_loadEntries()
    }

    private fun inv_loadEntries() {
        val inv_entries = inv_viewModel.inv_getAll()

        inv_entriesList.adapter = inv_InventoryAdapter(
            inv_entries
        ) { inv_entry ->
            util.openActivity(
                this,
                InvCrudActivity::class.java,
                EXTRA_ID,
                inv_entry.id
            )
        }
    }
}
