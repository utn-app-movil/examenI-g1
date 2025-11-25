package cr.ac.utn.movil

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import controller.bid_controller
import identities.bid_control
import java.lang.StringBuilder
import java.time.LocalDateTime

class bid_mainactivity : AppCompatActivity() {

    private val auctionController = bid_controller()
    private lateinit var nameEditText: EditText
    private lateinit var flastnameEditText: EditText
    private lateinit var articleCodeEditText: EditText
    private lateinit var articleDescEditText: EditText
    private lateinit var bidAmountEditText: EditText
    private lateinit var addButton: Button
    private lateinit var bidsListTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bid_activity)
        initializeViews()
        setupAddButton()
        updateBidsList()
    }

    private fun initializeViews() {
        nameEditText = findViewById(R.id.auction_et_person_name)
        flastnameEditText = findViewById(R.id.auction_et_flastname)
        articleCodeEditText = findViewById(R.id.auction_et_article_code)
        articleDescEditText = findViewById(R.id.auction_et_article_desc)
        bidAmountEditText = findViewById(R.id.auction_et_bid_amount)
        addButton = findViewById(R.id.auction_btn_add_bid)
        bidsListTextView = findViewById(R.id.auction_tv_bids_list)
    }

    private fun setupAddButton() {
        addButton.setOnClickListener {
            addBid()
        }
    }

    private fun addBid() {
        val name = nameEditText.text.toString()
        val flastname = flastnameEditText.text.toString()
        val articleCodeText = articleCodeEditText.text.toString()
        val description = articleDescEditText.text.toString()
        val amountText = bidAmountEditText.text.toString()

        if (name.isBlank() || flastname.isBlank() || articleCodeText.isBlank() || description.isBlank() || amountText.isBlank()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val articleCode = articleCodeText.toInt()
        val amount = amountText.toDouble()

        if (!auctionController.isBidAmountValid(amount, articleCode)) {
            Toast.makeText(this, "Bid amount must be higher than the previous one", Toast.LENGTH_LONG).show()
            return
        }

        val newBid = bid_control(
            date = LocalDateTime.now(),
            amount = amount,
            codeArt = articleCode,
            descriptionArt = description,
            adjudicated = false
        )
        auctionController.addBid(newBid)

        Toast.makeText(this, "Bid added successfully!", Toast.LENGTH_SHORT).show()
        clearInputFields()
        updateBidsList()
    }

    private fun clearInputFields() {
        nameEditText.text.clear()
        flastnameEditText.text.clear()
        articleCodeEditText.text.clear()
        articleDescEditText.text.clear()
        bidAmountEditText.text.clear()
    }


    private fun updateBidsList() {
        val allBids = auctionController.getAllBids()
        val stringBuilder = StringBuilder()

        if (allBids.isEmpty()) {
            bidsListTextView.text = "No bids yet."
        } else {
            allBids.forEach { bid ->
                stringBuilder.append(bid.FullDescription).append("\n\n")
            }
            bidsListTextView.text = stringBuilder.toString()
        }
    }
}

