//A generic ExpVal
public abstract class ExpVal extends StoVal {
	boolean getBoolVal() { throw new UnimplementedMethodException(); }
	int     getNumVal() { throw new UnimplementedMethodException(); }
	String  getStringVal() { throw new UnimplementedMethodException();} //or do instanceof
}
