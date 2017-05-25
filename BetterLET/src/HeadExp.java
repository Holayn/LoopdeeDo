
public class HeadExp extends Exp {
	private Exp e1;
	public HeadExp(Exp e) {		 
		e1 = e;
	}
	public Exp getExp1() {
		return this.e1;
	}
	public String toString() {
		return "head " + e1.toString();
	}
}
