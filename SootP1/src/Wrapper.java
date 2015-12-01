
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;
public class Wrapper extends BodyTransformer{

	@Override
	protected void internalTransform(Body b, String phase, Map options) 
	{
		// TODO Auto-generated method stub
		ExceptionalUnitGraph g=new ExceptionalUnitGraph(b);
		SootMethod sootMethod = b.getMethod();
		System.out.println("Method Name : "+sootMethod.getName());
		System.out.println("-----------------------");
		Analysis mustAn=new Analysis(g);
//		AllDefinitions allVar =new AllDefinitions(g);
//		RedefAnalysis ra=new RedefAnalysis(g);
//		Unit last=b.getUnits().getLast();
//		System.out.println("Definitely will be Inilialized");
//		FlowSet must = (FlowSet)mustAn.getFlowAfter(last);
//		System.out.println(must);
//		System.out.println();
//		System.out.println("--------------------------------");
//		HashSet<String> mustvar=new HashSet<String>();
//		Iterator it1=must.iterator();
//		while(it1.hasNext())
//		{
//			mustvar.add(it1.next().toString());
//		}
//		FlowSet allDef=(FlowSet)allVar.getFlowAfter(last);
//		HashSet<String> allvar=new HashSet<String>();
//		Iterator it2=allDef.iterator();
//		while(it2.hasNext())
//		{
//			allvar.add(it2.next().toString());
//		}
//		Chain<Local> localChain = b.getLocals();
//		HashSet<String> locvar =new HashSet<String>();
//		Iterator<Local> it = localChain.iterator();
//		//System.out.println("hi "+b.getLocals().size());
//		while(it.hasNext())
//		{
//			Local loc = it.next();
//			if(!loc.getName().contains("$"))
//				{
//					locvar.add(loc.getName());
//				}
//
//		}
//		
//		HashSet<String> may=new HashSet<String>();
//		System.out.println("May be Initialized");
//		for(String st:allvar)
//		{
//			if(!mustvar.contains(st))may.add(st);
//		}
//		if(may.isEmpty())System.out.println("None");
//		else
//		{
//			for(String st:may)
//				System.out.println(st);
//		}
//		System.out.println();
//		System.out.println("-----------------------------");
//		HashSet<String> not=new HashSet<String>();
//		System.out.println("Will Not be Initialized");
//		for(String st:locvar)
//		{
//			if(!allvar.contains(st))not.add(st);
//		}
//		if(not.isEmpty())System.out.println("None");
//		else
//		{
//			for(String st:not)
//				System.out.println(st);
//		}
//		
//		System.out.println();
//		System.out.println("----------------------------");
//		
//		System.out.println("Will be redefined");
//		HashSet<String> redef=new HashSet<String>();
//		Iterator<node> it3=ra.hn.iterator();
//		while(it3.hasNext())
//		{
//			node n=it3.next();
//			if(n.getCount()>1)
//			{
//				redef.add(n.getVal());
//				System.out.println(n.getVal());
//			}
//		}
//		System.out.println();
//		System.out.println("------------------------");
//		
//		HashSet<String> mayredef=new HashSet<String>();
//		HashSet<String>notredef=new HashSet<String>();
//		//Iterator<String> it4=ra.rem.iterator();
//		for(String st:mustvar)
//		{
//			if(ra.rem.contains(st) &&!redef.contains(st))
//				mayredef.add(st);
//			else if(!ra.rem.contains(st) &&!redef.contains(st))
//				notredef.add(st);
//		}
//		for(String st:may)
//		{
//			if(ra.rem.contains(st))
//				mayredef.add(st);
//		}
//		System.out.println("May be redefined");
//		if(mayredef.isEmpty())System.out.println("None");
//		else{
//		for(String st:mayredef)
//			System.out.println(st);
//		}
//		System.out.println();
//		System.out.println("------------------------");
//		System.out.println("Will Not be redefined");
//		if(notredef.isEmpty())System.out.println("None");
//		else{
//		for(String st:notredef)
//			System.out.println(st);
//		System.out.println();
//		System.out.println("----------------------");
//		}
	}
}
