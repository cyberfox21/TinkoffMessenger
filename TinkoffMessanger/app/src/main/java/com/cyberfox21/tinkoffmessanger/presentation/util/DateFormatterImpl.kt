package com.cyberfox21.tinkoffmessanger.presentation.util

import java.text.SimpleDateFormat
import java.util.*

class DateFormatterImpl : DateFormatter {

    override fun utcToDate(utc: Int) = Date(utc.toLong() * 1000)

    override fun isTheSameDay(date1: Date, date2: Date): Boolean {
        val dateObj1 = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date1)
        val dateObj2 = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date2)
        return dateObj1.equals(dateObj2)
    }

    override fun getDateForChatItem(date: Date): String {
        return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
    }

    override fun getTimeForMessage(date: Date): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
    }
}