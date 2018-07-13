package NEAT;

public class Organism {
	public double fitness = 0.0;
	public double probability = 0.0;
	public boolean activity = true;
	private Network network;
	private Genome genome;
	private Species species;
	
	public void setNetwork(Network net)
	{
		network = net;
	}
	public Network getNetwork()
	{
		return network;
	}
	public void setGenome(Genome dna)
	{
		genome = dna;
	}
	public Genome getGenome()
	{
		return genome;
	}
	public Species getSpecies(){return species;}
	public void addSpecies(Species s){species = s;}
	public Species getSpecies(Species s){return species;}

}
