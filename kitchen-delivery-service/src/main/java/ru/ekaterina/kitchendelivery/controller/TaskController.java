package ru.ekaterina.kitchendelivery.controller;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/group/{candidateGroup}")
    public ResponseEntity<List<Map<String, String>>> getTasksByGroup(@PathVariable String candidateGroup) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup(candidateGroup)
                .list();

        List<Map<String, String> > result = tasks.stream()
                .map(t -> {
                    Map<String, String> m = new HashMap<>();
                    m.put("taskId", t.getId());
                    m.put("name", t.getName() != null ? t.getName() : "");
                    Object orderId = taskService.getVariable(t.getId(), "orderId");
                    m.put("orderId", orderId != null ? String.valueOf(orderId) : "");
                    return m;
                })
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<?> completeTask(@PathVariable String taskId) {
        if (taskId == null || taskId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "taskId is empty",
                    "hint", "GET /api/tasks/group/ROLE_RESTAURANT или ROLE_COURIER, необходимо подставить taskId"
            ));
        }

        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "Task not found: " + taskId,
                    "hint", "Задача уже завершена или id неверный"
            ));
        }

        String name = task.getName();
        taskService.complete(taskId);

        return ResponseEntity.ok(Map.of(
                "completed", true,
                "taskId", taskId,
                "name", name != null ? name : ""
        ));
    }
}
