package com.github.se.stepquest

import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class ConnectionRepository {
    val reference = Firebase.database.getReference("connections")

    fun updateConnection(userId: String, data: ConnectionData) {
        reference.child(userId).setValue(data)
    }

    fun userConnectionReference(userId: String): DatabaseReference {
        return reference.child(userId)
    }
}