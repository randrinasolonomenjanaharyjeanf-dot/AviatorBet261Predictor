
package com.example.aviatorbet261predictor

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseManager {

    private val database =
        FirebaseDatabase.getInstance().reference

    fun connect(
        site: String,
        roomCode: String,
        onConnected: (Boolean) -> Unit
    ) {
        val room = database
            .child("rooms")
            .child(site.lowercase())
            .child(roomCode)

        room.child("status").setValue("CONNECTED")
            .addOnSuccessListener {
                onConnected(true)
            }
            .addOnFailureListener {
                onConnected(false)
            }
    }

    fun sendResult(
        site: String,
        roomCode: String,
        multiplier: Double
    ) {
        val result = mapOf(
            "multiplier" to multiplier,
            "timestamp" to System.currentTimeMillis()
        )

        database
            .child("rooms")
            .child(site.lowercase())
            .child(roomCode)
            .child("latestResult")
            .setValue(result)
    }

    fun listenResults(
        site: String,
        roomCode: String,
        onResult: (Double) -> Unit
    ): ValueEventListener {

        val reference = database
            .child("rooms")
            .child(site.lowercase())
            .child(roomCode)
            .child("latestResult")

        val listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val multiplier =
                    snapshot.child("multiplier")
                        .getValue(Double::class.java)

                if (multiplier != null) {
                    onResult(multiplier)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        reference.addValueEventListener(listener)
        return listener
    }

    fun disconnect(
        site: String,
        roomCode: String
    ) {
        database
            .child("rooms")
            .child(site.lowercase())
            .child(roomCode)
            .child("status")
            .setValue("DISCONNECTED")
    }
}
