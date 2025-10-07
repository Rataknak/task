package com.rataknak.taskservice.Controller;

import com.rataknak.taskservice.Entity.Task;
import com.rataknak.taskservice.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @PostMapping
    public Task createTask(@RequestParam Long userId, @RequestBody Task task) {
        task.setUserId(userId);
        return taskRepository.save(task);
    }

    @GetMapping("/user/{userId}")
    public List<Task> getTasksByUserId(@PathVariable Long userId) {
        return taskRepository.findAllByUserId(userId);
    }
}