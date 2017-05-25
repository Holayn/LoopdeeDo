//Starting node!
public class AProgram {
	private Exp e; //AProgram node should point to an expression
	public AProgram(Exp e){
		this.e = e;
	}
	public String toString(){
		return "The expression " + e;
	}
	public Exp getExp(){
		return e;
	}
	
	//ADDING INTERPRETER TO NODE !!!
	//Goes down the tree, prints out result
//	public void interpret() {
//		//Create a new environment
//		Environment env = Environment.emptyEnv();
//		System.out.println(e.interpret(env));
//	}
//	
}
