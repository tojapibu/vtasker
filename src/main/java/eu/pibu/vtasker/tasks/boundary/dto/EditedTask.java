package eu.pibu.vtasker.tasks.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditedTask {
    private String title;
    private String description;
}