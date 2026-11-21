
package cr.ac.utn.examenI

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.examenI.domain.model.Identifier
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.ens_InsuranceViewModel

class EnsListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var rvList: RecyclerView
    private lateinit var viewModel: ens_InsuranceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ens_list)

        viewModel = ViewModelProvider(this).get(
            ens_InsuranceViewModel::class.java
        )

        rvList = findViewById(R.id.rvList)
        rvList.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            util.openActivity(this, EnsCrudActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val items: List<Identifier> = viewModel.getPolicies()
        rvList.adapter = ListAdapter(items, this)
    }

    override fun onItemClick(obj: Identifier) {
        util.openActivity(
            this,
            EnsCrudActivity::class.java,
            EXTRA_ID,
            obj.id
        )
    }
}
