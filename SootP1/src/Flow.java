
public class Flow 
{
	/**
	 * @param args
	 */
	
	void foo(int x,int y)
	{
		int z,m,k;
		z=x+10;
		if(z<10)
		{
			k=bar1(10,z);
		}
		else 
		{
			k=bar2(z,20);
		}
		m=k/25;
	}
	
	int bar1(int a,int b)
	{
		int l;
		l=a*10;
		return l;
	}
	
	int bar2(int c,int d)
	{
		int n;
		n=d*5;
		return n;
	}
	
	
//	int foo(int param1,int param2)
//	{
//		int param3=param1;
//		int param4=10;
//		int param5;
//		if(param3<10)
//			param5=bar1(10,param4);
//		else
//			param5=bar2(param1,param3);
//		
//		return param5;
//	}
//	
//	int bar2(int param6,int param7)
//	{
//		param6=param6+param7;
//		int param8=50;
//		if(param8>10)
//			System.out.println("Hello");
//		return param8;
//	}
//	
//	
//	int bar1(int param9,int param10)
//	{
//		param9=param9+param10;
//		int param11=param9;
//		if(param11>10)
//			System.out.println("Hello");
//		return param11;
//	}
	
	
	
//	void func1()
//	{
//		int x,y;
//		int a=1;
//		while(a>0)
//		{
//			x=check(a);
//			y=x*10;
//			x=x+1;
//			y=y+1;
//			
//		}
//		//x=90;
//	}
//	
//	
//	
//	int check(int num)
//	{
//	 int j=2,qw=3;
//	int flag=0;
//	 while(j<=num/2)
//	{
//	 if(num <= j/2)
//	{
//	 flag=1;
//	 break;
//	 }
//	j=j+1;
//	 }
//	j=j+1;
//	 return qw;
//	}
	
	
}
