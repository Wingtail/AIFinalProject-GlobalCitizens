package NEATImplementation;
import java.util.*;

public class Node {

	public double eval = 0.0;
	public double sum;
	Node(double[] inputs, double[] weights){
		
		double sum = Summation(inputs, weights);
		sum = Activation(sum);
		eval = sum;
		
	}
	public double Summation(double[] inputs, double[] weights)
	{
		double sum = 0;
		int i;
		int j;
		
		for(i=0;i<inputs.length;i++)
		{
			sum+=(inputs[i]*weights[i]);
		}
		this.sum = sum;
		return sum;
	}
	public double Activation(double func)
	{
		double imsi = func;
		func = Math.tanh(imsi);
		return func;
	}
}
