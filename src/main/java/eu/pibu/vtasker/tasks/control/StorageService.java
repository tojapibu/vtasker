package eu.pibu.vtasker.tasks.control;

import eu.pibu.vtasker.VtaskerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final VtaskerProperties configuration;

    public void saveFile(String attachmentId, MultipartFile file) throws IOException {
        Path path = Paths.get(configuration.getStoragePath()).resolve(attachmentId);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    }

    public Resource loadFile(Long taskId, String filename) throws MalformedURLException {
        Path path = Paths.get(configuration.getStoragePath()).resolve(filename);
        return new UrlResource(path.toUri());
    }
}