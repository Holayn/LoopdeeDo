import java.util.HashMap;

public class Store {
	 HashMap<Integer,StoVal> hmap;
	 Integer addCount = 0;
	 
	 public Store(){
		 hmap = new HashMap<Integer,StoVal>();
	 }
	 
	public Integer newRef(StoVal s){
		hmap.put(addCount,s);
		addCount++;
		return ((Integer)addCount-1);
	}
	public void setRef(Integer i, StoVal s){
		hmap.put(i,s);
	}
	public StoVal deRef(Integer i){
		return hmap.get(i);
	}
	public String toString(){
		return hmap.toString();
	}
}
