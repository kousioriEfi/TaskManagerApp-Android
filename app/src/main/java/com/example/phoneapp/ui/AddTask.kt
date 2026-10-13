package com.example.phoneapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.phoneapp.R
import com.example.phoneapp.data.AppDatabase
import com.example.phoneapp.data.Status
import com.example.phoneapp.data.StatusEntity
import com.example.phoneapp.data.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class AddTask : AppCompatActivity() {

    private var startTimeMillis: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val etShortName = findViewById<EditText>(R.id.etShortName)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etDuration = findViewById<EditText>(R.id.etDuration)
        val etDifficulty = findViewById<EditText>(R.id.etDifficulty)
        val etLocation = findViewById<EditText>(R.id.etLocation)
        val tvStartTime = findViewById<TextView>(R.id.tvStartTime)

        findViewById<Button>(R.id.btnPickStartNow).setOnClickListener {
            val t = System.currentTimeMillis() + 60_000
            startTimeMillis = t
            tvStartTime.text = "Start set: $t"
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val shortName = etShortName.text.toString().trim()
            val desc = etDescription.text.toString().trim()
            val durationStr = etDuration.text.toString().trim()
            val difficultyStr = etDifficulty.text.toString().trim()
            val location = etLocation.text.toString().trim().ifEmpty { null }
            val start = startTimeMillis

            if (shortName.isEmpty()) return@setOnClickListener toast("Short name required")
            if (shortName.length > 20) return@setOnClickListener toast("Short name max 20")
            if (desc.isEmpty()) return@setOnClickListener toast("Description required")
            if (desc.length > 150) return@setOnClickListener toast("Description max 150")

            val duration = durationStr.toIntOrNull()
            if (duration == null || duration <= 0) return@setOnClickListener toast("Duration must be > 0")

            val difficulty = difficultyStr.toIntOrNull()
            if (difficulty == null || difficulty !in 0..10) return@setOnClickListener toast("Difficulty must be 0..10")

            if (start == null) return@setOnClickListener toast("Press 'Set start time'")

            val task = TaskEntity(
                shortName = shortName,
                description = desc,
                startTimeMillis = start,
                durationHours = duration,
                difficulty = difficulty,
                location = location,
                createdDayKey = dayKey(System.currentTimeMillis()),
                statusId = Status.RECORDED
            )

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val db = AppDatabase.get(this@AddTask)

                    // ✅ Seed statuses so FOREIGN KEY is valid
                    val statusDao = db.statusDao()
                    if (statusDao.count() == 0) {
                        statusDao.upsertAll(
                            listOf(
                                StatusEntity(Status.RECORDED, "recorded"),
                                StatusEntity(Status.IN_PROGRESS, "in-progress"),
                                StatusEntity(Status.EXPIRED, "expired"),
                                StatusEntity(Status.COMPLETED, "completed")
                            )
                        )
                    }

                    db.taskDao().insert(task)

                    runOnUiThread {
                        toast("Saved!")
                        finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        toast("ERROR: ${e.message}")
                    }
                }
            }
        }
    }

    private fun dayKey(millis: Long): Int {
        val c = Calendar.getInstance().apply { timeInMillis = millis }
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH) + 1
        val d = c.get(Calendar.DAY_OF_MONTH)
        return y * 10000 + m * 100 + d
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
