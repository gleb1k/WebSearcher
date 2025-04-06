package ru.glebik

import java.math.RoundingMode

fun Double.round(decimals: Int = 5): Double {
    return this.toBigDecimal().setScale(decimals, RoundingMode.UP).toDouble()
}