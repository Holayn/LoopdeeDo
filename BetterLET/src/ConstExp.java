//ConstExp node
public class ConstExp extends Exp{
	private Token t;
	public ConstExp(Token t){
		this.t = t;
	}
	public String toString(){
		return t.toString();
	}
	public int toInt (){
		return Integer.parseInt(this.toString());
	}
	public Token getToken(){
		return t;
	}
	public void setToken(Token t){
		this.t = t;
	}
//	public ExpVal interpret(Environment env){
//		return new NumVal(Integer.parseInt(t.toString()));
//	}
}
