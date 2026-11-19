# Oblig 1 – Computer Cluster

Mandatory assignment 1 in IN1010, spring 2022. The program models a computer
cluster (*dataklynge*) like UiO's **Saga**: a cluster consists of racks, and
each rack holds up to 12 nodes (individual computers). The program reads a
cluster description from a file and prints processor and memory statistics.

> The code is kept exactly as submitted in 2022, to show what I wrote at the
> time. Known limitations and deviations from the assignment are listed
> [below](#known-limitations-and-deviations).

## Assignment overview

- **Part A:** Draw the data structure after inserting three nodes into a new
  cluster, with at most 2 nodes per rack
  (see [the drawing](Oblig%201_v3.drawio.pdf)).
- **Part B:** Write the classes `Node`, `Rack` and `Dataklynge`.
- **Part C:** Add methods for the total number of processors and the number
  of nodes with at least a given amount of memory.
- **Part D:** Write a main program that builds the cluster `saga` and prints
  statistics.
- **Part E:** Read the cluster description from a file, and handle invalid
  data gracefully.

## Design

```
Dataklynge ──▶ ArrayList<Rack> ──▶ Rack ──▶ Node[12] ──▶ Node
```

| Class | Responsibility |
|-------|----------------|
| `Node` | A single computer with a number of processors and an amount of memory in GB. |
| `Rack` | Holds up to 12 nodes in an array. |
| `Dataklynge` | Holds a list of racks. New nodes go into the last rack, and a new rack is created when it is full. It also reads cluster data from a file and prints statistics. |
| `Hovedprogram` | Main program: creates the cluster `saga` and reads it from a file. |

## File format

Each line describes a group of identical nodes:

```
<number of nodes> <processors per node> <memory per node in GB>
```

Example (`Dataklynge1.txt`):

```
450 4 512
16 8 1024
```

Groups with more than 16 processors or more than 4096 GB (4 TB) of memory per
node are reported and skipped.

## How to run

Requires Java (JDK). From inside this folder:

```bash
javac *.java
java Hovedprogram
```

The file name is hardcoded in `Hovedprogram.java` (`Dataklynge2.txt`). To
test another file, change the file name there and compile again.

## Expected output

| File | Racks | Processors | ≥ 128 GB | ≥ 512 GB | ≥ 1024 GB |
|------|------:|-----------:|---------:|---------:|----------:|
| `Dataklynge1.txt` | 39 | 1928 | 466 | 466 | 16 |
| `Dataklynge2.txt` | 13 | 1112 | 148 | 146 | 130 |
| `Dataklynge3.txt` | 13 | 1400 | 100 | 50 | 50 |
| `Dataklynge4.txt` | 1 | 15 | 6 | 6 | 0 |

`Dataklynge4.txt` contains invalid data on purpose. Line 3 has 17 processors
and line 4 has 8192 GB of memory, so both groups are reported and skipped
before the statistics are printed.

## Known limitations and deviations

Looking back at the code, these are the cases it doesn't handle and the places
where it differs from what the assignment asked for.

**Deviations from the assignment**

- The assignment asks for `Rack` to add a node only if there is room.
  `Rack.leggTilNode` doesn't check this itself. The check happens in
  `Dataklynge.settInnNode` instead, so the program never overfills a rack,
  but calling `leggTilNode` directly on a full rack would throw an
  `ArrayIndexOutOfBoundsException`.
- The assignment asks for a method called `noderMedNokMinne(int paakrevdMinne)`
  in `Dataklynge`. In my code it's called `noderMedNokMinneIRack`.
- The assignment suggests taking the file name as a parameter. In my code it
  is hardcoded in `Hovedprogram`.

**Unhandled cases when reading the file**

- The loop checks `hasNextInt()` once and then reads three numbers. A line
  with fewer than three numbers makes the program crash with a
  `NoSuchElementException`.
- If the file contains text that isn't a number, reading stops silently and
  the rest of the file is ignored.
- Negative or zero values (e.g. `-5 4 512` or `10 0 512`) are accepted
  without a warning.
- `linjenummer` counts groups of three numbers, not actual lines in the file,
  so the reported line number can be wrong if the file isn't formatted
  exactly one group per line.
- One error message is missing a space and prints e.g. `på linje3`.

**Design**

- The maximum number of nodes per rack (12) is defined in both `Rack` and
  `Dataklynge`, so changing one without the other would cause errors.
- `maksPros` and `maksMinne` are `static` but not `final`, even though they
  are constants.
- `lesFraFil` both reads the file and prints the statistics, instead of
  leaving the printing to the main program.

**The drawing (Part A)**

- Some names in the drawing differ from the code, e.g. `settInnNodeIRack`,
  `noderMNokMinne`, `antProsTotalt`, `getAntNode` and `antPros`.
- `noderNokMinne` is drawn as an instance variable in `Rack`, but in the code
  it's a method. The counter `teller` is missing from the drawing.
- `maksMinne` is shown as 1024, but the code uses 4096 (4 TB).

## Concepts covered

- Arrays (`Node[]`) and `ArrayList`
- Encapsulation with private instance variables and public methods
- Delegating work through a hierarchy of objects
- Reading files with `Scanner` and handling `FileNotFoundException`
- Basic input validation
