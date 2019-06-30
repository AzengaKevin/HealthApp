package com.maze.healthapp.models

import java.io.Serializable

data class User(
    val uid: String,
    val role: String,
    val name: String,
    val photoUrl: String,
    val contact: String,
    val dob: String,
    val location: String
) : Serializable{

    constructor() : this("", "", "", "", "", "", "")
}