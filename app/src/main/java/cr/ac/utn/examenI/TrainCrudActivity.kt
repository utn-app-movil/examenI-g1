package cr.ac.utn.examenI

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.examenI.domain.model.TrainCourse
import cr.ac.utn.examenI.domain.model.TrainRegistration
import cr.ac.utn.examenI.util.EXTRA_ID
import cr.ac.utn.examenI.util.util
import cr.ac.utn.examenI.viewmodel.TrainViewModel
import java.time.LocalDate
import java.time.LocalDateTime

class TrainCrudActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var chkKotlin: CheckBox
    private lateinit var chkAndroid: CheckBox
    private lateinit var chkGit: CheckBox
    private lateinit var chkScrum: CheckBox
    private lateinit var txtDateTime: EditText
    private lateinit var btnDelete: Button

    private var currentId: String? = null
    private var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_train_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtId = findViewById(R.id.train_txtId)
        txtName = findViewById(R.id.train_txtName)
        txtFirstName = findViewById(R.id.train_txtFirstName)
        txtLastName = findViewById(R.id.train_txtLastName)
        txtPhone = findViewById(R.id.train_txtPhone)
        txtEmail = findViewById(R.id.train_txtEmail)
        txtAddress = findViewById(R.id.train_txtAddress)
        chkKotlin = findViewById(R.id.train_chkKotlin)
        chkAndroid = findViewById(R.id.train_chkAndroid)
        chkGit = findViewById(R.id.train_chkGit)
        chkScrum = findViewById(R.id.train_chkScrum)
        txtDateTime = findViewById(R.id.train_txtDateTime)
        btnDelete = findViewById(R.id.train_btnDelete)

        chkKotlin.text = TrainCourse.KOTLIN.displayName
        chkAndroid.text = TrainCourse.ANDROID_STUDIO.displayName
        chkGit.text = TrainCourse.GIT.displayName
        chkScrum.text = TrainCourse.SCRUM.displayName

        currentId = intent.getStringExtra(EXTRA_ID)
        if (!currentId.isNullOrEmpty()) {
            loadExistingData(currentId!!)
        } else {
            txtDateTime.setText(util.formatDateTime(LocalDateTime.now().plusDays(1)))
        }
        btnDelete.isEnabled = isEditMode

        findViewById<Button>(R.id.train_btnSave).setOnClickListener { onSaveClick() }
        btnDelete.setOnClickListener { doDelete() }
        findViewById<Button>(R.id.train_btnCancel).setOnClickListener { finish() }
    }

    private fun loadExistingData(id: String) {
        val item = TrainViewModel.getById(id)
        if (item == null) {
            Toast.makeText(this, getString(R.string.TextDataNoFound), Toast.LENGTH_SHORT).show()
            return
        }
        isEditMode = true
        txtId.setText(item.id)
        txtId.isEnabled = false
        txtName.setText(item.name)
        txtFirstName.setText(item.firstName)
        txtLastName.setText(item.lastName)
        txtPhone.setText(item.phone.toString())
        txtEmail.setText(item.email)
        txtAddress.setText(item.address)
        chkKotlin.isChecked = item.courses.contains(TrainCourse.KOTLIN)
        chkAndroid.isChecked = item.courses.contains(TrainCourse.ANDROID_STUDIO)
        chkGit.isChecked = item.courses.contains(TrainCourse.GIT)
        chkScrum.isChecked = item.courses.contains(TrainCourse.SCRUM)
        txtDateTime.setText(util.formatDateTime(item.trainingDateTime))
    }

    private fun getSelectedCourses(): List<TrainCourse> {
        val selectedCourses = mutableListOf<TrainCourse>()
        if (chkKotlin.isChecked) selectedCourses.add(TrainCourse.KOTLIN)
        if (chkAndroid.isChecked) selectedCourses.add(TrainCourse.ANDROID_STUDIO)
        if (chkGit.isChecked) selectedCourses.add(TrainCourse.GIT)
        if (chkScrum.isChecked) selectedCourses.add(TrainCourse.SCRUM)
        return selectedCourses
    }

    private fun isValidData(): Boolean {
        return txtId.text.toString().trim().isNotEmpty()
                && txtName.text.toString().trim().isNotEmpty()
                && txtFirstName.text.toString().trim().isNotEmpty()
                && txtLastName.text.toString().trim().isNotEmpty()
                && txtPhone.text.toString().trim().isNotEmpty()
                && txtEmail.text.toString().trim().isNotEmpty()
                && txtAddress.text.toString().trim().isNotEmpty()
                && txtDateTime.text.toString().trim().isNotEmpty()
    }

    private fun onSaveClick() {
        if (isEditMode) {
            util.showDialogCondition(this, getString(R.string.TextSaveQuestion)) { doSave() }
        } else {
            doSave()
        }
    }

    private fun doSave() {
        if (!isValidData()) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val phone = txtPhone.text.toString().trim().toIntOrNull()
        if (phone == null) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val selectedCourses = getSelectedCourses()
        if (selectedCourses.isEmpty()) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val trainingDateTime = util.parseStringToDateTimeModern(txtDateTime.text.toString().trim())
        if (trainingDateTime == null || trainingDateTime.toLocalDate().isBefore(LocalDate.now())) {
            Toast.makeText(this, getString(R.string.TextSaveError), Toast.LENGTH_SHORT).show()
            return
        }

        val item = TrainRegistration(
            txtId.text.toString().trim(),
            txtName.text.toString().trim(),
            txtFirstName.text.toString().trim(),
            txtLastName.text.toString().trim(),
            phone,
            txtEmail.text.toString().trim(),
            txtAddress.text.toString().trim(),
            selectedCourses,
            trainingDateTime
        )

        if (!isEditMode) {
            if (TrainViewModel.getById(item.id) != null) {
                Toast.makeText(this, getString(R.string.TextDuplicateData), Toast.LENGTH_SHORT).show()
                return
            }
            TrainViewModel.save(item)
        } else {
            TrainViewModel.update(item)
        }

        Toast.makeText(this, getString(R.string.TextSaveSuccess), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun doDelete() {
        if (!isEditMode || currentId.isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.TextDataNoFound), Toast.LENGTH_SHORT).show()
            return
        }
        util.showDialogCondition(this, getString(R.string.TextDeleteQuestion)) {
            TrainViewModel.delete(currentId!!)
            Toast.makeText(this, getString(R.string.TextDeleteSuccess), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.mnu_delete)?.isVisible = isEditMode
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> { onSaveClick(); true }
            R.id.mnu_delete -> { doDelete(); true }
            R.id.mnu_cancel -> { finish(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}