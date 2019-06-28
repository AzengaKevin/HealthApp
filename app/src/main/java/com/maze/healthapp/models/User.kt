package com.maze.healthapp.models

import java.io.Serializable

data class User(val name : String, val contact: String, val dob : String, var location: String) : Serializable