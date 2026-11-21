package cr.ac.utn.examenI

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EveListActivity : AppCompatActivity() {

    private lateinit var rvList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_eve_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvList = findViewById(R.id.rvList)
        rvList.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            startActivity(Intent(this, EveCrudActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val items = EveCrudActivity.getAllRecords()
        rvList.adapter = EventRowAdapter(items) { id ->
            val intent = Intent(this, EveCrudActivity::class.java)
            intent.putExtra("EXTRA_ID", id)
            startActivity(intent)
        }
    }

    private inner class EventRowAdapter(
        private val items: List<EveCrudActivity.EventRecord>,
        private val onItemClick: (String) -> Unit
    ) : RecyclerView.Adapter<EventRowAdapter.RowViewHolder>() {

        inner class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTitle: TextView = itemView.findViewById(android.R.id.text1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return RowViewHolder(view)
        }

        override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
            val item = items[position]
            holder.tvTitle.text = item.summaryText()
            holder.itemView.setOnClickListener { onItemClick(item.id) }
        }

        override fun getItemCount(): Int = items.size
    }
}