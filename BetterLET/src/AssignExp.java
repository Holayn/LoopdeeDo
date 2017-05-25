
public class AssignExp extends Exp{
	Token t;
	Exp e;
	public AssignExp(Token t, Exp e){
		this.t = t;
		this.e = e;
	}
	public String toString(){
		return t.toString() + " = " + e.toString();
	}
	public Token getVar(){
		return t;
	}
	public Exp getExp(){
		return e;
	}
}
