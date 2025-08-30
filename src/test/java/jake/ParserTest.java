package jake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ParserTest {
    @Test
    public void getCommandWord_validCommands_returnsCorrectCommand() {
        assertEquals("todo", Parser.getCommandWord("todo buy milk"));
        assertEquals("deadline", Parser.getCommandWord("deadline buy milk /2025-01-01T02:00:01"));
        assertEquals("event", Parser.getCommandWord("event buy milk /2025-01-01T02:00:01 /2025-01-02T02:00:00"));
        assertEquals("list", Parser.getCommandWord("list"));
        assertEquals("bye", Parser.getCommandWord("bye"));
        assertEquals("mark", Parser.getCommandWord("mark 1"));
        assertEquals("unmark", Parser.getCommandWord("unmark 1"));
        assertEquals("delete", Parser.getCommandWord("delete 1"));
    }

    @Test
    public void parseTaskName_onlySpaces_throwsException() {
        JakeException exception = assertThrows(JakeException.class, (
                ) -> Parser.parseTaskName("todo   ", "todo"));
        assertEquals("Todo task must have a name", exception.getMessage());
    }

    @Test
    public void parseDeadlineCommand_validInput_returnsNameAndDate() throws JakeException {
        String[] result = Parser.parseDeadlineCommand("deadline homework /2023-12-25T23:59:59");
        assertEquals(2, result.length);
        assertEquals("homework", result[0]);
        assertEquals("2023-12-25T23:59:59", result[1]);
    }

    @Test
    public void parseDeadlineCommand_complexName_returnsCorrectParts() throws JakeException {
        String[] result = Parser.parseDeadlineCommand("deadline CS2103T individual project /2023-12-25T23:59:59");
        assertEquals(2, result.length);
        assertEquals("CS2103T individual project", result[0]);
        assertEquals("2023-12-25T23:59:59", result[1]);
    }

    @Test
    public void parseDeadlineCommand_noSlash_throwsException() {
        assertThrows(JakeException.class, (
        ) -> Parser.parseDeadlineCommand("deadline homework tomorrow"));
    }

    @Test
    public void parseDeadlineCommand_noName_throwsException() {
        assertThrows(JakeException.class, (
        ) -> Parser.parseDeadlineCommand("deadline /2023-12-25T23:59:59"));
    }

    @Test
    public void parseEventCommand_validInput_returnsNameAndDates() throws JakeException {
        String[] result = Parser.parseEventCommand("event meeting /2023-12-25T10:00:00 /2023-12-25T11:00:00");
        assertEquals(3, result.length);
        assertEquals("meeting", result[0]);
        assertEquals("2023-12-25T10:00:00", result[1]);
        assertEquals("2023-12-25T11:00:00", result[2]);
    }

    @Test
    public void parseEventCommand_complexName_returnsCorrectParts() throws JakeException {
        String[] result = Parser.parseEventCommand(
                "event team project meeting /2023-12-25T10:00:00 /2023-12-25T11:00:00");
        assertEquals(3, result.length);
        assertEquals("team project meeting", result[0]);
        assertEquals("2023-12-25T10:00:00", result[1]);
        assertEquals("2023-12-25T11:00:00", result[2]);
    }

    @Test
    public void parseEventCommand_missingSecondSlash_throwsException() {
        assertThrows(JakeException.class, (
                ) -> Parser.parseEventCommand("event meeting /2023-12-25T10:00:00"));
    }

    @Test
    public void parseEventCommand_noName_throwsException() {
        assertThrows(JakeException.class, (
                ) -> Parser.parseEventCommand("event /2023-12-25T10:00:00 /2023-12-25T11:00:00"));
    }

    @Test
    public void parseEventCommand_noSlash_throwsException() {
        assertThrows(JakeException.class, (
                ) -> Parser.parseEventCommand("event meeting tomorrow"));
    }
}
