# Maze Solver with Recursion in Java

Mandatory assignment 6 in **IN1010 – Object-Oriented Programming**,
University of Oslo (spring 2022).

The program reads a maze from a file and uses **recursion** and
**polymorphism** to find every opening (exit) that can be reached
from a given starting cell. The user enters start coordinates one at
a time, and the program prints the exits that can be reached from
each one.

## Assignment overview

A maze is a grid of square cells. Each cell is either **white** (open)
or **black** (wall). Two cells are neighbours if they share a side, so
a cell has up to four neighbours. White cells on the edge of the maze
are **openings**. A path from a cell to an opening is a **way out**.

A maze is **cyclic** if some path visits the same cell more than once,
and **acyclic** otherwise. Only acyclic mazes are required.

| Part | Description |
|---|---|
| A – Classes and data structure | `Labyrint` holds a 2D array of cells. `Rute` is the abstract superclass of all cells and knows its coordinates, its maze and its four neighbours. `HvitRute` and `SortRute` are its subclasses, and `Aapning` is a subclass of `HvitRute` |
| B – Reading from file | The `Labyrint` constructor reads the file, creates all cell objects and then connects each cell to its neighbours |
| C – Solving with recursion | `finn(Rute fra)` asks every neighbour except the one the call came from to keep searching. Each subclass implements `finn` differently. Openings report themselves, and black cells stop the search |
| Main program | `Oblig6` loads a maze and runs a command loop. The user enters `<row> <column>` and the program prints all reachable openings. `-1` quits |

### File format

    7 9
    #########
    #........
    #.#######
    #.......#
    #######.#
    ........#
    #########

- The first line has the number of rows and the number of columns.
- `.` is a white cell and `#` is a black cell.
- Coordinates are written `(row, column)`, and `(0,0)` is the top left
  corner.

### Optional extensions (not implemented)
- Debug output that numbers the cells in the order they are visited
- Storing and printing the whole path to each opening
- Handling cyclic mazes by marking cells as visited
- Finding the shortest way out

## Key concepts demonstrated
- **Recursion:** each white cell passes the search on to its neighbours
- **Polymorphism:** `finn` behaves differently in `HvitRute`,
  `SortRute` and `Aapning`, so the search needs no type checks
- **Abstract classes and inheritance:** `Rute` can't be instantiated,
  only its subclasses can
- **A linked grid:** each cell holds direct references to its
  neighbours (`nord`, `sor`, `oest`, `vest`)
- **Reading files** with `Scanner` and handling errors with exceptions

## How it works

### 1. Building the maze
1. The first line of the file gives the size of the maze.
2. A cell object is created for every character. A `.` on the edge
   becomes an `Aapning`, any other `.` becomes a `HvitRute`, and a `#`
   becomes a `SortRute`.
3. Once every cell exists, a second pass connects each cell to its
   neighbours. Cells on the edge get `null` for neighbours outside the
   grid.

The connection step has to come after every cell has been created,
because a cell can't refer to a neighbour that doesn't exist yet.

### 2. Searching
`finnUtveiFra(rad, kol)` calls `finn(null)` on the start cell. `null`
means that the search didn't come from any cell, so every direction
is tried.

| Class | Behaviour of `finn(fra)` |
|---|---|
| `HvitRute` | Calls `finn(this)` on every neighbour except `fra` |
| `SortRute` | Does nothing, because it's a wall |
| `Aapning` | Prints its coordinates and stops |

The search never goes back the way it came, so in an acyclic maze it
ends after it has explored every possible path.

## Class overview

| Class | Responsibility |
|---|---|
| `Oblig6` | Entry point: loads the maze and runs the command loop |
| `Labyrint` | Reads the file, holds the grid, connects neighbours and starts the search |
| `Rute` | Abstract superclass: coordinates, maze reference and neighbours |
| `HvitRute` | Open cell: passes the search on |
| `SortRute` | Wall: stops the search |
| `Aapning` | Opening on the edge: reports itself as an exit |

### Glossary (the code uses Norwegian identifiers)
| Norwegian | English |
|---|---|
| labyrint | maze |
| rute | cell |
| hvit / sort | white (open) / black (wall) |
| aapning | opening (exit) |
| utvei | way out |
| finn | find |
| rad / kolonne (kol) | row / column |
| nabo / naboer | neighbour / neighbours |
| nord / sor / oest / vest | north / south / east / west |
| rutenett | grid |
| les fra fil | read from file |

