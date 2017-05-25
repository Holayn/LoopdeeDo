
public class AndExp extends Exp{
	private Exp e1;
	private Exp e2;
	public AndExp(Exp e1, Exp e2){
		this.e1 = e1;
		this.e2 = e2;
	}
	public Exp getExp1(){
		return e1;
	}
	public Exp getExp2(){
		return e2;
	}
	public String toString(){
		return "(" + e1.toString() + " && " + e2.toString() + ")";
	}
}
