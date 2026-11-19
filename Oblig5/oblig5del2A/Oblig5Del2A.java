import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Oblig5Del2A {

    public static void main(String[] args) {
        Monitor1 monitor1 = new Monitor1();

        try {
            String mappenavn = args[0];
            //testdatalitenlike og testdatalike skal testes
            System.out.println(mappenavn+":");
            lesMedtrader(mappenavn, monitor1);
            flettListe(monitor1.subreg.sekvenser);

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("feil innput, oppgi mappenavn");
        }
        
        
    }

    //metode for å slå sammen lister med hashmaps
    public static void flettListe(ArrayList<HashMap<String, Subsekvens>> listeHMaps){
        HashMap<String, Subsekvens> sammenslott = new HashMap<String, Subsekvens>();
        if(listeHMaps.size() >= 1){
            sammenslott = Monitor1.slaSammen(listeHMaps.get(1), listeHMaps.get(0)); //slår sammen de to første Subsekvensene

            for(int i = 2; i <= (listeHMaps.size() -1); i+=1){
                sammenslott = Monitor1.slaSammen(sammenslott, listeHMaps.get(i));
            }
            sjekkFrekvens(sammenslott);
        }
        else{
            System.out.println("Det ligger ingen Subsekvenser i hashmappet");
        }
    }

    //metode for å lese filer med tråder
    public static void lesMedtrader(String mappenavn, Monitor1 monitor){
        try {
            ArrayList<String> filer = finnFilnavn(mappenavn);
            ArrayList <Thread> trader = new ArrayList<>();

            for (String fil: filer){
                Thread t = new Thread(new LeseTrad(mappenavn+"/"+fil, monitor));
                trader.add(t);
                t.start();
            }
            try {
                for(Thread t: trader){
                    t.join();
                }
            } 
            catch (InterruptedException i) {
                System.err.println("feil bro: \n"+i);
                System.exit(1);
            }

        } catch (FileNotFoundException e) {
            System.out.println("finner ikke filen");
        }
    }

    //lager metode for å returnere filnavn
    public static ArrayList<String> finnFilnavn(String filbaneMappe) throws FileNotFoundException{ //leser av metadata for å lage Subsekvenser
        File metadataFil = new File(filbaneMappe+"/metadata.csv");
        Scanner sc = new Scanner(metadataFil);

        ArrayList<String> listeFiler = new ArrayList<String>();

        while(sc.hasNextLine()) {
            String linje = sc.nextLine();
            String stripLinje[] = linje.split(",");
            listeFiler.add(stripLinje[0]);
        }
        sc.close();
        return listeFiler;
    }

    //sjekker hvilken subsekves det er mest av (tar utgangspunkt i at kun én har flest)
    public static void sjekkFrekvens(HashMap<String, Subsekvens> hash){
        //sjekker hvilken subsekves det er mest av (tar utgangspunkt i at kun én har flest)
        int frekvens = 0;
        String hoyestFrekvens = "";
        for(String s: hash.keySet()){
            if(hash.get(s).hentAntall() > frekvens){
                frekvens = hash.get(s).hentAntall();
                hoyestFrekvens = s;
            }
        }
        System.out.println(hoyestFrekvens+" forekommer flest("+frekvens+") ganger"); //høyeste frekevesen
    }
}
 