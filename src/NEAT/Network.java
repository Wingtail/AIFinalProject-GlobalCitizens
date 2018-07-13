package NEAT;

import java.sql.Connection;
import java.util.ArrayList;

public class Network {

	private ArrayList<Node> nodes = new ArrayList<Node>();
	private int i;
	private int outputCount = 0;
	private int inputCount = 0;
	private double[] final_output_list = null;
	private Node[] outputNode;
	ArrayList<Node> computeOrder = new ArrayList<Node>();

	public void addNode(int ID)
	{
		Node node = new Node();
		node.ID = ID; //ID is global
		//node.Layer = Layer; //Indicator for forward and back-propagation
		nodes.add(node);
	}

	public Network constructNetwork(Genome genome)
	{
		int i;

		inputCount = genome.getInputCount();
		outputCount = genome.getOutputCount();
        outputNode = new Node[outputCount];
        int count = 0;
		for(i=0;i<genome.getNodeGeneSize();i++)
		{
			for(NodeGene nodeGene : genome.getNodeGeneList())
			{
				System.out.println("Node Gene: "+nodeGene.getID());
			}
			System.out.println("----------------");
			this.addNode(genome.getNodeGeneElement(i).getID());
            if(genome.getNodeGeneElement(i).isOutput)
			{
				for(Node node : nodes)
				{

					if(node.ID == genome.getNodeGeneElement(i).getID())
					{
						node.ifOutput = true;
						outputNode[count] = node;
						count++;
						break;
					}
				}
				/*nodes.get(i).ifOutput = true;
				System.out.println("Construct node "+nodes.get(i).ID);
				outputNode[count] = nodes.get(i);*/
				//count++;
			}
			if(genome.getNodeGeneElement(i).isInput)
			{
				for(Node node : nodes)
				{
					if(node.ID == genome.getNodeGeneElement(i).getID())
					{
						node.ifInput = true;
						node.activated = true;
						computeOrder.add(node);
						break;
					}
				}
				/*nodes.get(i).ifOutput = true;
				System.out.println("Construct node "+nodes.get(i).ID);
				outputNode[count] = nodes.get(i);*/

			}

		}
		for(i=0;i<genome.getConnectionGenesSize();i++)
		{
			System.out.println("Connection Gene: - out_ID: "+genome.getConnectionGeneElement(i).getOut_ID()+" -- in_ID: "+genome.getConnectionGeneElement(i).getIn_ID()+" isActive: "+genome.getConnectionGeneElement(i).ifActive());
		}
		for(i=0;i<genome.getConnectionGenesSize();i++)
		{
			if(genome.getConnectionGeneElement(i).ifActive())
			{
				this.addConnection(genome.getConnectionGeneElement(i).getOut_ID(), genome.getConnectionGeneElement(i).getIn_ID(), genome.getConnectionGeneElement(i).getWeight());
			}
		}

		/*for(i=0;i<inputCount;i++)
		{
			if(i<inputCount)
			{
				this.getNodeElement(i).activated = true;
				this.getNodeElement(i).ifInput = true;
				computeOrder.add(nodes.get(i));
			}
		}*/
		computeOrder();
		return this;
	}

	public void computeOrder(){
		while(computeOrder.size() < nodes.size())
		{
			double max = Double.MIN_VALUE;
			Node highPriority = null;

			for(Node node : nodes)
			{
				if(!node.ifInput && !node.activated)
				{
					int activated = 0;
					for(int j=0;j<node.getInputSize();j++)
					{
						if(node.getInputElement(j).activated)
						{
							activated++;
						}
					}
					System.out.println("Node "+node.ID+"  Activated: "+activated+"  InputSize: "+node.getInputSize());
					node.priority = (double)(activated)/(node.getInputSize());
				}
			}

			for(Node node :nodes) {
				System.out.println(node.priority + " "+max);
				if (node.priority > max) {

					max = node.priority;
					highPriority = node;
				}
			}
			System.out.println(nodes);
			highPriority.activated = true;
			computeOrder.add(highPriority);
			for(Node node :nodes) {
				node.priority = 0;
			}
		}

		for(Node node : computeOrder)
		{
			System.out.println(node.ID);
		}
	}

	public int getInputCount()
	{
		return inputCount;
	}
	public void setInputCount(int value)
	{
		inputCount = value;
	}

