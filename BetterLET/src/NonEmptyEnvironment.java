
public class NonEmptyEnvironment extends AbstractEnvironment{
	public Integer applyEnv(Token t){
		if(this.t.toString().equals(t.toString())){
			return n;
		}
		else{
			//recursive calls through environments
			return this.reference.applyEnv(t);
		}
	}
	public boolean isEmptyEnv(){
		return false;
	}
	public String toString(){
		return n.toString();
	}
}
