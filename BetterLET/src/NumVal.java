//A NumVal
public class NumVal extends ExpVal{
	int n;
	public NumVal(int num){
		n = num;
	}
	public void setNumVal(int num){
		n = num;
	}
	public int getNumVal(){
		return n;
	}
	public String toString(){
		return Integer.toString(n);
	}
}
