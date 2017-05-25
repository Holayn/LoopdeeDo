//IfExp node
public class IfExp extends Exp{
	private Exp exp1;
	private Exp exp2;
	private Exp exp3;
	
	public IfExp(Exp e1, Exp e2, Exp e3) {
		this.exp1 = e1;
		this.exp2 = e2;
		this.exp3 = e3;		
	}
	
	public Exp getExp1() {
		return exp1;
	}
	
	public Exp getExp2() {
		return exp2;
	}
	
	public Exp getExp3() {
		return exp3;
	}
	
	public String toString() {
		return "if "+ exp1.toString() + " then " + exp2.toString() + " else " + exp2.toString();	
	}
//	public void interpret(){
//		
//	}
}