package com.bayu.bhinneka.data.repository

import android.content.Context
import android.content.SharedPreferences

class Preferences private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("metabolic_prefs", Context.MODE_PRIVATE)

    var gender: Int = 0
        private set
    var age: Int = 0
        private set
    var weight: Float = 0f
        private set
    var height: Float = 0f
        private set
    var exercisePreference: Int = 0
        private set

    var result: Int = 0
        private set

    fun inputProperties(
        gender: Int,
        age: Int,
        weight: Float,
        height: Float,
        exercisePreference: Int
    ) {
        this.gender = gender
        this.age = age
        this.weight = weight
        this.height = height
        this.exercisePreference = exercisePreference

        with(sharedPreferences.edit()) {
            putInt("gender", gender)
            putInt("age", age)
            putFloat("weight", weight)
            putFloat("height", height)
            putInt("exercisePreference", exercisePreference)
            apply()
        }
    }

    fun readResult(): Int {
        // Calculate a result based on the stored preferences
        // This is a placeholder, replace with your actual calculation
        this.result = age + weight.toInt() + height.toInt() + exercisePreference
        return result
    }

    fun readPropertiesAsMap(): Map<String, Any> {
        val propertiesMap = mutableMapOf<String, Any>()
        propertiesMap["gender"] = gender
        propertiesMap["age"] = age
        propertiesMap["weight"] = weight
        propertiesMap["height"] = height
        propertiesMap["exercisePreference"] = exercisePreference
        return propertiesMap
    }

    fun loadPreferences() {
        gender = sharedPreferences.getInt("gender", 0)
        age = sharedPreferences.getInt("age", 0)
        weight = sharedPreferences.getFloat("weight", 0f)
        height = sharedPreferences.getFloat("height", 0f)
        exercisePreference = sharedPreferences.getInt("exercisePreference", 0)
    }

    companion object {
        @Volatile
        private var INSTANCE: Preferences? = null

        fun getInstance(context: Context): Preferences {
            synchronized(this) {
                return INSTANCE ?: Preferences(context).also {
                    INSTANCE = it
                    it.loadPreferences() // Load preferences when creating the instance
                }
            }
        }
    }
}