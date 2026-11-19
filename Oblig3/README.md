# IN1010 Spring 2022 – Mandatory Assignment 3

Linked lists and containers that are reused in assignment 4: an abstract
`Lenkeliste<T>` and four subclasses (`Stabel`, `Koe`, `IndeksertListe`,
`Prioritetskoe`).

## Assignment overview

| Part | Task |
|------|------|
| A | Draw the class hierarchy |
| B | `Lenkeliste<T>`: abstract linked list implementing the `Liste<T>` interface. New elements are added at the end and removed from the front |
| C | `Stabel<T>` (stack): `leggTil` inserts at the front, so the last element added is the first one removed (LIFO) |
| D | `Koe<T>` (queue): first in, first out (FIFO) |
| E | `IndeksertListe<T>`: insert, replace, get and remove at any position |
| F | `Prioritetskoe<T extends Comparable<T>>`: keeps the elements sorted, so the smallest element is always removed first |
| G | Draw the data structure after a given sequence of operations |

## Files

| File | Contents |
|------|----------|
| `Liste.java` | The `Liste<T>` interface (given in the assignment) |
| `Lenkeliste.java` | Abstract doubly linked list + helper class `Node<T>` |
| `Stabel.java` | LIFO: `leggTil` inserts at the front of the list |
| `Koe.java` | FIFO: inherits everything from `Lenkeliste` unchanged |
| `IndeksertListe.java` | Insertion, replacement, retrieval and removal at arbitrary positions |
| `Prioritetskoe.java` | Sorted insertion (smallest first) |
| `UgyldigListeindeks.java` | Exception class (provided by the course) |
| `TestStabel.java`, `TestKoe.java`, `TestIndeksertListe.java`, `TestPrioritetskoe.java` | Test programs (provided by the course) |
| `klassehieraki.jpg` | Class hierarchy drawing (part A) |
| `datastruktur_oblig3.jpg` | Data structure drawing (part G) |

## Compiling and running

```bash
javac *.java
java TestStabel
java TestKoe
java TestIndeksertListe
java TestPrioritetskoe
```

## Class hierarchy

```
        «interface» Liste<T>
                 ▲
                 │ implements
                 |
      «abstract» Lenkeliste<T>
     ▲        ▲          ▲              ▲
     |        |          |              |
  Stabel<T>  Koe<T>  IndeksertListe<T>  Prioritetskoe<T extends Comparable<T>>
```

## Implementation

- The list is **doubly linked**. Each `Node<T>` has `verdi` (value), `neste`
  (next) and `forrige` (previous).
- `Lenkeliste` keeps the references `start` and `slutt` (end). They are not
  private, so the subclasses can modify them.
- `Node.settNeste(n)` also sets `n.forrige`, so both directions are updated
  at the same time.
- `Koe` overrides no methods. `Lenkeliste` already inserts at the end and
  removes from the front.
- `Prioritetskoe` overrides only `leggTil`. The smallest element is kept
  first, so the inherited `hent()` and `fjern()` return the correct element.

## Test results

All four test programs print `Alt gikk bra!` ("All went well!"). The tests
don't cover every case, though. See the bugs below.

## Known bugs and limitations

These bugs were found in a code review after submission. They have not been
fixed, so the submitted version stays as it was.

### Prioritetskoe

- **`leggTil` loses elements.** Elements that belong right before the last
  element, or that are equal to the last element, are never inserted. The
  loop stops at the last node, and then `if (denneNoden.hentNeste() != null)`
  skips the insertion. Example: `B, D, C` results in the list `B, D`. In
  `TestPrioritetskoe` test 3, `"C"` and `"X"` disappear, but the test only
  checks `hent()`, so it still passes.

### IndeksertListe

- **`leggTil(pos, x)` in the middle of the list creates a cycle.**
  `denneNoden.settNeste(nyNode)` runs before the old next node has been saved.
  As a result `nyNode` points to itself, and `stoerrelse()` loops forever.
  This affects positions 1 to `stoerrelse() - 2`.
- **`fjern(pos)` in the middle of the list removes nothing.** Only the
  `forrige` pointer of the next node is updated. The `neste` pointer of the
  previous node still points to the element, so it stays in the list.
- **`sett(pos, x)` fails at the first and last position.** The method replaces
  the whole node instead of the value, and calls `hentForrige()`/`hentNeste()`
  without a null check. This throws `NullPointerException`. `start` and
  `slutt` are never updated either. `denneNoden.verdi = x` would have been
  enough.
- **`hent(pos)` has a wrong bounds check.** It checks `pos > stoerrelse()`
  instead of `pos >= stoerrelse()`. So `hent(stoerrelse())` throws
  `NullPointerException` instead of `UgyldigListeindeks`.
- The instance variable `str` is unused.

### Lenkeliste

- **`hent()` on an empty list** throws `NullPointerException` instead of
  `UgyldigListeindeks(-1)`.
- **`fjern()` doesn't clean up the pointers.** `start.forrige` is not set to
  `null`, and `slutt` does not become `null` when the list is emptied. Both
  still point to the removed node. The loop after the removal does nothing.
- **`stoerrelse()` and `leggTil()` traverse the whole list (O(n)).** It would
  be better to keep a counter and use `slutt` directly. Nodes with `null` as
  their value are also not counted.
- **`Node.settNeste(null)` throws `NullPointerException`**, because the method
  calls `neste.settForrige(this)`.

### Part G – the data structure drawing

The drawing shows **the intended result** (`B ⇄ D`), not what the code
actually does. With the code above, `liste.sett(1, "D")` crashes with
`NullPointerException`, because position 1 is the last node. Also,
`B.forrige` still points to the removed node `A` after `fjern()`. The object
should also have been labelled `IndeksertListe<String>`, not
`IndeksertListe<T>`.

### Other limitations

- The assignment asks for an **invariant** describing the state of the
  container. It is missing. It could have been: *Empty list ⇔ `start == null`
  and `slutt == null`. Otherwise `start.forrige == null`,
  `slutt.neste == null`, and for every node n: `n.neste.forrige == n`.*
- The label on the class hierarchy drawing says "Datastrukturtegning" (data
  structure drawing), but the drawing shows the class hierarchy.

## Credits

`Liste.java` (the interface), `UgyldigListeindeks.java` and the four test
programs are provided by IN1010. The rest of the code is my own.
