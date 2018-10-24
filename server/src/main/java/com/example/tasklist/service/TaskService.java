package com.example.tasklist.service;

import java.util.List;

import com.example.tasklist.exception.ResourceNotFoundException;
import com.example.tasklist.models.Task;
import com.example.tasklist.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    FileStorageService fileStorageService;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
    }

    public Task updateTask(Long taskId, Task taskDetails) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        task.setName(taskDetails.getName());
        task.setPending(taskDetails.getPending());
        task.setImage(taskDetails.getImage());

        Task updatedTask = taskRepository.save(task);
        return updatedTask;
    }

    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        // TODO remove file if needed

        taskRepository.delete(task);
    }

    public void finalizeTask(Long taskId, MultipartFile file) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        task.setPending(false);
        String fileName = fileStorageService.storeFile(file);
        task.setImage(fileName);

        taskRepository.save(task);
    }
}