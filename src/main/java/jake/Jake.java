package jake;
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
        assert fullCommand != null : "fullCommand should not be null";
        assert tasks != null : "tasks should be initialized";
        assert storage != null : "storage should be initialized";

        int taskNumber = Parser.parseTaskNumber(fullCommand);
        if (taskNumber > tasks.size()) {
            throw new JakeException("Invalid task number!");
        }
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
        if (taskNumber > tasks.size()) {
            throw new JakeException("Invalid task number!");
        }
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
     *
     * @param fullCommand the full command string containing the todo task description
     * @throws JakeException if the task name is empty or invalid
     */
    private String handleTodoCommand(String fullCommand) throws JakeException {
        assert fullCommand != null : "fullCommand should not be null";
        assert tasks != null : "tasks should be initialized";
        assert storage != null : "storage should be initialized";

        String name = Parser.parseTaskName(fullCommand, "todo");
        Todo todo = new Todo(name);
        tasks.add(todo);
        storage.save(tasks.getTasks());
        return guiUi.getTaskAddedMessage(todo, tasks.size());
    }

    /**
     * Handles the "deadline" command to add a new deadline task.
     *
     * @param fullCommand the full command string containing the task name and deadline
     * @throws JakeException if the command format is invalid or the date is malformed
     */
    private String handleDeadlineCommand(String fullCommand) throws JakeException {
        String[] parsed = Parser.parseDeadlineCommand(fullCommand);
        DeadlineTask deadline = new DeadlineTask(parsed[0], parsed[1]);
        tasks.add(deadline);
        storage.save(tasks.getTasks());
        return guiUi.getTaskAddedMessage(deadline, tasks.size());
    }

    /**
     * Handles the "event" command to add a new event task.
     *
     * @param fullCommand the full command string containing the task name, start time, and end time
     * @throws JakeException if the command format is invalid or the dates are malformed
     */
    private String handleEventCommand(String fullCommand) throws JakeException {
        String[] parsed = Parser.parseEventCommand(fullCommand);
        EventTask event = new EventTask(parsed[0], parsed[1], parsed[2]);
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
        assert fullCommand != null : "fullCommand should not be null";
        assert tasks != null : "tasks should be initialized";
        assert storage != null : "storage should be initialized";

        int taskNumber = Parser.parseTaskNumber(fullCommand);
        if (taskNumber <= 0 || taskNumber > tasks.size()) {
            throw new JakeException("Invalid task number!");
        }
        Task deletedTask = tasks.get(taskNumber - 1);
        tasks.delete(taskNumber - 1);
        storage.save(tasks.getTasks());
        return guiUi.getTaskDeletedMessage(deletedTask, tasks.size());
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
