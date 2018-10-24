package com.example.tasklist.repository;

import java.util.List;

import com.example.tasklist.models.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.pending = ?1")
    List<Task> findByPending(Boolean pending);
}