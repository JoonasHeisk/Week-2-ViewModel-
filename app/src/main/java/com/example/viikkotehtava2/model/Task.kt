package com.example.viikkotehtava2.model

import java.time.LocalDate

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val priority: Int,
    val dueDate: LocalDate,
    val done: Boolean
)
