package com.bayu.bhinneka.data.repository

import android.content.Context
import android.content.SharedPreferences

class Preferences private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("metabolic_prefs", Context.MODE_PRIVATE)

    var gender: Int = -1
        private set
    var age: Int = 0
        private set
    var weight: Float = 0f
        private set
    var height: Float = 0f
        private set
    var exercisePreference: Int = 0
        private set

    var result: Float = 0f
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

    fun readResult(): Float {
//        a. AMB Pria = 66,5 + (13,7 x berat badan(kg)) + (5 x tinggi badan(cm)) – (6,8 x
//        usia)
//        b. BMR Wanita = 655 + (9,6 x berat badan(kg)) + (1,8 x tinggi badan(cm)) – (4,7
//        x usia

        val genderConstant = when(gender) {
            0 -> 66.5f // male
            1 -> 655.0f // female
            else -> 0f
        }

        val weightMultiplier = when (gender) {
            0 -> 13.7f * weight
            1 -> 9.6f * weight
            else -> 0f
        }

        val heightMultiplier = when (gender) {
            0 -> 5f * height
            1 -> 1.8f * height
            else -> 0f
        }

        val ageMultiplier = when (gender) {
            0 -> 6.8f * age.toFloat()
            1 -> 4.7f * age.toFloat()
            else -> 0f
        }

        val calculatedBMR = genderConstant + weightMultiplier + heightMultiplier - ageMultiplier

        val calculatedResult = when (exercisePreference) {
            0 -> calculatedBMR * 1.2f
            1 -> calculatedBMR * 1.375f
            2 -> calculatedBMR * 1.55f
            3 -> calculatedBMR * 1.725f
            4 -> calculatedBMR * 1.8f
            else -> 0f
        }

        this.result = calculatedResult
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
        gender = sharedPreferences.getInt("gender", -1)
        age = sharedPreferences.getInt("age", 0)
        weight = sharedPreferences.getFloat("weight", 0f)
        height = sharedPreferences.getFloat("height", 0f)
        exercisePreference = sharedPreferences.getInt("exercisePreference", 0)
    }

    fun clearPreferences() {
        sharedPreferences.edit()
            .clear()
            .apply()

        this.gender = -1
        this.age = 0
        this.weight = 0f
        this.height = 0f
        this.exercisePreference = 0
        this.result = 0f
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