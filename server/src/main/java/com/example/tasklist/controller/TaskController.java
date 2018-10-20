package com.example.tasklist.controller;

import java.util.List;

import javax.validation.Valid;

import com.example.tasklist.exception.ResourceNotFoundException;
import com.example.tasklist.models.Task;
import com.example.tasklist.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    // Get All Tasks
    @RequestMapping(value="/tasks", method=RequestMethod.GET)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Create a new Tasks
    @RequestMapping(value="/tasks", method=RequestMethod.POST)
    public Task createTask(@Valid @RequestBody Task task) {
        return taskRepository.save(task);
    }

    // Get a Single Tasks
    @RequestMapping(value="/tasks/{id}", method=RequestMethod.GET)
    public Task getTaskById(@PathVariable(value = "id") Long taskId) {
        return taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
    }

    // Update a Tasks
    @RequestMapping(value="/tasks/{id}", method=RequestMethod.PUT)
    public Task updateTask(@PathVariable(value = "id") Long taskId, @Valid @RequestBody Task taskDetails) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        task.setName(taskDetails.getName());

        Task updatedTask = taskRepository.save(task);
        return updatedTask;
    }

    // Delete a Task
    @RequestMapping(value="/tasks/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteTask(@PathVariable(value = "id") Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        taskRepository.delete(task);
        return ResponseEntity.ok().build();
    }
}