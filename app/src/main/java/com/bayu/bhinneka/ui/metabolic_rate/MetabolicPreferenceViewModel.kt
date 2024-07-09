package com.bayu.bhinneka.ui.metabolic_rate

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.repository.Preferences

class MetabolicPreferenceViewModel : ViewModel() {

    lateinit var preferences: Preferences

    fun init(context: Context) {
        preferences = Preferences.getInstance(context)
    }

    fun inputPreferences(
        gender: Int,
        age: Int,
        weight: Float,
        height: Float,
        exercisePreference: Int
    ) {
        preferences.inputProperties(gender, age, weight, height, exercisePreference)
    }

    fun getPreferences(): Map<String, Any> {
        return preferences.readPropertiesAsMap()
    }

    fun getResult(): Float {
        return preferences.readResult()
    }

}