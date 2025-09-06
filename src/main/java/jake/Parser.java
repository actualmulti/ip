package jake;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for parsing user commands and extracting relavant information
 * Provides static methods to parse different types of commands and extract
 * command words, task names, and specific command parameters.
 */
public class Parser {

    /**
     * Extracts the command word (first word) from a full command string.
     *
     * @param fullCommand the complete command string entered by the user
     * @return the first word of the command, which represents the command type
     */
    public static String getCommandWord(String fullCommand) {
        assert fullCommand != null : "fullCommand should not be null";
        assert !fullCommand.isEmpty() : "fullCommand should not be empty";

        return fullCommand.split(" ")[0];
    }

    public static String[] parseCommand(String fullCommand) {
        return fullCommand.split(" ", 2);
    }

    /**
     * Extracts and parses the task number from commands that require a task index.
     * Used for commands like "mark", "unmark", and "delete".
     *
     * @param fullCommand the complete command string containing a task number
     * @return the task number as an integer
     * @throws JakeException if no task number is provided or if the format is invalid
     */
    public static int parseTaskNumber(String fullCommand) throws JakeException {
        assert fullCommand != null : "fullCommand should not be null";

        try {
            String[] parts = fullCommand.split(" ");
            if (parts.length < 2) {
                throw new JakeException("Please specify a task number!");
            }
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new JakeException("Invalid task number format!");
        }
    }

    /**
     * Extracts tags from a task description string.
     * Tags are identified by the # symbol (e.g., #work #urgent).
     *
     * @param taskDescription the task description that may contain tags
     * @return a list of tags found in the description
     */
    public static List<String> extractTags(String taskDescription) {
        List<String> tags = new ArrayList<>();
        Pattern tagPattern = Pattern.compile("#(\\w+)");
        Matcher matcher = tagPattern.matcher(taskDescription);

        while (matcher.find()) {
            tags.add(matcher.group(1));
        }

        return tags;
    }

    /**
     * Removes tags from a task description string.
     *
     * @param taskDescription the task description that may contain tags
     * @return the task description with tags removed
     */
    public static String removeTags(String taskDescription) {
        return taskDescription.replaceAll("#\\w+", "").trim().replaceAll("\\s+", " ");
    }

    /**
     * Extracts the task name from a command string by removing the command word.
     * Also extracts any inline tags from the task name.
     *
     * @param fullCommand the complete command string
     * @param commandWord the command word to be removed from the beginning
     * @return an array where index 0 is the task name (without tags) and index 1 is a comma-separated list of tags
     * @throws JakeException if the task name is empty after removing the command word
     */
    public static String[] parseTaskNameWithTags(String fullCommand, String commandWord) throws JakeException {
        assert fullCommand != null : "fullCommand should not be null";
        assert commandWord != null : "commandWord should not be null";

        String fullDescription = fullCommand.substring(commandWord.length()).trim();
        if (fullDescription.isEmpty()) {
            throw new JakeException(commandWord.substring(0, 1).toUpperCase()
                    + commandWord.substring(1) + " task must have a name");
        }

        List<String> tags = extractTags(fullDescription);
        String taskName = removeTags(fullDescription);

        if (taskName.isEmpty()) {
            throw new JakeException(commandWord.substring(0, 1).toUpperCase()
                    + commandWord.substring(1) + " task must have a name");
        }

        String tagsString = String.join(",", tags);
        return new String[]{taskName, tagsString};
    }

    /**
     * Extracts the task name from a command string by removing the command word.
     *
     * @param fullCommand the complete command string
     * @param commandWord the command word to be removed from the beginning
     * @return the task name after removing the command word and trimming whitespace
     * @throws JakeException if the task name is empty after removing the command word
     */
    public static String parseTaskName(String fullCommand, String commandWord) throws JakeException {
        assert fullCommand != null : "fullCommand should not be null";
        assert commandWord != null : "commandWord should not be null";

        String name = fullCommand.substring(commandWord.length()).trim();
        if (name.isEmpty()) {
            throw new JakeException(commandWord.substring(0, 1).toUpperCase()
                    + commandWord.substring(1) + " task must have a name");
        }
        return name;
    }

