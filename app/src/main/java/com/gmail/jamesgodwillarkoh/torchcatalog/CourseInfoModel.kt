package com.gmail.jamesgodwillarkoh.torchcatalog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CourseInfoModel: ViewModel() {

    val students:MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }

    val sessions:MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }


}