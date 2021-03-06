package com.example.tasklist.controller;

import java.util.List;

import javax.validation.Valid;

import com.example.tasklist.models.Task;
import com.example.tasklist.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskService taskService;

    // Get All Tasks
    @RequestMapping(value="/tasks", method=RequestMethod.GET)
    public List<Task> getAllTasks(@RequestParam(name = "pending", required = false) Boolean pending) {
        return pending != null ? taskService.getTasksByPending(pending) : taskService.getAllTasks();
    }

    // Create a new Tasks
    @RequestMapping(value="/tasks", method=RequestMethod.POST)
    public Task createTask(@Valid @RequestBody Task task) {
        return taskService.createTask(task);
    }

    // Get a Single Tasks
    @RequestMapping(value="/tasks/{id}", method=RequestMethod.GET)
    public Task getTaskById(@PathVariable(value = "id") Long taskId) {
        return taskService.getTaskById(taskId);
    }

    // Update a Tasks
    @RequestMapping(value="/tasks/{id}", method=RequestMethod.PUT)
    public Task updateTask(@PathVariable(value = "id") Long taskId, @Valid @RequestBody Task taskDetails) {
        return taskService.updateTask(taskId, taskDetails);
    }

    // Delete a Task
    @RequestMapping(value="/tasks/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteTask(@PathVariable(value = "id") Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }

    // Finalize a Task
    @RequestMapping(value="/tasks/finalize/{id}", method=RequestMethod.PUT)
    public ResponseEntity<?> finalizeTask(@PathVariable(value = "id") Long taskId, @RequestParam("file") MultipartFile file,
                                          @RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude) {
        taskService.finalizeTask(taskId, latitude, longitude, file);
        return ResponseEntity.ok().build();
    }
}