package com.example.viikkotehtava2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import com.example.viikkotehtava2.domain.Task
import com.example.viikkotehtava2.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(taskViewModel: TaskViewModel = viewModel()) {
    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newPriority by remember { mutableStateOf(1) }
    var newDueDateText by remember { mutableStateOf("") }

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Tehtävälista",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )


        Spacer(modifier = Modifier.height(16.dp))

        Column {
            OutlinedTextField(
                value = newTitle,
                onValueChange = { newTitle = it },
                placeholder = { Text("Otsikko", style = MaterialTheme.typography.bodyMedium) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Default),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = newDescription,
                onValueChange = { newDescription = it },
                placeholder = { Text("Kuvaus", style = MaterialTheme.typography.bodyMedium) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Default),
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newDueDateText,
                    onValueChange = { newDueDateText = it },
                    placeholder = { Text("dd.MM.yyyy", style = MaterialTheme.typography.bodyMedium) },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    singleLine = true,
                    modifier = Modifier.width(120.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = {
                    if (newTitle.isNotBlank() && newDueDateText.isNotBlank()) {
                        val parsedDate = try {
                            LocalDate.parse(newDueDateText, formatter)
                        } catch (e: Exception) {
                            LocalDate.now()
                        }

                        val newTask = Task(
                            id = (taskViewModel.tasks.maxOfOrNull { it.id } ?: 0) + 1,
                            title = newTitle,
                            description = newDescription,
                            priority = newPriority,
                            dueDate = parsedDate,
                            done = false
                        )

                        taskViewModel.addTask(newTask)
                        newTitle = ""
                        newDescription = ""
                        newDueDateText = ""
                    }
                }) {
                    Text("Lisää")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // sort ja filter napit
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { taskViewModel.sortByDueDate() }) {
                Text("Sort by Due Date")
            }

            Button(onClick = { taskViewModel.filterByDoneToggle() }) {
                Text("Filter by Done")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(taskViewModel.tasks) { task ->
                TaskRow(
                    task = task,
                    onToggleDone = { taskViewModel.toggleDone(it) },
                    onRemove = { taskViewModel.removeTask(it) }
                )
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun TaskRow(
    task: Task,
    onToggleDone: (Int) -> Unit,
    onRemove: (Int) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = task.done,
                onCheckedChange = { onToggleDone(task.id) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = task.title, style = MaterialTheme.typography.bodyLarge)
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }
                Text(
                    text = "Due: ${task.dueDate.format(formatter)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        Button(onClick = { onRemove(task.id) }) {
            Text("Poista")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
