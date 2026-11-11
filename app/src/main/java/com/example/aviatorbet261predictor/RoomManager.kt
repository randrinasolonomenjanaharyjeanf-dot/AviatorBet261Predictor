
package com.example.aviatorbet261predictor

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object RoomManager {

    private val database = FirebaseDatabase
        .getInstance(
            "https://aviatorbet261predictor-default-rtdb.firebaseio.com/"
        )
        .reference

    fun createRoom(
        site: String,
        onResult: (String?, String?) -> Unit
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid == null) {
            onResult(null, "Authentication required")
            return
        }

        val code = PairingManager.generateRoomCode()

        val roomData = mapOf(
            "owner" to uid,
            "createdAt" to System.currentTimeMillis(),
            "status" to "WAITING"
        )

        database.child("rooms")
            .child(site.lowercase())
            .child(code)
            .setValue(roomData)
            .addOnSuccessListener {
                database.child("rooms")
                    .child(site.lowercase())
                    .child(code)
                    .child("members")
                    .child(uid)
                    .setValue(true)

                onResult(code, null)
            }
            .addOnFailureListener {
                onResult(null, it.message)
            }
    }

    fun joinRoom(
        site: String,
        code: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid == null) {
            onResult(false, "Authentication required")
            return
        }

        if (!PairingManager.isValidRoomCode(code)) {
            onResult(false, "Invalid room code")
            return
        }

        val room = database.child("rooms")
            .child(site.lowercase())
            .child(code)

        room.get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) {
                    onResult(false, "Room not found")
                } else {
                    room.child("members")
                        .child(uid)
                        .setValue(true)
                        .addOnSuccessListener {
                            onResult(true, null)
                        }
                        .addOnFailureListener {
                            onResult(false, it.message)
                        }
                }
            }
            .addOnFailureListener {
                onResult(false, it.message)
            }
    }
}
