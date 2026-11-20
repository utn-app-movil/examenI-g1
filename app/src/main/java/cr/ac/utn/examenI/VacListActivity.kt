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

class VacListActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var rvList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vac_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvList = findViewById(R.id.rvList)
        rvList.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            util.openActivity(this, VacCrudActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val items = emptyList<Identifier>()
        rvList.adapter = ListAdapter(items, this)
    }

    override fun onItemClick(obj: Identifier) {
        util.openActivity(this, VacCrudActivity::class.java, EXTRA_ID, obj.id)
    }
}
