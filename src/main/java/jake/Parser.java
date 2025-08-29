package jake;

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
     * Extracts the task name from a command string by removing the command word.
     *
     * @param fullCommand the complete command string
     * @param commandWord the command word to be removed from the beginning
     * @return the task name after removing the command word and trimming whitespace
     * @throws JakeException if the task name is empty after removing the command word
     */
    public static String parseTaskName(String fullCommand, String commandWord) throws JakeException {
        String name = fullCommand.substring(commandWord.length()).trim();
        if (name.isEmpty()) {
            throw new JakeException(commandWord.substring(0, 1).toUpperCase() +
                                    commandWord.substring(1) + " task must have a name");
        }
        return name;
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
     * Expected format: "event [task name] /[start date] /[end date]"
     *
     * @param fullCommand the complete event command string
     * @return an array containing the task name at index 0, start date at index 1, and end date at index 2
     * @throws JakeException if the command format is invalid or missing required components
     */
    public static String[] parseEventCommand(String fullCommand) throws JakeException {
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
}
