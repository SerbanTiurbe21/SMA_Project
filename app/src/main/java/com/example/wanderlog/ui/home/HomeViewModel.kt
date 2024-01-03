package com.example.wanderlog.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wanderlog.database.models.Trip

class HomeViewModel : ViewModel() {

    private val _trips = MutableLiveData<Set<Trip>>()
    val trips: LiveData<Set<Trip>> = _trips
}