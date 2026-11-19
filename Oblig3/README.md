# IN1010 V22 – Obligatorisk oppgave 3

Lenkede lister og beholdere som skal gjenbrukes i oblig 4: en abstrakt
`Lenkeliste<T>` og fire subklasser (`Stabel`, `Koe`, `IndeksertListe`,
`Prioritetskoe`).

## Filer

| Fil | Innhold |
|-----|---------|
| `Liste.java` | Grensesnittet `Liste<T>` (gitt i oppgaveteksten) |
| `Lenkeliste.java` | Abstrakt dobbeltlenket liste + hjelpeklassen `Node<T>` |
| `Stabel.java` | LIFO: `leggTil` setter inn først i listen |
| `Koe.java` | FIFO: arver alt fra `Lenkeliste` uten endringer |
| `IndeksertListe.java` | Innsetting, endring, henting og fjerning på vilkårlig posisjon |
| `Prioritetskoe.java` | Sortert innsetting (minst først) |
| `UgyldigListeindeks.java` | Unntaksklasse (gitt av emnet) |
| `TestStabel.java`, `TestKoe.java`, `TestIndeksertListe.java`, `TestPrioritetskoe.java` | Testprogrammer (gitt av emnet) |
| `klassehieraki.jpg` | Tegning av klassehierarkiet (del A) |
| `datastruktur_oblig3.jpg` | Datastrukturtegning (del G) |

## Kompilere og kjøre

```bash
javac *.java
java TestStabel
java TestKoe
java TestIndeksertListe
java TestPrioritetskoe
```

## Klassehierarki

```
        «interface» Liste<T>
                 ▲
                 │ implements
                 │
      «abstract» Lenkeliste<T>
     ▲        ▲          ▲              ▲
     |        |          |              | 
  Stabel<T>  Koe<T>  IndeksertListe<T>  Prioritetskoe<T extends Comparable<T>>
```

## Implementasjon

- Listen er **dobbeltlenket**. Hver `Node<T>` har `verdi`, `neste` og `forrige`.
- `Lenkeliste` har referansene `start` og `slutt`. De er ikke private, så
  subklassene kan endre dem.
- `Node.settNeste(n)` setter også `n.forrige`, slik at begge retningene
  oppdateres samtidig.
- `Koe` trenger ingen redefinerte metoder. `Lenkeliste` setter allerede inn
  sist og tar ut først.
- `Prioritetskoe` redefinerer bare `leggTil`. Det minste elementet ligger
  først, så de arvede `hent()` og `fjern()` gir riktig element.

## Testresultater

Alle fire testprogrammene skriver ut `Alt gikk bra!`. Testene dekker likevel
ikke alle tilfeller. Se feilene under.

## Kjente feil og mangler

Feilene ble funnet ved en gjennomgang av koden etter innlevering. De er ikke
rettet, så det er lett å se hva som ble levert.

### Prioritetskoe

- **`leggTil` mister elementer.** Elementer som skal plasseres rett før det
  siste elementet, eller som er like det siste, blir aldri satt inn. Løkken
  stopper på den siste noden, og da hopper `if (denneNoden.hentNeste() != null)`
  over innsettingen. Eksempel: `B, D, C` gir listen `B, D`. I `TestPrioritetskoe`
  test 3 forsvinner `"C"` og `"X"`, men testen sjekker bare `hent()` og
  passerer derfor.

### IndeksertListe

- **`leggTil(pos, x)` midt i listen lager en løkke.** `denneNoden.settNeste(nyNode)`
  kjøres før den gamle neste-noden er tatt vare på. Derfor peker `nyNode` på
  seg selv, og `stoerrelse()` havner i en uendelig løkke. Det gjelder posisjoner
  fra 1 til `stoerrelse() - 2`.
- **`fjern(pos)` midt i listen fjerner ikke noe.** Bare `forrige`-pekeren til
  neste node oppdateres. `neste`-pekeren til forrige node peker fortsatt på
  elementet, så det blir liggende i listen.
- **`sett(pos, x)` feiler på første og siste posisjon.** Metoden bytter ut hele
  noden i stedet for verdien, og kaller `hentForrige()`/`hentNeste()` uten
  null-sjekk. Det gir `NullPointerException`. `start` og `slutt` oppdateres
  heller aldri. Det hadde holdt å skrive `denneNoden.verdi = x`.
- **`hent(pos)` har feil grensesjekk.** Den sjekker `pos > stoerrelse()` i
  stedet for `pos >= stoerrelse()`. Derfor gir `hent(stoerrelse())`
  `NullPointerException` i stedet for `UgyldigListeindeks`.
- Instansvariabelen `str` brukes ikke.

### Lenkeliste

- **`hent()` på en tom liste** gir `NullPointerException` i stedet for
  `UgyldigListeindeks(-1)`.
- **`fjern()` rydder ikke opp i pekerne.** `start.forrige` settes ikke til
  `null`, og `slutt` blir ikke `null` når listen blir tom. Begge peker
  fortsatt på den fjernede noden. Løkken etter fjerningen gjør ingenting.
- **`stoerrelse()` og `leggTil()` går gjennom hele listen (O(n)).** Det hadde
  vært bedre å holde en teller oppdatert og bruke `slutt` direkte. Noder med
  `null` som verdi blir dessuten ikke telt med.
- **`Node.settNeste(null)` gir `NullPointerException`**, fordi metoden kaller
  `neste.settForrige(this)`.

### Del G – datastrukturtegningen

Tegningen viser **det tiltenkte resultatet** (`B ⇄ D`), ikke det koden faktisk
gjør. Med koden over krasjer `liste.sett(1, "D")` med `NullPointerException`,
fordi posisjon 1 er siste node. I tillegg peker `B.forrige` fortsatt på den
fjernede noden `A` etter `fjern()`. Objektet burde også vært merket
`IndeksertListe<String>`, ikke `IndeksertListe<T>`.

### Andre mangler

- Oppgaven ber om en **invariant** som beskriver tilstanden til beholderen.
  Den mangler. Den kunne vært slik: *Tom liste ⇔ `start == null` og
  `slutt == null`. Ellers er `start.forrige == null`, `slutt.neste == null`,
  og for alle noder n gjelder `n.neste.forrige == n`.*
- Teksten på klassehierarki-tegningen sier «Datastrukturtegning», men tegningen
  viser klassehierarkiet.

## Kilder

`Liste.java` (grensesnittet), `UgyldigListeindeks.java` og de fire
testprogrammene er gitt av IN1010. Resten av koden har jeg skrevet selv.
