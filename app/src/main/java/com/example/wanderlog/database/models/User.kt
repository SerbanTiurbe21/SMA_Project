package com.example.wanderlog.database.models

data class User (
    var id: String,
    var email: String,
    var password: String,
    var trips: Set<Trip>
){
    constructor(email: String, password: String, trips: Set<Trip>) : this("", email, password, trips)
}