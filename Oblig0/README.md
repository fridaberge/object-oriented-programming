# Oblig 0 – Classes, Objects and References

The first (optional) assignment in IN1010, spring 2022. It introduces the basics of
object-oriented programming in Java: classes, objects, constructors, instance
variables, methods and references between objects.

## Assignment overview

The assignment is split into three parts, each building on the previous one:

1. **A simple class:** Create a `Bil1` (car) class with a method that prints
   *"Jeg er en bil"* ("I am a car"), and a program that creates a car object and
   calls this method.
2. **Constructors and instance variables:** Create `Bil2`, where every car has a
   registration number (a `String`) set through the constructor. The print method
   now prints the registration number.
3. **References between objects:**
   - Create `Bil3`, which also has a getter method `hentNummer()` ("get number").
   - Create a `Person` class. Every person owns a car, and a reference to the
     car is passed to the constructor. The person has a method that prints the
     registration number of their car.
   - Create a program that creates a car and a person who owns it, and then
     calls the person's print method.
   - Draw the data structure after the objects have been created (see below).

## Files

| File | Description |
|------|-------------|
| `Bil1.java` | Car class with a method `skrivBil()` that prints "Jeg er en bil". |
| `BilBruk1.java` | Main program for part 1: creates a `Bil1` object and calls `skrivBil()`. |
| `Bil2.java` | Car class with a registration number set by the constructor, and `skrivBil2()` that prints it. |
| `BilBruk2.java` | Main program for part 2: creates a `Bil2` with registration number `BS58137` and prints it. |
| `Bil3.java` | Like `Bil2`, plus the getter `hentNummer()` that returns the registration number. |
| `Person.java` | A person with a name and a reference to a `Bil3` object. `skrivBilTilPerson()` prints the registration number of the person's car. |
| `BilBruk3.java` | Main program for part 3: creates a car (`PP84705`) and a person (`Frida`) who owns it, then calls `skrivBilTilPerson()`. |

## How to run

Requires Java (JDK) 11 or newer. From inside this folder:

```bash
javac *.java

java BilBruk1   # Output: Jeg er en bil
java BilBruk2   # Output: BS58137
java BilBruk3   # Output: PP84705
```

## Data structure (part 3d)

The data structure after `main` in `BilBruk3` has created the `Person` object.
Both the local variable `bilenMin` and the `Person` object's `bil` field
reference the same `Bil3` object:

```
main()
┌──────────────┐          ┌─ Person ─────────────┐
│ frida    ●───┼────────▶ │ navn: "Frida"        │
│ bilenMin ●───┼──┐       │ bil:  ●──────────────┼──┐
└──────────────┘  │       └──────────────────────┘  │
                  │       ┌─ Bil3 ───────────────┐  │
                  └─────▶ │ regnummer: "PP84705" │◀┘
                          └──────────────────────┘
```

## Concepts covered

- Defining classes and creating objects with `new`
- Constructors and private instance variables
- Instance methods and getter methods
- Object references and how several references can point to the same object
