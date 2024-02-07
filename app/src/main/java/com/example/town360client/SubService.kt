package com.example.town360client


data class SubService(
    val name: String = "",
    val image: String = ""
) {
    // Add a no-argument constructor for Firebase
    constructor() : this("", "")
}
