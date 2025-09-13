# Jake User Guide


![Jake UI Example](./Ui.png?raw=true)

Jake is a lightweight, chat-style desktop task assistant (Java + JavaFX).  
It lets you quickly add and manage:
- Todos
- Deadlines (with date/time)
- Events (with start & end)
- Tags (inline or added later)
- Powerful search (by keyword or tags)

All tasks are saved automatically between sessions.

---

## Adding deadlines

Adds a task that must be completed by a specific date/time.

You can include tags inline using `#tag` tokens (optional).  
Dates use an ISO-like format: `YYYY-MM-DDTHH:MM:SS`

Examples:  
`deadline submit report /2025-03-15T23:59:59`  
`deadline project phase 1 #school #important /2025-10-01T18:00:00`

Format:  
`deadline TASK_NAME [#tag1 #tag2 ...] /YYYY-MM-DDTHH:MM:SS`

Expected outcome: The deadline task is stored, numbered, and shown in the list.

```
Deadline task has been added:
  [D][ ] submit report (by: 2025-03-15T23:59:59)
You now have 3 tasks in the list.
```

If the format is invalid (e.g. missing `/` segment or empty name), you’ll see an error like:
```
Deadline task must have a valid name and/or date!
```

---

## Feature ABC – Tagging Tasks

Tag tasks to group and filter them later.

You can:
1. Add tags inline when creating a task:  
   `todo buy milk #errand #today`
2. Add tags afterward:  
   `tag 2 #urgent #focus`
3. Remove specific tags:  
   `untag 2 #focus`

Rules:
- Tags start with `#`
- A task can hold multiple tags
- Re-adding an existing tag is ignored

Example session:
```
todo read book #leisure
tag 1 #longterm
untag 1 #leisure
```

Expected output excerpt:
```
Todo task has been added:
  [T][ ] read book | #leisure
Tag(s) added: #longterm
Tag(s) removed: #leisure
```

---

## Feature XYZ – Searching by Tags

Search for tasks that contain one or more tags.

Format:
`search #tag1 [#tag2 ...]`

Behavior:
- Returns only tasks that contain ALL listed tags (logical AND).
- Order of tags doesn’t matter.
- If no tasks match, a friendly “No matching tasks” style message appears.

Examples:
```
search #urgent
search #urgent #work
```

Expected output (example):
```
Here are the matching tasks:
1. [D][ ] project report (by: 2025-03-15T23:59:59) | #work #urgent
2. [T][X] fix prod bug | #work #urgent #today
```

No results example:
```
No tasks found with the specified tag(s).
```

---

## Additional Useful Commands (Overview)

| Action | Command Example |
|--------|------------------|
| Add todo | `todo plan trip #fun` |
| Add deadline | `deadline taxes /2025-04-15T23:59:59` |
| Add event | `event retreat /2025-06-10T09:00:00 /2025-06-10T17:00:00` |
| List tasks | `list` |
| Mark / unmark | `mark 3`, `unmark 3` |
| Delete | `delete 2` |
| Find by keyword | `find report` |
| Search tags | `search #school #q1` |
| Exit | `bye` |

---

## Data Persistence

All tasks are auto-saved to `data/jake.txt`.  
You can safely quit with `bye` or by closing the window; state is restored next launch.

---

## AI Assistance

Some GUI polish (e.g. circular avatar clipping) and parts of this documentation were drafted with AI support and then manually reviewed.

---

Enjoy using Jake! 🐾