package jake.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class representing a task in the Jake task management system.
 * Provides common functionality for all task types including completion status tracking,
 * name management, and basic operations like marking done/undone.
 *
 * Subclasses must implement the toFileString() method to define their file storage format.
 */
public abstract class Task {
    protected final String name;
    protected boolean isDone;
    protected List<String> tags;

    /**
            * Creates a new task with the specified name.
     * The task is initially marked as not done.
            *
            * @param name the name or description of the task
     */
    public Task(String name) {
        this.name = name;
        this.isDone = false;
        this.tags = new ArrayList<>();
    }

    public boolean isDone() {
        return isDone;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void unmarkDone() {
        this.isDone = false;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Adds a tag to this task.
     * @param tag the tag to add
     */
    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty() && !this.tags.contains(tag.trim())) {
            tags.add(tag.trim());
        }
    }

    /**
     * Removes a tag from this task.
     * @param tag the tag to remove
     */
    public void removeTag(String tag) {
        tags.remove(tag);
    }

    /**
     * Gets all tags for this task.
     * @return a copy of the tags list
     */
    public List<String> getTags() {
        return new ArrayList<>(tags);
    }

    /**
     * Checks if this task has the specified tag.
     * @param tag the tag to check for
     * @return true if the task has the tag, false otherwise
     */
    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    /**
     * Sets the tags for this task (used for loading from storage).
     * @param tags the list of tags to set
     */
    public void setTags(List<String> tags) {
        this.tags = new ArrayList<>(tags);
    }

    /**
     * Returns a formatted string of tags for display.
     * @return formatted tags string, e.g., "#work #urgent" or empty string if no tags
     */
    protected String getTagsDisplay() {
        if (tags.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String tag : tags) {
            sb.append("#").append(tag).append(" ");
        }
        return sb.toString().trim();
    }

    public abstract String toFileString();

    @Override
    public String toString() {
        String marker;
        if (this.isDone()) {
            marker = "X";
        } else {
            marker = " ";
        }
        return String.format("[%s] %s", marker, this.name);
    }
}
