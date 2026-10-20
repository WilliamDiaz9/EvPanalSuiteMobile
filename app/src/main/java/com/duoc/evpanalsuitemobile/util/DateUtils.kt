package com.duoc.evpanalsuitemobile.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun nowString(): String =
    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

