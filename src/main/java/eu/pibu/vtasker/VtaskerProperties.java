package eu.pibu.vtasker;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.tasker")
public class VtaskerProperties {
    private String message;
    private String storagePath;
}