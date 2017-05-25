
public class EmptyEnvironment extends AbstractEnvironment {
	public Integer applyEnv(Token t){
		throw new UndefinedTokenEnvironmentException("Token "+t.toString()+" is not defined!");
	}
	public boolean isEmptyEnv(){
		return true;
	}
}
