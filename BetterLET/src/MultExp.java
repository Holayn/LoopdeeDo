//MultExp node
public class MultExp extends Exp{
	private Exp exp1;
	private Exp exp2;
	public MultExp(Exp e1, Exp e2){
		exp1 = e1;
		exp2 = e2;
	}
	public String toString(){
		return "(" + exp1 + " * " + exp2 + ")";
	}
	public Exp getExp1(){
		return exp1;
	}
	public Exp getExp2(){
		return exp2;
	}
}
