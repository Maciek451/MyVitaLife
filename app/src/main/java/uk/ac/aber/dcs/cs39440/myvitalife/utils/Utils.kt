/**
 * A utility class with static methods for common operations.
 *
 * @author Maciej Traczyk
 */
package uk.ac.aber.dcs.cs39440.myvitalife.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.aber.dcs.cs39440.myvitalife.model.ThemeSettings
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        /**
         * Returns the current date in the format "dd-MM-yyyy".
         *
         * @return The current date as a string.
         */
        fun getCurrentDate(): String {
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd-MM-yyyy")

            return formatter.format(date)
        }

        /**
         * Returns the current time in the format "HH:mm:ss".
         *
         * @return The current time as a string.
         */
        fun getCurrentTime(): String {
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("HH:mm:ss")

            return formatter.format(time)
        }

        /**
         * Converts a date string from "yyyy-MM-dd" format to "dd-MM-yyyy" format.
         *
         * @param inputDate The date string to be formatted.
         * @return The formatted date string.
         */
        fun getFormattedDateString(inputDate: String): String {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputDateFormat = SimpleDateFormat("dd-MM-yyyy")
            //30

            val date: Date = inputDateFormat.parse(inputDate) as Date

            return outputDateFormat.format(date)
        }

        /**
         * Returns the date one day before the given date.
         *
         * @param date The date in "dd-MM-yyyy" format.
         * @return The date one day before the given date as a string in "dd-MM-yyyy" format.
         */
        fun getDateDayBefore(date: String): String {
            return calculateDate(date, -1) ?: ""
        }

        /**
         * Returns the date one day after the given date.
         *
         * @param date The date in "dd-MM-yyyy" format.
         * @return The date one day after the given date as a string in "dd-MM-yyyy" format.
         */
        fun getDateDayAfter(date: String): String {
            return calculateDate(date, 1) ?: ""
        }

        /**
         * Calculates a date by adding or subtracting the given number of days from the input date.
         *
         * @param date The input date in "dd-MM-yyyy" format.
         * @param numOfDays The number of days to add or subtract from the input date.
         * @return The calculated date as a string in "dd-MM-yyyy" format, or null if there is an error.
         */
        private fun calculateDate(date: String, numOfDays: Int): String? {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
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

        /**
         * Draws a circular progress bar using Jetpack Compose.
         *
         * @param currentValue The current progress value.
         * @param maxGoal The maximum goal value.
         * @param fontSize The font size of the progress text.
         * @param radius The radius of the progress bar.
         * @param color The color of the progress bar.
         * @param strokeWidth The stroke width of the progress bar.
         * @param animDuration The duration of the progress animation.
         * @param animDelay The delay before the progress animation starts.
         */
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
            val lightModeText = Color.Black
            val darkModeText = Color.White

            val currentColor = if (ThemeSettings.isDarkTheme) {
                darkModeText
            } else {
                lightModeText
            }

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
                    color = currentColor,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}