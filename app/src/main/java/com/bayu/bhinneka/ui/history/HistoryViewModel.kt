package com.bayu.bhinneka.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.repository.Repository

class HistoryViewModel : ViewModel() {

    private val repository = Repository()

    fun getJajanan(name: String): LiveData<Jajanan?> {
        return repository.getJajanan(name)
    }
}