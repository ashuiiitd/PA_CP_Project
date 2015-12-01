import java.util.HashSet;
import java.util.Iterator;

import soot.Body;
import soot.Local;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.Chain;


public class AllDefinitions extends ForwardFlowAnalysis {
	
	Body b;
	AllDefinitions(UnitGraph g)
	{
		super(g);
		ExceptionalUnitGraph ug=(ExceptionalUnitGraph)g;
		b=ug.getBody();
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
					outval.add(v);
				}
				//inval.remove(v);
			}
		}
		//System.out.println("out after "+outval);
		
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

		//System.out.println("Hey");
		FlowSet outSet=(FlowSet)out;
		inval1.union(inval2,outSet);
//		inval1.intersection(inval2,inval1);
//		outSet.difference(inval1, outSet);
//		inval1.union(inval2, temp1);
//		inval1.intersection(inval2, temp2);
//		temp1.difference(temp2, outSet);
//		System.out.println("inval1 "+inval1);		
//		System.out.println("inval2 "+inval2);
//		System.out.println("outSet "+outSet);
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
