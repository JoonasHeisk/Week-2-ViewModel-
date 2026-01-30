package com.example.viikkotehtava2.model

import java.time.LocalDate

val mockTasks = listOf(
    Task(
        id = 1,
        title = "Kaupassa käynti",
        description = "Osta maitoa ja leipää",
        priority = 1,
        dueDate = LocalDate.now().plusDays(1),
        done = false
    ),
    Task(
        id = 2,
        title = "Koulu",
        description = "Palauta fysiikan tehtävä",
        priority = 3,
        dueDate = LocalDate.now().plusDays(2),
        done = false
    ),
    Task(
        id = 3,
        title = "Siivous",
        description = "Imuroi keittiö ja oma huone",
        priority = 2,
        dueDate = LocalDate.now().plusDays(3),
        done = true
    ),
    Task(
        id = 4,
        title = "Treenit",
        description = "Fifa treenit pleikkarilla",
        priority = 2,
        dueDate = LocalDate.now().plusDays(1),
        done = false
    ),
    Task(
        id = 5,
        title = "Laskut",
        description = "Maksa vuokra ja sähkö",
        priority = 3,
        dueDate = LocalDate.now().minusDays(1),
        done = true
    )
)
