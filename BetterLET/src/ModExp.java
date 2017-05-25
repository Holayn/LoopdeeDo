//ModExp node
public class ModExp extends Exp{
	private Exp exp1;
	private Exp exp2;
	public ModExp(Exp e1, Exp e2){
		exp1 = e1;
		exp2 = e2;
	}
	public String toString(){
		return "(" + exp1 + " % " + exp2 + ")";
	}
	public Exp getExp1(){
		return exp1;
	}
	public Exp getExp2(){
		return exp2;
	}
//	public ExpVal interpret(Environment env){
//		NumVal e1 = (NumVal)exp1.interpret(env);
//		NumVal e2 = (NumVal)exp2.interpret(env);
//		return new NumVal(e1.getNumVal() - e2.getNumVal());
//	}
}
