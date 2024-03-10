import java.io.*;
class Csum
{
	public static void main(String args[])
        {
		
          int n1,n2;
          DataInputStream din;
	 din=new DataInputStream(System.in);
         
try
{
System.out.println("enter the value of n1");
n1=Integer.parseInt(din.readLine());
System.out.println("enter the value of n2");
n2=Integer.parseInt(din.readLine());
System.out.println("sum of "+n1+" and " +n2+ " is " +(n1+n2));

}
catch(Exception e)
{
System.out.println("error:"+e);
}
}
}
