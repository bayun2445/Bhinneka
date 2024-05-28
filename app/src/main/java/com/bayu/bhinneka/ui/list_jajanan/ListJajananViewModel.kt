package com.bayu.bhinneka.ui.list_jajanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.repository.Repository

class ListJajananViewModel: ViewModel() {

    private val repository = Repository()

    fun getAllJajanan(): LiveData<List<Jajanan>> {
        return repository.getAllJajanan()
    }
    fun deleteJajanan(jajanan: Jajanan) {
        return repository.deleteJajanan(jajanan)
    }
}