    /**
     * Parses a deadline command to extract the task name and deadline date.
     * Also extracts any inline tags from the task name.
     * Expected format: "deadline [task name] #tag1 #tag2 /[date]"
     *
     * @param fullCommand the complete deadline command string
     * @return an array containing the task name at index 0, the deadline date at index 1, and tags at index 2
     * @throws JakeException if the command format is invalid or missing required components
     */
    public static String[] parseDeadlineCommandWithTags(String fullCommand) throws JakeException {
        assert fullCommand != null : "fullCommand should not be null";
        assert fullCommand.trim().startsWith("deadline") : "deadline should start with 'deadline'";

        try {
            int beginIndex = fullCommand.indexOf(" ") + 1;
            int endIndex = fullCommand.indexOf("/", beginIndex);
            if (beginIndex > endIndex) {
                throw new JakeException("Deadline task must have a valid name and/or date!");
            }
            String nameWithTags = fullCommand.substring(beginIndex, endIndex - 1);
            String deadline = fullCommand.substring(endIndex + 1);

            List<String> tags = extractTags(nameWithTags);
            String taskName = removeTags(nameWithTags);
            String tagsString = String.join(",", tags);

            return new String[]{taskName, deadline, tagsString};
        } catch (Exception e) {
            throw new JakeException("Deadline task must have a valid name and/or date!");
        }
    }

    /**
     * Parses a deadline command to extract the task name and deadline date.
     * Expected format: "deadline [task name] /[date]"
     *
     * @param fullCommand the complete deadline command string
     * @return an array containing the task name at index 0 and the deadline date at index 1
     * @throws JakeException if the command format is invalid or missing required components
     */
    public static String[] parseDeadlineCommand(String fullCommand) throws JakeException {
        assert fullCommand != null : "fullCommand should not be null";
        assert fullCommand.trim().startsWith("deadline") : "deadline should start with 'deadline'";

        try {
            int beginIndex = fullCommand.indexOf(" ") + 1;
            int endIndex = fullCommand.indexOf("/", beginIndex);
            if (beginIndex > endIndex) {
                throw new JakeException("Deadline task must have a valid name and/or date!");
            }
            String name = fullCommand.substring(beginIndex, endIndex - 1);
            String deadline = fullCommand.substring(endIndex + 1);
            return new String[]{name, deadline};
        } catch (Exception e) {
            throw new JakeException("Deadline task must have a valid name and/or date!");
        }
    }

    /**
     * Parses an event command to extract the task name, start date, and end date.
     * Also extracts any inline tags from the task name.
     * Expected format: "event [task name] #tag1 #tag2 /[start date] /[end date]"
     *
     * @param fullCommand the complete event command string
     * @return an array containing the task name at index 0, start date at index 1, end date at index 2, and tags at index 3
     * @throws JakeException if the command format is invalid or missing required components
     */
    public static String[] parseEventCommandWithTags(String fullCommand) throws JakeException {
        assert fullCommand != null : "fullCommand should not be null";
        assert fullCommand.trim().startsWith("event") : "event should start with 'event'";

        try {
            int beginIndex = fullCommand.indexOf(" ") + 1;
            int endIndex = fullCommand.indexOf("/");
            if (beginIndex >= endIndex) {
                throw new JakeException("Event task must have a valid name and/or date!");
            }
            String nameWithTags = fullCommand.substring(beginIndex, endIndex - 1);
            String dates = fullCommand.substring(endIndex + 1);
            int dateIndex = dates.indexOf("/");
            if (dateIndex < 0) {
                throw new JakeException("Event task must have a valid name and/or date!");
            }
            String startDate = dates.substring(0, dateIndex - 1);
            String endDate = dates.substring(dateIndex + 1);

            List<String> tags = extractTags(nameWithTags);
            String taskName = removeTags(nameWithTags);
            String tagsString = String.join(",", tags);

            return new String[]{taskName, startDate, endDate, tagsString};
        } catch (Exception e) {
            throw new JakeException("Event task must have a valid name and/or date!");
        }
    }

