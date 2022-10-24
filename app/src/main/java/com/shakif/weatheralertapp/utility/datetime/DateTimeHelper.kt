package com.shakif.weatheralertapp.utility.datetime

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(format: String, locale: Locale = Locale.ENGLISH): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun formatStringToDate(date: String, format: String): Date {
    var selectDate: Date = Date()
    try {
        selectDate = SimpleDateFormat(format, Locale.ENGLISH).parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return selectDate
}

fun formatDateWithPattern(date: String, currentFormat: String = TimeFormat.TIME_FORMAT_SHORT, expectedFormat: String): String {
    val parser = SimpleDateFormat(currentFormat, Locale.ENGLISH)
    val formatter = SimpleDateFormat(expectedFormat, Locale.ENGLISH)
    var dateTime = ""
    try {
        dateTime = formatter.format(parser.parse(date))
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return dateTime
}

fun getMonth(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.MONTH)
}

fun getDay(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun getYear(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.YEAR)
}

fun differenceInDays(startDate: Date, endDate: Date): Long {
    return (endDate.time - startDate.time) / (1000 * 60 * 60 * 24)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun getTwelveHourFromTime(date: Date = Calendar.getInstance().time): Long {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.HOUR, 12)
    return calendar.timeInMillis
}

fun getFiveMinutesBeforeTime(date: Date = Calendar.getInstance().time): Long {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.MINUTE, -5)
    return calendar.timeInMillis
}

fun getDateOfDifference(date: Date = Calendar.getInstance().time, difference: Int): Date {
    val calender = Calendar.getInstance()
    calender.time = date
    calender.add(Calendar.DAY_OF_YEAR, difference)
    return calender.time
}

fun getDateStringUTC(format: String, dateString: String): String {
    val df = SimpleDateFormat(format, Locale.ENGLISH)
    df.timeZone = TimeZone.getDefault()
    val outputDateFormat = SimpleDateFormat(format, Locale.getDefault())
    outputDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = df.parse(dateString)
    return date?.let { outputDateFormat.format(date) } ?: ""
}

fun getDateStringUTC(currentFormat: String = TimeFormat.YYYY_MM_DD, expectedFormat: String, dateString: String): String? {
    val parser = SimpleDateFormat(currentFormat, Locale.ENGLISH)
    val formatter = SimpleDateFormat(expectedFormat, Locale.ENGLISH)
    var dateTime: String? = ""

    try {
        val date = parser.parse(dateString)
        date?.let { dateTime = formatter.format(it) }
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return dateTime
}

fun getLastDateOfMonth(date: Date): Date {
    val calender = Calendar.getInstance()
    calender.time = date
    calender.set(Calendar.DATE, calender.getActualMaximum(Calendar.DATE))
    return calender.time
}