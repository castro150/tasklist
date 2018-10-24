package com.example.tasklist.controller;

import java.util.List;

import javax.validation.Valid;

import com.example.tasklist.exception.ResourceNotFoundException;
import com.example.tasklist.models.Task;
import com.example.tasklist.repository.TaskRepository;
import com.example.tasklist.service.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    FileStorageService fileStorageService;

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
        task.setPending(taskDetails.getPending());
        task.setImage(taskDetails.getImage());

        Task updatedTask = taskRepository.save(task);
        return updatedTask;
    }

    // Delete a Task
    @RequestMapping(value="/tasks/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteTask(@PathVariable(value = "id") Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        // TODO remove file if needed

        taskRepository.delete(task);
        return ResponseEntity.ok().build();
    }

    // Finalize a Task
    @RequestMapping(value="/tasks/finalize/{id}", method=RequestMethod.PUT)
    public ResponseEntity<?> finalizeTask(@PathVariable(value = "id") Long taskId, @RequestParam("file") MultipartFile file) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        task.setPending(false);
        String fileName = fileStorageService.storeFile(file);
        task.setImage(fileName);

        taskRepository.save(task);
        return ResponseEntity.ok().build();
    }
}