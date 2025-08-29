package jake;

import jake.task.DeadlineTask;
import jake.task.EventTask;
import jake.task.Task;
import jake.task.Todo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

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
