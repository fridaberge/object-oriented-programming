# Assignment 2 – Medicines, Prescriptions and Doctors (IN1010, spring 2022)

## About this submission

This code was written in 2022 as Assignment 2 (Oblig 2) in IN1010 at the
University of Oslo. I have kept it as close to the original as possible to show
how I coded at the time. A few bugs have been fixed since then (see "Changes
made afterwards"). Everything else is documented as known deviations and
shortcomings. The diagrams (`Klassehierakier_oblig2.drawio.pdf` and
`oblig2_Lever.drawio.pdf`) are unchanged from the original.

Class names, method names and output text are in Norwegian, as in the original
assignment.

## Assignment overview

This was an individual assignment, and the first of three that together build
a system for keeping track of doctors, patients, prescriptions and medicines.
This first part covers medicines, prescriptions and doctors, plus tests and
diagrams. The main topics are inheritance, abstract classes, interfaces and
polymorphism.

**Note on IDs:** Medicines and prescriptions each get a unique, positive ID.
The first object of a class gets ID 1, the next gets 2, and so on. IDs only
have to be unique within a class, so a medicine and a prescription can have
the same ID.

### Part A – Medicines

- `Legemiddel` (medicine) is abstract and has a name, an ID, a price in whole
  NOK (`int`) and the total amount of active ingredient in mg (`double`).
- Subclasses:
  - `Narkotisk` (narcotic), with an integer for how strongly narcotic it is
  - `Vanedannende` (habit-forming), with an integer for how habit-forming it is
  - `Vanlig` (regular), with no extra properties
- Required methods: `hentId`, `hentNavn`, `hentPris`, `hentVirkestoff`,
  `settNyPris`, plus `hentNarkotiskStyrke` / `hentVanedannendeStyrke`.
- A1: class hierarchy drawing. A3: unit tests in `TestLegemiddel`.
  A4 (optional): `toString()`.

### Part B – Prescriptions

- `Resept` (prescription) is abstract and has an ID, a medicine, the doctor
  who wrote it, a patient ID and a number of refills (`reit`). With 0 refills
  left, the prescription is invalid.
- `bruk()` tries to use the prescription once. It returns `false` if it is
  already used up and `true` otherwise.
- Each subclass decides `farge()` (colour) and `prisAaBetale()` (price to pay):

| Prescription type | Colour | Price the patient pays |
|-------------------|--------|------------------------|
| White (`HviteResepter`) | `"hvit"` | Full price |
| Military (`MilResept`) | `"hvit"` | Free (100 % discount), always 3 refills |
| P-prescription (`Presept`), contraceptives for young people | `"hvit"` | 108 NOK less, never below 0 |
| Blue (`BlaResepter`) | `"blaa"` | 25 % of the price (75 % discount) |

- Prices are rounded to the nearest whole krone. `MilResept` does not take
  `reit` in its constructor.
- B1: class hierarchy drawing. B3: unit tests in `TestResepter`, covering
  what each class does differently from its parent class.
  B4 (optional): `toString()`.

### Part C – Doctors

- A `Lege` (doctor) has a name and a method to get it.
- A `Spesialist` (specialist) is a doctor with an approval exemption for
  prescribing narcotic medicines. Since people other than doctors can also have
  this exemption, it is an interface, `Godkjenningsfritak`, with
  `hentKontrollID()`. The control ID is used to check that the exemption is
  not misused.
- C1: class hierarchy drawing including the interface.
  C3 (optional): `toString()`.

### Part D – Integration test

- A main program (`Integrasjonstest`) that creates at least one instance of
  every class, with the necessary references between objects, and prints
  relevant information about each object.

### Part E – Data structure

- A drawing of the objects and their state for a very simple program that
  creates one narcotic medicine, one specialist and one military prescription
  for that medicine, written by that specialist. The class data structures
  must be included.

## How to run

    javac *.java
    java TestLegemiddel
    java TestResepter
    java Integrasjonstest

## Files

| File | Contents | Part |
|------|----------|------|
| `Legemiddel.java` | Abstract superclass for medicines | A |
| `Narkotisk.java`, `Vanedannende.java`, `Vanlig.java` | Subclasses of `Legemiddel` | A |
| `TestLegemiddel.java` | Unit tests for medicines | A |
| `Resept.java` | Abstract superclass for prescriptions | B |
| `HviteResepter.java`, `BlaResepter.java` | White and blue prescriptions | B |
| `MilResept.java`, `Presept.java` | Subclasses of `HviteResepter` | B |
| `TestResepter.java` | Unit tests for prescriptions | B |
| `Lege.java`, `Spesialist.java` | Doctor and specialist | C |
| `Godkjenningsfritak.java` | Interface for approval exemption | C |
| `Integrasjonstest.java` | Integration test | D |
| `Klassehierakier_oblig2.drawio.pdf` | Class hierarchy (A1, B1, C1 combined) | A–C |
| `oblig2_Lever.drawio.pdf` | Data structure drawing | E |

## Changes made afterwards

- `Presept` and `MilResept`: `prisAaBetale` took a parameter, so it overloaded
  the method instead of overriding it, and the patient paid full price. The
  method now overrides correctly with `@Override`.
- `BlaResepter`: the patient now pays 25 % of the price.
- `Resept.bruk()`: now checks whether the refills are used up *before*
  decrementing, so `reit` can no longer go negative.
- Expected values in `TestResepter` and `TestLegemiddel` have been corrected to
  match the correct behaviour.

## Known deviations from the assignment

- **A2:** The strength methods are named `hentStyrke()` instead of
  `hentNarkotiskStyrke()` / `hentVanedannendeStyrke()`.
- **B3:** `TestResepter` does not create an instance of `HviteResepter`, and
  it does not test the price of the blue prescription, even though that is
  what makes `BlaResepter` different from its parent class.
- **A3/B3:** The tests only print `true`/`false`. The assignment suggests
  printing a message that depends on whether the result was as expected.
- **D:** `Integrasjonstest` does not create an instance of `HviteResepter`.
  Medicines and doctors are only printed indirectly, through the prescriptions'
  `toString()`.
- **E:** The drawing does not include the class data structures, so the static
  `id` counters are drawn inside the objects. They should have been in separate
  class data structures for `Legemiddel` and `Resept`.

## Test shortcomings (beyond the requirements)

- No tests for `bruk()`, `settNyPris()` or `Spesialist`/`hentKontrollID()`.
- Strings are compared with `==` instead of `.equals()`. This only works here
  because Java reuses identical string literals.
- The tests cast to subclasses unnecessarily. `hentId()`, `farge()` and
  `prisAaBetale()` are defined in the superclass, so polymorphism would have
  been enough.
- The ID tests depend on the order in which objects are created, because the
  counter is static.

## Code quality

- `Narkotisk` and `Vanedannende` have an unused field `id`. All three medicine
  classes override `hentId()` with a copy of the method in `Legemiddel`.
- `toString()` is duplicated in the subclasses of `Legemiddel` instead of
  building on a shared `toString()` in the superclass.
- `Presept` compares against the literal `108` instead of the constant
  `rabatt` (discount), and the comment "hvis kjøper er ung" ("if the buyer is
  young") does not describe what the code does.
- `HviteResepter` is missing `@Override` on `farge()` and `prisAaBetale()`.
- `Lege.navn` is `public`. It should have been `private`. The method
  `skrivNavn()` ("write name") returns the name rather than printing it, so
  `hentNavn()` would have been a clearer name.
- Some typos in the output: "Vikrestoff", "Spessialistinfo", "Stryke test"
  (should be "Virkestoff", "Spesialistinfo", "Styrke test").

## The diagrams

- The class hierarchy matches the code and the assignment.
- The data structure drawing shows the program described in Part E (a
  narcotic medicine, a specialist and a military prescription). It is labelled
  "Klassen Integrasjonstest", but it does not show the actual
  `Integrasjonstest`, which creates other objects.
- In the drawing, the field `reit` is incorrectly labelled with type
  `Spesialist`, and "HvieResepter" should be `HviteResepter`.
