package com.cyberfox21.tinkoffmessanger.presentation.util

import java.util.*

interface DateFormatter {

    fun utcToDate(utc: Int): Date

    fun isTheSameDay(date1: Date, date2: Date): Boolean

    fun getDateForChatItem(date: Date): String

    fun getTimeForMessage(date: Date): String
}