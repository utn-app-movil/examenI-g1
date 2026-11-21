package cr.ac.utn.examenI

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.examenI.domain.model.Identifier
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import cr.ac.utn.examenI.domain.model.RecruApplication
import cr.ac.utn.examenI.viewmodel.RecruViewModel

class RecruListActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var rvList: RecyclerView

    private val recruViewModel: RecruViewModel by lazy {
        ViewModelProvider(this)[RecruViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recru_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recru_list_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvList = findViewById(R.id.recru_applications_list)
        rvList.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.recru_btn_add).setOnClickListener {
            util.openActivity(this, RecruCrudActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        rvList.adapter = RecruListAdapter(recruViewModel.getAll(), this)
    }

    override fun onItemClick(obj: Identifier) {
        util.openActivity(this, RecruCrudActivity::class.java, EXTRA_ID, obj.id)
    }
}
private class RecruListAdapter(
    private val applications: List<RecruApplication>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecruListAdapter.RecruViewHolder>() {

    class RecruViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(android.R.id.text1)
        val company: TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecruViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return RecruViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecruViewHolder, position: Int) {
        val application = applications[position]
        holder.name.text = application.fullName
        holder.company.text = application.targetCompany
        holder.itemView.setOnClickListener {
            listener.onItemClick(application)
        }
    }

    override fun getItemCount(): Int = applications.size
}