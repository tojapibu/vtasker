package eu.pibu.vtasker.tasks.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
public class Attachment {
    private final String id = UUID.randomUUID().toString();
    private String filename;

    public Attachment(String filename) {
        this.filename = filename;
    }
}