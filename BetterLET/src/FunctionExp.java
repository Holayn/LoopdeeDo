//This is the FunctionExp node
public class FunctionExp extends Exp{
	private VarExp e1;
	private Exp e2;
	public FunctionExp(Exp e1, Exp e2){
		this.e1 = (VarExp) e1;
		this.e2 = e2;
	}
	public Exp getExp1(){
		return e1;
	}
	public Exp getExp2(){
		return e2;
	}
	public String toString() {
		return "fn(" + e1.toString() + ") " + e2.toString();
	}
}
