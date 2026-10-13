package com.example.phoneapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneapp.data.AppDatabase
import com.example.phoneapp.ui.AddTask
import com.example.phoneapp.ui.TaskAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Open AddTask screen
        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            startActivity(Intent(this, AddTask::class.java))
        }

        // RecyclerView setup
        val rv = findViewById<RecyclerView>(R.id.rvTasks)
        adapter = TaskAdapter()
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun loadTasks() {
        lifecycleScope.launch(Dispatchers.IO) {
            val tasks = AppDatabase.get(this@MainActivity).taskDao().getAllNotCompleted()
            runOnUiThread {
                adapter.submitList(tasks)
            }
        }
    }
}
