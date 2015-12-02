import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import soot.Body;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.JastAddJ.AssignMinusExpr;
import soot.baf.AddInst;
import soot.baf.VirtualInvokeInst;
import soot.dava.toolkits.base.AST.structuredAnalysis.MustMayInitialize;
import soot.jimple.AddExpr;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.DivExpr;
import soot.jimple.IfStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.MulExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.scalar.Evaluator;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.LoopNestTree;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.Chain;


public class Analysis extends ForwardFlowAnalysis 
{
	
	Body b;
	int count=0;
	HashMap<String,String> original_value;
	Analysis(UnitGraph g)
	{
		super(g);
		ExceptionalUnitGraph ug=(ExceptionalUnitGraph)g;
		b=ug.getBody();
		System.out.println();
		System.out.println("$$$$$$$$$$$$$");
		System.out.println();
		original_value=new HashMap<String,String>();
		doAnalysis();
	}

	@Override
	protected void flowThrough(Object in, Object unit, Object out) 
	{
		//TODO Auto-generated method stub
		//check=false;
//		FlowSet inval=(FlowSet)in;
//		FlowSet outval=(FlowSet)out;
		
		int DEBUG=0;
		Stmt s=(Stmt)unit;
		HashMap<String,String> inval =(HashMap) in;
		HashMap<String,String > outval =(HashMap) out;
		//inval.copy(outval);
		
		System.out.println("Inset before "+inval);
		
		count++;
		System.out.println("Currrent "+unit);
		
		
		if(count==1)
		{
			try 
			{	
				Chain<Local> localChain = b.getLocals();
				Iterator<Local> it = localChain.iterator();
				while(it.hasNext())
				{		
					Local loc = it.next();
					BufferedReader br=new BufferedReader(new FileReader(""+b.getMethod().getName()+"_in.txt"));
					if(br!=null)
					{
						String line="";
						int i=0;
						while((line=br.readLine())!=null)
						{	
							
							inval.put(b.getParameterLocal(i).toString(), line);
							original_value.put(b.getParameterLocal(i).toString(),line);
							i++;
						}
					}
					br.close();
				}	
				
				//System.out.println("original hashmap "+original_value);
				
			} 
			catch (FileNotFoundException e) 
			{
			} 
			catch (IOException e) 
			{
			}
		}
		
	
		
		Set<String> set = inval.keySet();
		for(String key:set)
		{
			outval.put(key, inval.get(key));
		}
		
		//InvokeExpr
		if(s.containsInvokeExpr())
		{
			//System.out.println("Original Set "+original_value);
			if(DEBUG==1)
			{
				System.out.println();
				System.out.println("Invoke Statement is: "+s.getInvokeExpr());
				System.out.println();
			}
			String str_stmt=s.toString();
			String return_param="";
			
			if(str_stmt.contains("="))
			{	
				return_param=str_stmt.substring(0,str_stmt.indexOf("="));
				return_param=return_param.trim();
			}
			
			InvokeExpr call_expr=s.getInvokeExpr();
			List<Value> params=call_expr.getArgs();
			
			try 
			{
				BufferedWriter bw=new BufferedWriter(new FileWriter(call_expr.getMethod().getName()+"_in.txt"));
				
				
				
				Iterator<Value> it_params=params.iterator();
				while(it_params.hasNext())
				{
					String temp_var=it_params.next().toString();
					if(inval.containsKey(temp_var))
					{
						// if parameter is some variable
						if(DEBUG==1)
						{
							System.out.println("parameters :"+ temp_var+" "+inval.get(temp_var));
						}
						bw.write(inval.get(temp_var));
						bw.newLine();
					}
					else
					{
						// if parameter is literal constant
						bw.write(temp_var);
						bw.newLine();
					}
				}
				bw.close();
				
				if(return_param.length()>0)
				{
//					bw.write(return_param+" "+"!");
//					bw.newLine();
					original_value.put(return_param, "!");
					outval.put(return_param, "!");
				}
				
				BufferedReader br=new BufferedReader(new FileReader(call_expr.getMethod().getName()+"_out.txt"));
				
				if(br!=null)
				{
					String temp=br.readLine();
					original_value.put(return_param, temp);
					outval.put(return_param, temp);
					
				}
				br.close();
				
				
				
				
			} catch (IOException e)
			{
			}
		}
		
		
		//gen //Kill operation
		else if(s instanceof AssignStmt)
		{
			//System.out.println("hahaa "+((AssignStmt) s).getRightOp());
			Value val=((AssignStmt) s).getRightOp();
			Iterator box=s.getDefBoxes().iterator();
			while(box.hasNext())
			{
				final ValueBox vb=(ValueBox) box.next();
				Value v=vb.getValue();
				String var= v.toString();
					if(v instanceof Local)
					{
						//System.out.println(inval);
						//System.out.println(outval);

						
						if(val instanceof AddExpr)
						{
//							System.out.println("-------original value---------");
//							System.out.println(original_value);
//							System.out.println("-------original value---------");
							
							
							Value op1=((AddExpr) val).getOp1();
							Value op2=((AddExpr) val).getOp2();
							
//							System.out.println("------------");
//							System.out.println(op1+"   "+op2);
//							System.out.println("------------");
							
							boolean isComputable=true;
							int first=0,second=0;
							if(original_value.containsKey(op1.toString()))
							{
								String temp=original_value.get(op1.toString());
								if(!temp.equals("!") && !temp.equals("?"))
									first=Integer.parseInt(temp);
								else
									isComputable=false;
							}
							else
							{
								first=Integer.parseInt(op1.toString());
							}
							
							if(original_value.containsKey(op2.toString()))
							{
								String temp=original_value.get(op2.toString());
								if(!temp.equals("!") && !temp.equals("?"))
									second=Integer.parseInt(temp);
								else
									isComputable=false;
							}
							else
								second=Integer.parseInt(op2.toString());
							if(isComputable)
							{
								Integer third=first+second;
							    original_value.put(var, third.toString());
							    outval.put(var,third.toString());
							}
							else
							{
								original_value.put(var, "?");
								outval.put(var,"?");
							}
						}
						
						else if(val instanceof MulExpr)
						{
//							System.out.println("-------original value---------");
//							System.out.println(original_value);
//							System.out.println("-------original value---------");
//							
							
							Value op1=((MulExpr) val).getOp1();
							Value op2=((MulExpr) val).getOp2();
//							
//							System.out.println("------------");
//							System.out.println(op1+"   "+op2);
//							System.out.println("------------");
							
							boolean isComputable=true;
							int first=0,second=0;
							if(original_value.containsKey(op1.toString()))
							{
								String temp=original_value.get(op1.toString());
								if(!temp.equals("!") && !temp.equals("?"))
									first=Integer.parseInt(temp);
								else
									isComputable=false;
							}
							else
							{
								first=Integer.parseInt(op1.toString());
							}
							
							if(original_value.containsKey(op2.toString()))
							{
								String temp=original_value.get(op2.toString());
								if(!temp.equals("!") && !temp.equals("?"))
									second=Integer.parseInt(temp);
								else
									isComputable=false;
							}
							else
								second=Integer.parseInt(op2.toString());
							if(isComputable)
							{
								Integer third=first*second;
							    original_value.put(var, third.toString());
							    outval.put(var,third.toString());
							}
							else
							{
								original_value.put(var, "?");
								outval.put(var,"?");
							}
						}
						
						else if(val instanceof SubExpr)
						{
//							System.out.println("-------original value---------");
//							System.out.println(original_value);
//							System.out.println("-------original value---------");
							
							
							Value op1=((SubExpr) val).getOp1();
							Value op2=((SubExpr) val).getOp2();
							
//							System.out.println("------------");
//							System.out.println(op1+"   "+op2);
//							System.out.println("------------");
							
							boolean isComputable=true;
							int first=0,second=0;
							if(original_value.containsKey(op1.toString()))
							{
								String temp=original_value.get(op1.toString());
								if(!temp.equals("!") && !temp.equals("?"))
									first=Integer.parseInt(temp);
								else
									isComputable=false;
							}
							else
							{
								first=Integer.parseInt(op1.toString());
							}
							
							if(original_value.containsKey(op2.toString()))
							{
								String temp=original_value.get(op2.toString());
								if(!temp.equals("!") && !temp.equals("?"))
									second=Integer.parseInt(temp);
								else
									isComputable=false;
							}
							else
								second=Integer.parseInt(op2.toString());
							if(isComputable)
							{
								Integer third=first-second;
							    original_value.put(var, third.toString());
							    outval.put(var,third.toString());
							}
							else
							{
								original_value.put(var, "?");
								outval.put(var,"?");
							}
						}
						
						
						else if(val instanceof DivExpr)
						{
//							System.out.println("-------original value---------");
//							System.out.println(original_value);
//							System.out.println("-------original value---------");
//							
							
							Value op1=((DivExpr) val).getOp1();
							Value op2=((DivExpr) val).getOp2();
							
//							System.out.println("------------");
//							System.out.println(op1+"   "+op2);
//							System.out.println("------------");
							
							boolean isComputable=true;
							int first=0,second=0;
							if(original_value.containsKey(op1.toString()))
							{
								String temp=original_value.get(op1.toString());
								if(!temp.equals("!") && !temp.equals("?"))
									first=Integer.parseInt(temp);
								else
									isComputable=false;
							}
							else
							{
								first=Integer.parseInt(op1.toString());
							}
							
							if(original_value.containsKey(op2.toString()))
							{
								String temp=original_value.get(op2.toString());
								if(!temp.equals("!") && !temp.equals("?"))
									second=Integer.parseInt(temp);
								else
									isComputable=false;
							}
							else
								second=Integer.parseInt(op2.toString());
							if(isComputable)
							{
								Integer third=first/second;
							    original_value.put(var, third.toString());
							    outval.put(var,third.toString());
							}
							else
							{
								original_value.put(var, "?");
								outval.put(var,"?");
							}
						}
						
						
						
						
						else
						{
						
						//Iterator it=useb.iterator();
							//System.out.println("in else expression");
							String temp=val.toString();
							while(original_value.get(temp.toString())!=null)
							{
								temp=original_value.get(temp.toString());
							}
							if(!temp.contains("@"))
							{
								original_value.put(var,temp);
							}
						}
						
						//System.out.println("original 2222"+original_value);
						
						if(!v.toString().contains("$"))
						{
							if(!inval.get(var).equals("!"))
							{
								outval.put(var,original_value.get(var));	
							}
							else
								outval.put(var, "!");
						}
					}
			}
		}
		
		else if(s instanceof ReturnStmt)
		{
			
			if(s.toString().length()>6)
			{
				String param_return=s.toString().substring(6);
				param_return=param_return.trim();
				
				try 
				{
					if(DEBUG==1)
					{
						System.out.println("while return statement  "+param_return);
					}
					BufferedWriter bw=new BufferedWriter(new FileWriter(""+b.getMethod().getName()+"_out.txt"));
					bw.write(inval.get(param_return));
					bw.close();
					
				} catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		System.out.println("Outset After  "+outval);
		System.out.println("------------------------------------------------------------------------------------");
	}

	@Override
	protected void copy(Object src, Object dest) {
		// TODO Auto-generated method stub
//		FlowSet srcSet=(FlowSet)src;
//		FlowSet destSet=(FlowSet)dest;
//		srcSet.copy(destSet);
		//System.out.println("InCopy");
		HashMap<String,String> destination=(HashMap)dest;
		HashMap<String,String> source=(HashMap)src;
		Set<String> set = source.keySet();
		for(String key:set)
		{
			destination.put(key, source.get(key));
		}
	}
	
	@Override
	protected void merge(Object in1, Object in2, Object out) 
	{
		// TODO Auto-generated method stub
		HashMap<String,String> inval1=(HashMap)in1;
		HashMap<String,String> inval2=(HashMap)in2;
		if(inval1.isEmpty())
		{
			Chain<Local> localChain = b.getLocals();
			Iterator<Local> it = localChain.iterator();
			while(it.hasNext())
			{
				Local loc = it.next();
				if(!loc.getName().contains("$"))
					{
						inval1.put(loc.getName(),"!");
					}

			}
		}
		else if(inval2.isEmpty())
		{
			Chain<Local> localChain = b.getLocals();
			Iterator<Local> it = localChain.iterator();
			while(it.hasNext())
			{
				Local loc = it.next();
				if(!loc.getName().contains("$"))
					{
						inval2.put(loc.getName(),"!");
					}

			}
		}
//		System.out.println("In Merge inval1 "+inval1);
//		System.out.println("In Merge inval2 "+inval2);
		HashMap<String,String> outval=(HashMap)out;
		Set<String> set=inval1.keySet();
		for(String key:set)
		{
			//original_value.put(var,val.toString());
			if(inval1.get(key).equals(inval2.get(key)))
			{
				original_value.put(key,inval1.get(key));
				outval.put(key, inval1.get(key));
			}
			else
			{
				if(inval1.get(key).equals("?") || inval2.get(key).equals("?"))
				{
					original_value.put(key,"?");
					outval.put(key, "?");
				}
				else if(!inval1.get(key).equals("?") && !inval2.get(key).equals("?") &&!inval1.get(key).equals("!") && !inval2.get(key).equals("!"))
				{
					original_value.put(key,"?");
					outval.put(key, "?");
				}
				else if(!inval1.get(key).equals("!") )//!inval2.get(key).equals("!"))
				{
					original_value.put(key,inval1.get(key));
					outval.put(key, inval1.get(key));
				}
				else
				{
					original_value.put(key,inval2.get(key));
					outval.put(key, inval2.get(key));
				}
			}
		}
		//System.out.println("In Merge outval "+outval);
		//System.out.println("Hey");
	}

	@Override
	protected Object entryInitialFlow() 
	{
		// TODO Auto-generated method stub
		//System.out.println("In entryInitialFlow");
		HashMap<String,String> inval1=new HashMap<String,String>();
			
		Chain<Local> localChain = b.getLocals();
		Iterator<Local> it = localChain.iterator();
		while(it.hasNext())
		{		
			Local loc = it.next();
			original_value.put(loc.getName(),"?");	
//			if(!loc.getName().contains("$"))
//			{
//				inval1.put(loc.getName(),"?");
//			}
			inval1.put(loc.getName(),"?");
		}
				
		return inval1;
	}

	@Override
	protected Object newInitialFlow() 
	{
		// TODO Auto-generated method stub
		//System.out.println("In InitialFlow");
		HashMap<String,String> inval1=new HashMap<String,String>();
		Chain<Local> localChain = b.getLocals();
		Iterator<Local> it = localChain.iterator();
		while(it.hasNext())
		{
			Local loc = it.next();
			
//			if(!loc.getName().contains("$"))
//				{
//					inval1.put(loc.getName(),"!");
//				}
			inval1.put(loc.getName(),"!");
		}
		return inval1;
	}

}
