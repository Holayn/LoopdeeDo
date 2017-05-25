//A generic expression
public abstract class Exp {
	public ExpVal  getExpVal()  { throw new UnimplementedMethodException(); }
	public NumVal  getNumVal()  { throw new UnimplementedMethodException(); }
	public BoolVal getBoolVal() { throw new UnimplementedMethodException(); }
	public StringVal getStringVal() {throw new UnimplementedMethodException();}
	public ListVal getListVal() {throw new UnimplementedMethodException();}
	public abstract String toString();
//	public abstract ExpVal interpret(Environment env);
	//public abstract ExpVal interpret();
}
