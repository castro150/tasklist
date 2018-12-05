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

        if(task.getImage() != null) fileStorageService.deleteFile(task.getImage());

        taskRepository.delete(task);
    }

    public void finalizeTask(Long taskId, Double latitude, Double longitude, MultipartFile file) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        task.setPending(false);
        task.setLatitude(latitude);
        task.setLongitude(longitude);
        String fileName = fileStorageService.storeFile(file);
        task.setImage(fileName);

        taskRepository.save(task);
    }
    
    public List<Task> getTasksByPending(Boolean pending) {
        return taskRepository.findByPending(pending);
    }
}