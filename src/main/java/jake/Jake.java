package jake;
import jake.task.DeadlineTask;
import jake.task.EventTask;
import jake.task.Task;
import jake.task.Todo;
import jake.ui.Ui;


public class Jake {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

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

    private void handleMarkCommand(String fullCommand) throws JakeException {
        int taskNumber = Parser.parseTaskNumber(fullCommand);
        if (taskNumber > tasks.size()) {
            throw new JakeException("Invalid task number!");
        }
        tasks.markTask(taskNumber - 1);
        storage.save(tasks.getTasks());
        ui.showTaskMarked(tasks.get(taskNumber - 1));
    }

    private void handleUnmarkCommand(String fullCommand) throws JakeException {
        int taskNumber = Parser.parseTaskNumber(fullCommand);
        if (taskNumber > tasks.size()) {
            throw new JakeException("Invalid task number!");
        }
        tasks.unmarkTask(taskNumber - 1);
        storage.save(tasks.getTasks());
        ui.showTaskUnmarked(tasks.get(taskNumber - 1));
    }

    private void handleTodoCommand(String fullCommand) throws JakeException {
        String name = Parser.parseTaskName(fullCommand, "todo");
        Todo todo = new Todo(name);
        tasks.add(todo);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(todo, tasks.size());
    }

    private void handleDeadlineCommand(String fullCommand) throws JakeException {
        String[] parsed = Parser.parseDeadlineCommand(fullCommand);
        DeadlineTask deadline = new DeadlineTask(parsed[0], parsed[1]);
        tasks.add(deadline);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(deadline, tasks.size());
    }

    private void handleEventCommand(String fullCommand) throws JakeException {
        String[] parsed = Parser.parseEventCommand(fullCommand);
        EventTask event = new EventTask(parsed[0], parsed[1], parsed[2]);
        tasks.add(event);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(event, tasks.size());
    }

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

    public static void main(String[] args) {
        new Jake("./data/jake.txt").run();
    }


}