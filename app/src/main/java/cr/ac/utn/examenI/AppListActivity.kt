package cr.ac.utn.examenI

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.examenI.domain.model.Identifier
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.AppViewModel

class AppListActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var appointmentList: RecyclerView
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_app_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_list_root)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        appointmentList = findViewById(R.id.app_appointment_list)
        appointmentList.layoutManager = LinearLayoutManager(this)
        findViewById<Button>(R.id.app_add).setOnClickListener {
            util.openActivity(this, AppCrudActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        appointmentList.adapter = AppListAdapter(viewModel.getAppointments(), this)
    }

    override fun onItemClick(obj: Identifier) {
        util.openActivity(this, AppCrudActivity::class.java, EXTRA_ID, obj.id)
    }
}
