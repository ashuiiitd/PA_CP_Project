import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.dava.toolkits.base.AST.structuredAnalysis.MustMayInitialize;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.LoopNestTree;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.Chain;


public class RedefAnalysis extends ForwardFlowAnalysis {
	
	Body b;
	boolean check=true;
	int count=0;
	HashSet<String>hv;
	public HashSet<node> hn;
	public HashSet<String> rem;
	RedefAnalysis(UnitGraph g)
	{
		super(g);
		ExceptionalUnitGraph ug=(ExceptionalUnitGraph)g;
		b=ug.getBody();
		hv=new HashSet<String>();
		hn=new HashSet<node>();
		rem=new HashSet<String>();
		System.out.println();
		System.out.println("$$$$$$$$$$$$$");
		System.out.println();
		doAnalysis();
	}

	@Override
	protected void flowThrough(Object in, Object unit, Object out) {
		// TODO Auto-generated method stub
		//check=false;
		FlowSet inval=(FlowSet)in;
		FlowSet outval=(FlowSet)out;
		Stmt s=(Stmt)unit;

		inval.copy(outval);
		//System.out.println("Currrent "+unit);
		if(check){
		//gen //Kill operation
		Iterator box=s.getDefBoxes().iterator();
		while(box.hasNext())
		{
			final ValueBox vb=(ValueBox) box.next();
			Value v=vb.getValue();
			if(v instanceof Local)
			{
				
				if(!v.toString().contains("$"))
				{
					boolean check=false;
					if(!hv.contains(v.toString()))
					{
						hv.add(v.toString());
						node n= new node();
						n.setVal(v.toString());
						n.setCount(1);
						hn.add(n);
						outval.add(v);
					}
					else
					{
						outval.add(v);
						for(node n:hn)
						{
							if(n.getVal().equals(v.toString()))
							{
								int count=n.getCount();
								n.setCount(count+1);
							}
						}
					}
					
				}
				//inval.remove(v);
			}
		}
		}
		else
		{
			Iterator box=s.getDefBoxes().iterator();
			while(box.hasNext())
			{
				final ValueBox vb=(ValueBox) box.next();
				Value v=vb.getValue();
				if(v instanceof Local)
				{
					
					if(!v.toString().contains("$"))
					{
						//System.out.println("Iam "+v);
						rem.add(v.toString());
					}
				}
			}
		}
		//System.out.println("In after "+outval);
		
	}

	@Override
	protected void copy(Object src, Object dest) {
		// TODO Auto-generated method stub
		FlowSet srcSet=(FlowSet)src;
		FlowSet destSet=(FlowSet)dest;
		srcSet.copy(destSet);
	}
	
	@Override
	protected void merge(Object in1, Object in2, Object out) {
		// TODO Auto-generated method stub
		FlowSet inval1=(FlowSet)in1;
		FlowSet inval2=(FlowSet)in2;
		if(inval1.isEmpty())
		{
			inval1=inval2;
			check=false;
			count++;
		}
		else if(inval2.isEmpty())
		{
			inval2=inval1;
			check=false;
			count++;
		}
		else
		{
			count--;
		}
		if(count==0){check=true;}
		//System.out.println("Hey");
		FlowSet outSet=(FlowSet)out;
		inval1.intersection(inval2, outSet);
	}

	@Override
	protected Object entryInitialFlow() {
		// TODO Auto-generated method stub
		//System.out.println("In entryInitialFlow");
		return new ArraySparseSet();
	}

	@Override
	protected Object newInitialFlow() {
		// TODO Auto-generated method stub
		//System.out.println("In InitialFlow");
		return new ArraySparseSet();
	}

}
