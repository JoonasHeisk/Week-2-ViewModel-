package com.example.viikkotehtava2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.viikkotehtava2.model.Task
import com.example.viikkotehtava2.model.mockTasks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TaskViewModel : ViewModel() {

    private val _allTasks = mutableListOf<Task>()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    // Sort ja filter tilat
    private var sortAscending = true
    private var filterDoneState: Boolean? = null

    init {
        _allTasks.addAll(mockTasks)
        refreshTasks()
    }

    // Lisää uusi tehtävä
    fun addTask(task: Task) {
        _allTasks.add(task)
        refreshTasks()
    }

    // Toggle tehtävän valmis-tila
    fun toggleDone(id: Int) {
        val index = _allTasks.indexOfFirst { it.id == id }
        if (index >= 0) {
            val old = _allTasks[index]
            _allTasks[index] = old.copy(done = !old.done)
            refreshTasks()
        }
    }

    // Poista tehtävä
    fun removeTask(id: Int) {
        _allTasks.removeAll { it.id == id }
        refreshTasks()
    }

    // Päivitä tehtävä
    fun updateTask(updatedTask: Task) {
        val index = _allTasks.indexOfFirst { it.id == updatedTask.id }
        if (index >= 0) {
            _allTasks[index] = updatedTask
            refreshTasks()
        }
    }

    // Vaihda sort järjestys
    fun sortByDueDate() {
        sortAscending = !sortAscending
        refreshTasks()
    }

    // Filter by done
    fun filterByDoneToggle() {
        filterDoneState = when(filterDoneState) {
            null -> true      // näytä vain tehdyt
            true -> false     // näytä vain tekemättömät
            false -> null     // näytä kaikki
        }
        refreshTasks()
    }

    fun showAll() {
        filterDoneState = null
        refreshTasks()
    }

    private fun refreshTasks() {
        var updated = _allTasks.toList() // tee kopio

        // Filter
        filterDoneState?.let { state ->
            updated = updated.filter { it.done == state }
        }

        // Sort
        updated = if (sortAscending) updated.sortedBy { it.dueDate }
        else updated.sortedByDescending { it.dueDate }

        _tasks.value = updated
    }
}
