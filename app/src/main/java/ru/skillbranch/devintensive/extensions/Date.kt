package ru.skillbranch.devintensive.extensions

import java.lang.IllegalStateException
import java.lang.Math.abs
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR
const val MONTH = 30 * DAY
const val YEAR = 365 * DAY

fun Date.format(pattern: String="HH:mm:ss dd.MM.yy") : String{
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value:Int, units: TimeUnits = TimeUnits.SECOND) : Date{
    var time = this.time

    time += when(units){
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
        else -> throw IllegalStateException("invalidate unit")
    }
    this.time = time
    return this
}

fun Date.humanizeDiff() : String{
    var result = ""

    val milliseconds = Date().time -  this.time
    result = if (milliseconds != 0.toLong())
        Date().difference(milliseconds)
    else "сейчас"

    return result
}

fun Date.difference(value:Long) : String{
    var result = ""

    if(abs(value) < 365*DAY) {
        println("value -> ${value}")
        when (abs(value)) {
            in 0..60 * SECOND -> if (value < 0) {
                result += "через несколько секунд"
            } else {
                result += "несколько секунд назад"
            }
            in 1 * MINUTE until 2 * MINUTE, in 21 * MINUTE until 22 * MINUTE,
            in 31 * MINUTE until 32 * MINUTE, in 41 * MINUTE until 42 * MINUTE,
            in 51 * MINUTE until 52 * MINUTE
            -> if (value < 0) {
                result += "через ${abs(round(value.toDouble() / MINUTE))} минуту"
            } else {
                result += "${abs(round(value.toDouble() / MINUTE))} минуту назад"
            }
            in 2 * MINUTE until 5 * MINUTE, in 22 * MINUTE until 25 * MINUTE,
            in 32 * MINUTE until 35 * MINUTE, in 42 * MINUTE until 45 * MINUTE,
            in 52 * MINUTE until 55 * MINUTE
            -> if (value < 0) {
                result += "через ${abs(round(value.toDouble() / MINUTE))} минуты"
            } else {
                result += "${abs(round(value.toDouble() / MINUTE))} минуты назад"
            }
            in 5 * MINUTE..20 * MINUTE, in 25 * MINUTE until 31 * MINUTE,
            in 35 * MINUTE until 41 * MINUTE, in 45 * MINUTE until 51 * MINUTE,
            in 55 * MINUTE until 60 * MINUTE
            -> if (value < 0) {
                result += "через ${abs(round(value.toDouble() / MINUTE))} минут"
            } else {
                result += "${abs(round(value.toDouble() / MINUTE))} минут назад"
            }

            in 1 * HOUR until 2 * HOUR, in 21 * HOUR until 22 * HOUR -> if (value < 0) {
                result += "через ${abs(round(value.toDouble() / HOUR))} час"
            } else {
                result += "${abs(round(value.toDouble() / HOUR))} час назад"
            }
            in 2 * HOUR until 5 * HOUR, in 22 * HOUR until 24 * HOUR -> if (value < 0) {
                result += "через ${abs(round(value.toDouble() / HOUR))} часа"
            } else {
                result += "${abs(round(value.toDouble() / HOUR))} часа назад"
            }
            in 5 * HOUR until 21 * HOUR -> if (value < 0) {
                result += "через ${abs(round(value.toDouble() / HOUR))} часов"
            } else {
                result += "${abs(round(value.toDouble() / HOUR))} часов назад"
            }

            in 1 * DAY until 2 * DAY, in 21 * DAY until 22 * DAY -> if (value < 0) {
                result += "через ${abs(value) / DAY} день"
            } else {
                result += "${abs(round(value.toDouble() / DAY))} день назад"
            }
            in 2 * DAY until 5 * DAY, in 22 * DAY until 25 * DAY -> if (value < 0) {
                result += "через ${abs(round(value.toDouble() / DAY))} дня"
            } else {
                result += "${abs(round(value.toDouble() / DAY))} дня назад"
            }
            in 5 * DAY until 21 * DAY, in 25 * DAY until 31 * DAY -> if (value < 0) {
                result += "через ${abs(round(value.toDouble() / DAY))} дней"
            } else {
                result += "${abs(round(value.toDouble() / DAY))} дней назад"
            }

            in 31 * DAY until 62 * DAY -> if (value < 0) {
                result += "через ${abs(round(value.toDouble() / 30))} месяц"
            } else {
                result += "${value / DAY} месяц назад"
            }
            in 1 * MONTH until 5 * MONTH -> if (value < 0) {
                result += "через ${abs(round(value.toDouble() / 30))} месяца"
            } else {
                result += "${abs(round(value.toDouble() / 30))} месяца назад"
            }
            in 5 * MONTH until 365 * DAY -> if (value < 0) {
                result += "через ${abs(round(value.toDouble() / 30))} месяцев"
            } else {
                result += "${abs(round(value.toDouble() / 30))} месяцев назад"
            }
        }
    }else{
        if (value < 0) {
            result += "более чем через год"
        } else {
            result += "более года назад"
        }
    }
    return result
}

enum class TimeUnits{
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    fun plural(value: Int) : String {
        var result = "$value "
        var temp_value = value

        if(value > 9) {
            var temp = value.toString()
            temp = temp.substring(temp.length-1)
            try {
                temp_value = temp.toInt()
            } catch (nfe: NumberFormatException) {}
        }

        when (this){
            SECOND -> {
                when(temp_value){
                    1 -> result += "секунду"
                    in 2..4 -> result += "секунды"
                    0, in 5..20 -> result += "секунд"
                }
            }
            MINUTE -> {
                when(temp_value){
                    1 -> result += "минуту"
                    in 2..4 -> result += "минуты"
                    0, in 5..20 -> result += "минут"
                }
            }
            HOUR -> {
                when(temp_value){
                    1 -> result += "час"
                    in 2..4 -> result += "часа"
                    0, in 5..20 -> result += "часов"
                }
            }
            DAY -> {
                when(temp_value){
                    1 -> result += "день"
                    in 2..4 -> result += "дня"
                    0, in 5..20 -> result += "дней"
                }
            }
        }

        return result
    }

}

//TimeUnits.SECOND.plural(1) //1 секунду
//TimeUnits.MINUTE.plural(4) //4 минуты
//TimeUnits.HOUR.plural(19) //19 часов
//TimeUnits.DAY.plural(222) //222 дня