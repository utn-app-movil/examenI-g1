package cr.ac.utn.movil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import cr.ac.utn.movil.controller.InventoryController
import cr.ac.utn.movil.util.EXTRA_ID
import kotlin.jvm.java

class InventoryListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InventoryAdapter
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var tvEmpty: TextView
    private val controller = InventoryController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inv_list)

        supportActionBar?.title = getString(R.string.inv_list_title)

        recyclerView = findViewById(R.id.inv_recycler_view)
        fabAdd = findViewById(R.id.inv_fab_add)
        tvEmpty = findViewById(R.id.inv_tv_empty)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InventoryAdapter(emptyList()) { entry ->
            openDetailActivity(entry.ID)
        }
        recyclerView.adapter = adapter

        fabAdd.setOnClickListener {
            openDetailActivity(null)
        }

        loadInventoryData()
    }

    override fun onResume() {
        super.onResume()
        loadInventoryData()
    }

    private fun loadInventoryData() {
        val inventoryList = controller.getAllInventoryEntries()

        if (inventoryList.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            adapter.updateData(inventoryList)
        }
    }

    private fun openDetailActivity(id: String?) {
        val intent = Intent(this, InventoryDetailActivity::class.java)
        if (id != null) {
            intent.putExtra(EXTRA_ID, id)
        }
        startActivity(intent)
    }
}