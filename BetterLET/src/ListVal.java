import java.util.LinkedList;
import java.util.List;

public class ListVal extends ExpVal{
	LinkedList<Integer> list;
	
	public ListVal() {
		this.list = new LinkedList<Integer>();
	}
	
	public String toString() {
		return list.toString();
	}
	
	public List getListVal() {
		return this.list;
	}
}
