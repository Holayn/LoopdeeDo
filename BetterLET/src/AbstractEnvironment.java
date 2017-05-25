
public abstract class AbstractEnvironment implements Environment{
	protected Token t;
	protected Integer n;
	protected Environment reference; //reference to previous environment
	//EmptyEnvironment and NonEmptyEnvironment call same extendEnv. EmptyEnvironment stays empty.
	public Environment extendEnv(Token t, Integer n){
		NonEmptyEnvironment env = new NonEmptyEnvironment();
		env.t = t;
		env.n = n;
		env.reference = this; //reference to previous environment
		return env;
	}
}
