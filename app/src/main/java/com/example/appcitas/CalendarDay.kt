package com.example.appcitas

import java.time.LocalDate

data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val hasAppointments: Boolean = false,
    val isSelectable: Boolean = true
)