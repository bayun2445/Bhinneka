package com.bayu.bhinneka.ui.metabolic_rate

import android.content.Context
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.repository.PreferencesRepository

class MetabolicPreferenceViewModel : ViewModel() {

    lateinit var preferences: PreferencesRepository

    fun init(context: Context) {
        preferences = PreferencesRepository.getInstance(context)
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