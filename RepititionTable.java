import java.util.HashMap;
public class RepititionTable{
    HashMap<Long, Integer> repititions;
    public RepititionTable(){
        repititions = new HashMap();
    }
    public boolean add(long l){
        repititions.putIfAbsent(l, 0);
        int a = repititions.get(l);
        a++;
        repititions.put(l,a);
        if(a>2){
            return false;
        }
        return true;
    }
}