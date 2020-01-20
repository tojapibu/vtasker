package eu.pibu.vtasker.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import eu.pibu.vtasker.tasks.boundary.dto.EditedTask;
import eu.pibu.vtasker.tasks.boundary.dto.ViewedTask;
import eu.pibu.vtasker.tasks.control.TaskService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskEditor extends Dialog {
    private final Button delete = new Button("Delete");
    private final TaskService taskService;
    private final TaskView taskView;
    private final TaskForm taskForm;
    private ViewedTask viewedTask;
    private EditedTask editedTask;

    public TaskEditor(TaskView taskView, TaskService taskService) {
        this.taskView = taskView;
        this.taskService = taskService;
        this.viewedTask = null;
        this.editedTask = new EditedTask("", "");
        this.taskForm = new TaskForm(editedTask);
        init();
    }

    public TaskEditor(TaskView taskView, TaskService taskService, ViewedTask task) {
        this.taskView = taskView;
        this.taskService = taskService;
        this.viewedTask = task;
        this.editedTask = new EditedTask(task.getTitle(), task.getDescription());
        this.taskForm = new TaskForm(editedTask);
        init();
    }

    private void init() {
        delete.setEnabled(false);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        delete.getStyle().set("margin-right", "auto");
        delete.addClickListener(e -> delete());
        Button cancel = new Button("Cancel");
        cancel.addClickListener(e -> close());
        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> save());
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.addAndExpand(taskForm);
        HorizontalLayout h1 = new HorizontalLayout();
        h1.addAndExpand(upload);
        layout.addAndExpand(h1);
        HorizontalLayout h2 = new HorizontalLayout(delete, cancel, save);
        h2.setSizeFull();
        layout.add(h2);
        add(layout);
        setSizeUndefined();
    }

    private void save() {
        if (viewedTask == null) {
            log.info("Create new task");
            taskService.addTask(taskForm.getEditedTask());
        } else {
            log.info("Update task with id: {}", viewedTask.getId());
            taskService.updateTask(viewedTask.getId(), taskForm.getEditedTask());
        }
        taskView.refreshGrid();
        close();
    }

    private void delete() {
        log.info("Delete task with id: {}", viewedTask.getId());
        taskService.deleteTaskById(viewedTask.getId());
        taskView.refreshGrid();
        close();
    }
}