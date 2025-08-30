package jake;
import jake.task.DeadlineTask;
import jake.task.EventTask;
import jake.task.Task;
import jake.task.Todo;
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

    /**
     * Constructs a new Jake instance with specified file path for data storage.
     * Initialises the UI, storage, and attempts to load existing tasks from the file.
     * If loading fails, will start with empty task list.
     *
     * @param filePath the path to the file where tasks are stored.
     */
    public Jake(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showLoadingError();
            tasks = new TaskList();
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
     * Handles the "mark" command to mark a task as done.
     *
     * @param fullCommand the full command string containing the task number to mark
     * @throws JakeException if the task number is invalid or out of range
     */
    private void handleMarkCommand(String fullCommand) throws JakeException {
        int taskNumber = Parser.parseTaskNumber(fullCommand);
        if (taskNumber > tasks.size()) {
            throw new JakeException("Invalid task number!");
        }
        tasks.markTask(taskNumber - 1);
        storage.save(tasks.getTasks());
        ui.showTaskMarked(tasks.get(taskNumber - 1));
    }

    /**
     * Handles the "unmark" command to mark a task as not done.
     *
     * @param fullCommand the full command string containing the task number to unmark
     * @throws JakeException if the task number is invalid or out of range.
     */
    private void handleUnmarkCommand(String fullCommand) throws JakeException {
        int taskNumber = Parser.parseTaskNumber(fullCommand);
        if (taskNumber > tasks.size()) {
            throw new JakeException("Invalid task number!");
        }
        tasks.unmarkTask(taskNumber - 1);
        storage.save(tasks.getTasks());
        ui.showTaskUnmarked(tasks.get(taskNumber - 1));
    }

    /**
     * Handles the "todo" command to add a new todo task.
     *
     * @param fullCommand the full command string containing the todo task description
     * @throws JakeException if the task name is empty or invalid
     */
    private void handleTodoCommand(String fullCommand) throws JakeException {
        String name = Parser.parseTaskName(fullCommand, "todo");
        Todo todo = new Todo(name);
        tasks.add(todo);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(todo, tasks.size());
    }

    /**
     * Handles the "deadline" command to add a new deadline task.
     *
     * @param fullCommand the full command string containing the task name and deadline
     * @throws JakeException if the command format is invalid or the date is malformed
     */
    private void handleDeadlineCommand(String fullCommand) throws JakeException {
        String[] parsed = Parser.parseDeadlineCommand(fullCommand);
        DeadlineTask deadline = new DeadlineTask(parsed[0], parsed[1]);
        tasks.add(deadline);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(deadline, tasks.size());
    }

    /**
     * Handles the "event" command to add a new event task.
     *
     * @param fullCommand the full command string containing the task name, start time, and end time
     * @throws JakeException if the command format is invalid or the dates are malformed
     */
    private void handleEventCommand(String fullCommand) throws JakeException {
        String[] parsed = Parser.parseEventCommand(fullCommand);
        EventTask event = new EventTask(parsed[0], parsed[1], parsed[2]);
        tasks.add(event);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(event, tasks.size());
    }

    /**
     * Handles the "delete" command to remove a task from the list.
     *
     * @param fullCommand the full command string containing the task number to delete
     * @throws JakeException if the task number is invalid or out of range
     */
    private void handleDeleteCommand(String fullCommand) throws JakeException {
        int taskNumber = Parser.parseTaskNumber(fullCommand);
        if (taskNumber <= 0 || taskNumber > tasks.size()) {
            throw new JakeException("Invalid task number!");
        }
        Task deletedTask = tasks.get(taskNumber - 1);
        tasks.delete(taskNumber - 1);
        storage.save(tasks.getTasks());
        ui.showTaskDeleted(deletedTask, tasks.size());
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