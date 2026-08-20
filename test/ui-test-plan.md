# Aider UI Test Plan

## Test configuration

- Java version: 25
- Entry point: `src/main/java/Aider.java`
- Compile command: `javac -d out/production/ip src/main/java/*.java`
- Run command: `java -cp out/production/ip Aider`
- Input: one command per line, in the order shown for each test case
- Comparison: compare chatbot output exactly, excluding terminal echo of typed input
- Failure policy: stop the current test session immediately at the first mismatch

## Test case: Start and exit

Aim: Verify that Aider starts with its greeting and exits when the user enters `bye`.

Commands:

1. Input: `bye`
   Expected output:
   ```text
   Bye. Hope to see you again soon!
   ```

## Test case: Add and list tasks

Aim: Verify that ordinary text is stored as a task and displayed in numbered order by `list`.

Commands:

1. Input: `read book`
   Expected output:
   ```text
   added: read book
   ```
2. Input: `return book`
   Expected output:
   ```text
   added: return book
   ```
3. Input: `list`
   Expected output:
   ```text
   Here are the tasks in your list:
   1.[ ] read book
   2.[ ] return book
   ```
4. Input: `bye`
   Expected output:
   ```text
   Bye. Hope to see you again soon!
   ```

## Test case: Mark and unmark tasks

Aim: Verify that `mark N` sets a task to done and `unmark N` reverses it.

Commands:

1. Input: `read book`
   Expected output:
   ```text
   added: read book
   ```
2. Input: `return book`
   Expected output:
   ```text
   added: return book
   ```
3. Input: `mark 2`
   Expected output:
   ```text
   Nice! I've marked this task as done:
     [X] return book
   ```
4. Input: `list`
   Expected output:
   ```text
   Here are the tasks in your list:
   1.[ ] read book
   2.[X] return book
   ```
5. Input: `unmark 2`
   Expected output:
   ```text
   OK, I've marked this task as not done yet:
     [ ] return book
   ```
6. Input: `list`
   Expected output:
   ```text
   Here are the tasks in your list:
   1.[ ] read book
   2.[ ] return book
   ```
7. Input: `bye`
   Expected output:
   ```text
   Bye. Hope to see you again soon!
   ```
