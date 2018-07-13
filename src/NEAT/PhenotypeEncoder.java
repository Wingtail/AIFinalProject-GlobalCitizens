package NEAT;

public class PhenotypeEncoder {
	private int outputCount;
	
	PhenotypeEncoder(int output)
	{
		outputCount = output;
	}
	public Network Encode(Genome genome)
	{
		Network network = new Network();
		int i;
		
		
		for(i=0;i<genome.getNodeGeneSize();i++)
		{
			network.addNode(genome.getNodeGeneElement(i).getID());
		}
		
		for(i=0;i<genome.getConnectionGenesSize();i++)
		{
			if(genome.getConnectionGeneElement(i).ifActive())
			{
				network.addConnection(genome.getConnectionGeneElement(i).getOut_ID(), genome.getConnectionGeneElement(i).getIn_ID(), genome.getConnectionGeneElement(i).getWeight());
			}
		}
		
		network.setOutputCount(outputCount);
		
		return network;
	}
}
