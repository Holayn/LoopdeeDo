
public class StringExp extends Exp {
	private Token t;
	public StringExp(Token token){
		this.t = token;
	}
	public String toString(){
		return t.toString();
	}
	public Token getToken(){
		return t;
	}
	
}
