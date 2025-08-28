public class Parser {

    public static String getCommandWord(String fullCommand) {
        return fullCommand.split(" ")[0];
    }

    public static String[] parseCommand(String fullCommand) {
        return fullCommand.split(" ", 2);
    }

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

    public static String parseTaskName(String fullCommand, String commandWord) throws JakeException {
        String name = fullCommand.substring(commandWord.length()).trim();
        if (name.isEmpty()) {
            throw new JakeException(commandWord.substring(0, 1).toUpperCase() +
                                    commandWord.substring(1) + " task must have a name");
        }
        return name;
    }

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
