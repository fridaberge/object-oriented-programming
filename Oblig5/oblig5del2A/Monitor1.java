import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
  
public class Monitor1 {
    public static ReentrantLock laas = new ReentrantLock();
    public SubsekvensRegister subreg;

    public Monitor1(){
        this.subreg = new SubsekvensRegister();
    }

    public void settInn(HashMap<String, Subsekvens> sekvens){
        subreg.settInn(sekvens);
    }

    public HashMap<String, Subsekvens> hentUt(){
        return subreg.hentUt(); //tar ut første? skal hente ut en vilkårlig
    }

    public int antall(){
        return subreg.antall();
    }

    //utvider slik at kun én tråd kan sette inn et hashmap av gangen
    public static HashMap<String, Subsekvens> lesFil(String fil) throws FileNotFoundException{
        laas.lock();

        try{
            return SubsekvensRegister.lesFil(fil);
        }
        finally{
            laas.unlock();
        }
    }

    public static HashMap<String, Subsekvens> slaSammen(HashMap<String, Subsekvens> map1, HashMap<String, Subsekvens> map2){
        return SubsekvensRegister.slaSammen(map1, map2);
    }
}