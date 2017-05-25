
public class FunctionVal extends ExpVal{
	VarExp e1;
	Exp e2;
	Environment env;
	Store sto;
	public FunctionVal(VarExp e1, Exp e2, Environment env) {
		this.e1 = e1;
		this.e2 = e2;
		this.env = env;
	}
	public FunctionVal(VarExp e1, Exp e2, Environment env, Store sto) {
		this.e1 = e1;
		this.e2 = e2;
		this.env = env;
		this.sto = sto;
	}
	public VarExp getE1() {
		return e1;
	}
	public void setE1(VarExp e1) {
		this.e1 = e1;
	}
	public Exp getE2() {
		return e2;
	}
	public void setE2(Exp e2) {
		this.e2 = e2;
	}
	public Environment getEnv() {
		return env;
	}
	public Store getStore(){
		return sto;
	}
	public void setEnv(Environment env) {
		this.env = env;
	}
	public String toString(){
		return e2.toString();
	}
}
