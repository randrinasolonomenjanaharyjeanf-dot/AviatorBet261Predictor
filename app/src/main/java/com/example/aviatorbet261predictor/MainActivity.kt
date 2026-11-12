
package com.example.aviatorbet261predictor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import java.util.Locale

private val Dark = Color(0xFF101014)
private val Red = Color(0xFFED174C)
private val CardColor = Color(0xFF202027)
private val Green = Color(0xFF16A34A)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val roundViewModel =
            ViewModelProvider(this)[RoundViewModel::class.java]

        setContent {
            MaterialTheme {
                AviatorApp(roundViewModel)
            }
        }
    }
}

@Composable
fun AviatorApp(viewModel: RoundViewModel) {

    var selectedSite by remember {
        mutableStateOf("")
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Dark
    ) {
        if (selectedSite.isEmpty()) {
            HomeScreen {
                selectedSite = it
            }
        } else {
            AnalyzerScreen(
                site = selectedSite,
                viewModel = viewModel,
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
            "AVIATOR ANALYZER",
            color = Red,
            fontSize = 27.sp,
            fontWeight = FontWeight.Black
        )

        Text(
            "HISTORICAL DATA • RNG • STATISTICS",
            color = Color.LightGray,
            fontSize = 11.sp
        )

        Spacer(Modifier.height(45.dp))

        PlatformCard(
            "AVIATOR BET261",
            "Open Bet261 analyzer"
        ) {
            onSelect("BET261")
        }

        Spacer(Modifier.height(18.dp))

        PlatformCard(
            "AVIATOR 1WIN",
            "Open 1Win analyzer"
        ) {
            onSelect("1WIN")
        }
    }
}

@Composable
fun PlatformCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(22.dp)) {
            Text(
                title,
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(7.dp))

            Text(subtitle, color = Color.LightGray)

            Spacer(Modifier.height(12.dp))

            Text(
                "OPEN ANALYZER  →",
                color = Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AnalyzerScreen(
    site: String,
    viewModel: RoundViewModel,
    onBack: () -> Unit
) {
    var input by remember { mutableStateOf("") }
    var analysis by remember {
        mutableStateOf("Ampidiro ny vokatra efa nivoaka.")
    }

    val history by viewModel.rounds.collectAsState()

    LaunchedEffect(site) {
        viewModel.loadRounds(site)
    }

    val values = history.map { it.multiplier }
    val sorted = values.sorted()

    val average = values.average()

    val median = when {
        sorted.isEmpty() -> 0.0
        sorted.size % 2 == 0 ->
            (sorted[sorted.size / 2 - 1] +
             sorted[sorted.size / 2]) / 2.0
        else -> sorted[sorted.size / 2]
    }

    val low = values.count { it < 2.0 }
    val high = values.count { it >= 2.0 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Dark)
            .padding(18.dp)
    ) {

        TextButton(onClick = onBack) {
            Text("← BACK", color = Color.White)
        }

        Text(
            "$site AVIATOR ANALYZER",
            color = Red,
            fontSize = 23.sp,
            fontWeight = FontWeight.Black
        )

        Text(
            "Historical statistics",
            color = Color.LightGray,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(18.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = CardColor
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {

                Text(
                    "ADD REAL ROUND RESULT",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Multiplier (ex: 2.45x)")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Red,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Red,
                        unfocusedLabelColor = Color.LightGray
                    )
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        val value = input.trim()
                            .replace(",", ".")
                            .removeSuffix("x")
                            .toDoubleOrNull()

                        if (
                            value != null &&
                            value.isFinite() &&
                            value >= 1.0
                        ) {
                            viewModel.addRound(site, value)
                            input = ""
                            analysis = "Vokatra nampidirina tao amin'ny historique."
                        } else {
                            analysis =
                                "Ampidiro multiplier valide, ohatra 2.45"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green
                    )
                ) {
                    Text("SAVE ROUND RESULT")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "STATISTICS",
            color = Red,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                "ROUNDS",
                history.size.toString(),
                Modifier.weight(1f)
            )

            StatCard(
                "AVERAGE",
                if (values.isEmpty()) "--"
                else String.format(Locale.US, "%.2fx", average),
                Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                "MEDIAN",
                if (values.isEmpty()) "--"
                else String.format(Locale.US, "%.2fx", median),
                Modifier.weight(1f)
            )

            StatCard(
                "LOW / HIGH",
                "$low / $high",
                Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(14.dp))

        Button(
            onClick = {
                analysis = if (values.isEmpty()) {
                    "Tsy mbola misy historique azo dinihina."
                } else {
                    "Analyse de ${values.size} rounds. " +
                    "Moyenne: ${
                        String.format(Locale.US, "%.2fx", average)
                    }. Médiane: ${
                        String.format(Locale.US, "%.2fx", median)
                    }. Statistiques historiques uniquement."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Red
            )
        ) {
            Text("ANALYZE NEXT")
        }

        Spacer(Modifier.height(8.dp))

        Text(
            analysis,
            color = Color.White,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "ROUND HISTORY",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            TextButton(
                onClick = {
                    viewModel.clearHistory(site)
                    analysis = "Historique voafafa."
                }
            ) {
                Text("CLEAR", color = Red)
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(
                items = history,
                key = { it.id }
            ) { round ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Round #${round.id}",
                        color = Color.LightGray
                    )

                    Text(
                        String.format(
                            Locale.US,
                            "%.2fx",
                            round.multiplier
                        ),
                        color = if (round.multiplier >= 2.0) {
                            Green
                        } else {
                            Color.White
                        },
                        fontWeight = FontWeight.Bold
                    )
                }

                HorizontalDivider(color = CardColor)
            }
        }

        Text(
            "RNG: NEXT ROUND CANNOT BE KNOWN IN ADVANCE",
            color = Color.Gray,
            fontSize = 10.sp
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                title,
                color = Color.LightGray,
                fontSize = 11.sp
            )

            Spacer(Modifier.height(7.dp))

            Text(
                value,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
