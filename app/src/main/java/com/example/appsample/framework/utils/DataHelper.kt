package com.example.appsample.framework.utils

import java.text.SimpleDateFormat
import java.util.*


object DataHelper {

    fun getData(): String {
        val calendar: Calendar = Calendar.getInstance()
        val simpledateformat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

        return simpledateformat.format(calendar.time)
    }
}