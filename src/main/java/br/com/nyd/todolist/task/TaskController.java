package br.com.nyd.todolist.task;

import br.com.nyd.todolist.enums.Priority;
import br.com.nyd.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository repository;

    @GetMapping
    public ResponseEntity listAllById(HttpServletRequest request){
        return  ResponseEntity.ok().body(repository.findByUserId((UUID) request.getAttribute("userId")));
    }

    @PostMapping
    public ResponseEntity create (@RequestBody TaskModel taskModel, HttpServletRequest request){
        var userId = request.getAttribute("userId");
        var task = new TaskModel();
        var currentDate = LocalDateTime.now();
        switch (taskModel.getPriority()) {
            case "0":
                task.setPriority(String.valueOf(Priority.URGENT));
                break;
            case "1":
                task.setPriority(String.valueOf(Priority.HIGH));
                break;
            case "2":
                task.setPriority(String.valueOf(Priority.MEDIUM));
                break;
            case "3":
                task.setPriority(String.valueOf(Priority.LOW));
                break;
        }
        if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt()) ){
            return ResponseEntity.badRequest().body("The start date and end date must be before today");
        }
        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.badRequest().body("The start date must be before the end date ");
        }
        taskModel.setPriority(task.getPriority());
        taskModel.setUserId((UUID) userId);
        return ResponseEntity.ok().body(repository.save(taskModel));
    }

    @PutMapping("/{id}")
    public ResponseEntity update (@PathVariable UUID id, @RequestBody TaskModel taskModel, HttpServletRequest request){
        var currentTime = LocalDateTime.now();
        var task = repository.findById(id).orElse(null);
        var userId = request.getAttribute("userId");

        if(task == null){
            return  ResponseEntity.badRequest().body("Task not found.");
        }
        if(!task.getUserId().equals(userId)){
            return  ResponseEntity.badRequest().body(" The user does not have permission to change this task.");
        }
        Utils.CopyNotNullProperties(taskModel,task);
        task.setUpdatedAt(currentTime);
        return ResponseEntity.ok().body(repository.save(task));
    }
}


