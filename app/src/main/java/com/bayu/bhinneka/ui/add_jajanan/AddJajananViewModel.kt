package com.bayu.bhinneka.ui.add_jajanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.repository.Repository

class AddJajananViewModel(): ViewModel() {

    private val repository = Repository()

    fun addNewJajanan(jajanan: Jajanan) {
        return repository.addNewJajanan(jajanan)
    }
}