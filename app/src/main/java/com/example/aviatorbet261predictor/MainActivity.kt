
package com.example.aviatorbet261predictor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AviatorPredictorApp()
        }
    }
}

@Composable
fun AviatorPredictorApp() {
    var multiplier by remember { mutableStateOf("--.--x") }
    var status by remember { mutableStateOf("READY TO ANALYZE") }
    var history by remember { mutableStateOf("No results yet") }

    val background = Color(0xFF101014)
    val red = Color(0xFFE91E50)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "AVIATOR",
            color = red,
            fontSize = 34.sp,
            fontWeight = FontWeight.Black
        )

        Text(
            "PREDICTOR V1",
            color = Color.White,
            fontSize = 17.sp,
            letterSpacing = 4.sp
        )

        Spacer(Modifier.height(35.dp))

        Text(
            "ESTIMATED MULTIPLIER",
            color = Color.LightGray,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(12.dp))

        Text(
            multiplier,
            color = red,
            fontSize = 54.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        Text(
            status,
            color = Color(0xFF4CAF50),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = {
                multiplier = "N/A"
                status = "WAITING FOR REAL GAME DATA"
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = red
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                "GET PREDICTION",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(14.dp))

        OutlinedButton(
            onClick = {
                status = "ANALYZER ACTIVATED"
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text("ACTIVATE ANALYZER")
        }

        Spacer(Modifier.height(30.dp))

        Text(
            "LIVE DATA • RNG • BET261",
            color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(12.dp))

        Text(
            history,
            color = Color.LightGray,
            fontSize = 12.sp
        )
    }
}
