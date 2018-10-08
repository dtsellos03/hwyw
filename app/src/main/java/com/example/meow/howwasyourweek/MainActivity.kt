package com.example.meow.howwasyourweek

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var adapter: TaskAdapter? = null
    var TAG = "MAINZZZ"
    private var firestoreDB: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        var myStrings = arrayOf("One", "Two", "Three", "Four", "Five")


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab?.setOnClickListener { _ ->
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST)
        }

        firestoreDB = FirebaseFirestore.getInstance()

        loadNotesList()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_TASK_REQUEST && resultCode == Activity.RESULT_OK) {
            val task = Task(data!!.getStringExtra(DESCRIPTION_TEXT), false, 1)
            adapter?.addTask(task)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadNotesList() {
        Log.d(TAG, "starting")
        firestoreDB!!.collection("tasks")
                .get()
                .addOnCompleteListener { task ->
                    Log.d(TAG, "finished")
                    if (task.isSuccessful) {
                        val notesList = mutableListOf<Task>()
                        Log.d("AAA", task.result.toString())
                        for (doc in task.result) {
                            Log.d("QAA", doc.toString())
                            var note = Task("aa", true, 1)
                            Log.d("AAA", doc.data.toString())
                            note = doc.toObject(Task::class.java)

                            notesList.add(note)
                        }

                        adapter = TaskAdapter(notesList, applicationContext, firestoreDB!!)
                        val recyclerView = findViewById(R.id.task_list) as RecyclerView
                        val layoutManager = LinearLayoutManager(this)
                        recyclerView.layoutManager = layoutManager
                        recyclerView.adapter = adapter
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.exception)
                    }
                }
    }

    private fun getSampleTasks(): MutableList<Task> {
        val task1 = Task("task1", true, 1)
        val task2 = Task("task2", true, 1)

        return mutableListOf(task1, task2)
    }

    companion object {
        private val ADD_TASK_REQUEST = 0
        val DESCRIPTION_TEXT = "description"
    }
}
