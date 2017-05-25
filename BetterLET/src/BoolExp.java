//BoolExp node. Currently not part of LET implementation, but we included it anyway. Not used by official part of LET
//so removing this would have no effect
public class BoolExp extends Exp{
	private Token t;
	public BoolExp(Token t){
		this.t = t;
	}
	public String toString(){
		return t.toString();
	}
	public Token getToken(){
		return t;
	}
//	public ExpVal interpret(Environment env){
//		return new NumVal(Integer.parseInt(t.toString()));
//	}
}