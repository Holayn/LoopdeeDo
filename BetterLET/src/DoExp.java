//DoExp node
public class DoExp extends Exp{
	private Exp exp1;
	private Exp exp2;
	private Token token;
	public DoExp(Token t, Exp e1, Exp e2){
		token = t;
		exp1 = e1;
		exp2 = e2;
	}
	public String toString(){
		return "do " + exp2.toString() + token.toString() + " = " + exp2.toString();
	}
	public Token getToken(){
		return token;
	}
	public Exp getExp1(){
		return exp1;
	}
	public Exp getExp2(){
		return exp2;
	}
}
