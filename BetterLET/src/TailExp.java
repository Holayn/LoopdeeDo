
public class TailExp extends Exp {
	Exp e1;
	
	public TailExp(Exp e) {
		 this.e1 = e;		 
	}
	
	public Exp getExp1() {
		return this.e1;
	}
	
	public String toString() {
		return "tail " + e1.toString();
	}
}
