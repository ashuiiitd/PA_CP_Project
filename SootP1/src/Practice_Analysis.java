import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;


public class Practice_Analysis extends ForwardFlowAnalysis
{

	public Practice_Analysis(DirectedGraph graph) 
	{
		super(graph);
		// TODO Auto-generated constructor stub
		doAnalysis();
	}

	@Override
	protected void flowThrough(Object arg0, Object unit, Object arg2) 
	{
		// TODO Auto-generated method stub
		
		Stmt s=(Stmt)unit;
		System.out.println("Currrent "+unit);
		
	}

	@Override
	protected void copy(Object arg0, Object arg1) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object entryInitialFlow() 
	{
		// TODO Auto-generated method stub
		System.out.println("entry h bhaiya");
		System.out.println();
		return new String();
	}

	@Override
	protected void merge(Object arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object newInitialFlow() 
	{
		// TODO Auto-generated method stub
		System.out.println("initial h bhaiya");
		System.out.println();
		return new String();
	}

}
