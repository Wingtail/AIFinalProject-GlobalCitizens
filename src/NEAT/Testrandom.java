package NEAT;

public class Testrandom {
	public static void main(String[] args)
	{
		
		
		for(int i = 0; i<5000000;i++)
		{
			
			System.out.println(randomBoolean());
		}
		
	}
	
	public static int randomInteger(int high, int low)
	{
		return (int)(Math.random()*((high+1)-low))+low;  //(+1) since x<high, want to make it x<=high
	}
	public static boolean randomBoolean() 
	{
		return (((int)(Math.random()*2))==1)?true:false;
	}
}
