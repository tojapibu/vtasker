package eu.pibu.vtasker.tasks.boundary;

import eu.pibu.vtasker.exceptions.NotFoundException;
import eu.pibu.vtasker.tasks.boundary.dto.CreateTaskRequest;
import eu.pibu.vtasker.tasks.boundary.dto.TaskResponse;
import eu.pibu.vtasker.tasks.boundary.dto.UpdateTaskRequest;
import eu.pibu.vtasker.tasks.control.StorageService;
import eu.pibu.vtasker.tasks.control.TaskService;
import eu.pibu.vtasker.tasks.entity.Attachment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static java.util.stream.Collectors.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final StorageService storageService;

    @GetMapping
    public ResponseEntity<?> getTasks() {
        log.info("Fetch all tasks");
        return ResponseEntity.ok().body(
                taskService.findAllTasks().stream()
                        .map(TaskResponse::new)
                        .collect(toList())
        );
    }

    @PostMapping
    public ResponseEntity<?> addTask(@RequestBody CreateTaskRequest dto) {
        log.info("Create new task: {}", dto);
        Long taskId = taskService.addTask(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(taskId).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        log.info("Fetch task with id: {}", id);
        try {
            return ResponseEntity.ok().body(new TaskResponse(taskService.getTaskById(id)));
        } catch (NotFoundException e) {
            log.error("Unable to fetch a task", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest dto) {
        log.info("Update task with id: {}", id);
        try {
            taskService.updateTask(id, dto);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            log.error("Unable to update a task", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        log.info("Delete task with id: {}", id);
        try {
            taskService.deleteTaskById(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            log.error("Unable to delete a task", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping(path = "/{id}/attachments")
    public ResponseEntity<?> getAttachments(@PathVariable Long id) {
        log.info("List all attachments from task with id: {}", id);
        return ResponseEntity.ok().body(taskService.findTaskAttachments(id));
    }

    @GetMapping(path = "/{id}/attachments/{fileId}")
    public ResponseEntity<?> getAttachment(@PathVariable Long id, @PathVariable String fileId) {
        log.info("Download an attachment: {} from task with id: {}", fileId, id);
        try {
            Attachment attachment = taskService.getTaskAttachmentById(id, fileId);
            Resource resource = storageService.loadFile(id, fileId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFilename() + "\"")
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        } catch (Exception e) {
            log.error("Unable to download an attachment", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(path = "/{id}/attachments")
    public ResponseEntity<?> addAttachment(@PathVariable Long id, @RequestParam MultipartFile file) {
        log.info("Upload an attachment: {} to task with id: {}", file.getOriginalFilename(), id);
        try {
            String filename = taskService.addAttachmentToTask(id, file);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{filename}")
                    .buildAndExpand(filename)
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            log.error("Unable to upload an attachment", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}