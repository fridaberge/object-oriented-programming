public class Subsekvens {
    public final String subsek;
    private int antall = 0;

    public Subsekvens(String s){
        subsek = s; 
    }

    //Lag metoder for å hente hente og endre antall forekomster
    public int hentAntall(){
        return antall;
    } 

    public void oek(){
        antall ++;
    }

    public void mink(){
        antall--;
    }

    public String toString(){
        return ("(" +subsek+ ","+antall+")"); //eks. (ABC,4)
    }
}
