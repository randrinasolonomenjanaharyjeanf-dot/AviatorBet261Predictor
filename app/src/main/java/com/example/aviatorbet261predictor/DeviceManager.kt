
package com.example.aviatorbet261predictor

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

object DeviceManager {

    private val database = FirebaseDatabase
        .getInstance(
            "https://aviatorbet261predictor-default-rtdb.firebaseio.com/"
        )
        .reference

    fun registerDevice(
        site: String,
        roomCode: String,
        deviceName: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid == null) {
            onResult(false, "Authentication required")
            return
        }

        val device = mapOf(
            "name" to deviceName,
            "connectedAt" to System.currentTimeMillis(),
            "online" to true
        )

        database.child("rooms")
            .child(site.lowercase())
            .child(roomCode)
            .child("devices")
            .child(uid)
            .setValue(device)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener {
                onResult(false, it.message)
            }
    }

    fun listenDevices(
        site: String,
        roomCode: String,
        onUpdate: (Int) -> Unit
    ): ValueEventListener {

        val reference = database.child("rooms")
            .child(site.lowercase())
            .child(roomCode)
            .child("devices")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onUpdate(snapshot.childrenCount.toInt())
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        reference.addValueEventListener(listener)
        return listener
    }

    fun removeDevice(
        site: String,
        roomCode: String
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        database.child("rooms")
            .child(site.lowercase())
            .child(roomCode)
            .child("devices")
            .child(uid)
            .removeValue()
    }
}
