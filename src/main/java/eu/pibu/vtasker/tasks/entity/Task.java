package eu.pibu.vtasker.tasks.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Task {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private final Set<Attachment> attachments = new HashSet<>();

    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
    }
    public List<Attachment> getAttachments() {
        return new ArrayList<>(attachments);
    }
}