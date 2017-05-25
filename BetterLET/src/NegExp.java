
public class NegExp extends Exp{
	private Exp e;
	private Token t;
	public NegExp(Token t){
		this.t = t;
	}
	public Token getToken(){
		return t;
	}
	public void setToken(Token t){
		this.t = t;
	}
	public String toString(){
		return "-" + t;
	}
}
