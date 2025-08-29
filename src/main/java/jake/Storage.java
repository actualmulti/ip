package jake;

import jake.task.DeadlineTask;
import jake.task.EventTask;
import jake.task.Task;
import jake.task.Todo;

import java.io.*;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Handles loading and saving tasks to and from a file.
 * Manages persistent storage of task data with automatic file creation and error handling.
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a Storage instance with the specified file path.
     *
     * @param filePath the path to the file where tasks will be stored
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Ensures that the storage file and its parent directories exist.
     * Creates the necessary directories and file if they don't exist.
     */
    private void ensureFileExists() {
        File file = new File(filePath);
        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                boolean dirCreated = parentDir.mkdirs();
                if (dirCreated) {
                    System.out.println("Created directory: " + parentDir.getPath());
                } else {
                    System.out.println("Failed to create directory (might already exist): " + parentDir.getPath());
                }
            }

            if (!file.exists()) {
                boolean fileCreated = file.createNewFile();
                if (fileCreated) {
                    System.out.println("Created new save file: " + file.getPath());
                } else {
                    System.out.println("Failed to create file: " + file.getPath());
                }
            }
        } catch (IOException e) {
            System.out.println("Error ensuring storage file exists: " + e.getMessage());
        }
    }


    /**
     * Loads tasks from the storage file.
     * Parses each line in the file to recreate Task objects.
     *
     * @return an ArrayList of tasks loaded from the file
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        ensureFileExists();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
                System.out.println("Error while reading file: " + e.getMessage());
            }
            return tasks;
    }

    /**
     * Saves the given list of tasks to the storage file.
     * Overwrites the existing file content with the current task list.
     *
     * @param tasks the list of tasks to be saved to the file
     */
    public void save(ArrayList<Task> tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : tasks) {
                writer.write(task.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error while writing file: " + e.getMessage());
        }
    }

    /**
     * Parses a line from the storage file to create a Task object.
     * Handles different task types (Todo, Deadline, Event) and their specific formats.
     *
     * @param line a line from the storage file representing a task
     * @return the parsed Task object, or null if the line is corrupted
     */
    private Task parseTask(String line) {
        try {
            String[] parts = line.split(" \\| ");
            String type = parts[0].trim();
            boolean isDone = parts[1].equals("1");
            String name = parts[2].trim();

            SimpleDateFormat inputFormatter = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");
            SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            switch (type) {
                case "T":
                    Todo todo = new Todo(name);
                    if (isDone) todo.markDone();
                    return todo;
                case "D":
                    Date byDate = inputFormatter.parse(parts[3].trim());
                    String by = outputFormatter.format(byDate);
                    DeadlineTask deadline = new DeadlineTask(name, by);
                    if (isDone) deadline.markDone();
                    return deadline;
                case "E":
                    Date startDate = inputFormatter.parse(parts[3].trim());
                    Date endDate = inputFormatter.parse(parts[4].trim());
                    String start = outputFormatter.format(startDate);
                    String end = outputFormatter.format(endDate);
                    EventTask event = new EventTask(name, start, end);
                    if (isDone) event.markDone();
                    return event;
                default:
                    System.out.println("Corrupted line ignored: " + line);
                    return null;
            }
        } catch (Exception e) {
            System.out.println("Corrupted line ignored: " + line);
            return null;
        }
    }

}
