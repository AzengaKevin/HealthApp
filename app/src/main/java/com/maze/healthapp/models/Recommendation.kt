package com.maze.healthapp.models

import java.io.Serializable

data class Recommendation(val content: String, val photoUrl: String) : Serializable {

    constructor() : this("", "")
}