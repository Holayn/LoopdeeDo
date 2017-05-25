import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

//Interpreter file
public class Interp {
	private static Environment env = Environment.emptyEnv(); //global environment
	private static Store sto = new Store(); //global store
	private static String savedFunction = "";
	public static void interp(AProgram pgm){
		Exp e = pgm.getExp();
		Store passedStore = new Store(); //local store
		Environment passedEnv = Environment.emptyEnv(); // local environment
		System.out.println(Interp.valueOf(e, passedEnv, passedStore));
//		System.out.println(Interp.valueOf(e, env));
	}
	
	//valueOf for global store and environment, with local store and environment
	
	public static ExpVal valueOf(Exp exp, Environment passedEnv, Store passedSto){
		if(exp instanceof ConstExp){
			return new NumVal(Integer.parseInt(((ConstExp)exp).getToken().toString()));
		} 
		else if(exp instanceof AssignExp){
			Token t = ((AssignExp)exp).getVar();
			ExpVal e = valueOf(((AssignExp)exp).getExp(), passedEnv, passedSto);
			Integer addr = sto.newRef(e);
			env = env.extendEnv(t,addr); //set in global environment...?
			return (ExpVal) sto.deRef(env.applyEnv(t));
//			return e;
		}
		else if(exp instanceof DiffExp){
			NumVal n1 = (NumVal)valueOf(((DiffExp)exp).getExp1(), passedEnv, passedSto);
			NumVal n2 = (NumVal)valueOf(((DiffExp)exp).getExp2(), passedEnv, passedSto);
			return new NumVal(n1.getNumVal() - n2.getNumVal());
		}
		else if(exp instanceof VarExp){
			Token t = ((VarExp)exp).getVar();
			try{
				ExpVal e2 = (ExpVal) sto.deRef(env.applyEnv(t));
				try{
					ExpVal e3;
					e3 = (ExpVal) passedSto.deRef(passedEnv.applyEnv(t)); //get locally defined over global one
					return e3;
				}
				catch(Exception ex){
					return e2;
				}
			}
			catch (Exception ex){
				ExpVal e3 = (ExpVal) passedSto.deRef(passedEnv.applyEnv(t));
				return e3;
			}
		}
		else if(exp instanceof DoExp){
			ExpVal e1 = valueOf(((DoExp)exp).getExp1(), passedEnv, passedSto);
			Integer addr = passedSto.newRef(e1);
			Environment newEnv = passedEnv.extendEnv(((DoExp)exp).getToken(), addr);
			return valueOf(((DoExp)exp).getExp2(), newEnv, passedSto);
		}
		else if(exp instanceof StringExp){
			return new StringVal(((StringExp)exp).getToken().toString());
		}
		else if(exp instanceof FunctionExp){
			VarExp e1 = (VarExp) ((FunctionExp) exp).getExp1();
			Exp e2 = ((FunctionExp) exp).getExp2();
			return new FunctionVal(e1, e2, passedEnv, passedSto);
		}
		else if(exp instanceof CallExp){
			FunctionVal e1 = (FunctionVal) valueOf(((CallExp) exp).getExp1(), passedEnv, passedSto);
			ExpVal e2 = valueOf(((CallExp) exp).getExp2(), passedEnv, passedSto);
			return applyFunction(e1,e2);
		}
		else if(exp instanceof ListExp){
			ListVal list = new ListVal();
			if(((ListExp)exp).getExp1() != null){
				NumVal n1 = (NumVal) valueOf(((ListExp)exp).getExp1(), passedEnv, passedSto);
				list.getListVal().add(n1);
				if(((ListExp)exp).getExp2() != null){
					ListVal templist = (ListVal)valueOf(((ListExp)exp).getExp2(), passedEnv, passedSto);
					list.getListVal().addAll(templist.getListVal());
					return list;
				}
				return list;
			}
			return list;
		}
		else if(exp instanceof HeadExp){
			ListVal templist = (ListVal)valueOf(((HeadExp)exp).getExp1(), passedEnv, passedSto);
			LinkedList list = (LinkedList)templist.getListVal();
			if(list.size() != 0)
				return (NumVal)templist.getListVal().get(0);
			else
				return new StringVal("Head of empty list is nothing");
		}
		else if(exp instanceof TailExp){
			ListVal list = new ListVal();
			ListVal templist = (ListVal)valueOf(((TailExp)exp).getExp1(), passedEnv, passedSto);
			list.getListVal().addAll(templist.getListVal());
			if(list.getListVal().size() != 0){
				list.getListVal().remove(0);
				return list;
			}
			else{
				return new StringVal("Tail of empty list is nothing");
			}
		}
		else if(exp instanceof ConsExp){
			ListVal list = new ListVal();
			ListVal templist1 = (ListVal)valueOf(((ConsExp)exp).getExp1(), passedEnv, passedSto);
			ListVal templist2 = (ListVal)valueOf(((ConsExp)exp).getExp2(), passedEnv, passedSto);
			list.getListVal().addAll(templist2.getListVal());
			list.getListVal().addAll(templist1.getListVal());
			//If the second arg in cons is a variable, change it in memory
			if(((ConsExp)exp).getExp2() instanceof VarExp){
				//get the variable
				VarExp v = (VarExp)((ConsExp)exp).getExp2();
				//get location stored in that variable
				try{
					//local
					Integer addr = passedEnv.applyEnv(v.getVar());
					passedSto.setRef(addr, list);
				}
				catch(Exception ex){
					//global
					Integer addr = env.applyEnv(v.getVar());
					sto.setRef(addr, list);
				}
			}
			return list;
		}
		else if(exp instanceof AndExp){
			ExpVal e1 = valueOf(((AndExp)exp).getExp1(), passedEnv, passedSto);
			ExpVal e2 = valueOf(((AndExp)exp).getExp2(), passedEnv, passedSto);
			if(e1.getBoolVal() && e2.getBoolVal()){
				return new BoolVal(true);
			}
			else return new BoolVal(false);
		}
		else if(exp instanceof OrExp){
			ExpVal e1 = valueOf(((OrExp)exp).getExp1(), passedEnv, passedSto);
			ExpVal e2 = valueOf(((OrExp)exp).getExp2(), passedEnv, passedSto);
			if(e1.getBoolVal() || e2.getBoolVal()){
				return new BoolVal(true);
			}
			else return new BoolVal(false);
		}
		else if(exp instanceof GreaterEqualExp){
			ExpVal e1 = valueOf(((GreaterEqualExp)exp).getExp1(), passedEnv, passedSto);
			ExpVal e2 = valueOf(((GreaterEqualExp)exp).getExp2(), passedEnv, passedSto);
			return new BoolVal(e1.getNumVal() >= e2.getNumVal());
		}
		else if(exp instanceof LessEqualExp){
			ExpVal e1 = valueOf(((LessEqualExp)exp).getExp1(), passedEnv, passedSto);
			ExpVal e2 = valueOf(((LessEqualExp)exp).getExp2(), passedEnv, passedSto);
			return new BoolVal(e1.getNumVal() <= e2.getNumVal());
		}
		else if(exp instanceof GreaterExp){
			ExpVal e1 = valueOf(((GreaterExp)exp).getExp1(), passedEnv, passedSto);
			ExpVal e2 = valueOf(((GreaterExp)exp).getExp2(), passedEnv, passedSto);
			return new BoolVal(e1.getNumVal() > e2.getNumVal());
		}
		else if(exp instanceof LessExp){
			ExpVal e1 = valueOf(((LessExp)exp).getExp1(), passedEnv, passedSto);
			ExpVal e2 = valueOf(((LessExp)exp).getExp2(), passedEnv, passedSto);
			return new BoolVal(e1.getNumVal() < e2.getNumVal());
		}
		else if(exp instanceof EqualsExp){
			ExpVal e1 = valueOf(((EqualsExp)exp).getExp1(), passedEnv, passedSto);
			ExpVal e2 = valueOf(((EqualsExp)exp).getExp2(), passedEnv, passedSto);
			return new BoolVal(e1.getNumVal() == e2.getNumVal());
		}
		else if(exp instanceof NegExp){
			return new NumVal(-1*Integer.parseInt(((NegExp)exp).getToken().toString()));
		} 
		else if(exp instanceof BoolExp) {
			return new BoolVal(Boolean.parseBoolean(((BoolExp)exp).getToken().toString()));
		}
		else if (exp instanceof IfExp) {
			BoolVal e1 = (BoolVal)valueOf(((IfExp)exp).getExp1(), passedEnv, passedSto);
			if (e1.getBoolVal()) {
				return valueOf(((IfExp)exp).getExp2(), passedEnv, passedSto);
			} else {
				return valueOf(((IfExp)exp).getExp3(), passedEnv, passedSto);
			}
		}
		else if(exp instanceof DiffExp){
			NumVal n1 = (NumVal)valueOf(((DiffExp)exp).getExp1(), passedEnv, passedSto);
			NumVal n2 = (NumVal)valueOf(((DiffExp)exp).getExp2(), passedEnv, passedSto);
			return new NumVal(n1.getNumVal() - n2.getNumVal());
		}
		else if(exp instanceof AddExp){
			NumVal n1 = (NumVal)valueOf(((AddExp)exp).getExp1(), passedEnv, passedSto);
			NumVal n2 = (NumVal)valueOf(((AddExp)exp).getExp2(), passedEnv, passedSto);
			return new NumVal(n1.getNumVal() + n2.getNumVal());
		}
		else if(exp instanceof MultExp){
			NumVal n1 = (NumVal)valueOf(((MultExp)exp).getExp1(), passedEnv, passedSto);
			NumVal n2 = (NumVal)valueOf(((MultExp)exp).getExp2(), passedEnv, passedSto);
			return new NumVal(n1.getNumVal() * n2.getNumVal());
		}
		else if(exp instanceof DivExp){
			NumVal n1 = (NumVal)valueOf(((DivExp)exp).getExp1(), passedEnv, passedSto);
			NumVal n2 = (NumVal)valueOf(((DivExp)exp).getExp2(), passedEnv, passedSto);
			return new NumVal(n1.getNumVal() / n2.getNumVal());
		}
		else if(exp instanceof ModExp){
			NumVal n1 = (NumVal)valueOf(((ModExp)exp).getExp1(), passedEnv, passedSto);
			NumVal n2 = (NumVal)valueOf(((ModExp)exp).getExp2(), passedEnv, passedSto);
			return new NumVal(n1.getNumVal() % n2.getNumVal());
		}
		else if(exp instanceof IsZeroExp){
			ExpVal e = valueOf(((IsZeroExp)exp).getExp(), passedEnv, passedSto);
			return new BoolVal(e.getNumVal() == 0);
		}
		else if(exp instanceof StringExp){
			return new StringVal(((StringExp)exp).getToken().toString());
		}
		else if(exp instanceof PrintExp){
			return new StringVal(((StringExp)(((PrintExp)exp).getExp())).getToken().toString());
		}
		else if(exp instanceof ReadExp){
			try {
					File initialFile = new File(((ReadExp)exp).getToken().toString());
				    InputStream targetStream = new FileInputStream(initialFile);
				    Parser parser = new Parser(targetStream);
				    ExpVal e;
				    try
			          {
				    	 AProgram subpgm = parser.Pgm();
			             System.out.println("Successful parse of input file! Interpreting...");
						 e = Interp.valueOf(subpgm.getExp(), passedEnv, passedSto);
						 return e;
			          }
			          catch (Exception ex)
			          {
			             System.out.println("Syntax check failed: " + ex.getMessage());
			             parser.ReInit(System.in);
			          }
			          catch (Error ex)
			          {
			             System.out.println("Oops");
			             System.out.println(ex.getMessage());
			          }
			} catch (FileNotFoundException e) {
				System.out.println("Incorrect file or file is missing ");
				throw new UnimplementedMethodException();
			}
			return new StringVal("Finished reading input file");
		}
		else if(exp instanceof LoopExp){
			String localFunction = ((LoopExp)exp).getExp().toString();
			boolean loop = true;
			System.out.println("Program " + localFunction + " running. \nEnter a function parameter; break to exit:");
			while(loop){
				System.out.print("LoopFunction>");
				Scanner scan = new Scanner(System.in); 
				String s = scan.next();
				try{
					if(s.equals("break")){
						loop = false;
					}
					if(loop){
						//Building LetExp in order to call function
						String letExp = "";
						letExp += "do";
						letExp += "(f," + s;
						letExp += ") " + "f = " + localFunction;
						//Now run the LetExp through the parser and interpreter
						InputStream stream = new ByteArrayInputStream(letExp.getBytes(StandardCharsets.UTF_8));
						Parser parser = new Parser(stream);
						try
				        {
							AProgram subpgm = parser.Pgm();
							Interp.interp(subpgm);
				        }
				        catch (Exception e)
				        {
				           System.out.println("Syntax check failed: " + e.getMessage() + ". Check your original saved function/expression syntax");
				           parser.ReInit(System.in);
				        }
				        catch (Error e)
				        {
				           System.out.println("Oops");
				           System.out.println(e.getMessage());
				        }
					}
				}
				catch(Exception e){
					System.out.println("Something went wrong");
				}
			}
			return new StringVal("Exiting...");
		}
		else if(exp instanceof SaveExp){
			String function = ((SaveExp)exp).getExp().toString();
			savedFunction = function; 
			return new StringVal("Function saved");
		}
		else if(exp instanceof RunExp){
			if(!savedFunction.equals("")){
			boolean loop = true;
			System.out.println("Program " + savedFunction + " running. \nEnter a function parameter; break to exit:");
			while(loop){
				System.out.print("SavedFunction>");
				Scanner scan = new Scanner(System.in); 
				String s = scan.next();
				try{
					if(s.equals("break")){
						loop = false;
					}
					if(loop){
						//Building LetExp in order to call function
						String letExp = "";
						letExp += "do";
						letExp += "(f," + s;
						letExp += ") " + "f = " + savedFunction;
						//Now run the LetExp through the parser and interpreter
						InputStream stream = new ByteArrayInputStream(letExp.getBytes(StandardCharsets.UTF_8));
						Parser parser = new Parser(stream);
						try
				        {
							AProgram subpgm = parser.Pgm();
							Interp.interp(subpgm);
				        }
				        catch (Exception e)
				        {
				           System.out.println("Syntax check failed: " + e.getMessage() + ". Check your original saved function/expression syntax");
				           parser.ReInit(System.in);
				        }
				        catch (Error e)
				        {
				           System.out.println("Oops");
				           System.out.println(e.getMessage());
				        }
					}
				}
				catch(Exception e){
					System.out.println("Something went wrong");
				}
			}
			return new StringVal("Exiting...");
		}
			return new StringVal("No function saved");
		}
		else{
			throw new UnimplementedMethodException();
		}
	}
	
	
	
	
	
	
	
	
	
