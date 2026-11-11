
package com.example.aviatorbet261predictor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Dark
    ) {
        if (selectedSite.isEmpty()) {
            HomeScreen(
                onSelect = { selectedSite = it }
            )
        } else {
            PredictorScreen(
                site = selectedSite,
                onBack = { selectedSite = "" }
            )
        }
    }
}

@Composable
fun HomeScreen(onSelect: (String) -> Unit) {
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
            fontSize = 26.sp,
            fontWeight = FontWeight.Black
        )

        Text(
            "DUAL PLATFORM ANALYZER",
            color = Color.LightGray,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(45.dp))

        PlatformCard(
            title = "AVIATOR BET261",
            subtitle = "Predictor pour Bet261",
            onClick = { onSelect("BET261") }
        )

        Spacer(Modifier.height(20.dp))

        PlatformCard(
            title = "AVIATOR 1WIN",
            subtitle = "Predictor pour 1Win",
            onClick = { onSelect("1WIN") }
        )

        Spacer(Modifier.height(45.dp))

        Text(
            "LIVE DATA • RNG • STATISTICS",
            color = Color.Gray,
            fontSize = 11.sp
        )
    }
}

@Composable
fun PlatformCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardColor, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(22.dp)
    ) {
        Text(
            title,
            color = Color.White,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(7.dp))

        Text(subtitle, color = Color.LightGray)

        Spacer(Modifier.height(15.dp))

        Text(
            "OPEN PREDICTOR  →",
            color = Red,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PredictorScreen(
    site: String,
    onBack: () -> Unit
) {
    var status by remember {
        mutableStateOf("NOT CONNECTED")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "✈",
            color = Red,
            fontSize = 65.sp
        )

        Text(
            "$site AVIATOR PREDICTOR",
            color = Red,
            fontSize = 23.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(Modifier.height(35.dp))

        Text(
            "--.--x",
            color = Color.White,
            fontSize = 55.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            status,
            color = Color.LightGray
        )

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = {
                status = "WAITING FOR DEVICE PAIRING"
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF16A34A)
            )
        ) {
            Text("CONNECT")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                status = "WAITING FOR REAL GAME DATA"
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Red
            )
        ) {
            Text("NEXT PREDICTION")
        }

        Spacer(Modifier.height(30.dp))

        Text(
            "RNG • HISTORY • STATISTICS",
            color = Color.Gray,
            fontSize = 11.sp
        )

        TextButton(onClick = onBack) {
            Text("← BACK TO HOME", color = Color.White)
        }
    }
}
