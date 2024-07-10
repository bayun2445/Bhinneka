package com.bayu.bhinneka.ui.history

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.repository.PreferencesRepository
import com.bayu.bhinneka.data.repository.FirebaseRepository

class HistoryViewModel : ViewModel() {

    private val repository = FirebaseRepository()
    private lateinit var preferences: PreferencesRepository

    fun init(context: Context) {
        preferences = PreferencesRepository.getInstance(context)
    }

    fun getJajanan(name: String): LiveData<Jajanan?> {
        return repository.getJajanan(name)
    }

    fun getResult(): Float {
        return preferences.readResult()
    }
}