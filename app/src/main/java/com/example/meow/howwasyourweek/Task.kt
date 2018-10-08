package com.example.meow.howwasyourweek

import java.io.Serializable


class Task {

    var description: String? = null
    var completed: Boolean = false
    var difficulty: Int? = null

    constructor(description: String, completed: Boolean, difficulty: Int) {
        this.description = description
        this.completed = completed
        this.difficulty = difficulty
    }

    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("description", description!!)
        result.put("completed", completed!!)
        result.put("difficulty", difficulty!!)

        return result
    }

}