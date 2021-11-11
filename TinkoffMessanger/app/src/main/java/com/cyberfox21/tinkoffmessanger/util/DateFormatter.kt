package com.cyberfox21.tinkoffmessanger.util

import java.util.Date

object DateFormatter {

    fun utcToDate(utc: Int)= Date(utc.toLong()  * 1000)

}