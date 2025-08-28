package jake.ui;

import jake.task.*;
import jake.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class UiTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    private Ui ui;

    @BeforeEach
    public void setUp() {
        // Redirect System.out to capture output
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        // Restore original streams
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void showWelcome_displaysCorrectWelcomeMessage() {
        ui = new Ui();
        ui.showWelcome();

        String output = outputStream.toString();
        assertTrue(output.contains("Hello from"));
        assertTrue(output.contains("What can I do for you today?"));
        assertTrue(output.contains("____________________________________________________________"));
    }

    @Test
    public void showGoodbye_displaysCorrectGoodbyeMessage() {
        ui = new Ui();
        ui.showGoodbye();

        String output = outputStream.toString();
        assertTrue(output.contains("Bye. Hope to see you again soon!"));
        assertTrue(output.contains("____________________________________________________________"));
    }

    @Test
    public void showLine_displaysCorrectLine() {
        ui = new Ui();
        ui.showLine();

        String output = outputStream.toString().trim();
        assertEquals("____________________________________________________________", output);
    }

    @Test
    public void showError_displaysErrorWithLines() {
        ui = new Ui();
        String errorMessage = "This is an error message";
        ui.showError(errorMessage);

        String output = outputStream.toString();
        assertTrue(output.contains(errorMessage));

        // Check that long lines appear before and after the message
        String[] lines = output.split("\n");
        assertEquals(errorMessage, lines[1]);
    }

    @Test
    public void showLoadingError_displaysCorrectMessage() {
        ui = new Ui();
        ui.showLoadingError();

        String output = outputStream.toString().trim();
        assertEquals("Error loading tasks from file. Starting with empty task list.", output);
    }

    @Test
    public void showTaskAdded_todoTask_displaysCorrectFormat() {
        ui = new Ui();
        Todo task = new Todo("buy milk");
        ui.showTaskAdded(task, 1);

        String output = outputStream.toString();
        assertTrue(output.contains("Todo task has been added:"));
        assertTrue(output.contains("[T][ ] buy milk"));
        assertTrue(output.contains("Now you have 1 tasks in the list."));
    }

    @Test
    public void showTaskAdded_deadlineTask_displaysCorrectFormat() {
        ui = new Ui();
        DeadlineTask task = new DeadlineTask("homework", "2023-12-25T23:59:59");
        ui.showTaskAdded(task, 2);

        String output = outputStream.toString();
        assertTrue(output.contains("Deadline task has been added:"));
        assertTrue(output.contains("[D][ ] homework"));
        assertTrue(output.contains("Now you have 2 tasks in the list."));
    }

    @Test
    public void showTaskAdded_eventTask_displaysCorrectFormat() {
        ui = new Ui();
        EventTask task = new EventTask("meeting", "2023-12-25T10:00:00", "2023-12-25T11:00:00");
        ui.showTaskAdded(task, 3);

        String output = outputStream.toString();
        assertTrue(output.contains("Event task has been added:"));
        assertTrue(output.contains("[E][ ] meeting"));
        assertTrue(output.contains("Now you have 3 tasks in the list."));
    }

    @Test
    public void showTaskMarked_displaysCorrectFormat() {
        ui = new Ui();
        Todo task = new Todo("buy milk");
        task.markDone();
        ui.showTaskMarked(task);

        String output = outputStream.toString();
        assertTrue(output.contains("The following task has been marked as done:"));
        assertTrue(output.contains("[T][X] buy milk"));
        assertTrue(output.contains("____________________________________________________________"));
    }

    @Test
    public void showTaskUnmarked_displaysCorrectFormat() {
        ui = new Ui();
        Todo task = new Todo("buy milk");
        ui.showTaskUnmarked(task);

        String output = outputStream.toString();
        assertTrue(output.contains("The following task has been unmarked:"));
        assertTrue(output.contains("[T][ ] buy milk"));
    }

    @Test
    public void showTaskDeleted_displaysCorrectFormat() {
        ui = new Ui();
        Todo task = new Todo("buy milk");
        ui.showTaskDeleted(task, 0);

        String output = outputStream.toString();
        assertTrue(output.contains("The following task has been removed:"));
        assertTrue(output.contains("[T][ ] buy milk"));
        assertTrue(output.contains("Now you have 0 tasks in the list."));
    }

    @Test
    public void showTaskList_emptyList_displaysEmptyList() {
        ui = new Ui();
        TaskList emptyTaskList = new TaskList();
        ui.showTaskList(emptyTaskList);

        String output = outputStream.toString();
        assertTrue(output.contains("List of tasks:"));
        assertTrue(output.contains("____________________________________________________________"));

        // Should only contain header and lines, no numbered tasks
        String[] lines = output.split("\n");
        assertEquals(3, lines.length); // Two lines + "List of tasks:"
    }

    @Test
    public void showTaskList_withTasks_displaysNumberedList() {
        ui = new Ui();
        TaskList taskList = new TaskList();
        taskList.add(new Todo("buy milk"));
        taskList.add(new DeadlineTask("homework", "2023-12-25T23:59:59"));

        ui.showTaskList(taskList);

        String output = outputStream.toString();
        assertTrue(output.contains("List of tasks:"));
        assertTrue(output.contains("1. [T][ ] buy milk"));
        assertTrue(output.contains("2. [D][ ] homework"));
    }

    @Test
    public void showInvalidCommand_displaysCorrectMessage() {
        ui = new Ui();
        ui.showInvalidCommand();

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid task!!! Try another one"));
    }

    @Test
    public void readCommand_withInput_returnsCorrectString() {
        String input = "todo buy milk\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ui = new Ui(); // Create Ui after setting System.in
        String result = ui.readCommand();

        assertEquals("todo buy milk", result);
    }

    @Test
    public void readCommand_multipleInputs_returnsEachLineCorrectly() {
        String input = "todo buy milk\nlist\nbye\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ui = new Ui();
        assertEquals("todo buy milk", ui.readCommand());
        assertEquals("list", ui.readCommand());
        assertEquals("bye", ui.readCommand());
    }

    @Test
    public void readCommand_emptyInput_returnsEmptyString() {
        String input = "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ui = new Ui();
        String result = ui.readCommand();

        assertEquals("", result);
    }
}