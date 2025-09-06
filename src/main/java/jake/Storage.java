package jake;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jake.task.DeadlineTask;
import jake.task.EventTask;
import jake.task.Task;
import jake.task.Todo;




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
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating data file: " + e.getMessage());
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
     * @param tasks the list of tasks to save to the file
     */
    public void save(ArrayList<Task> tasks) {
        ensureFileExists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : tasks) {
                writer.write(task.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error while writing to file: " + e.getMessage());
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

            Task task = null;

            switch (type) {
            case "T":
                task = new Todo(name);
                if (isDone) {
                    task.markDone();
                }
                // Handle tags for Todo (at index 3)
                if (parts.length > 3 && !parts[3].trim().isEmpty()) {
                    List<String> tags = Arrays.asList(parts[3].split(","));
                    task.setTags(tags);
                }
                return task;
            case "D":
                Date byDate = inputFormatter.parse(parts[3].trim());
                String by = outputFormatter.format(byDate);
                task = new DeadlineTask(name, by);
                if (isDone) {
                    task.markDone();
                }
                // Handle tags for DeadlineTask (at index 4)
                if (parts.length > 4 && !parts[4].trim().isEmpty()) {
                    List<String> tags = Arrays.asList(parts[4].split(","));
                    task.setTags(tags);
                }
                return task;
            case "E":
                Date startDate = inputFormatter.parse(parts[3].trim());
                Date endDate = inputFormatter.parse(parts[4].trim());
                String start = outputFormatter.format(startDate);
                String end = outputFormatter.format(endDate);
                task = new EventTask(name, start, end);
                if (isDone) {
                    task.markDone();
                }
                // Handle tags for EventTask (at index 5)
                if (parts.length > 5 && !parts[5].trim().isEmpty()) {
                    List<String> tags = Arrays.asList(parts[5].split(","));
                    task.setTags(tags);
                }
                return task;
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