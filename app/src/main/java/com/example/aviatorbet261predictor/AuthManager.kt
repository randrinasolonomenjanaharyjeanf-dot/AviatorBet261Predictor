
package com.example.aviatorbet261predictor

import com.google.firebase.auth.FirebaseAuth

object AuthManager {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun signIn(
        onResult: (Boolean, String?) -> Unit
    ) {
        if (auth.currentUser != null) {
            onResult(true, null)
            return
        }

        auth.signInAnonymously()
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { error ->
                onResult(false, error.message)
            }
    }

    fun currentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun signOut() {
        auth.signOut()
    }
}
