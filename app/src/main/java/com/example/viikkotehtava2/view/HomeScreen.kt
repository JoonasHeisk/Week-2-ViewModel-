package com.example.viikkotehtava2.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viikkotehtava2.model.Task
import com.example.viikkotehtava2.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(taskViewModel: TaskViewModel = viewModel()) {

    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newPriority by remember { mutableStateOf(1) }
    var newDueDateText by remember { mutableStateOf("") }
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    val tasks by taskViewModel.tasks.collectAsState()
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

        // --- Lisää tehtävä ---
        Column {
            OutlinedTextField(
                value = newTitle,
                onValueChange = { newTitle = it },
                placeholder = { Text("Otsikko") },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Default),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = newDescription,
                onValueChange = { newDescription = it },
                placeholder = { Text("Kuvaus") },
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
                    placeholder = { Text("dd.MM.yyyy") },
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
                            id = (tasks.maxOfOrNull { it.id } ?: 0) + 1,
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

        // --- Sort / Filter napit ---
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { taskViewModel.sortByDueDate() }) {
                Text("Suodata pvm")
            }
            Button(onClick = { taskViewModel.filterByDoneToggle() }) {
                Text("Suodata tehty")
            }
        }

        // --- Tehtävälista ---
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(tasks) { task ->
                TaskRow(
                    task = task,
                    onToggleDone = { taskViewModel.toggleDone(it) },
                    onEdit = { selectedTask = it },
                    onRemove = { taskViewModel.removeTask(it) }
                )
                Divider(color = Color.Gray, thickness = 1.dp)
            }
        }
    }

    // --- DetailScreen dialogi ---
    selectedTask?.let { task ->
        DetailScreen(
            task = task,
            onDismiss = { selectedTask = null },
            onSave = { updatedTask ->
                taskViewModel.updateTask(updatedTask)
                selectedTask = null
            },
            onRemove = {
                taskViewModel.removeTask(it.id)
                selectedTask = null
            }
        )
    }
}

@Composable
fun TaskRow(
    task: Task,
    onToggleDone: (Int) -> Unit,
    onEdit: (Task) -> Unit,
    onRemove: (Int) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onEdit(task) },
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

@Composable
fun DetailScreen(
    task: Task,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit,
    onRemove: (Task) -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    var dueDateText by remember { mutableStateOf(task.dueDate.format(formatter)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Muokkaa tehtävää") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Otsikko") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Kuvaus") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = dueDateText,
                    onValueChange = { dueDateText = it },
                    label = { Text("Suorituspäivä (dd.MM.yyyy)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val parsedDate = try {
                    LocalDate.parse(dueDateText, formatter)
                } catch (e: Exception) {
                    task.dueDate
                }
                onSave(task.copy(title = title, description = description, dueDate = parsedDate))
            }) {
                Text("Tallenna")
            }
        },
        dismissButton = {
            Button(onClick = { onRemove(task) }) {
                Text("Poista")
            }
        }
    )
}
