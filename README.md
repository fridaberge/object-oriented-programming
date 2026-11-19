# IN1010 – Object-Oriented Programming (Spring 2022)

My solutions to the assignments (*obligatoriske oppgaver*, or
"obliger") in **IN1010 – Object-Oriented Programming** at the
University of Oslo, spring 2022.

The course teaches object-oriented programming in Java: classes and
objects, inheritance, polymorphism, interfaces, generic data
structures, threads and recursion. The assignments get harder as the
course goes on, and the later ones build on concepts from the earlier
ones.

## Assignments

| Assignment | Topic | Key concepts |
|---|---|---|
| [Oblig0](Oblig0/) | Cars and people | Classes, objects, constructors, references |
| [Oblig1](Oblig1/) | Computer cluster | Encapsulation, arrays, `ArrayList`, file reading, error handling |
| [Oblig2](Oblig2/) | Medicines, prescriptions and doctors | Inheritance, abstract classes, interfaces, unit testing |
| [Oblig3](Oblig3/) | Generic linked lists | Generics, linked lists, stacks, queues, priority queues, exceptions |
| Oblig4 | *Not included* | See below |
| [Oblig5](Oblig5/) | Immune repertoire analysis with threads | Threads, monitors, `ReentrantLock`, `HashMap`, composition |
| [Oblig6](Oblig6/) | Maze solver | Recursion, polymorphism, abstract classes, a linked grid |

Each folder has its own README with the task description, an
explanation of the solution, how to run it, and some thoughts on what
I would do differently today. Most assignments also include the
required drawings: class hierarchies and data structure diagrams.

### Oblig0 – Cars and people
A warm-up assignment. It was optional, but students were strongly
encouraged to do it. Over three steps I build a car class: first one
that only prints a message, then one with a registration number set
by the constructor, and finally one with a getter method. The last
step adds a `Person` class that holds a reference to the car the
person owns. Each step has its own main program (`BilBruk1`–`BilBruk3`).
There's also a drawing of the data structure once a person and their
car have been created.

### Oblig1 – Computer cluster
A program that keeps track of the parts of a computer cluster like
UiO's *Saga*. A `Dataklynge` (cluster) holds a list of `Rack`s. Each
rack holds an array of up to 12 `Node`s, and each node has a number of
processors and an amount of memory. When a node is added, it goes into
the first rack with free space, and a new rack is created if they are
all full. The cluster can count its total number of processors and
the number of nodes with at least a given amount of memory. The final
main program reads the cluster setup from a file. If the file contains
invalid data, such as too many processors or too much memory in a
node, the program stops with an error message instead of crashing.

### Oblig2 – Medicines, prescriptions and doctors
The first part of a three-assignment series (Oblig2–4) that builds a
system for doctors, patients, prescriptions and medicines.
- **Medicines:** an abstract `Legemiddel` class with the subclasses
  `Narkotisk` (narcotic), `Vanedannende` (habit-forming) and `Vanlig`
  (regular).
- **Prescriptions:** an abstract `Resept` class split into white and
  blue prescriptions. White ones have the subclasses `MilResept`
  (military) and `PResept` (contraceptive). Each type calculates the
  price the patient pays in its own way.
- **Doctors:** `Lege` (doctor) and `Spesialist` (specialist). Only
  specialists may prescribe narcotics, so they implement the
  `Godkjenningsfritak` (prescribing exemption) interface.

Every object gets a unique ID per class from a static counter. The
classes are tested with unit tests (`TestLegemiddel`,
`TestResepter`) and with an integration test in a main program.

### Oblig3 – Generic linked lists
Generic list classes written from scratch without Java's built-in
lists. They were reused in Oblig4. All of them implement an interface
`Liste<T>`:
- `Lenkeliste<T>`: an abstract linked list where elements are added at
  the end and removed from the start
- `Stabel<T>`: a stack (last in, first out)
- `Koe<T>`: a queue (first in, first out)
- `IndeksertListe<T>`: a list where you can insert, replace, get and
  remove elements at any position
- `Prioritetskoe<T extends Comparable<T>>`: a list that stays sorted,
  so the smallest element always comes out first

If you use an invalid index or try to remove from an empty list, the
classes throw a custom exception, `UgyldigListeindeks`. The solution
passes the tests provided by the course.

### Oblig4 – Not included
Oblig4 continued the prescription system from Oblig2 and Oblig3. I
did it together with another student, so it isn't included here.

### Oblig5 – Immune repertoire analysis with threads
The program analyses immune repertoires (DNA sequences of immune
receptors) to find short patterns that are much more common in people
who have had a certain virus. Files are read in parallel by reader
threads. The resulting `HashMap`s are then merged in parallel by
merge threads, with monitors protecting the shared data. The
assignment is solved in steps, ending with a full analysis that
compares an infected group with a healthy group.

### Oblig6 – Maze solver
The program reads a maze from a file and finds every exit that can be
reached from a given starting cell. The search is recursive: each
cell as
