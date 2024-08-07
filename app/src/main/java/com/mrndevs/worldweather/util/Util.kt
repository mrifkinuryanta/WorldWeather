package com.mrndevs.worldweather.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import com.mrndevs.worldweather.BuildConfig
import com.mrndevs.worldweather.ui.theme.LocalTheme
import com.mrndevs.worldweather.ui.theme.backgroundDarkGradient
import com.mrndevs.worldweather.ui.theme.backgroundLightGradient
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Util {
    private const val ASSET_FILE_PREFIX = "file:///android_asset/"

    fun Int.getAssetPrefix(isDay: Boolean): String {
        return "${ASSET_FILE_PREFIX}${if (isDay) "day" else "night"}/$this.png"
    }

    fun Double.addDegreeSymbol(): String {
        return "${this.toInt()}\u00B0"
    }

    fun Int.getFormattedTime(timeZone: String): String {
        val instant = Instant.ofEpochSecond(this.toLong())
        val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
            .withZone(ZoneId.of(timeZone))
        return formatter.format(instant)
    }

    fun Int.getFormattedDate(timeZone: String): String {
        val instant = Instant.ofEpochSecond(this.toLong())
        val formatter = DateTimeFormatter.ofPattern("MMM, dd", Locale.ENGLISH)
            .withZone(ZoneId.of(timeZone))
        return formatter.format(instant)
    }

    fun Int.getFormattedDay(timeZone: String): String {
        val instant = Instant.ofEpochSecond(this.toLong())
        val zoneId = ZoneId.of(timeZone)

        val formatter = DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)
            .withZone(ZoneId.of(timeZone))

        val formattedInstant = formatter.format(instant)
        val nowInTimeZone = ZonedDateTime.now(zoneId)
        val formattedCurrent = formatter.format(nowInTimeZone.toInstant())
        val formattedTomorrow = formatter.format(nowInTimeZone.plusDays(1).toInstant())

        return when (formattedInstant) {
            formattedCurrent -> "Today"
            formattedTomorrow -> "Tomorrow"
            else -> formattedInstant
        }
    }

    fun openBrowser(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    fun openPlayStore(context: Context) {
        val appPackageName = BuildConfig.APPLICATION_ID

        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    @Composable
    fun getColorGradient(): Brush {
        val isDark = LocalTheme.current.isDarkTheme
        val darkGradient = backgroundDarkGradient
        val lightGradient = backgroundLightGradient

        val color1 by animateColorAsState(targetValue = if (isDark) darkGradient[0] else lightGradient[0])
        val color2 by animateColorAsState(targetValue = if (isDark) darkGradient[1] else lightGradient[1])
        val color3 by animateColorAsState(targetValue = if (isDark) darkGradient[2] else lightGradient[2])

        return Brush.verticalGradient(listOf(color1, color2, color3))
    }

    fun Context.isInternetAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @Suppress("DEPRECATION")
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                val cap = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
                return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }

            else -> {
                val networks = cm.allNetworks
                for (n in networks) {
                    val nInfo = cm.getNetworkInfo(n)
                    if (nInfo != null && nInfo.isConnected) return true
                }
            }
        }
        return false
    }
}