	public double[] getFinalOutputList()
	{
		return final_output_list;
	}
	public void formatFinalOutputList(double[] array)
	{
		final_output_list = array;
	}
	public void setFinalOutputListElement(int index, double value)
	{
		final_output_list[index] = value;
	}
	public double getFinalOutputListElement(int index)
	{
		return final_output_list[index];
	}

	public int getNodeSize()
	{
		return nodes.size();
	}
	public Node getNodeElement(int index)
	{
		return nodes.get(index);
	}

	public int getOutputCount()
	{
		return outputCount;
	}
	public void setOutputCount(int max)
	{
		outputCount = max;

	}

	public void addConnection(int ID_sender, int ID_receiver, double weight)
	{


		Node senderNode = null;
		Node receiverNode = null;

		for(Node node : nodes)
		{
			if(node.ID == ID_sender)
			{
				senderNode = node;
			}

			if(node.ID == ID_receiver)
			{
				receiverNode = node;
			}

			if(senderNode != null && receiverNode != null)
			{
				break;
			}
		}

		receiverNode.addInput(senderNode);
		senderNode.addOutput(receiverNode);
		senderNode.addWeight(weight);

		/*if(ID_sender != -1 && ID_receiver != -1)
		{
			nodes.get(ID_receiver).addInput(nodes.get(ID_sender));
			nodes.get(ID_sender).addOutput(nodes.get(ID_receiver));
			nodes.get(ID_sender).addWeight(weight);
		}else {
			System.out.println("Error in adding Connection\nEither specified node does not exist");
		}*/

	}


	public void removeConnection(int ID_sender, int ID_receiver)
	{
		ID_sender--; //Node ID starts from 1
		ID_receiver--;

		for(i=0;i<nodes.get(ID_receiver).getInputSize();i++)
		{
			if(nodes.get(ID_receiver).getInputElement(i).ID == nodes.get(ID_sender).ID)
			{
				nodes.get(ID_receiver).removeInput(i);
				nodes.get(ID_sender).removeOutput(i);
				nodes.get(ID_receiver).removeWeight(i);
				return;
			}
		}
		System.out.println("Failed to Remove Connection...");
	}

