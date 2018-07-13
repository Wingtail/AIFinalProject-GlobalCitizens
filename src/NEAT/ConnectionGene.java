package NEAT;

public class ConnectionGene {
	private int IN_ID; //Receiver Node
	private int OUT_ID; //Sender Node
	private double weight;
	private boolean active;
	private int innovation;
	private static int globalInnovation = 0;

	public ConnectionGene()
	{
		IN_ID = -1;
		OUT_ID = -1;
		weight = -1;
		active = false;
		innovation = 0;
	}
	public ConnectionGene(int in, int out, double weight, boolean active, int innovation)
	{
		IN_ID = in;
		OUT_ID = out;
		this.weight = weight;
		this.active = active;
		this.innovation = innovation;
	}

	public int getIn_ID()
	{
		return IN_ID;
	}

	public int getOut_ID()
	{
		return OUT_ID;
	}

	public double getWeight()
	{
		return weight;
	}

	public boolean ifActive()
	{
		return active;
	}

	public int getInnovation()
	{
		return innovation;
	}

	public void setIn_ID(int ID)
	{
		IN_ID = ID;
	}

	public void setOut_ID(int ID)
	{
		OUT_ID = ID;
	}

	public void setWeight(double value)
	{
		weight = value;
	}

	public void setActive(boolean flag)
	{
		active = flag;
	}

	public void setInnovation(int value)
	{
		innovation = value;
	}

	public static int getGlobalInnovation()
	{
		return globalInnovation;
	}
	//Why put this?
	public void checkConnectionOverlap(Genome genome)
	{
		int i;
		for(i=0;i<genome.getConnectionGenesSize();i++)
		{
			if(genome.getConnectionGeneElement(i).getIn_ID() == this.IN_ID && genome.getConnectionGeneElement(i).getOut_ID() == this.OUT_ID)
			{
				if(!genome.getConnectionGeneElement(i).active)
				{
					genome.addConnectionGene(this);
				}
			}else {
				genome.addConnectionGene(this);
			}
		}
	}
	public static int incrementGlobalInnovation()
	{
		globalInnovation++; //Variables can be initialized directly through this function
		return globalInnovation;
	}
	public static int decrementGlobalInnovation()
	{
		globalInnovation--; //Variables can be initialized directly through this function
		return globalInnovation;
	}


}
