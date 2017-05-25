//The LoopExp node in the parser tree
public class LoopExp extends Exp{
	private Exp e1;
	private Exp e2;
	private Token t;
	public LoopExp(Exp e1){
		this.e1 = e1;
	}
	public Exp getExp(){
		return e1;
	}
	public String toString(){
		return "save " + e1.toString();
	}
}
