package com.bayu.bhinneka.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.repository.Repository

class MainViewModel: ViewModel() {
    private val repository = Repository()

    fun getAllJajanan(): LiveData<List<Jajanan>> {
        return repository.getAllJajanan()
    }
}