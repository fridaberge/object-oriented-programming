# Assignment 2 – Medicines, Prescriptions and Doctors (IN1010, 2022)

## About this submission

This code was written in 2022 as Assignment 2 (Oblig 2) in IN1010. I have kept it
as close to the original as possible to show how I coded at the time. A few bugs
have been fixed since then (see "Changes made afterwards"). Everything else is
documented as known deviations and shortcomings. The diagrams
(`Klassehierakier_oblig2.drawio.pdf` and `oblig2_Lever.drawio.pdf`) are unchanged
from the original.

Class names, method names and output text are in Norwegian, as in the original
assignment.

## How to run

    javac *.java
    java TestLegemiddel
    java TestResepter
    java Integrasjonstest

## Files

| File | Contents |
|------|----------|
| `Legemiddel.java` | Abstract superclass for medicines with name, price, active ingredient and unique ID |
| `Narkotisk.java`, `Vanedannende.java`, `Vanlig.java` | Subclasses of `Legemiddel` (narcotic, habit-forming, regular) |
| `Resept.java` | Abstract superclass for prescriptions with medicine, doctor, patient ID, refills (`reit`) and unique ID |
| `HviteResepter.java`, `BlaResepter.java` | Subclasses of `Resept` (white and blue prescriptions) |
| `MilResept.java`, `Presept.java` | Subclasses of `HviteResepter` (military prescription and P-prescription, i.e. contraceptive prescription) |
| `Lege.java`, `Spesialist.java` | Doctors. `Spesialist` (specialist) implements `Godkjenningsfritak` |
| `Godkjenningsfritak.java` | Interface ("approval exemption") with `hentKontrollID()` |
| `TestLegemiddel.java`, `TestResepter.java` | Unit tests |
| `Integrasjonstest.java` | Integration test that creates objects of several classes and prints them |

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

- The methods are named `hentStyrke()` instead of `hentNarkotiskStyrke()` /
  `hentVanedannendeStyrke()`. `Lege` has `skrivNavn()` instead of `hentNavn()`.
- Some class names differ from the assignment (`BlaResepter`, `HviteResepter`,
  `Presept`, `MilResept`).
- `Integrasjonstest` does not create an instance of `HviteResepter`. It only
  prints the results and does not check them automatically.
- `Presept` compares against the literal `108` instead of the constant `rabatt`
  (discount), and the comment "hvis kjøper er ung" ("if the buyer is young")
  does not describe what the code actually does.

## Test shortcomings

- No tests for `bruk()`, the blue prescription price, `HviteResepter`,
  `settNyPris()` or `Spesialist`/`hentKontrollID()`.
- Strings are compared with `==` instead of `.equals()`. This only works here
  because Java reuses identical string literals.
- The tests cast to subclasses unnecessarily. `hentId()`, `farge()` and
  `prisAaBetale()` are defined in the superclass, so polymorphism would have
  been enough.
- The ID tests depend on the order in which objects are created, because the
  counter is static.
- The tests only print `true`/`false`, not the expected and actual values.

## Code quality

- `Narkotisk` and `Vanedannende` have an unused field `id`. All three medicine
  classes override `hentId()` with a copy of the method in `Legemiddel`.
- `toString()` is duplicated in the subclasses of `Legemiddel` instead of
  building on a shared `toString()` in the superclass.
- `HviteResepter` is missing `@Override` on `farge()` and `prisAaBetale()`.
- `Lege.navn` is `public`. It should have been `private`.
- Some typos in the output: "Vikrestoff", "Spessialistinfo", "Stryke test"
  (should be "Virkestoff", "Spesialistinfo", "Styrke test").

## The diagrams

- The class hierarchy matches the code.
- The data structure diagram places the static counters `id` inside the
  objects. They should have been in separate class data structures for
  `Legemiddel` and `Resept`.
- The field `reit` is incorrectly labelled with type `Spesialist`, and
  "HvieResepter" should be `HviteResepter`.
- The diagram does not quite match `Integrasjonstest`. In the diagram the
  narcotic medicine is "Paracet" at 150 NOK, while in the code it is "Oksygen"
  at 15 NOK ("Paracet" is a regular medicine there). The military prescription
  also points to different objects in the diagram than in the code.
