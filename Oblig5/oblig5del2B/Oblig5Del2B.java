import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//opg5

public class Oblig5Del2B {

    public static void main(String[] args) {
    
        //skal teste filene: testdatalitenlike og testdatalike
        try{
            Monitor2 monitor1 = new Monitor2();
            String mappenavn = args[0];
            int antTrader = Integer.parseInt(args[1]);
            lesMedtrader(mappenavn, monitor1);
            flettMedTrader(monitor1, antTrader);
            System.out.println("Subsekvensen med flest forekomster i " +mappenavn + " er: ");
            sjekkFrekvens(monitor1.hentUt(0));
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("feil input, oppgi mappenavn, sa antall trader");
        }
    }

    public static void flettMedTrader(Monitor2 monitor, int antTrad){
        ArrayList <Thread> Ftrader = new ArrayList<>();
        for (int a = 0; a< antTrad; a++){
            Thread t = new Thread(new FletteTrad(monitor,a));
            Ftrader.add(t);
            t.start();
        }
        try {
            for(Thread t: Ftrader){
                t.join();
            }
        } 
        catch (InterruptedException i) {
            System.err.println(i);
        }        
    }

    //metode for å lese filer med tråder
    public static void lesMedtrader(String mappenavn, Monitor2 monitor){
        try {
            ArrayList<String> filer = finnFilnavn(mappenavn);
            ArrayList <Thread> trader = new ArrayList<>();
            //System.out.println(filer);

            for (String fil: filer){
                Thread t = new Thread(new LeseTrad(mappenavn+"/"+fil, monitor));
                trader.add(t);
                t.start();
            }
            monitor.settFerdigFlett(trader.size()-1); //sier at flettingen skal foregå antall ganger det er linjer i filen minus én

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
            e.printStackTrace();
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
        System.out.println(hash.get(hoyestFrekvens)); //høyeste frekevesen
    }
}
 