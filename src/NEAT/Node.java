package NEAT;

import java.util.ArrayList;

public class Node {

	private double sum = 0;
	private double output = 0;
	//public int Layer = 0;
	public boolean ifInput = false;
	public boolean isWaiting = false;
	public boolean ifOutput = false;
	public int ID = 0;
	public boolean activated = false;
	public boolean hasBeen = false;

	public double priority = 0;

	private ArrayList<Node> input_nodes = new ArrayList<Node>();
	private ArrayList<Node> output_nodes = new ArrayList<Node>();
	private ArrayList<Double> weight = new ArrayList<Double>();

	public boolean hasBeen()
	{
		return hasBeen;
	}
	public void sethasBeen(boolean flag)
	{
		hasBeen = flag;
	}

	public void setSum(double value)
	{
		sum = value;
	}
	public double getSum()
	{
		return sum;
	}

	public ArrayList<Node> getOutputArray()
	{
		return output_nodes;
	}

	public void addInput(Node node)
	{
		input_nodes.add(node);
	}
	public void removeInput(int index)
	{
		input_nodes.remove(index);
	}
	public int getInputSize()
	{
		return input_nodes.size();
	}
	public Node getInputElement(int index)
	{
		return input_nodes.get(index);
	}

	public void addOutput(Node node)
	{
		output_nodes.add(node);
	}
	public void removeOutput(int index)
	{
		output_nodes.remove(index);
	}
	public int getOutputSize()
	{
		return output_nodes.size();
	}
	public Node getOutputElement(int index)
	{
		return output_nodes.get(index);
	}

	public void setNodeOutput(double value)
	{
		output = value;
	}
	public double getNodeOutput()
	{
		return output;
	}

	public void addWeight(double weight)
	{
		this.weight.add(weight);
	}
	public void removeWeight(int index)
	{
		weight.remove(index);
	}
	public int getWeightSize()
	{
		return weight.size();
	}
	public double getWeightElement(int index)
	{
		return weight.get(index);
	}



	public double activate ()
	{
		/*if(ifOutput)
		{
			output = negSigmoid(sum);
		}else {
			output = sigmoid(sum);
		}*/
		output = negSigmoid(sum);
		sum = 0;
		return output;
	}

	private double sigmoid(double x)
	{
		return (1/(1+Math.exp(-x)));
	}
	private double negSigmoid(double x){
		return (1/(1+Math.exp(-x)))*2-1;
	}
}