	public void ForwardProp(double... inputs) //Assuming that nodes list is stored in consecutive order by ID, hence also the Layers
	{
		int i;
		int j;
		int k;
        //System.out.println("Propagate");
		if(inputs.length == inputCount)
		{
			for(i=0;i<inputs.length;i++)
			{
				for(Node node : nodes)
				{
					if(node.ifInput && i < inputs.length)
					{
						node.setSum(inputs[i]);
						//System.out.println("Node ID to input: "+node.ID+" Sum: "+node.getSum());
						i++;
					}
				}


				//nodes.get(i).activate();
			}

		}else {

			System.out.println("All inputs not defined (NULL)");
		}

		for(Node node : computeOrder)
		{
			node.activate();
			for(i=0;i<node.getOutputSize();i++)
			{
				//System.out.println("Node "+node.getOutputElement(i).ID+" Sum: "+node.getOutputElement(i).getSum()+"\n");
				node.getOutputElement(i).setSum(node.getOutputElement(i).getSum()+node.getNodeOutput()*node.getWeightElement(i));
				//System.out.println("Node "+node.ID+" has output "+node.getNodeOutput()+" passing value to Node "+node.getOutputElement(i).ID+"\n");
				//System.out.println("Node "+node.getOutputElement(i).ID+" computed sum: "+node.getOutputElement(i).getSum()+"\n");
			}
		}

		formatFinalOutputList(output());

  /*int i;
  double[] memoi = null;
  final_output_list = new double[outputCount];



  for(i=0;i<outputCount;i++)
  {
     memoi = nodes.get(inputCount+i).Summation(this, nodes.get(inputCount+i), memoi);
     final_output_list[i] = memoi[inputCount+i];
  }
  nodes.get(0).evaluateRecursion(this, memoi);*/

		//Order to Forward Propagate

		/*ArrayList<Node> qeue = new ArrayList<Node>();
		int currentSize = 0;

		for(i=0;i<inputCount;i++)
		{
			qeue.add(this.getNodeElement(i));
			this.getNodeElement(i).sethasBeen(true);
		}

		for(i=0;i<qeue.size();)
		{
			currentSize = qeue.size();

			for(j=0;j<currentSize;j++)
			{
				ArrayList<Node> waitList = new ArrayList<Node>();
				for(k=0;k<qeue.get(j).getInputSize();k++)
				{
					if(!qeue.get(j).getInputElement(k).isWaiting && qeue.get(j).getInputElement(k).getNodeOutput() == 0)
					{
						qeue.get(j).isWaiting = true;
						waitList.add(qeue.get(j));
					}
				}


				for(int count = 0;count<waitList.size();count++)
				{
					for(k=0;k<waitList.get(count).getInputSize();k++)
					{
						if(!waitList.get(count).getInputElement(k).isWaiting && waitList.get(count).getInputElement(k).getNodeOutput() == 0)
						{
							waitList.get(count).getInputElement(k).isWaiting = true;
							waitList.add(waitList.get(count).getInputElement(k));
						}
					}
				}

				System.out.println("WaitList!");
				for(Node node : waitList)
				{
					System.out.print(node.ID+", ");
				}
				System.out.println();

				for(int count=waitList.size()-1;count>=0;count--)
				{
					Node node = waitList.get(count);
					node.activate();
					node.isWaiting = false;
					node.sethasBeen(true                                                                                                                                                                                                                );
					for(k=0;k<node.getOutputSize();k++)
					{
						double sum = node.getOutputElement(k).getSum() + (node.getNodeOutput() * node.getWeightElement(k));
						node.getOutputElement(k).setSum(sum);
						System.out.println("WaitList Node "+node.ID+" has value "+node.getNodeOutput()+" WEIGHT "+node.getWeightElement(k)+" Total output: "+(node.getNodeOutput() * node.getWeightElement(k))+" Passing to Node "+node.getOutputElement(k).ID);
						System.out.println("Node "+node.getOutputElement(k).ID+" Current Sum: "+node.getOutputElement(k).getSum()+"\n------\n");
						if(!node.getOutputElement(k).hasBeen)
						{
							qeue.add(node.getOutputElement(k));
							node.getOutputElement(k).hasBeen = true;
						}
					}

				}

				waitList.removeAll(waitList);
				if(!qeue.get(j).ifInput && !qeue.get(j).hasBeen)
				{
					qeue.get(j).activate();
				}

				if(!qeue.get(j).isWaiting)
				{
					for(k=0;k<qeue.get(j).getOutputSize();k++)
					{
						double sum = qeue.get(j).getOutputElement(k).getSum() + (qeue.get(j).getNodeOutput() * qeue.get(j).getWeightElement(k));
						qeue.get(j).getOutputElement(k).setSum(sum);
						System.out.println("Node "+qeue.get(j).ID+" has value "+qeue.get(j).getNodeOutput()+" WEIGHT "+qeue.get(j).getWeightElement(k)+" Total output: "+(qeue.get(j).getNodeOutput() * qeue.get(j).getWeightElement(k))+" Passing to Node "+qeue.get(j).getOutputElement(k).ID);
						System.out.println("Node "+qeue.get(j).getOutputElement(k).ID+" Current Sum: "+qeue.get(j).getOutputElement(k).getSum()+"\n-------\n");
						if(!qeue.get(j).getOutputElement(k).hasBeen() && !qeue.get(j).isWaiting)
						{
							qeue.add(qeue.get(j).getOutputElement(k));
							qeue.get(j).getOutputElement(k).sethasBeen(true);
						}
					}
				}


			}

			for(j=0;j<currentSize;j++)
			{
				qeue.remove(0);
			}

		}


		for(i=0;i<this.getNodeSize();i++)
		{
			this.getNodeElement(i).sethasBeen(false);
			this.getNodeElement(i).isWaiting = false;
		}
		formatFinalOutputList(output());*/
	}






	public double[] output() //Assuming that nodes list is stored in consecutive order by ID, hence also the Layers

	{
		double[] outputs = new double[outputCount];

		for(int i = 0;i<outputCount;i++)
        {
        	for(Node node : nodes)
			{
				if(node.ifOutput && i<outputCount)
				{
					outputs[i] = node.getNodeOutput();
					//System.out.println("Output Node"+node.ID+" Output: "+node.getNodeOutput());
					i++;
				}
			}

        }


		//System.out.println(outputs[0]+","+outputs[1]);

		return outputs;
	}

	public int[] searchID(int ID_1, int ID_2)
	{
		int[] indexes = new int[2];

		for(i=0;i<nodes.size();i++)
		{
			if(indexes[0] != -1 && indexes[1] != -1)
			{
				return indexes;
			}
			else if(nodes.get(i).ID == ID_1)
			{
				indexes[0] = i;
			}
			else if(nodes.get(i).ID == ID_2)
			{
				indexes[1] = i;
			}
		}

		return indexes;
	}





}