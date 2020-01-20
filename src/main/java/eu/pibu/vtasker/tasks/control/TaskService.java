package eu.pibu.vtasker.tasks.control;

import eu.pibu.vtasker.Clock;
import eu.pibu.vtasker.exceptions.NotFoundException;
import eu.pibu.vtasker.tasks.boundary.TaskRepository;
import eu.pibu.vtasker.tasks.boundary.dto.CreateTaskRequest;
import eu.pibu.vtasker.tasks.boundary.dto.EditedTask;
import eu.pibu.vtasker.tasks.boundary.dto.UpdateTaskRequest;
import eu.pibu.vtasker.tasks.entity.Attachment;
import eu.pibu.vtasker.tasks.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final Clock clock;
    private final TaskRepository taskRepository;
    private final StorageService storageService;
    private final AtomicLong taskId = new AtomicLong(1L);

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.getById(id);
    }

    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    public Long addTask(CreateTaskRequest dto) {
        Long id = taskId.getAndIncrement();
        taskRepository.create(new Task(id, dto.getTitle(), dto.getDescription(), clock.time()));
        return id;
    }

    public void updateTask(Long id, UpdateTaskRequest dto) {
        taskRepository.update(id, dto.getTitle(), dto.getDescription());
    }

    public Long addTask(EditedTask dto) {
        Long id = taskId.getAndIncrement();
        taskRepository.create(new Task(id, dto.getTitle(), dto.getDescription(), clock.time()));
        return id;
    }

    public void updateTask(Long id, EditedTask dto) {
        taskRepository.update(id, dto.getTitle(), dto.getDescription());
    }

    public String addAttachmentToTask(Long id, MultipartFile file) throws IOException {
        Attachment attachment = new Attachment(file.getOriginalFilename());
        taskRepository.getById(id).addAttachment(attachment);
        storageService.saveFile(attachment.getId(), file);
        return attachment.getId();
    }
    public List<Attachment> findTaskAttachments(Long id) {
        return taskRepository.getById(id).getAttachments();
    }

    public Attachment getTaskAttachmentById(Long id, String fileId) {
        return taskRepository.getById(id)
                .getAttachments()
                .stream()
                .filter(a -> a.getId().equals(fileId))
                .findFirst()
                .orElseThrow(()->new NotFoundException("File not found"));
    }
}