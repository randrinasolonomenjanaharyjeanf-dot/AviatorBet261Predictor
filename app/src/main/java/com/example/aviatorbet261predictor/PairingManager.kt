
package com.example.aviatorbet261predictor

import java.security.SecureRandom

object PairingManager {

    private const val CODE_LENGTH = 6
    private const val CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"

    fun generateRoomCode(): String {
        val random = SecureRandom()

        return buildString {
            repeat(CODE_LENGTH) {
                val index = random.nextInt(CHARACTERS.length)
                append(CHARACTERS[index])
            }
        }
    }

    fun isValidRoomCode(code: String): Boolean {
        return code.length == CODE_LENGTH &&
            code.all { it in CHARACTERS }
    }
}
