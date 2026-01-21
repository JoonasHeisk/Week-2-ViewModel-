package com.example.viikkotehtava2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.viikkotehtava2.domain.Task
import com.example.viikkotehtava2.domain.mockTasks
import java.time.LocalDate

class TaskViewModel : ViewModel() {

    private val _allTasks = mutableStateListOf<Task>()
    var tasks by mutableStateOf(listOf<Task>())
        private set

    private var sortAscending = true
    private var filterDoneState: Boolean? = null

    init {
        _allTasks.addAll(mockTasks)
        refreshTasks()
    }

    fun addTask(task: Task) {
        _allTasks.add(task)
        refreshTasks()
    }

    fun toggleDone(id: Int) {
        val index = _allTasks.indexOfFirst { it.id == id }
        if (index >= 0) {
            val old = _allTasks[index]
            _allTasks[index] = old.copy(done = !old.done)
        }
        refreshTasks()
    }

    fun removeTask(id: Int) {
        _allTasks.removeAll { it.id == id }
        refreshTasks()
    }

    fun sortByDueDate() {
        sortAscending = !sortAscending
        refreshTasks()
    }

    fun filterByDoneToggle() {
        filterDoneState = when(filterDoneState) {
            null -> true       // done
            true -> false      // ei done
            false -> null      // molemmat
        }
        refreshTasks()
    }

    fun showAll() {
        filterDoneState = null
        refreshTasks()
    }

    private fun refreshTasks() {
        var updated = _allTasks.toList()

        filterDoneState?.let { updated = updated.filter { it.done == filterDoneState } }

        updated = if (sortAscending) updated.sortedBy { it.dueDate }
        else updated.sortedByDescending { it.dueDate }

        tasks = updated
    }
}
