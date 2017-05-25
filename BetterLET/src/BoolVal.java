//Boolean value
public class BoolVal extends ExpVal{
	boolean bool;
	public BoolVal(boolean b){
		bool = b;
	}
	public String toString(){
		if(bool){
			return "true";
		}
		else{
			return "false";
		}
	}

	public boolean getBoolVal(){
		if(bool){
			return true;
		}
		else{
			return false;
		}
	}
	
}
