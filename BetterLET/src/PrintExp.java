
public class PrintExp extends Exp {
	private Exp e;
	
	public PrintExp(Exp expression) {
		this.e = expression;
	}
	
	public Exp getExp() {
		return e;
	}
	public String toString() {
		return "print " + e.toString();
	}

}
