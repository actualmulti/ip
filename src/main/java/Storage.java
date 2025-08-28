import java.io.*;
import java.util.ArrayList;

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
            String type = parts[0];
            boolean isDone = parts[1].equals("1");
            String name = parts[2];

            switch (type) {
                case "T":
                    Todo todo = new Todo(name);
                    if (isDone) todo.markDone();
                    return todo;
                case "D":
                    String deadline = parts[3];
                    DeadlineTask d = new DeadlineTask(name, deadline);
                    if (isDone) d.markDone();
                    return d;
                case "E":
                    String startDate = parts[3];
                    String endDate = parts[4];
                    EventTask e = new EventTask(name, startDate, endDate);
                    if (isDone) e.markDone();
                    return e;
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
