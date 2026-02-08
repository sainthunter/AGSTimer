package com.ags.countdown

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Clock
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.createChannel(this)
        AlarmScheduler.scheduleOneDayBefore(this)
        WidgetUpdateScheduler.scheduleNextUpdate(this)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CountdownScreen()
                }
            }
        }
    }
}

@Composable
private fun CountdownScreen(clock: Clock = Clock.systemUTC()) {
    var state by remember { mutableStateOf(CountdownCalculator.calculate(clock)) }

    LaunchedEffect(Unit) {
        while (true) {
            state = CountdownCalculator.calculate(clock)
            delay(1_000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "ags geri sayım",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = if (state.isExpired) {
                "Süre doldu"
            } else {
                "${state.daysRemaining} gün kaldı"
            },
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp)
        )

        if (!state.isExpired) {
            Text(
                text = "${state.hours} saat ${state.minutes} dk ${state.seconds} sn",
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
