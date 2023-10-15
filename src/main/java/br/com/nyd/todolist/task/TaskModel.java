package br.com.nyd.todolist.task;

import br.com.nyd.todolist.enums.Priority;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private UUID userId;
    private String description;
    @Column(length = 50)
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public void setTitle(String title) throws Exception {
        if (title.length() > 50){
            throw new Exception("The title field must have a maximum of 50 characters.");
        }
        this.title = title;
    }
}
