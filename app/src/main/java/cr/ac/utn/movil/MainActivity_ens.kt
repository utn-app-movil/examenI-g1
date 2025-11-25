package cr.ac.utn.movil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import cr.ac.utn.movil.controllers.InsurancePolicyController_ens
import cr.ac.utn.movil.entities.InsurancePolicy
import java.text.SimpleDateFormat
import java.util.*

class MainActivity_ens : AppCompatActivity() {

    private lateinit var listViewPolicies_ens: ListView
    private lateinit var tvEmpty_ens: TextView
    private lateinit var adapter_ens: PolicyAdapter_ens
    private lateinit var controller_ens: InsurancePolicyController_ens

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ens)

        val btnBackMain_ens = findViewById<ImageView>(R.id.btnBackMain_ens)
        btnBackMain_ens.setOnClickListener { finish() }

        listViewPolicies_ens = findViewById(R.id.listViewPolicies_ens)
        tvEmpty_ens = findViewById(R.id.tvEmpty_ens)

        controller_ens = InsurancePolicyController_ens(this)
        adapter_ens = PolicyAdapter_ens(this, mutableListOf())
        listViewPolicies_ens.adapter = adapter_ens

        listViewPolicies_ens.setOnItemClickListener { _, _, position, _ ->
            val policy = adapter_ens.getItem(position)
            val intent = Intent(this, RegisterActivity_ens::class.java)
            intent.putExtra("POLICY_NUMBER", policy?.policyNumber_ens)
            startActivity(intent)
        }

        val fabAdd_ens = findViewById<FloatingActionButton>(R.id.fabAdd_ens)
        fabAdd_ens.setOnClickListener {
            startActivity(Intent(this, RegisterActivity_ens::class.java))
        }

        refreshList_ens()
    }

    override fun onResume() {
        super.onResume()
        refreshList_ens()
    }

    private fun refreshList_ens() {
        val policies = controller_ens.getAllPolicies_ens()
        adapter_ens.clear()
        adapter_ens.addAll(policies)
        adapter_ens.notifyDataSetChanged()
        tvEmpty_ens.visibility = if (policies.isEmpty()) View.VISIBLE else View.GONE
    }

    private inner class PolicyAdapter_ens(
        context: Context,
        policies: MutableList<InsurancePolicy>
    ) : ArrayAdapter<InsurancePolicy>(context, android.R.layout.simple_list_item_2, android.R.id.text1, policies) {

        private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            val policy = getItem(position)

            val text1 = view.findViewById<TextView>(android.R.id.text1)
            val text2 = view.findViewById<TextView>(android.R.id.text2)

            policy?.let {
                text1.text = "${it.policyNumber_ens} - ${it.company_ens}"
                text2.text = "${it.insuranceType_ens} | $${it.premium_ens} | Exp: ${dateFormat.format(it.expirationDate_ens)}"
            }

            return view
        }
    }
}