package cr.ac.utn.movil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import cr.ac.utn.movil.controllers.InsurancePolicyController_ens
import cr.ac.utn.movil.entities.InsurancePolicy
import java.text.SimpleDateFormat
import java.util.*

class MainActivity_ens : AppCompatActivity() {

    private lateinit var listViewPolicies: ListView
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: PolicyAdapter
    private lateinit var controller: InsurancePolicyController_ens

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_ens)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_ens)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listViewPolicies = findViewById(R.id.listViewPolicies)
        tvEmpty = findViewById(R.id.tvEmpty)

        controller = InsurancePolicyController_ens(this)

        adapter = PolicyAdapter(this, mutableListOf())
        listViewPolicies.adapter = adapter

        listViewPolicies.setOnItemClickListener { _, _, position, _ ->
            val policy = adapter.getItem(position)
            val intent = Intent(this@MainActivity_ens, RegisterActivity_ens::class.java)
            intent.putExtra("POLICY_NUMBER", policy?.policyNumber)
            startActivity(intent)
        }

        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        fabAdd.setOnClickListener(View.OnClickListener { view ->
            startActivity(Intent(this, RegisterActivity_ens::class.java))
        })

        refreshList()
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        val policies = controller.getAllPolicies()
        adapter.clear()
        adapter.addAll(policies)
        adapter.notifyDataSetChanged()

        tvEmpty.visibility = if (policies.isEmpty()) View.VISIBLE else View.GONE
    }

    private class PolicyAdapter(
        context: android.content.Context,
        policies: MutableList<InsurancePolicy>
    ) : ArrayAdapter<InsurancePolicy>(context, android.R.layout.simple_list_item_2, android.R.id.text1, policies) {

        private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

        override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
            val view = super.getView(position, convertView, parent)
            val policy = getItem(position)

            val text1 = view.findViewById<TextView>(android.R.id.text1)
            val text2 = view.findViewById<TextView>(android.R.id.text2)

            policy?.let {
                text1.text = "${it.policyNumber} - ${it.company}"
                text2.text = "${it.insuranceType} | $${it.premium} | Exp: ${dateFormat.format(it.expirationDate)}"
            }

            return view
        }
    }
}