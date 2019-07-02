package com.maze.healthapp.models

import java.io.Serializable

class Chat(val sender: String, val reciver: String, val msg: String) : Serializable {
    constructor() : this("", "", "")
}