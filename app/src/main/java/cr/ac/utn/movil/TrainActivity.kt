package cr.ac.utn.movil

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import controller.TrainController
import cr.ac.utn.movil.util.util
import identities.Training
import java.time.LocalDate
import java.util.Calendar

class TrainActivity: AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var txtTrainId: EditText
    private lateinit var txtUserId: EditText
    private lateinit var txtUserName: EditText
    private lateinit var txtFLastName: EditText
    private lateinit var txtSLastName: EditText
    private lateinit var lbTrainList: TextView
    private lateinit var lbTrainDate: TextView

    private var isEditMode: Boolean = false
    private var year = 0
    private var month = 0
    private var day = 0

    private lateinit var trainController: TrainController
    private lateinit var menuitemDelete: MenuItem

    private lateinit var listTraining: ListView
    private lateinit var items: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_train)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rvPerson)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        trainController = TrainController(this)

        txtTrainId = findViewById(R.id.txtId_Training)
        txtUserId = findViewById(R.id.txtId_user)
        txtUserName = findViewById(R.id.txtName_user)
        txtFLastName = findViewById(R.id.txtFLastName_user)
        txtSLastName = findViewById(R.id.txtSLastName_user)

        lbTrainList = findViewById(R.id.lbTrainingList)
        lbTrainDate = findViewById(R.id.lbTrainingDate)

        ResetDate()

        val btnSelectDate = findViewById<ImageButton>(R.id.btnSelectDate_train)
        btnSelectDate.setOnClickListener { showDatePickerDialog() }

        val btnSearch = findViewById<ImageButton>(R.id.btnSearchId_train)
        btnSearch.setOnClickListener { searchTrain(txtTrainId.text.trim().toString()) }



        listTraining = findViewById(R.id.listTraining)
        listTraining.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        items = arrayOf("Capacitacion 1", "Capacitacion 2", "Capacitacion 3", "Capacitacion 4")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items)
        listTraining.adapter = adapter

        listTraining.setOnItemClickListener { _, _, _, _ ->
            updateTrainingListLabel()
        }
    }

    private fun updateTrainingListLabel() {
        val selected = mutableListOf<String>()
        val checked = listTraining.checkedItemPositions

        for (i in 0 until checked.size()) {
            val position = checked.keyAt(i)
            if (checked[position]) {
                selected.add(items[position])
            }
        }

        lbTrainList.text = selected.joinToString(", ")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_crud, menu)
        menuitemDelete = menu.findItem(R.id.mnu_delete)

        menuitemDelete.isVisible = isEditMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                if (isEditMode) {
                    util.showDialogCondition(
                        this,
                        getString(R.string.TextSaveActionQuestion)
                    ) { saveTraining() }
                } else {
                    saveTraining()
                }
                true
            }
            R.id.mnu_delete -> {
                util.showDialogCondition(
                    this,
                    getString(R.string.TextDeleteActionQuestion)
                ) { deleteTraining() }
                true
            }
            R.id.mnu_cancel -> {
                cleanScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun searchTrain(id: String){
        try {
            val training = trainController.getById(id)
            if (training != null){
                isEditMode = true

                txtTrainId.setText(training.ID.toString())
                txtUserId.setText(training.UserId.toString())
                txtTrainId.isEnabled = false
                txtUserId.isEnabled = false

                txtUserName.setText(training.UserName)
                txtFLastName.setText(training.UserFLastName)
                txtSLastName.setText(training.UserFLastName)

                lbTrainDate.text = getDateFormatString(
                    training.TrainingDate.dayOfMonth,
                    training.TrainingDate.month.value,
                    training.TrainingDate.year
                )

                year = training.TrainingDate.year
                month = training.TrainingDate.month.value
                day = training.TrainingDate.dayOfMonth

                val selectedText = training.TrainingList.trim()

                for (i in items.indices) {
                    listTraining.setItemChecked(i, false)
                }

                if (selectedText.isNotEmpty()) {
                    val selectedItems = selectedText.split(",")
                        .map { it.trim() }
                        .toSet()

                    for (i in items.indices) {
                        if (selectedItems.contains(items[i])) {
                            listTraining.setItemChecked(i, true)
                        }
                    }

                    lbTrainList.text = selectedItems.joinToString(", ")
                } else {
                    lbTrainList.text = ""
                }

                invalidateOptionsMenu()

            } else {
                Toast.makeText(this, R.string.MsgDataNoFound, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception){
            cleanScreen()
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun cleanScreen() {
        ResetDate()
        isEditMode = false
        txtTrainId.isEnabled = true
        txtUserId.isEnabled = true
        txtTrainId.setText("")
        txtUserId.setText("")
        txtUserName.setText("")
        txtFLastName.setText("")
        txtSLastName.setText("")
        lbTrainDate.text = ""
        lbTrainList.text = ""
        invalidateOptionsMenu()
    }

    private fun ResetDate() {
        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(this, this, year, month, day)

        val minDate = Calendar.getInstance()
        datePickerDialog.datePicker.minDate = minDate.timeInMillis

        val maxDate = Calendar.getInstance()
        maxDate.set(2100, Calendar.DECEMBER, 31)
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

        datePickerDialog.show()
    }

    fun getDateFormatString(dayOfMonth: Int, monthValue: Int, yearValue: Int): String {
        return "${if (dayOfMonth < 10) "0" else ""}$dayOfMonth/" +
                "${if (monthValue < 10) "0" else ""}$monthValue/$yearValue"
    }

    fun isValidatedData(): Boolean {
        val dateparse = util.parseStringToDateModern(lbTrainDate.text.toString(), "dd/MM/yyyy")
        return txtTrainId.text.trim().isNotEmpty() && txtUserId.text.trim().isNotEmpty()
                && txtFLastName.text.trim().isNotEmpty() && txtSLastName.text.trim().isNotEmpty()
                && txtUserName.text.trim().isNotEmpty() && lbTrainDate.text.trim().isNotEmpty()
                && lbTrainList.text.trim().isNotEmpty() && dateparse != null
    }

    fun saveTraining() {
        try {
            if (isValidatedData()) {
                if (trainController.getById(txtTrainId.text.toString()) != null && !isEditMode) {
                    Toast.makeText(this, R.string.MsgDuplicateDate, Toast.LENGTH_LONG).show()
                } else {
                    val training = Training()
                    training.ID = txtTrainId.text.toString()
                    training.UserId = txtUserId.text.toString()
                    training.UserName = txtUserName.text.toString()
                    training.UserFLastName = txtFLastName.text.toString()
                    training.UserSLastname = txtSLastName.text.toString()

                    val date2 = util.parseStringToDateModern(lbTrainDate.text.toString(), "dd/MM/yyyy")
                    training.TrainingDate = LocalDate.of(date2?.year!!, date2.month.value, date2.dayOfMonth)

                    training.TrainingList = lbTrainList.text.toString()

                    if (!isEditMode)
                        trainController.addTraining(training)
                    else
                        trainController.updateTraining(training)

                    cleanScreen()
                    Toast.makeText(this, R.string.MsgSaveSuccess, Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, R.string.MsgMissingData, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun deleteTraining(): Unit{
        try {
            trainController.removeTraining(txtTrainId.text.toString())
            cleanScreen()
            Toast.makeText(this, R.string.MsgDeleteSuccess
                , Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString()
                , Toast.LENGTH_LONG).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        lbTrainDate.text = getDateFormatString(dayOfMonth, month + 1, year)
    }
}
