package cr.ac.utn.examenI

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.examenI.util.util

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnAppointment_main).setOnClickListener {
            util.openActivity(this, AppListActivity::class.java)
        }

        findViewById<Button>(R.id.btnVaccine_main).setOnClickListener {
            util.openActivity(this, VacListActivity::class.java)
        }

        findViewById<Button>(R.id.btnLicense_main).setOnClickListener {
            util.openActivity(this, LicListActivity::class.java)
        }

        findViewById<Button>(R.id.btnMedChecking_main).setOnClickListener {
            util.openActivity(this, MedListActivity::class.java)
        }

        findViewById<Button>(R.id.btnClients_main).setOnClickListener {
            util.openActivity(this, CliListActivity::class.java)
        }

        findViewById<Button>(R.id.btnFlights_main).setOnClickListener {
            util.openActivity(this, FliListActivity::class.java)
        }

        findViewById<Button>(R.id.btnRent_main).setOnClickListener {
            util.openActivity(this, RentListActivity::class.java)
        }

        findViewById<Button>(R.id.btnEvents_main).setOnClickListener {
            util.openActivity(this, EveListActivity::class.java)
        }

        findViewById<Button>(R.id.btnPharmacy_main).setOnClickListener {
            util.openActivity(this, PhaListActivity::class.java)
        }

        findViewById<Button>(R.id.btnRecruitering_main).setOnClickListener {
            util.openActivity(this, RecruListActivity::class.java)
        }

        findViewById<Button>(R.id.btnBidding_main).setOnClickListener {
            util.openActivity(this, BidListActivity::class.java)
        }

        findViewById<Button>(R.id.btnSinpe_main).setOnClickListener {
            util.openActivity(this, SinListActivity::class.java)
        }

        findViewById<Button>(R.id.btnPayroll_main).setOnClickListener {
            util.openActivity(this, PayListActivity::class.java)
        }

        findViewById<Button>(R.id.btnInventory_main).setOnClickListener {
            util.openActivity(this, InvListActivity::class.java)
        }

        findViewById<Button>(R.id.btnShipper_main).setOnClickListener {
            util.openActivity(this, ShipListActivity::class.java)
        }

        findViewById<Button>(R.id.btnLibrary_main).setOnClickListener {
            util.openActivity(this, LibListActivity::class.java)
        }

        findViewById<Button>(R.id.btnExchange_main).setOnClickListener {
            util.openActivity(this, ExchListActivity::class.java)
        }

        findViewById<Button>(R.id.btnTraining_main).setOnClickListener {
            util.openActivity(this, TrainListActivity::class.java)
        }

        findViewById<Button>(R.id.btnNotification_main).setOnClickListener {
            util.openActivity(this, NotifListActivity::class.java)
        }

        findViewById<Button>(R.id.btnDashboard_main).setOnClickListener {
            util.openActivity(this, DashListActivity::class.java)
        }

        findViewById<Button>(R.id.btnPaymentEnsurance_main).setOnClickListener {
            util.openActivity(this, PayenListActivity::class.java)
        }

        findViewById<Button>(R.id.btnMarketing_main).setOnClickListener {
            util.openActivity(this, MarkListActivity::class.java)
        }

        findViewById<Button>(R.id.btnEnsurance_main).setOnClickListener {
            util.openActivity(this, EnsListActivity::class.java)
        }

        findViewById<Button>(R.id.btnVehicle_main).setOnClickListener {
            util.openActivity(this, VehListActivity::class.java)
        }

        findViewById<Button>(R.id.btnProduction_main).setOnClickListener {
            util.openActivity(this, ProdListActivity::class.java)
        }

        findViewById<Button>(R.id.btnWater_main).setOnClickListener {
            util.openActivity(this, WatListActivity::class.java)
        }

        findViewById<Button>(R.id.btnRecycling_main).setOnClickListener {
            util.openActivity(this, RecyListActivity::class.java)
        }

    }
}