    /**
     * Parses an event command to extract the task name, start date, and end date.
     * Expected format: "event [task name] /[start date] /[end date]"
     *
     * @param fullCommand the complete event command string
     * @return an array containing the task name at index 0, start date at index 1, and end date at index 2
     * @throws JakeException if the command format is invalid or missing required components
     */
    public static String[] parseEventCommand(String fullCommand) throws JakeException {
        assert fullCommand != null : "fullCommand should not be null";
        assert fullCommand.trim().startsWith("event") : "event should start with 'event'";

        try {
            int beginIndex = fullCommand.indexOf(" ") + 1;
            int endIndex = fullCommand.indexOf("/");
            if (beginIndex >= endIndex) {
                throw new JakeException("Event task must have a valid name and/or date!");
            }
            String name = fullCommand.substring(beginIndex, endIndex - 1);
            String dates = fullCommand.substring(endIndex + 1);
            int dateIndex = dates.indexOf("/");
            if (dateIndex < 0) {
                throw new JakeException("Event task must have a valid name and/or date!");
            }
            String startDate = dates.substring(0, dateIndex - 1);
            String endDate = dates.substring(dateIndex + 1);
            return new String[]{name, startDate, endDate};
        } catch (Exception e) {
            throw new JakeException("Event task must have a valid name and/or date!");
        }
    }

    /**
     * Parses a tag command to extract the task number and tag operation.
     * Expected format: "tag [task number] [add/remove] [tag]"
     *
     * @param fullCommand the complete tag command string
     * @return an array containing task number at index 0, operation at index 1, and tag at index 2
     * @throws JakeException if the command format is invalid
     */
    public static String[] parseTagCommand(String fullCommand) throws JakeException {
        assert fullCommand != null : "fullCommand should not be null";

        String[] parts = fullCommand.split(" ", 4);
        if (parts.length < 4) {
            throw new JakeException("Tag command format: tag [task number] [add/remove] [tag]");
        }

        String taskNumber = parts[1];
        String operation = parts[2];
        String tag = parts[3];

        if (!operation.equals("add") && !operation.equals("remove")) {
            throw new JakeException("Tag operation must be 'add' or 'remove'");
        }

        if (tag.trim().isEmpty()) {
            throw new JakeException("Tag cannot be empty");
        }

        return new String[]{taskNumber, operation, tag.trim()};
    }

    /**
     * Parses an untag command to extract the task number and tags to remove.
     * Expected format: "untag [task number] [tag1] [tag2] ..." or "untag [task number] all"
     *
     * @param fullCommand the complete untag command string
     * @return an array containing task number at index 0 and tags to remove at subsequent indices
     * @throws JakeException if the command format is invalid
     */
    public static String[] parseUntagCommand(String fullCommand) throws JakeException {
        assert fullCommand != null : "fullCommand should not be null";

        String[] parts = fullCommand.split(" ");
        if (parts.length < 3) {
            throw new JakeException("Untag command format: untag [task number] [tag1] [tag2] ... or untag [task number] all");
        }

        String taskNumber = parts[1];

        // Check if it's "untag [number] all"
        if (parts.length == 3 && parts[2].equals("all")) {
            return new String[]{taskNumber, "all"};
        }

        // Otherwise, collect all tags to remove
        String[] result = new String[parts.length - 1];
        result[0] = taskNumber;
        for (int i = 2; i < parts.length; i++) {
            result[i - 1] = parts[i];
        }

        return result;
    }

    /**
     * Parses a search by tag command.
     * Expected format: "search #[tag]"
     *
     * @param fullCommand the complete search command string
     * @return the tag to search for (without the # prefix)
     * @throws JakeException if the command format is invalid
     */
    public static String parseSearchTagCommand(String fullCommand) throws JakeException {
        assert fullCommand != null : "fullCommand should not be null";

        String searchTerm = fullCommand.substring("search".length()).trim();
        if (searchTerm.isEmpty()) {
            throw new JakeException("Search term cannot be empty");
        }

        if (searchTerm.startsWith("#")) {
            return searchTerm.substring(1).trim();
        } else {
            return searchTerm;
        }
    }
}
