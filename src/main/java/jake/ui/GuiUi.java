package jake.ui;

import java.util.List;

import jake.TaskList;
import jake.task.DeadlineTask;
import jake.task.EventTask;
import jake.task.Task;
import jake.task.Todo;

/**
 * GUI-adapted version of Ui that returns formatted strings instead of printing to console.
 * Reuses all the formatting logic from the original Ui class.
 */
public class GuiUi {

    /**
     * Returns the welcome message for the application.
     * @return formatted welcome message
     */
    public String getWelcomeMessage() {
        return "Hello! I'm Jake\nWhat can I do for you today?";
    }

    /**
     * Returns the goodbye message.
     * @return formatted goodbye message
     */
    public String getGoodbyeMessage() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Returns a formatted error message.
     * @param message the error message to display
     * @return formatted error message
     */
    public String getErrorMessage(String message) {
        return "Error: " + message;
    }

    /**
     * Returns the invalid command message.
     * @return formatted invalid command message
     */
    public String getInvalidCommandMessage() {
        return "Invalid task!!! Try another one";
    }

    /**
     * Returns a formatted message for when a task is added.
     * @param task the task that was added
     * @param totalTasks the total number of tasks after adding
     * @return formatted task added message
     */
    public String getTaskAddedMessage(Task task, int totalTasks) {
        return String.format("%s task has been added:\n%s\nNow you have %d tasks in the list.",
                getTaskTypeString(task), task.toString(), totalTasks);
    }

    /**
     * Returns a formatted message for when a task is deleted.
     * @param task the task that was deleted
     * @param remainingTasks the number of tasks remaining after deletion
     * @return formatted task deleted message
     */
    public String getTaskDeletedMessage(Task task, int remainingTasks) {
        return String.format("Noted. I've removed this task:\n%s\nNow you have %d tasks in the list.",
                task.toString(), remainingTasks);
    }

    /**
     * Returns a formatted string showing the complete list of tasks.
     * @param tasks the TaskList containing all tasks to display
     * @return formatted task list
     */
    public String getTaskListMessage(TaskList tasks) {
        if (tasks.size() == 0) {
            return "Your task list is empty!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d. %s\n", i + 1, tasks.get(i).toString()));
        }
        return sb.toString().trim();
    }

    /**
     * Returns a formatted message for when a task is marked as done.
     * @param task the task that was marked
     * @return formatted task marked message
     */
    public String getTaskMarkedMessage(Task task) {
        return String.format("Nice! I've marked this task as done:\n%s", task.toString());
    }

    /**
     * Returns a formatted message for when a task is unmarked.
     * @param task the task that was unmarked
     * @return formatted task unmarked message
     */
    public String getTaskUnmarkedMessage(Task task) {
        return String.format("OK, I've marked this task as not done yet:\n%s", task.toString());
    }

    /**
     * Returns a formatted string showing search results.
     * @param tasks the TaskList containing search results
     * @param searchTerm the term that was searched for
     * @return formatted search results
     */
    public String getSearchResultsMessage(TaskList tasks, String searchTerm) {
        if (tasks.size() == 0) {
            return String.format("No tasks found matching '%s'.", searchTerm);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Here are the tasks matching '%s':\n", searchTerm));
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d. %s\n", i + 1, tasks.get(i).toString()));
        }
        return sb.toString().trim();
    }

    /**
     * Returns a formatted message for when a tag is added to a task.
     * @param task the task that received the tag
     * @param tag the tag that was added
     * @return formatted tag added message
     */
    public String getTagAddedMessage(Task task, String tag) {
        return String.format("Tag '%s' added to task:\n%s", tag, task.toString());
    }

    /**
     * Returns a formatted message for when a tag is removed from a task.
     * @param task the task from which the tag was removed
     * @param tag the tag that was removed
     * @return formatted tag removed message
     */
    public String getTagRemovedMessage(Task task, String tag) {
        return String.format("Tag '%s' removed from task:\n%s", tag, task.toString());
    }

    /**
     * Returns a formatted message for when a tag is not found on a task.
     * @param task the task that doesn't have the tag
     * @param tag the tag that was not found
     * @return formatted tag not found message
     */
    public String getTagNotFoundMessage(Task task, String tag) {
        return String.format("Tag '%s' not found on task:\n%s", tag, task.toString());
    }

    /**
     * Returns a formatted message for when all tags are removed from a task.
     * @param task the task from which all tags were removed
     * @param removedTags the list of tags that were removed
     * @return formatted all tags removed message
     */
    public String getAllTagsRemovedMessage(Task task, List<String> removedTags) {
        StringBuilder sb = new StringBuilder();
        sb.append("All tags removed from task:\n");
        if (!removedTags.isEmpty()) {
            sb.append("Removed tags: ");
            for (int i = 0; i < removedTags.size(); i++) {
                sb.append(removedTags.get(i));
                if (i < removedTags.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        sb.append("Updated task: ").append(task.toString());
        return sb.toString();
    }

    /**
     * Returns a formatted string showing matching tasks from find command.
     * @param tasks the TaskList containing matching tasks
     * @return formatted search results
     */
    public String getFindResultsMessage(TaskList tasks) {
        if (tasks.size() == 0) {
            return "No matching tasks found in your list.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the matching tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d. %s\n", i + 1, tasks.get(i).toString()));
        }
        return sb.toString().trim();
    }

    /**
     * Determines the display string for different task types.
     * @param task the task whose type string is needed
     * @return a string representing the task type for display purposes
     */
    private String getTaskTypeString(Task task) {
        if (task instanceof Todo) {
            return "Todo";
        } else if (task instanceof DeadlineTask) {
            return "Deadline";
        } else if (task instanceof EventTask) {
            return "Event";
        }
        return "Task";
    }
}
