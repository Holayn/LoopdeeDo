
public class ReadExp extends Exp{
	private Token e;
	
	public ReadExp(Token token){
		this.e = token;
	}
	public Token getToken() {
		return e;
	}
	@Override
	public String toString() {
		return "read " + e.toString();
	}
	

	
}
