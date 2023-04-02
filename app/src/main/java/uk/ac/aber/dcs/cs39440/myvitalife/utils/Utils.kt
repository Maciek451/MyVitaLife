package uk.ac.aber.dcs.cs39440.myvitalife.utils

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun getCurrentDate(): String {
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat("yyyy-MM-dd")

            return formatter.format(date)
        }

        fun getCurrentTime(): String {
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("HH:mm:ss")

            return formatter.format(time)
        }

        fun getDateDayBefore(date: String): String {
            return calculateDate(date, -1) ?: ""
        }

        fun getDateDayAfter(date: String): String {
            return calculateDate(date, 1) ?: ""
        }

        private fun calculateDate(date: String, numOfDays: Int): String? {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            return try {
                val date = dateFormat.parse(date)
                val calendar = Calendar.getInstance()
                if (date != null) {
                    calendar.time = date
                }
                calendar.add(Calendar.DAY_OF_YEAR, numOfDays)
                dateFormat.format(calendar.time)
            } catch (e: Exception) {
                // Handle parsing errors
                null
            }
        }

        @Composable
        fun CircularProgressBar(
            currentValue: Int,
            maxGoal: Int,
            fontSize: TextUnit = 28.sp,
            radius: Dp = 150.dp,
            color: Color = MaterialTheme.colorScheme.tertiaryContainer,
            strokeWidth: Dp = 14.dp,
            animDuration: Int = 100,
            animDelay: Int = 0
        ) {
            var animationPlayed by remember {
                mutableStateOf(false)
            }
            val percentage = currentValue.toFloat() / maxGoal
            val currentPercentage = animateFloatAsState(
                targetValue = if (animationPlayed) percentage else 0f,
                animationSpec = tween(
                    durationMillis = animDuration,
                    delayMillis = animDelay
                )
            )
            LaunchedEffect(key1 = true) {
                animationPlayed = true
            }

            Box(
                modifier = Modifier.size(radius * 2f),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(radius * 2f)) {
                    drawArc(
                        color = color,
                        -90f,
                        360 * currentPercentage.value,
                        useCenter = false,
                        style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = "$currentValue/$maxGoal",
                    color = Color.Black,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}