package eu.pibu.vtasker.tasks.boundary.dto;

import eu.pibu.vtasker.tasks.entity.Attachment;
import lombok.Data;

@Data
public class AttachmentResponse {
    private final String id;
    private final String filename;

    public AttachmentResponse(Attachment attachment) {
        this.id = attachment.getId();
        this.filename = attachment.getFilename();
    }
}