package eu.pibu.vtasker.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import eu.pibu.vtasker.tasks.boundary.dto.ViewedTask;
import eu.pibu.vtasker.tasks.control.TaskService;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Route(value = TaskView.ROUTE)
@PageTitle(value = TaskView.TITLE)
public class TaskView extends VerticalLayout {
    public static final String ROUTE = "";
    public static final String TITLE = "Tasker";
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Grid<ViewedTask> grid = new Grid<>();
    private final TaskService taskService;

    @PostConstruct
    void init() {
        initTaskGrid();
        add(new H1(TITLE));
        add(new Button("Add new task", e -> createTask()));
        addAndExpand(grid);
        refreshGrid();
    }

    private void initTaskGrid() {
        grid.addColumn(ViewedTask::getId).setHeader("ID");
        grid.addColumn(ViewedTask::getTitle).setHeader("Title");
        grid.addColumn(ViewedTask::getDescription).setHeader("Description");
        grid.addColumn(new LocalDateTimeRenderer<>(ViewedTask::getCreatedAt, dateTimeFormatter)).setHeader("Created");
        grid.addItemClickListener(e -> editTask(e.getItem()));
    }

    private void createTask() {
        TaskEditor editor = new TaskEditor(this, taskService);
        editor.open();
    }

    private void editTask(ViewedTask task) {
        TaskEditor editor = new TaskEditor(this, taskService, task);
        editor.open();
    }

    public void refreshGrid() {
        grid.setItems(taskService.findAllTasks().stream().map(ViewedTask::new).collect(Collectors.toList()));
    }
}