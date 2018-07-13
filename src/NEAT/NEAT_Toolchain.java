package NEAT;

public class NEAT_Toolchain {
	private Crossover crossover;
	private Mutation mutation;
	private int totalPopulation;
	//private Population population;
	private Speciation speciation;
	private int generation;


	public NEAT_Toolchain()
	{
		crossover = new Crossover();
		mutation = new Mutation();
		//population = new Population();
		speciation = new Speciation();
	}

	public void incrementGeneration()
	{
		generation++;
	}

	public void decrementGeneration()
	{
		generation--;
	}

	public int getGeneration()
	{
		return generation;
	}

	public int getTotalPopulation()
	{
		return totalPopulation;
	}
	public void setTotalPopulation(int value)
	{
		totalPopulation = value;
	}
	public Network createNetwork(Genome genome)
	{
		Network network = new Network();
		network.constructNetwork(genome);
		return network;
	}


	public Speciation getSpeciationComponent()
	{
		return speciation;
	}
	public Genome initializeRootGenome(int inputNum, int outputNum)
	{
		int i;
		int j;
		Genome genome = new Genome();

		genome.setInputCount(inputNum);
		genome.setOutputCount(outputNum);


		for(i=0;i<inputNum;i++)
		{
			for(j=0;j<outputNum;j++)
			{
				ConnectionGene connect = new ConnectionGene(j+inputNum+1, i+1, mutation.randomDouble(1.0, -1.0), true, 0);
				Genome.checkInnovationOverlaps(genome, connect);
				genome.checkConnectionOverlap(connect);
				genome.addNodeGene(connect);
			}
		}

		return genome;
	}
	public void generateInitialPopulation(int inputCount, int outputCount, int size)
	{
		int i;
		Population.setTotalPopulation(size);
		for(i=0;i<size;i++)
		{
			Genome gene = new Genome();
			System.out.println("New Genome Created");
			gene = initializeRootGenome(inputCount, outputCount);
			System.out.println("initialized root genome");
			Organism organism = new Organism();
			organism.setGenome(gene);
			Network network = new Network();
			organism.setNetwork(network.constructNetwork(gene));
			speciateOrganism(organism);
		}
	}

	public Organism EvaluateGeneration()
	{
		int i;

		Organism[] offsprings;
		//resetInnovationList();

		Organism bestfit = Population.rankAllOrganisms();
		offsprings = Population.generateNewPopulation();
		Population.removePopulation();

		for(i=0;i<offsprings.length;i++)
		{
			Network network = new Network();
			offsprings[i].setNetwork(network.constructNetwork(offsprings[i].getGenome()));
			speciation.speciation(offsprings[i]);
		}

		return bestfit;

	}

	public void resetInnovationList()
	{
		Genome.innovationList.clear();
	}
	public void computePopulationSharedFitness(Organism organism)
	{
		int i;
		for(i=0;i<Population.getPopulationSize();i++)
		{
			speciation.computeSharedFitness(Population.getPopulationElement(i), organism.fitness);
		}
	}
	/*public Population getPopulationComponent()
	{
		return population;
	}*/
	public Mutation getMutationComponent()
	{
		return mutation;
	}
	public Crossover getCrossoverComponent()
	{
		return crossover;
	}

	public void addNodeMutation(Genome genome)
	{
		mutation.addNode(genome);
	}

	public void addConnectionMutation(Genome genome)
	{
		mutation.addConnection(genome);
	}

	public void speciateOrganism(Organism organism)
	{
		speciation.speciation(organism);
	}



}
