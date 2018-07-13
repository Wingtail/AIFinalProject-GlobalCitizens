package NEAT;

import java.util.ArrayList;

public class Genome {
	private ArrayList<NodeGene> nodeGenes = new ArrayList<NodeGene>();
	private ArrayList<ConnectionGene> connectionGenes = new ArrayList<ConnectionGene>();
	public static ArrayList<ConnectionGene> innovationList = new ArrayList<ConnectionGene>();
	private int outputCount = 0;
	private int inputCount = 0;
	
	public NodeGene getNodeGeneElement(int index)
	{
		return nodeGenes.get(index);
	}
	public ArrayList<NodeGene> getNodeGeneList()
	{
		return nodeGenes;
	}

	public ArrayList<ConnectionGene> getConnectionGenes(){return connectionGenes;}
	
	public int getOutputCount()
	{
		return outputCount;
	}
	public void setOutputCount(int value)
	{
		outputCount = value;
	}
	
	public int getInputCount()
	{
		return inputCount;
	}
	public void setInputCount(int value)
	{
		inputCount = value;
	}
	
	public ConnectionGene getConnectionGeneElement(int index)
	{
		return connectionGenes.get(index);
	}
	
	public int getNodeGeneSize()
	{
		return nodeGenes.size();
	}
	public int getConnectionGenesSize()
	{
		return connectionGenes.size();
	}

	public void addNodeGene(NodeGene nodeGene)
    {
        nodeGenes.add(nodeGene);
    }
	
	public void addNodeGene(ConnectionGene connectionGene)
	{
	    NodeGene receiveNode = new NodeGene();
        NodeGene submitNode = new NodeGene();

        boolean addSubmit = true;
        boolean addReceive = true;

        submitNode.setID(connectionGene.getOut_ID());
        receiveNode.setID(connectionGene.getIn_ID());

        if(submitNode.getID() <= inputCount)
        {
        	System.out.println("Submit Node is input: "+submitNode.getID());
            submitNode.isInput = true;
        }

        if(submitNode.getID() > inputCount && submitNode.getID() <= inputCount+outputCount)
        {
            submitNode.isOutput = true;
        }

        if(receiveNode.getID() <= inputCount)
        {
			System.out.println("Receive Node is input: "+receiveNode.getID());
            receiveNode.isInput = true;
        }

        if(receiveNode.getID() > inputCount && receiveNode.getID() <= inputCount+outputCount)
        {
			System.out.println("Receive Node is output: "+receiveNode.getID());
            receiveNode.isOutput = true;
        }

        for(NodeGene nodeGene : nodeGenes)
        {
            if(nodeGene.getID() == submitNode.getID())
            {
                addSubmit = false;
            }
            if(nodeGene.getID() == receiveNode.getID())
            {
                addReceive = false;
            }
            if(!addSubmit && !addReceive)
            {
                break;
            }
        }

        if(addSubmit)
        {
            nodeGenes.add(submitNode);
        }
        if(addReceive)
        {
            nodeGenes.add(receiveNode);
        }
	}
	public void checkConnectionOverlap(ConnectionGene gene)
	{
		int i;
		for(i=0;i<this.getConnectionGenesSize();i++)
		{
			if(this.getConnectionGeneElement(i).getIn_ID() == gene.getIn_ID() && this.getConnectionGeneElement(i).getOut_ID() == gene.getOut_ID())
			{
				if(!this.getConnectionGeneElement(i).ifActive())
				{
					this.addConnectionGene(gene);
					return;
				}else {
					return;
				}
			}
		}
		this.addConnectionGene(gene);
	}
	
	public void addConnectionGene(ConnectionGene gene)
	{
		connectionGenes.add(gene);
		innovationList.add(gene);
	}
	
	public void removeNodeGene(int index)
	{
		if(index < nodeGenes.size() && index >= 0)
		{
			nodeGenes.remove(index);
		}else {
			System.out.println("Error! Invalid index of removeNodeGene");
		}
	}
	public void removeConnectionGene(int index)
	{
		if(index < connectionGenes.size() && index >= 0)
		{
			connectionGenes.remove(index);
		}else {
			System.out.println("Error! Invalid index of removeConnectionGene");
		}
	}

	public static void checkInnovationOverlaps(Genome genome, ConnectionGene gene_connection)
	{
		int i; //In case this function runs within another for loop

		for(i=0;i<Genome.innovationList.size();i++)
		{
			if(gene_connection != null)
			{
				if(Genome.innovationList.get(i).getIn_ID() == gene_connection.getIn_ID() && Genome.innovationList.get(i).getOut_ID() == gene_connection.getOut_ID())
				{
					gene_connection.setInnovation(Genome.innovationList.get(i).getInnovation());
					return;
				}
			}else {
				System.out.println("Error! ConnectionGene is null!");
				return;
			}
		}

		gene_connection.setInnovation(ConnectionGene.incrementGlobalInnovation());
		Genome.innovationList.add(gene_connection);
		return;
	}
}
