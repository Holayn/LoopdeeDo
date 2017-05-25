//IsZeroExp node
public class IsZeroExp extends Exp{
	private Exp exp;
	public IsZeroExp(Exp exp){
		this.exp = exp;
	}
	public String toString(){
		return "zero?(" + exp.toString() + ")";
	}
	public Exp getExp(){
		return exp;
	}
//	public ExpVal interpret(Environment env){
//		ExpVal e = exp.interpret(env);
//		return new BoolVal(e.getNumVal() == 0);
//	}
}
