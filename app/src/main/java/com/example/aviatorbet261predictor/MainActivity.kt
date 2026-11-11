
package com.example.aviatorbet261predictor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.ValueEventListener

private val Dark = Color(0xFF101014)
private val Red = Color(0xFFED174C)
private val CardColor = Color(0xFF202027)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AviatorApp()
        }
    }
}

@Composable
fun AviatorApp() {

    var selectedSite by remember { mutableStateOf("") }
    var roomCode by remember { mutableStateOf("") }
    var deviceCount by remember { mutableIntStateOf(0) }
    var latestResult by remember { mutableStateOf("--.--x") }
    var message by remember { mutableStateOf("Ready") }
    var connected by remember { mutableStateOf(false) }
    var multiplierInput by remember { mutableStateOf("") }

    var deviceListener by remember {
        mutableStateOf<ValueEventListener?>(null)
    }

    var resultListener by remember {
        mutableStateOf<ValueEventListener?>(null)
    }

    val database = remember {
        com.google.firebase.database.FirebaseDatabase
            .getInstance(
                "https://aviatorbet261predictor-default-rtdb.firebaseio.com/"
            ).reference
    }

    DisposableEffect(selectedSite, roomCode, connected) {
        if (connected && selectedSite.isNotEmpty() && roomCode.isNotEmpty()) {

            deviceListener = DeviceManager.listenDevices(
                selectedSite,
                roomCode
            ) {
                deviceCount = it
            }

            resultListener = FirebaseManager.listenResults(
                selectedSite,
                roomCode
            ) {
                latestResult = String.format("%.2fx", it)
            }
        }

        onDispose {
            deviceListener?.let {
                database.child("rooms")
                    .child(selectedSite.lowercase())
                    .child(roomCode)
                    .child("devices")
                    .removeEventListener(it)
            }

            resultListener?.let {
                database.child("rooms")
                    .child(selectedSite.lowercase())
                    .child(roomCode)
                    .child("latestResult")
                    .removeEventListener(it)
            }
        }
    }

    MaterialTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Dark
        ) {

            if (selectedSite.isEmpty()) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text("✈", color = Red, fontSize = 65.sp)

                    Text(
                        "AVIATOR PREDICTOR",
                        color = Red,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Black
                    )

                    Text(
                        "DUAL PLATFORM ANALYZER",
                        color = Color.LightGray
                    )

                    Spacer(Modifier.height(40.dp))

                    Button(
                        onClick = { selectedSite = "BET261" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("AVIATOR BET261")
                    }

                    Spacer(Modifier.height(15.dp))

                    Button(
                        onClick = { selectedSite = "1WIN" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("AVIATOR 1WIN")
                    }
                }

            } else {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(Modifier.height(25.dp))

                    Text(
                        "$selectedSite AVIATOR",
                        color = Red,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(Modifier.height(25.dp))

                    Text(
                        latestResult,
                        color = Color.White,
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "LAST RECEIVED RESULT",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )

                    Spacer(Modifier.height(25.dp))

                    Text(
                        "ROOM CONNECTION",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = roomCode,
                        onValueChange = {
                            roomCode = it.uppercase().take(6)
                        },
                        label = { Text("Room Code") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {

                            message = "Connecting..."

                            AuthManager.signIn { success, error ->

                                if (!success) {
                                    message = error ?: "Authentication failed"
                                    return@signIn
                                }

                                RoomManager.createRoom(selectedSite) {
                                    code, roomError ->

                                    if (code == null) {
                                        message = roomError ?: "Room creation failed"
                                    } else {

                                        roomCode = code

                                        RoomManager.joinRoom(
                                            selectedSite,
                                            code
                                        ) { joined, joinError ->

                                            if (joined) {
                                                DeviceManager.registerDevice(
                                                    selectedSite,
                                                    code,
                                                    "Android device"
                                                ) { registered, deviceError ->

                                                    connected = registered
                                                    message = if (registered) {
                                                        "ROOM CREATED: $code"
                                                    } else {
                                                        deviceError ?: "Device registration failed"
                                                    }
                                                }
                                            } else {
                                                message = joinError ?: "Join failed"
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF16A34A)
                        )
                    ) {
                        Text("CREATE ROOM")
                    }

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = {

                            if (roomCode.length != 6) {
                                message = "Enter a valid 6-character code"
                            } else {

                                message = "Joining room..."

                                AuthManager.signIn { success, error ->

                                    if (!success) {
                                        message = error ?: "Authentication failed"
                                        return@signIn
                                    }

                                    RoomManager.joinRoom(
                                        selectedSite,
                                        roomCode
                                    ) { joined, joinError ->

                                        if (!joined) {
                                            message = joinError ?: "Room not found"
                                        } else {

                                            DeviceManager.registerDevice(
                                                selectedSite,
                                                roomCode,
                                                "Android device"
                                            ) { registered, deviceError ->

                                                connected = registered
                                                message = if (registered) {
                                                    "ROOM CONNECTED"
                                                } else {
                                                    deviceError ?: "Device registration failed"
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("JOIN ROOM")
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "CONNECTED DEVICES: $deviceCount",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        message,
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(20.dp))

                    OutlinedTextField(
                        value = multiplierInput,
                        onValueChange = {
                            multiplierInput = it
                        },
                        label = { Text("Observed multiplier (e.g. 2.45)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            val value = multiplierInput
                                .replace(',', '.')
                                .toDoubleOrNull()

                            if (!connected) {
                                message = "Connect to a room first"
                            } else if (value == null || !value.isFinite() || value < 1.0) {
                                message = "Enter a valid multiplier"
                            } else {
                                FirebaseManager.sendResult(
                                    selectedSite,
                                    roomCode,
                                    value
                                )
                                message = "Observed result sent"
                                multiplierInput = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red
                        )
                    ) {
                        Text("SEND OBSERVED RESULT")
                    }

                    Spacer(Modifier.weight(1f))

                    TextButton(
                        onClick = {
                            DeviceManager.removeDevice(
                                selectedSite,
                                roomCode
                            )
                            connected = false
                            selectedSite = ""
                            roomCode = ""
                            deviceCount = 0
                            latestResult = "--.--x"
                            message = "Disconnected"
                        }
                    ) {
                        Text(
                            "BACK TO HOME",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
