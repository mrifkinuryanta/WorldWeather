package com.mrndevs.worldweather.data.source.local.model

import androidx.annotation.DrawableRes
import com.mrndevs.worldweather.R

enum class WindSpeedUnitEnum(val value: String, @DrawableRes val icon: Int) {
    KPH(value = "km/h", icon = R.drawable.ic_windy_line_24),
    MPH(value = "mph", icon = R.drawable.ic_windy_line_24);

    companion object {
        val windSpeedUnitOption
            get() = entries.map { item ->
                OptionData(label = item.value, value = item)
            }
    }
}