	//BEFORE IMPLEMENTING GLOBAL/LOCAL STORE/ENVIRONMENT
	
	public static ExpVal valueOf(Exp exp, Environment passedEnv){
		if(exp instanceof ConstExp){
			return new NumVal(Integer.parseInt(((ConstExp)exp).getToken().toString()));
		} 
		else if(exp instanceof AssignExp){
			Token t = ((AssignExp)exp).getVar();
			ExpVal e = valueOf(((AssignExp)exp).getExp(), passedEnv);
			Integer addr = sto.newRef(e);
			env = passedEnv.extendEnv(t,addr); //set in global environment...?
			return (ExpVal) sto.deRef(env.applyEnv(t));
//			return e;
		}
		else if(exp instanceof ListExp){
			ListVal list = new ListVal();
			if(((ListExp)exp).getExp1() != null){
				NumVal n1 = (NumVal) valueOf(((ListExp)exp).getExp1(), passedEnv);
				list.getListVal().add(n1);
				if(((ListExp)exp).getExp2() != null){
					ListVal templist = (ListVal)valueOf(((ListExp)exp).getExp2(), passedEnv);
					list.getListVal().addAll(templist.getListVal());
					return list;
				}
				return list;
			}
			return list;
		}
		else if(exp instanceof HeadExp){
			ListVal templist = (ListVal)valueOf(((HeadExp)exp).getExp1(), passedEnv);
			LinkedList list = (LinkedList)templist.getListVal();
			if(list.size() != 0)
				return (NumVal)templist.getListVal().get(0);
			else
				return new StringVal("Head of empty list is nothing");
		}
		else if(exp instanceof TailExp){
			ListVal list = new ListVal();
			ListVal templist = (ListVal)valueOf(((TailExp)exp).getExp1(), passedEnv);
			list.getListVal().addAll(templist.getListVal());
			if(list.getListVal().size() != 0){
				list.getListVal().remove(0);
				return list;
			}
			else{
				return new StringVal("Tail of empty list is nothing");
			}
		}
		else if(exp instanceof ConsExp){
			ListVal list = new ListVal();
			ListVal templist1 = (ListVal)valueOf(((ConsExp)exp).getExp1(), passedEnv);
			ListVal templist2 = (ListVal)valueOf(((ConsExp)exp).getExp2(), passedEnv);
			list.getListVal().addAll(templist2.getListVal());
			list.getListVal().addAll(templist1.getListVal());
			//If the second arg in cons is a variable, change it in memory
			if(((ConsExp)exp).getExp2() instanceof VarExp){
				VarExp v = (VarExp)((ConsExp)exp).getExp2();
				Integer addr = passedEnv.applyEnv(v.getVar());
				sto.setRef(addr, list);
			}
			return list;
		}
		else if(exp instanceof AndExp){
			ExpVal e1 = valueOf(((AndExp)exp).getExp1(), passedEnv);
			ExpVal e2 = valueOf(((AndExp)exp).getExp2(), passedEnv);
			if(e1.getBoolVal() && e2.getBoolVal()){
				return new BoolVal(true);
			}
			else return new BoolVal(false);
		}
		else if(exp instanceof OrExp){
			ExpVal e1 = valueOf(((OrExp)exp).getExp1(), passedEnv);
			ExpVal e2 = valueOf(((OrExp)exp).getExp2(), passedEnv);
			if(e1.getBoolVal() || e2.getBoolVal()){
				return new BoolVal(true);
			}
			else return new BoolVal(false);
		}
		else if(exp instanceof GreaterEqualExp){
			ExpVal e1 = valueOf(((GreaterEqualExp)exp).getExp1(), passedEnv);
			ExpVal e2 = valueOf(((GreaterEqualExp)exp).getExp2(), passedEnv);
			return new BoolVal(e1.getNumVal() >= e2.getNumVal());
		}
		else if(exp instanceof LessEqualExp){
			ExpVal e1 = valueOf(((LessEqualExp)exp).getExp1(), passedEnv);
			ExpVal e2 = valueOf(((LessEqualExp)exp).getExp2(), passedEnv);
			return new BoolVal(e1.getNumVal() <= e2.getNumVal());
		}
		else if(exp instanceof GreaterExp){
			ExpVal e1 = valueOf(((GreaterExp)exp).getExp1(), passedEnv);
			ExpVal e2 = valueOf(((GreaterExp)exp).getExp2(), passedEnv);
			return new BoolVal(e1.getNumVal() > e2.getNumVal());
		}
		else if(exp instanceof LessExp){
			ExpVal e1 = valueOf(((LessExp)exp).getExp1(), passedEnv);
			ExpVal e2 = valueOf(((LessExp)exp).getExp2(), passedEnv);
			return new BoolVal(e1.getNumVal() < e2.getNumVal());
		}
		else if(exp instanceof EqualsExp){
			ExpVal e1 = valueOf(((EqualsExp)exp).getExp1(), passedEnv);
			ExpVal e2 = valueOf(((EqualsExp)exp).getExp2(), passedEnv);
			return new BoolVal(e1.getNumVal() == e2.getNumVal());
		}
		else if(exp instanceof NegExp){
			return new NumVal(-1*Integer.parseInt(((NegExp)exp).getToken().toString()));
		} 
		else if(exp instanceof BoolExp) {
			return new BoolVal(Boolean.parseBoolean(((BoolExp)exp).getToken().toString()));
		}
		else if (exp instanceof IfExp) {
			BoolVal e1 = (BoolVal)valueOf(((IfExp)exp).getExp1(), passedEnv);
			if (e1.getBoolVal()) {
				return valueOf(((IfExp)exp).getExp2(), passedEnv);
			} else {
				return valueOf(((IfExp)exp).getExp3(), passedEnv);
			}
		}
		else if(exp instanceof DiffExp){
			NumVal n1 = (NumVal)valueOf(((DiffExp)exp).getExp1(), passedEnv);
			NumVal n2 = (NumVal)valueOf(((DiffExp)exp).getExp2(), passedEnv);
			return new NumVal(n1.getNumVal() - n2.getNumVal());
		}
		else if(exp instanceof AddExp){
			NumVal n1 = (NumVal)valueOf(((AddExp)exp).getExp1(), passedEnv);
			NumVal n2 = (NumVal)valueOf(((AddExp)exp).getExp2(), passedEnv);
			return new NumVal(n1.getNumVal() + n2.getNumVal());
		}
		else if(exp instanceof MultExp){
			NumVal n1 = (NumVal)valueOf(((MultExp)exp).getExp1(), passedEnv);
			NumVal n2 = (NumVal)valueOf(((MultExp)exp).getExp2(), passedEnv);
			return new NumVal(n1.getNumVal() * n2.getNumVal());
		}
		else if(exp instanceof DivExp){
			NumVal n1 = (NumVal)valueOf(((DivExp)exp).getExp1(), passedEnv);
			NumVal n2 = (NumVal)valueOf(((DivExp)exp).getExp2(), passedEnv);
			return new NumVal(n1.getNumVal() / n2.getNumVal());
		}
		else if(exp instanceof ModExp){
			NumVal n1 = (NumVal)valueOf(((ModExp)exp).getExp1(), passedEnv);
			NumVal n2 = (NumVal)valueOf(((ModExp)exp).getExp2(), passedEnv);
			return new NumVal(n1.getNumVal() % n2.getNumVal());
		}
		else if(exp instanceof IsZeroExp){
			ExpVal e = valueOf(((IsZeroExp)exp).getExp(), passedEnv);
			return new BoolVal(e.getNumVal() == 0);
		}
		else if(exp instanceof VarExp){
			Token t = ((VarExp)exp).getVar();
			return (ExpVal) sto.deRef(passedEnv.applyEnv(t));
		}
		else if(exp instanceof DoExp){
			ExpVal e1 = valueOf(((DoExp)exp).getExp1(), passedEnv);
			Integer addr = sto.newRef(e1);
			Environment newEnv = passedEnv.extendEnv(((DoExp)exp).getToken(), addr);
			return valueOf(((DoExp)exp).getExp2(), newEnv);
		}
		else if(exp instanceof StringExp){
			return new StringVal(((StringExp)exp).getToken().toString());
		}
		else if(exp instanceof PrintExp){
			return new StringVal(((StringExp)(((PrintExp)exp).getExp())).getToken().toString());
		}
		else if(exp instanceof ReadExp){
			try {
					File initialFile = new File(((ReadExp)exp).getToken().toString());
				    InputStream targetStream = new FileInputStream(initialFile);
				    Parser parser = new Parser(targetStream);
				    ExpVal e;
				    try
			          {
				    	 AProgram subpgm = parser.Pgm();
			             System.out.println("Successful parse of input file! Interpreting...");
						 e = Interp.valueOf(subpgm.getExp(), passedEnv);
						 return e;
			          }
			          catch (Exception ex)
			          {
			             System.out.println("Syntax check failed: " + ex.getMessage());
			             parser.ReInit(System.in);
			          }
			          catch (Error ex)
			          {
			             System.out.println("Oops");
			             System.out.println(ex.getMessage());
			          }
			} catch (FileNotFoundException e) {
				System.out.println("Incorrect file or file is missing ");
				throw new UnimplementedMethodException();
			}
			return new StringVal("Finished reading input file");
		}
		else if(exp instanceof FunctionExp){
			VarExp e1 = (VarExp) ((FunctionExp) exp).getExp1();
			Exp e2 = ((FunctionExp) exp).getExp2();
			return new FunctionVal(e1, e2, passedEnv);
		}
		else if(exp instanceof CallExp){
			FunctionVal e1 = (FunctionVal) valueOf(((CallExp) exp).getExp1(), passedEnv);
			ExpVal e2 = valueOf(((CallExp) exp).getExp2(), passedEnv);
			return applyFunction(e1,e2);
		}
		else if(exp instanceof LoopExp){
			String localFunction = ((LoopExp)exp).getExp().toString();
			boolean loop = true;
			System.out.println("Program " + localFunction + " running. \nEnter a function parameter; break to exit:");
			while(loop){
				System.out.print("LoopFunction>");
				Scanner scan = new Scanner(System.in); 
				String s = scan.next();
				try{
					if(s.equals("break")){
						loop = false;
					}
					if(loop){
						//Building LetExp in order to call function
						String letExp = "";
						letExp += "do";
						letExp += "(f," + s;
						letExp += ") " + "f = " + localFunction;
						//Now run the LetExp through the parser and interpreter
						InputStream stream = new ByteArrayInputStream(letExp.getBytes(StandardCharsets.UTF_8));
						Parser parser = new Parser(stream);
						try
				        {
							AProgram subpgm = parser.Pgm();
							Interp.interp(subpgm);
				        }
				        catch (Exception e)
				        {
				           System.out.println("Syntax check failed: " + e.getMessage() + ". Check your original saved function/expression syntax");
				           parser.ReInit(System.in);
				        }
				        catch (Error e)
				        {
				           System.out.println("Oops");
				           System.out.println(e.getMessage());
				        }
					}
				}
				catch(Exception e){
					System.out.println("Something went wrong");
				}
			}
			return new StringVal("Exiting...");
		}
		else if(exp instanceof SaveExp){
			String function = ((SaveExp)exp).getExp().toString();
			savedFunction = function; 
			return new StringVal("Function saved");
		}
		else if(exp instanceof RunExp){
			if(!savedFunction.equals("")){
			boolean loop = true;
			System.out.println("Program " + savedFunction + " running. \nEnter a function parameter; break to exit:");
			while(loop){
				System.out.print("SavedFunction>");
				Scanner scan = new Scanner(System.in); 
				String s = scan.next();
				try{
					if(s.equals("break")){
						loop = false;
					}
					if(loop){
						//Building LetExp in order to call function
						String letExp = "";
						letExp += "do";
						letExp += "(f," + s;
						letExp += ") " + "f = " + savedFunction;
						//Now run the LetExp through the parser and interpreter
						InputStream stream = new ByteArrayInputStream(letExp.getBytes(StandardCharsets.UTF_8));
						Parser parser = new Parser(stream);
						try
				        {
							AProgram subpgm = parser.Pgm();
							Interp.interp(subpgm);
				        }
				        catch (Exception e)
				        {
				           System.out.println("Syntax check failed: " + e.getMessage() + ". Check your original saved function/expression syntax");
				           parser.ReInit(System.in);
				        }
				        catch (Error e)
				        {
				           System.out.println("Oops");
				           System.out.println(e.getMessage());
				        }
					}
				}
				catch(Exception e){
					System.out.println("Something went wrong");
				}
			}
			return new StringVal("Exiting...");
		}
			return new StringVal("No function saved");
		}
		else{
			throw new UnimplementedMethodException();
		}
	}
	//Helper functions
	//CallExp's helper function
	private static ExpVal applyFunction(FunctionVal p1, ExpVal e1){
		VarExp FE1 = p1.getE1();
		Exp FE2 = p1.getE2();
		Environment env = p1.getEnv();
		Store passedSto = p1.getStore();
		Integer addr = passedSto.newRef(e1);
		Environment newEnv = env.extendEnv(FE1.getVar(), addr);
		return valueOf(FE2,newEnv, passedSto);
	}
}
