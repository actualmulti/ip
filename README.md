# Jake 🐾
A friendly, lightweight task assistant that runs on your desktop (JavaFX GUI + persistent storage).  
Track tasks, deadlines, events, and tags — all via simple natural-ish commands.

Declaration: This README was made with the assistance of AI.

---

## Table of Contents
1. What Jake Does
2. Quick Start
3. Installation (Jar / Source / IDE)
4. Running Jake
5. Commands (Cheat Sheet)
6. Command Details
7. Tags & Search
8. Data Storage
9. GUI Features
10. Error Messages & Validation
11. Development & Building
12. Testing

---

## 1. What Jake Does
Jake helps you:
- Add three kinds of tasks: Todo, Deadline, Event
- Mark / unmark tasks as done
- Tag and untag tasks with inline hashtags
- Search by keyword or by tag
- Persist everything automatically across runs (`data/jake.txt`)
- Interact through a clean JavaFX chat-style GUI with circular profile avatars

Designed for: Students who want a minimal assistant without cloud sync overhead.

---

## 2. Quick Start
If you just want to run it:

```bash
# From project root (after cloning)
./gradlew shadowJar
java -jar build/libs/jake.jar
```

You'll see a GUI window. Type a command like:
```
todo buy milk #errand
```

---

## 3. Installation

### Option A: Download / Build the Jar
1. Ensure you have Java 17+  
   Check:
   ```bash
   java -version
   ```
2. Build:
   ```bash
   ./gradlew shadowJar
   ```
3. Jar will appear at:
   ```
   build/libs/jake.jar
   ```

### Option B: Run from Source (IDE)
1. Import project as a Gradle project in IntelliJ / VS Code.
2. Run `Launcher` (GUI) or `Main` if you have a CLI variant retained (optional).
3. If JavaFX runtime issues occur, ensure Gradle handles dependencies (no manual module args needed here).

---

## 4. Running Jake

```bash
java -jar build/libs/jake.jar
```

To override the save file location (optional future enhancement suggestion), you’d pass a flag (not yet implemented):
```
java -jar jake.jar --data mytasks.txt
```

Exit the app with:
```
bye
```
(or closing the window)

---

## 5. Commands (Cheat Sheet)

| Command | Format | Example |
|---------|--------|---------|
| Add Todo | `todo NAME [#tag1 #tag2 ...]` | `todo refill water #health` |
| Add Deadline | `deadline NAME [#tags] /YYYY-MM-DDTHH:MM:SS` | `deadline CS2103T iP #school /2025-03-15T23:59:59` |
| Add Event | `event NAME [#tags] /START /END` | `event project sync #work /2025-03-15T10:00:00 /2025-03-15T11:00:00` |
| List tasks | `list` | `list` |
| Mark done | `mark INDEX` | `mark 2` |
| Unmark | `unmark INDEX` | `unmark 2` |
| Delete | `delete INDEX` | `delete 3` |
| Find by keyword | `find KEYWORD` | `find milk` |
| Tag task | `tag INDEX #tag1 #tag2 ...` | `tag 4 #urgent #today` |
| Untag task | `untag INDEX #tag1 ...` | `untag 4 #today` |
| Search by tag(s) | `search #tag1 [#tag2 ...]` | `search #urgent` |
| Exit | `bye` | `bye` |

Index values come from the current `list` ordering (1-based).

---

## 6. Command Details

### Todos
```
todo read book #leisure #longterm
```
Adds a basic task.

### Deadlines
```
deadline submit report #work #q1 /2025-01-10T23:59:59
```
Uses an ISO-like timestamp. Internally validated; malformed formats trigger an error.

### Events
```
event workshop #training /2025-02-01T09:00:00 /2025-02-01T12:00:00
```

### Mark / Unmark
```
mark 5
unmark 5
```

### Delete
```
delete 3
```

### Find (Substring Match)
```
find milk
```

### Tag / Untag
```
tag 2 #important #deepwork
untag 2 #deepwork
```

### Search by Tag(s)
```
search #important
search #important #work   (matches tasks containing ALL listed tags)
```

### Exit
```
bye
```
Triggers graceful save + window close.

---

## 7. Tags & Search
- Inline tagging on creation (todo/deadline/event) OR add later via `tag`.
- Tags are stored and displayed when present.
- `search` expects `#` prefixes.
- Case sensitivity: (state your current implementation — if not enforced, assume case-sensitive or convert to lowercase consistently; adjust if you later normalize).

---

## 8. Data Storage
File: `data/jake.txt` (auto-created if missing).  
Format: One line per task with a pipe-delimited structure:
```
TYPE | DONE_FLAG | NAME | DATE(S)... | TAGS
```
Examples (illustrative):
```
T | 0 | buy milk | #errand #today
D | 1 | CS2103T iP | 2025-03-15T23:59:59 | #school
E | 0 | workshop | 2025-02-01T09:00:00 | 2025-02-01T12:00:00 | #training
```
Corrupt lines are (currently) likely to throw an exception (future enhancement: skip + warn).

---

## 9. GUI Features
- Chat-style layout: user messages vs. Jake replies.
- Circular profile avatars (applied via a runtime `Circle` clip in `DialogBox`).
- Auto-scrolls to latest interaction.
- Wrap text for long task names.
- Graceful shutdown with small delay after `bye` (if implemented in `MainWindow`).

### Customizing Avatars
Replace the images in:
```
src/main/resources/images/
```
Prefer transparent PNGs for best visual blending.

---

## 10. Error Messages & Validation
Typical errors:
- Missing task name: “Todo task must have a name”
- Malformed deadline/event syntax: “Deadline task must have a valid name and/or date!”
- Invalid index: “Please provide a valid task index.”
  (Adjust this section if you refine wording.)

---

## 11. Development & Building

### Build All
```bash
./gradlew clean build
```

### Run Tests
```bash
./gradlew test
```

### Checkstyle
```bash
./gradlew check
```

### Fat / Shadow Jar
```bash
./gradlew shadowJar
```
Output:
```
build/libs/jake.jar
```

---

## 12. Testing
Text-based regression tests live in:
```
text-ui-test/
```
To run the scripted test (Unix/macOS):
```bash
cd text-ui-test
bash runtest.sh
```
JUnit tests: `./gradlew test`  
Reports: `build/reports/tests/test/index.html`
