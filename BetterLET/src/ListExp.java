
public class ListExp extends Exp {
	Exp e1;
	Exp e2;
	public ListExp(){
		e1 = null;
		e2 = null;
	}
	public ListExp(Exp e){
		this.e1 = e;
	}
	public ListExp(Exp e1, Exp e2){
		this.e1 = e1;
		this.e2 = e2;
	}
	public String toString(){
		String list = "[";
		if(e1 == null){
		}
		else if(e2 == null){
			list += e1.toString();
		}
		else{
			list += e1.toString() + "," + ((ListExp)e2).subToString();
		}
		list += "]";
		return list;
	}
	public String subToString(){
		if(e1 == null){
			return "[]";
		}
		else if(e2 == null){
			return e1.toString();
		}
		else{
			return e1.toString() + "," +  ((ListExp)e2).subToString();
		}
	}
	public Exp getExp1(){
		return e1;
	}
	
	public Exp getExp2(){
		return e2;
	}

}