## Running

    javac *.java
    java Oblig6 labyrinter/3.in

Example session:

    $ java Oblig6 labyrinter/7.in
    # . # # #
    # . # . #
    . . # # #
    # # # . .
    # . # # #
    Oppgi startkoordinater <rad> <kolonne>: (-1 for a avslutte)
    1 1
    Aapninger
    (0,1)
    (2,0)
    -1

### Test files
The `labyrinter/` folder has the course's test mazes `1.in`–`7.in`.

| File | Start | Expected openings |
|---|---|---|
| `7.in` | (1,1) | (0,1), (2,0) |
| `7.in` | (1,3) | none (an enclosed area) |
| `7.in` | (2,2) | can't start on a black cell |
| `1.in` | (3,3) | (0,5) |
| `3.in` | (3,5) | (1,0), (11,12) |
| `3.in` | (1,5) | none |
| `4.in` | any | **not supported**: the maze is cyclic (see below) |

Openings may be printed in a different order. Only the set of openings
matters.

`fil1.txt` is a small maze I made myself to test edge cases. It has
openings that are neighbours of each other, which the assignment says
won't happen, so its expected output isn't defined.

## Reflections
I wrote this as a student in 2022. Looking back, these are the things
I would improve.

### Breaking the task's rules
- **`instanceof` is used:** the task asked for a solution built on
  polymorphism with no type checks. `finnUtveiFra` uses `instanceof`
  to find a black start cell, and `Labyrint.toString` uses it to
  choose between `.` and `#`. A better design lets
  `SortRute.finn(null)` print "can't start on a black cell" and lets
  each cell's own `toString` draw it. Then the `toString` methods in
  `HvitRute` and `SortRute` would actually be used.
- **No `return` after the black-cell check:** after the message the
  program still prints "Aapninger" and starts the search.
- **Static grid:** `rutenett` is `static`, so all `Labyrint` objects
  share one grid, and loading a second maze would overwrite the first.
  It should be an instance variable.

### Input handling
- **String comparison:** `while (input != "-1")` compares references,
  not text, so the condition is always true. Typing `-1` only exits
  because `"-1".split(" ")` has one element: reading the second one
  throws an exception, and the catch block exits with error code 1.
  It should use `.equals("-1")` and leave the loop normally.
- **Bad input ends the program:** non-numeric input such as `a b` or an
  empty line throws a `NumberFormatException` that isn't caught inside
  the loop, so the whole program stops. A single number gets the
  misleading message "coordinates outside the maze".
- **Using exceptions for bounds:** coordinates outside the maze are
  caught as `ArrayIndexOutOfBoundsException`. An explicit bounds check
  would be clearer, and it wouldn't hide real bugs in the recursion.

### Reading files
- **Other characters aren't ignored:** the task says characters other
  than `.` and `#` (for example spaces) should be ignored. My code
  prints a warning but still moves to the next column, so the grid
  gets shifted.
- **Bad formats aren't detected:** the program should stop with a
  clear message when the size in the first line doesn't match the
  grid. Instead it carries on until a `NullPointerException` happens
  and gets caught by accident.
- **The file is read three times:** `settDimensjon` is called twice and
  then the file is read again. One `Scanner` would be enough.

### Search logic
- **Starting on an opening:** `Aapning.finn` always stops, which is
  right when the search reaches an opening but not when it starts on
  one. From (1,0) in `3.in` the program should also find (11,12). The
  fix is to keep searching when `fra == null`.
- **Repeated code:** the four `if/else` branches in `HvitRute.finn`
  could be replaced by one helper method in `Rute`, for example
  `provNabo(nabo, fra)`, that checks for `null` and the cell the call
  came from. `finn` in `Rute` could also be `abstract` so every
  subclass has to implement it.
- **Cyclic mazes:** in a cyclic maze like `4.in` the recursion goes in
  circles until it fails with a `StackOverflowError`. The optional
  `besoekt` (visited) flag in the task would fix this.

### Cleanup
- A leftover test `main` method in `Labyrint` with hard-coded
  coordinates `(3, 3)`
- An unused `type` field in `Rute`
- Messages and prompts that don't quite match the example run (for
  example "Aapninger" without a colon)
