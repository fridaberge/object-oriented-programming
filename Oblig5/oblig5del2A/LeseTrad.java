import java.io.FileNotFoundException;

public class LeseTrad implements Runnable{
    String filnavn;
    Monitor1 monitor;

    public LeseTrad(String f, Monitor1 m){
        filnavn = f; 
        monitor = m;
    }

    @Override
    public void run(){
        try{
            monitor.settInn(Monitor1.lesFil(filnavn));
        }
        catch (FileNotFoundException e) {
            System.out.println("Finner ikke filen");
        }
    }


}