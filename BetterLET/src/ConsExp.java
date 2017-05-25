
public class ConsExp extends Exp {
	Exp e1;
	Exp e2;
	
	public ConsExp(Exp e1, Exp e2) {
		 this.e1 = e1;
		 this.e2 = e2;
	}
	
	public Exp getExp1() {
		return this.e1;
	}
	
	public Exp getExp2() {
		return this.e2;
	}
	
	public String toString() {
		return "cons("+ e1.toString() + "," + e2.toString()+")";
	}
}
