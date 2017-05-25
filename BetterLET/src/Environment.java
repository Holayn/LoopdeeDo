//The environment
public interface Environment {
	public static Environment emptyEnv(){
		return new EmptyEnvironment(); //returns an empty environment
	}
	public Environment extendEnv(Token t, Integer n);
	public Integer applyEnv(Token t);
	public boolean isEmptyEnv();
}
