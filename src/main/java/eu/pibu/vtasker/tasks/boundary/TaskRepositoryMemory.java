package eu.pibu.vtasker.tasks.boundary;

import eu.pibu.vtasker.exceptions.NotFoundException;
import eu.pibu.vtasker.tasks.entity.Task;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TaskRepositoryMemory implements TaskRepository {
    private final Set<Task> tasks = new HashSet<>();

    @Override
    public void create(Task task) {
        tasks.add(task);
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    @Override
    public Task getById(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Task with id: " + id + " not found"));
    }

    @Override
    public void update(Long id, String title, String description) {
        Task task = findById(id).orElseThrow(() -> new NotFoundException("Task with id: " + id + " not found"));
        task.setTitle(task.getTitle());
        task.setDescription(task.getDescription());
    }

    @Override
    public void deleteById(Long id) {
        Task task = findById(id).orElseThrow(() -> new NotFoundException("Task with id: " + id + " not found"));
        tasks.remove(task);
    }

    public Optional<Task> findById(Long id) {
        return tasks.stream()
                .filter(task -> id.equals(task.getId()))
                .findFirst();
    }
}