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
import cr.ac.utn.examenI.viewmodel.DashViewModel

class DashListActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var rvList: RecyclerView
    private val viewModel = DashViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dash_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dash_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvList = findViewById(R.id.dash_rvList)
        rvList.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.dash_btnAdd).setOnClickListener {
            util.openActivity(this, DashCrudActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val items = viewModel.getAll()
        rvList.adapter = DashListAdapter(items, this)
    }

    override fun onItemClick(obj: Identifier) {
        util.openActivity(this, DashCrudActivity::class.java, EXTRA_ID, obj.id)
    }
}