package eu.pibu.vtasker.tasks.boundary.dto;

import eu.pibu.vtasker.tasks.entity.Task;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TaskResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final LocalDateTime createdAt;
    private final List<AttachmentResponse> attachments;

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.createdAt = task.getCreatedAt();
        this.attachments = task.getAttachments().stream().map(AttachmentResponse::new).collect(Collectors.toList());
    }
}