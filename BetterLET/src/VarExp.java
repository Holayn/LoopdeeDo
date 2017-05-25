//A VarExp node
public class VarExp extends Exp{
	Token t;
	public VarExp(Token t){
		this.t = t;
	}
	public String toString(){
//		return "The VarExp is " + t.toString();
		return t.toString();
	}
	public Token getVar(){
		return t;
	}
//	public ExpVal interpret(Environment env){
//		return env.applyEnv(t);
//	}
}
