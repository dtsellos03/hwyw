package com.example.meow.howwasyourweek

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class TaskAdapter(var tasks: MutableList<Task>, private val context: Context,
                  private val firestoreDB: FirebaseFirestore) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val TAG = "BBB"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val context = parent?.context
        val view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder?.bindTask(tasks[position])
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descriptionTextView = view?.findViewById(R.id.task_description) as TextView
        val completedCheckBox = view?.findViewById(R.id.task_completed) as CheckBox

        fun bindTask(task: Task) {
            descriptionTextView.text = task.description
            completedCheckBox.isChecked = task.completed

            completedCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                tasks[adapterPosition].completed = isChecked
            }
        }


        //item selected listener for spinner
        completed_spinner.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.d("AA", p2.toString())
            }

        }
    }

    fun addTask(task: Task) {
        var id = (1..3).shuffled().last().toString()
        tasks.add(task)
        firestoreDB!!.collection("tasks")
                .document(id)
                .set(task.toMap())
                .addOnSuccessListener {
                    Log.e(TAG, "Note document update successful!")

                    notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error adding Note document", e)

                }

    }
}
