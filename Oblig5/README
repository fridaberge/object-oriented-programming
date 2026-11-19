# Immune Repertoire Analysis with Java Threads

Mandatory assignment 5 in **IN1010 – Object-Oriented Programming**,
University of Oslo (spring 2022).

The program analyses *immune repertoires*: the DNA sequences of immune
receptors in a person's blood. It looks for short patterns that are
much more common in people who have had a certain virus than in people
who have not. Each person is one file, and each line in the file is
one receptor sequence written as capital letters. The patterns are
3-letter subsequences. The same approach can in principle be used to
detect infections such as SARS-CoV-2.

## Assignment overview

| Part | Tasks | Folder | Description |
|---|---|---|---|
| 2A | 6–8 | `Oblig5Del2A/` | Parallel reading: a monitor (`Monitor1`) built around the register using composition, and a reader thread (`LeseTrad`) for each file. The main thread does the merging |
| 2B | 9–11 | `Oblig5Del2B/` | Parallel merging: a full monitor (`Monitor2`) and merge threads (`FletteTrad`) that take out two maps, merge them and put the result back until one map remains |
| Full | 12 | `Oblig5Hele/` | Complete program: splits people into infected and healthy groups, uses one monitor per group, and reports the subsequences that are much more common in the infected group |

### Task details
1. **`Subsekvens`**: a subsequence (a constant `String`) and a count,
   printed as `(ABC,4)`.
2. **`SubsekvensRegister`**: a container of
   `HashMap<String, Subsekvens>`, with methods to insert a map, remove
   any map and get the number of maps.
3. **File reading**: a static method reads one person's file and
   returns a map of all *distinct* 3-letter subsequences, each with
   count 1. How often a pattern appears within one person doesn't
   matter.
4. **Merging**: a static method merges two maps and adds up the counts
   of subsequences found in both.
5. **Sequential test program**: reads all files listed in
   `metadata.csv`, merges everything into one map and prints the most
   common subsequence.
6. **`Monitor1`**: a thread-safe wrapper around the register, built by
   composition.
7. **`LeseTrad`**: a thread that reads one file and inserts the
   resulting map into the monitor.
8. **`Oblig5Del2A`**: reads with one thread per file, then merges in
   the main thread.
9. **`FletteTrad`**: a thread that repeatedly takes out two maps,
   merges them and puts the result back.
10. **`Monitor2`**: a monitor that protects the register against both
    reader and merge threads, and works out when only one map is left.
11. **`Oblig5Del2B`**: the same output as task 8, now with parallel
    merging.
12. **`Oblig5Hele`**: two `Monitor2` objects (infected and healthy),
    reader and merge threads for both, and a comparison of the two
    final maps.

*Optional:* a one-sided binomial test (Apache Commons Math) to choose
dominant patterns by statistical significance. This is **not
implemented**. The program uses the simpler required method: the
difference in counts.

## Key concepts demonstrated
- Encapsulation and composition: the monitor wraps the register
  instead of inheriting from it
- Creating and coordinating threads with `Runnable`, `Thread` and
  `join()`
- Monitors with `ReentrantLock` to protect shared data
- A two-phase parallel pipeline: parallel reading, then parallel
  merging
- Hash-based counting with `HashMap`

## How it works (`Oblig5Hele`)

### 1. Reading phase
`metadata.csv` lists each data file and whether the person had the
virus (`True`/`False`). A `LeseTrad` thread is started for every file.
It:
- reads the sequences in the file
- extracts every 3-letter subsequence, e.g. `ABCD` gives `ABC` and
  `BCD`
- counts each subsequence **once per person**
- inserts the resulting map into the monitor for the infected group or
  the healthy group

The main thread waits for all reader threads with `join()`.

### 2. Merging phase
For each monitor, a number of `FletteTrad` threads run in parallel.
Each one repeatedly:
1. takes two maps out of the monitor (under lock)
2. merges them outside the lock
3. puts the merged map back (under lock)

The threads stop when fewer than two maps remain. Each monitor then
holds one map with the number of people who had each subsequence.
Merging happens outside the lock, so several threads can do the
expensive work at the same time.

### 3. Analysis
The two final maps are compared, and the program prints:
- the subsequence with the largest difference in favour of the
  infected group
- all subsequences that occur in at least 7 more infected people than
  healthy people

## Class overview

| Class | Responsibility |
|---|---|
| `Oblig5Hele` | Entry point: reads metadata, starts threads, runs the analysis |
| `Monitor2` | Thread-safe wrapper around the register (`ReentrantLock`) |
| `SubsekvensRegister` | Stores maps, reads files and merges maps |
| `Subsekvens` | A subsequence and its count |
| `LeseTrad` | Reader thread: turns one file into one map |
| `FletteTrad` | Merge thread: turns two maps into one |

### Glossary (the code uses Norwegian identifiers)
| Norwegian | English |
|---|---|
| subsekvens | subsequence |
| les / lese | read |
| flette | merge |
| tråd / trad | thread |
| syk / ikkeSyk | infected / healthy |
| settInn / taUt | insert / remove |
| antall | count |
| oek | increment |
| laas | lock |

## Running

Each part is a separate program. Compile it and run it from its own
folder:

    cd Oblig5Del1        # or Oblig5Del2A / Oblig5Del2B
    javac *.java
    java Oblig5Del1 <data folder>

    cd Oblig5Hele
    javac *.java
    java Oblig5Hele <data folder> <number of merge threads>

Example: `java Oblig5Hele TestData 8`

The task asked for the number of merge threads to be a constant (8).
In the full program I made it a command-line argument instead, so
different thread counts can be tried.

Each data folder must contain `metadata.csv` and the files it lists.

### Expected results
| Program | Data folder | Result |
|---|---|---|
| Part 1 / 2A / 2B | `TestDataLitenLike` | `ASS` with 3 occurrences |
| Part 1 / 2A / 2B | `TestDataLike` | `QYF` with 9 occurrences |
| Full | `TestDataLiten` | `ABC`, 2 more in the infected group |
| Full | `TestData` | `GAE`, 5 more in the infected group |
| Full | `Data` (real data) | all subsequences with a difference of 7 or more |

Note: the full real dataset is not included in this repository.

## Reflections
I wrote this as a student in 2022. Looking back, these are the things
I would improve:
- **Encapsulation:** the task said `antall` should be `private`, but
  it is `public` and is set directly in `printDeStorste`. Several
  other fields are also `public` and should be private.
- **Waiting in the monitor:** task 10 asked for the method that takes
  out two maps to *wait* while fewer than two are available. My
  `hentUtTo()` returns `null` instead. This is correct only because
  all reading finishes before merging starts. If reading and merging
  overlapped, it would need `await()` together with a counter to
  detect the last merge. A `Condition` is created and signalled but
  never awaited.
- **Shared static lock:** the lock in `Monitor2` is `static`, so both
  monitors use the same lock, and the two groups can't be merged at
  the same time.
- **Short lines:** the task said the program should stop on lines
  shorter than 3 characters. My code skips them without a message.
- **Input validation:** a missing folder or a thread count that isn't
  a number makes the program crash instead of showing a clear error
  message.
- **File parsing:** subsequences could be counted once per person by
  checking `containsKey` directly. The current code rebuilds a
  `HashSet` for every position in the line.
- **Ties:** if several subsequences share the largest difference, only
  one of them is reported.
- **Cleanup:** there are unused helper methods and commented-out code
  left over from development.
- **Statistics:** the optional binomial test would be a better way to
  choose dominant patterns than a fixed difference threshold.
