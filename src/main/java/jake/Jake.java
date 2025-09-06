package jake;
import java.util.Arrays;
import java.util.List;

import jake.task.DeadlineTask;
import jake.task.EventTask;
import jake.task.Task;
import jake.task.Todo;
import jake.ui.GuiUi;
import jake.ui.Ui;

/**
 * Main class for Jake chatbot app.
 * Jake is a task management assistant.
 * It supports todo tasks, deadline tasks, and event tasks with persistent storage.
 *
 * @author Mitchel lee
 */
public class Jake {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private GuiUi guiUi;

    /**
     * Constructs a new Jake instance with specified file path for data storage.
     * Initialises the UI, storage, and attempts to load existing tasks from the file.
     * If loading fails, will start with empty task list.
     *
     * @param filePath the path to the file where tasks are stored.
     */
    public Jake(String filePath) {
        assert filePath != null : "filePath should not be null";
        assert !filePath.trim().isEmpty() : "filePath should not be empty";

        ui = new Ui();
        guiUi = new GuiUi();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Default constructor for Jake using the default file path.
     */
    public Jake() {
        this("./data/jake.txt");
    }

    /**
     * Processes a user command and returns the appropriate response string.
     * Uses GuiUi for clean, consistent formatting.
     *
     * @param input the user's command input
     * @return the response string to be displayed in the GUI
     */
    public String getResponse(String input) {
        try {
            String command = Parser.getCommandWord(input);

            switch (command) {
            case "bye":
                return "BYE_COMMAND:" + guiUi.getGoodbyeMessage();
            case "list":
                return guiUi.getTaskListMessage(tasks);
            case "mark":
                return handleMarkCommand(input);
            case "unmark":
                return handleUnmarkCommand(input);
            case "todo":
                return handleTodoCommand(input);
            case "deadline":
                return handleDeadlineCommand(input);
            case "event":
                return handleEventCommand(input);
            case "delete":
                return handleDeleteCommand(input);
            case "find":
                return handleFindCommand(input);
            case "tag":
                return handleTagCommand(input);
            case "untag":
                return handleUntagCommand(input);
            case "search":
                return handleSearchCommand(input);
            default:
                return guiUi.getInvalidCommandMessage();
            }
        } catch (JakeException e) {
            return guiUi.getErrorMessage(e.getMessage());
        }
    }

    /**
     * Handles the "mark" command to mark a task as done.
     *
     * @param fullCommand the full command string containing the task number to mark
     * @throws JakeException if the task number is invalid or out of range
     */
    private String handleMarkCommand(String fullCommand) throws JakeException {
        int taskNumber = Parser.parseTaskNumber(fullCommand);
        tasks.markTask(taskNumber - 1);
        storage.save(tasks.getTasks());
        return guiUi.getTaskMarkedMessage(tasks.get(taskNumber - 1));
    }

    /**
     * Handles the "unmark" command to mark a task as not done.
     *
     * @param fullCommand the full command string containing the task number to unmark
     * @throws JakeException if the task number is invalid or out of range.
     */
    private String handleUnmarkCommand(String fullCommand) throws JakeException {
        int taskNumber = Parser.parseTaskNumber(fullCommand);
        tasks.unmarkTask(taskNumber - 1);
        storage.save(tasks.getTasks());
        return guiUi.getTaskUnmarkedMessage(tasks.get(taskNumber - 1));
    }

    private String handleFindCommand(String fullCommand) throws JakeException {
        String name = Parser.parseTaskName(fullCommand, "find");
        TaskList out = tasks.findTasks(name);
        return guiUi.getTaskListMessage(out);
    }

    /**
     * Handles the "todo" command to add a new todo task.
     * Supports inline tagging: "todo buy groceries #personal #urgent"
     *
     * @param fullCommand the full command string containing the todo task description
     * @throws JakeException if the task name is empty or invalid
     */
    private String handleTodoCommand(String fullCommand) throws JakeException {
        assert fullCommand != null : "fullCommand should not be null";
        assert tasks != null : "tasks should be initialized";
        assert storage != null : "storage should be initialized";

        String[] parsed = Parser.parseTaskNameWithTags(fullCommand, "todo");
        String name = parsed[0];
        String tagsString = parsed[1];

        Todo todo = new Todo(name);

        // Add tags if any were found
        if (!tagsString.isEmpty()) {
            List<String> tags = Arrays.asList(tagsString.split(","));
            todo.setTags(tags);
        }

        tasks.add(todo);
        storage.save(tasks.getTasks());
        return guiUi.getTaskAddedMessage(todo, tasks.size());
    }

    /**
     * Handles the "deadline" command to add a new deadline task.
     * Supports inline tagging: "deadline submit assignment #work #important /2023-12-25T23:59:59"
     *
     * @param fullCommand the full command string containing the task name and deadline
     * @throws JakeException if the command format is invalid or the date is malformed
     */
    private String handleDeadlineCommand(String fullCommand) throws JakeException {
        String[] parsed = Parser.parseDeadlineCommandWithTags(fullCommand);
        String name = parsed[0];
        String deadline = parsed[1];
        String tagsString = parsed[2];

        DeadlineTask deadlineTask = new DeadlineTask(name, deadline);

        // Add tags if any were found
        if (!tagsString.isEmpty()) {
            List<String> tags = Arrays.asList(tagsString.split(","));
            deadlineTask.setTags(tags);
        }

        tasks.add(deadlineTask);
        storage.save(tasks.getTasks());
        return guiUi.getTaskAddedMessage(deadlineTask, tasks.size());
    }

    /**
     * Handles the "event" command to add a new event task.
     * Supports inline tagging: "event team meeting #work #weekly /2023-12-20T10:00:00 /2023-12-20T11:00:00"
     *
     * @param fullCommand the full command string containing the task name, start time, and end time
     * @throws JakeException if the command format is invalid or the dates are malformed
     */
    private String handleEventCommand(String fullCommand) throws JakeException {
        String[] parsed = Parser.parseEventCommandWithTags(fullCommand);
        String name = parsed[0];
        String startDate = parsed[1];
        String endDate = parsed[2];
        String tagsString = parsed[3];

        EventTask event = new EventTask(name, startDate, endDate);

        // Add tags if any were found
        if (!tagsString.isEmpty()) {
            List<String> tags = Arrays.asList(tagsString.split(","));
            event.setTags(tags);
        }

        tasks.add(event);
        storage.save(tasks.getTasks());
        return guiUi.getTaskAddedMessage(event, tasks.size());
    }

    /**
     * Handles the "delete" command to remove a task from the list.
     *
     * @param fullCommand the full command string containing the task number to delete
     * @throws JakeException if the task number is invalid or out of range
     */
    private String handleDeleteCommand(String fullCommand) throws JakeException {
        int taskNumber = Parser.parseTaskNumber(fullCommand);
        Task deletedTask = tasks.get(taskNumber - 1);
        tasks.delete(taskNumber - 1);
        storage.save(tasks.getTasks());
        return guiUi.getTaskDeletedMessage(deletedTask, tasks.size());
    }

    /**
     * Handles the "tag" command to add or remove tags from tasks.
     *
     * @param fullCommand the full command string containing task number, operation, and tag
     * @throws JakeException if the command format is invalid or task number is out of range
     */
    private String handleTagCommand(String fullCommand) throws JakeException {
        String[] parsed = Parser.parseTagCommand(fullCommand);
        int taskNumber = Integer.parseInt(parsed[0]);
        String operation = parsed[1];
        String tag = parsed[2];

        Task task = tasks.get(taskNumber - 1);

        if (operation.equals("add")) {
            task.addTag(tag);
            storage.save(tasks.getTasks());
            return guiUi.getTagAddedMessage(task, tag);
        } else { // operation.equals("remove")
            if (task.hasTag(tag)) {
                task.removeTag(tag);
                storage.save(tasks.getTasks());
                return guiUi.getTagRemovedMessage(task, tag);
            } else {
                return guiUi.getTagNotFoundMessage(task, tag);
            }
        }
    }

    /**
     * Handles the "untag" command to remove one or more tags from a task.
     * Usage: "untag [task number] [tag1] [tag2] ..." or "untag [task number] all"
     *
     * @param fullCommand the full command string containing task number and tags to remove
     * @throws JakeException if the command format is invalid or task number is out of range
     */
    private String handleUntagCommand(String fullCommand) throws JakeException {
        String[] parsed = Parser.parseUntagCommand(fullCommand);
        int taskNumber = Integer.parseInt(parsed[0]);
        Task task = tasks.get(taskNumber - 1);

        if (parsed.length == 2 && parsed[1].equals("all")) {
            // Remove all tags
            List<String> removedTags = task.getTags();
            task.setTags(Arrays.asList()); // Clear all tags
            storage.save(tasks.getTasks());
            return guiUi.getAllTagsRemovedMessage(task, removedTags);
        } else {
            // Remove specific tags
            StringBuilder result = new StringBuilder();
            boolean anyRemoved = false;

            for (int i = 1; i < parsed.length; i++) {
                String tag = parsed[i];
                if (task.hasTag(tag)) {
                    task.removeTag(tag);
                    result.append("Removed tag '").append(tag).append("'\n");
                    anyRemoved = true;
                } else {
                    result.append("Tag '").append(tag).append("' not found\n");
                }
            }

            if (anyRemoved) {
                storage.save(tasks.getTasks());
            }

            result.append("Updated task: ").append(task.toString());
            return result.toString().trim();
        }
    }

    /**
     * Handles the "search" command to find tasks by tag.
     *
     * @param fullCommand the full command string containing the search term
     * @throws JakeException if the command format is invalid
     */
    private String handleSearchCommand(String fullCommand) throws JakeException {
        String searchTerm = Parser.parseSearchTagCommand(fullCommand);

        // If search term starts with #, treat it as a tag search
        if (fullCommand.contains("#")) {
            TaskList filteredTasks = tasks.findTasksByTag(searchTerm);
            return guiUi.getSearchResultsMessage(filteredTasks, "#" + searchTerm);
        } else {
            // Regular text search in task names
            TaskList filteredTasks = tasks.findTasks(searchTerm);
            return guiUi.getSearchResultsMessage(filteredTasks, searchTerm);
        }
    }

    /**
     * Runs the main application loop.
     * Displays welcome message and processes user commands until "bye" is entered.
     * Handles various commands including adding, marking/unmarking, listing, and deleting tasks.
     */
    public void run() {
        ui.showWelcome();

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                String command = Parser.getCommandWord(fullCommand);

                switch (command) {
                case "bye":
                    isExit = true;
                    ui.showGoodbye();
                    break;
                case "list":
                    ui.showTaskList(tasks);
                    break;
                case "mark":
                    handleMarkCommand(fullCommand);
                    break;
                case "unmark":
                    handleUnmarkCommand(fullCommand);
                    break;
                case "todo":
                    handleTodoCommand(fullCommand);
                    break;
                case "deadline":
                    handleDeadlineCommand(fullCommand);
                    break;
                case "event":
                    handleEventCommand(fullCommand);
                    break;
                case "delete":
                    handleDeleteCommand(fullCommand);
                    break;
                case "find":
                    handleFindCommand(fullCommand);
                    break;
                case "tag":
                    handleTagCommand(fullCommand);
                    break;
                case "untag":
                    handleUntagCommand(fullCommand);
                    break;
                case "search":
                    handleSearchCommand(fullCommand);
                    break;
                default:
                    ui.showInvalidCommand();
                    break;
                }
            } catch (JakeException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * The main entry point of the Jake application.
     * Creates a new Jake instance with the default data file path and runs the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new Jake("./data/jake.txt").run();
    }
}
