package NEAT;

import java.util.ArrayList;

public class Population {
	private static ArrayList<Species> population = new ArrayList<Species>();
	private static int totalPopulation = 0;
	private static double[] totalFitnessList = null;
	private static double totalFitness = 0.0;
	public static double crossoverRate = 0.3;
	public static double mutationRate = 0.25;
	public static double sumTotalFitness = 0;

	public static int getTotalPopulation()
	{
		return totalPopulation;
	}
	public static void setTotalPopulation(int value)
	{
		totalPopulation = value;
	}

	public static double getTotalFitness()
	{
		return totalFitness;
	}

	public static void removePopulation()
	{
		population.clear();
	}


	public static int getPopulationSize()
	{
		return population.size();
	}
	public static Species getPopulationElement(int index)
	{
		return population.get(index);
	}

	/*public static void rankSpecies(Organism organism){
		int i;
		totalFitness = 0;
		totalFitnessList = new double[population.size()];
		Speciation speciation = new Speciation();

		Species species = organism.getSpecies();

		for(i=0;i<species.getSpeciesSize();i++)
		{
			species.getSpeciesElement(i).fitness = speciation.computeSharedFitness(species, organism.fitness);
			totalFitnessList[i] += population.get(i).getSpeciesElement(j).fitness;
			totalFitness += population.get(i).getSpeciesElement(j).fitness;

		}
	}*/

	public static Organism rankAllOrganisms()
	{
		int i;
		int j;
		int k;

		double average_fitness = 0;

		totalFitness = 0;
		totalFitnessList = new double[population.size()];

		Speciation speciation = new Speciation();

		for(i=0;i<population.size();i++)
		{
			for(j=0;j<population.get(i).getSpeciesSize();j++)
			{
				population.get(i).getSpeciesElement(j).fitness = speciation.computeSharedFitness(population.get(i), population.get(i).getSpeciesElement(j).fitness);
				totalFitnessList[i] += population.get(i).getSpeciesElement(j).fitness;
				totalFitness += population.get(i).getSpeciesElement(j).fitness;
			}
		}
		//Arranges population array in order of best performing organism to least performing organism
		for(i=0;i<population.size();i++)
		{
			double previousProbability = 0;

			for(j=0;j<population.get(i).getSpeciesSize();j++)
			{
				double maxFitness = Double.MIN_VALUE;
				int index = 0;

				for(k=j;k<population.get(i).getSpeciesSize();k++)
				{
					if(maxFitness < population.get(i).getSpeciesElement(j).fitness)
					{
						maxFitness = population.get(i).getSpeciesElement(j).fitness;
						index = j;
					}
				}
				Organism temp = population.get(i).getSpeciesElement(j);
				population.get(i).setSpeciesElement(j, population.get(i).getSpeciesElement(index));
				population.get(i).setSpeciesElement(index, temp);
			}

			for(j=0;j<population.get(i).getSpeciesSize();j++)
			{
				population.get(i).getSpeciesElement(j).probability = previousProbability + (population.get(i).getSpeciesElement(j).fitness / totalFitnessList[i]);
				previousProbability = population.get(i).getSpeciesElement(j).probability;
			}
		}
		
		double max = Double.MIN_VALUE;
		int best = 0;

		for(i=0;i<totalFitnessList.length;i++)
		{
			if(max<totalFitnessList[i])
			{
				best = i;
				max = totalFitnessList[i];
			}
		}

		System.out.println("Neural Network of Best Fit:\n\n");

		for(i=0;i<population.get(best).getSpeciesElement(0).getGenome().getConnectionGenesSize();i++)
		{
			System.out.println(population.get(best).getSpeciesElement(0).getGenome().getConnectionGeneElement(i).getOut_ID()+" --> "+population.get(best).getSpeciesElement(0).getGenome().getConnectionGeneElement(i).getIn_ID());
		}
		System.out.println("\n\n");

		sumTotalFitness += totalFitness;
		return population.get(best).getSpeciesElement(0);
	}
	
	public static Organism[] generateLocalPopulation(int amount, Organism organism)
	{
		int i;
		int j;
		int k;
		int counter;
		int leftPopulation;
		
		Organism[] offspring_list;
		counter = 0;

		if(amount == -1)
		{
		 offspring_list = new Organism[totalPopulation];
		 leftPopulation = totalPopulation;
		}else {
		 offspring_list = new Organism[amount];
		 leftPopulation = amount;
		}

		

		//Work on whether optimization does not progress within 6 generations

		for(i=0;i<population.size();i++)
		{

			if(leftPopulation < 0)
			{
				break;
			}

			int position = 0;

			double percentage = (totalFitnessList[i]/totalFitness);
			int producePopulation = (int) Math.rint(amount*percentage);
			leftPopulation -= producePopulation;

			if(leftPopulation < 0)
			{
				producePopulation -= Math.abs(leftPopulation);
			}

			if(i==population.size()-1 && leftPopulation>0)
			{
				double max = Double.MIN_VALUE;
				int candidate = 0;
				for(j=0;j<totalFitnessList.length;j++)
				{
					if(totalFitnessList[j] > max)
					{
						max = totalFitnessList[j];
						candidate = j;
					}
				}
				while(leftPopulation > 0)
				{
					offspring_list[counter] = new Organism();
					double randomChoice = randomDouble(1.0, 0);
					/*for(j=0;j<population.get(candidate).getSpeciesSize();j++)
					{
						if(randomChoice < population.get(candidate).getSpeciesElement(j).probability)
						{
							position = j; //Select first position
							break;
						}
					}*/

					if(randomDouble(1.0,0) < crossoverRate)
					{
						randomChoice = randomDouble(1.0,0);
						int position_2 = 0;
						for(j=0;j<population.get(candidate).getSpeciesSize();j++)
						{
							if(j != position)
							{
								if(randomChoice < population.get(candidate).getSpeciesElement(j).probability)
								{
									position_2 = j; //Select second position
									break;
								}
							}
						}

						Crossover cross = new Crossover();
						offspring_list[counter].setGenome(cross.CrossOver(organism, population.get(i).getSpeciesElement(position_2)));
						counter++;

					}
					else if(randomDouble(1.0,0) < mutationRate){
						Mutation mutate = new Mutation();

						Genome offspring = new Genome();
						offspring.setInputCount(organism.getGenome().getInputCount());
						offspring.setOutputCount(organism.getGenome().getOutputCount());

						for(k=0;k<organism.getGenome().getConnectionGenesSize();k++)
						{
							ConnectionGene geneCopy = new ConnectionGene();
							geneCopy.setActive(organism.getGenome().getConnectionGeneElement(k).ifActive());
							geneCopy.setIn_ID(organism.getGenome().getConnectionGeneElement(k).getIn_ID());
							geneCopy.setOut_ID(organism.getGenome().getConnectionGeneElement(k).getOut_ID());
							geneCopy.setInnovation(organism.getGenome().getConnectionGeneElement(k).getInnovation());
							geneCopy.setWeight(organism.getGenome().getConnectionGeneElement(k).getWeight());
							offspring.addConnectionGene(geneCopy);
						}
						for(k=0;k<organism.getGenome().getNodeGeneSize();k++)
						{
							NodeGene geneCopy = new NodeGene();
							geneCopy.setID(organism.getGenome().getNodeGeneElement(k).getID());
							geneCopy.setLayer(organism.getGenome().getNodeGeneElement(k).getLayer());
							geneCopy.isOutput = organism.getGenome().getNodeGeneElement(k).isOutput;
							geneCopy.isInput = organism.getGenome().getNodeGeneElement(k).isInput;
							offspring.addNodeGene(geneCopy); //addNodeGene(NodeGene node)
						}

						if(randomBoolean())
						{
							mutate.addNode(offspring);
							offspring_list[counter].setGenome(offspring);
							counter++;
						}else {
							mutate.addConnection(offspring);
							offspring_list[counter].setGenome(offspring);
							counter++;
						}
					}
					else {
						Genome offspring = new Genome();
						offspring.setInputCount(organism.getGenome().getInputCount());
						offspring.setOutputCount(organism.getGenome().getOutputCount());

						for(k=0;k<organism.getGenome().getConnectionGenesSize();k++)
						{
							ConnectionGene geneCopy = new ConnectionGene();
							geneCopy.setActive(organism.getGenome().getConnectionGeneElement(k).ifActive());
							geneCopy.setIn_ID(organism.getGenome().getConnectionGeneElement(k).getIn_ID());
							geneCopy.setOut_ID(organism.getGenome().getConnectionGeneElement(k).getOut_ID());
							geneCopy.setInnovation(organism.getGenome().getConnectionGeneElement(k).getInnovation());
							geneCopy.setWeight(organism.getGenome().getConnectionGeneElement(k).getWeight());
							offspring.addConnectionGene(geneCopy);
						}
						for(k=0;k<organism.getGenome().getNodeGeneSize();k++)
						{
							NodeGene geneCopy = new NodeGene();
							geneCopy.setID(organism.getGenome().getNodeGeneElement(k).getID());
							geneCopy.setLayer(organism.getGenome().getNodeGeneElement(k).getLayer());
							geneCopy.isOutput = organism.getGenome().getNodeGeneElement(k).isOutput;
							geneCopy.isInput = organism.getGenome().getNodeGeneElement(k).isInput;
							offspring.addNodeGene(geneCopy);//addNodeGene(NodeGene node)
						}
						offspring_list[counter].setGenome(offspring);
						counter++;

					}

					--leftPopulation;
				}
			}

			while(producePopulation > 0)
			{
				offspring_list[counter] = new Organism();
				double randomChoice = randomDouble(1.0, 0);
				/*
				for(j=0;j<population.get(i).getSpeciesSize();j++)
				{
					if(randomChoice < population.get(i).getSpeciesElement(j).probability)
					{
						position = j;
						break;
					}
				}*/

				if(randomDouble(1.0,0) < crossoverRate)
				{
					randomChoice = randomDouble(1.0,0);
					int position_2 = 0;
					for(j=0;j<population.get(i).getSpeciesSize();j++)
					{
						if(j != position)
						{
							if(randomChoice < population.get(i).getSpeciesElement(j).probability)
							{
								position_2 = j;
								break;
							}
						}
					}

					Crossover cross = new Crossover();
					offspring_list[counter].setGenome(cross.CrossOver(organism, population.get(i).getSpeciesElement(position_2)));
					counter++;

				}
				else if(randomDouble(1.0,0) < mutationRate){
					Mutation mutate = new Mutation();

					Genome offspring = new Genome();
					offspring.setInputCount(organism.getGenome().getInputCount());
					offspring.setOutputCount(organism.getGenome().getOutputCount());

					for(k=0;k<organism.getGenome().getConnectionGenesSize();k++)
					{
						ConnectionGene geneCopy = new ConnectionGene();
						geneCopy.setActive(organism.getGenome().getConnectionGeneElement(k).ifActive());
						geneCopy.setIn_ID(organism.getGenome().getConnectionGeneElement(k).getIn_ID());
						geneCopy.setOut_ID(organism.getGenome().getConnectionGeneElement(k).getOut_ID());
						geneCopy.setInnovation(organism.getGenome().getConnectionGeneElement(k).getInnovation());
						geneCopy.setWeight(organism.getGenome().getConnectionGeneElement(k).getWeight());
						offspring.addConnectionGene(geneCopy);
					}
					for(k=0;k<organism.getGenome().getNodeGeneSize();k++)
					{
						NodeGene geneCopy = new NodeGene();
						geneCopy.setID(organism.getGenome().getNodeGeneElement(k).getID());
						geneCopy.setLayer(organism.getGenome().getNodeGeneElement(k).getLayer());
						geneCopy.isOutput = organism.getGenome().getNodeGeneElement(k).isOutput;
						geneCopy.isInput = organism.getGenome().getNodeGeneElement(k).isInput;
						offspring.addNodeGene(geneCopy);//addNodeGene(NodeGene node)
					}

					if(randomBoolean())
					{
						mutate.addNode(offspring);
						offspring_list[counter].setGenome(offspring);
						counter++;
					}else {
						mutate.addConnection(offspring);
						offspring_list[counter].setGenome(offspring);
						counter++;
					}
				}
				else {
					Genome offspring = new Genome();
					offspring.setInputCount(organism.getGenome().getInputCount());
					offspring.setOutputCount(organism.getGenome().getOutputCount());

					for(k=0;k<organism.getGenome().getConnectionGenesSize();k++)
					{
						ConnectionGene geneCopy = new ConnectionGene();
						geneCopy.setActive(organism.getGenome().getConnectionGeneElement(k).ifActive());
						geneCopy.setIn_ID(organism.getGenome().getConnectionGeneElement(k).getIn_ID());
						geneCopy.setOut_ID(organism.getGenome().getConnectionGeneElement(k).getOut_ID());
						geneCopy.setInnovation(organism.getGenome().getConnectionGeneElement(k).getInnovation());
						geneCopy.setWeight(organism.getGenome().getConnectionGeneElement(k).getWeight());
						offspring.addConnectionGene(geneCopy);
					}
					for(k=0;k<organism.getGenome().getNodeGeneSize();k++)
					{
						NodeGene geneCopy = new NodeGene();
						geneCopy.setID(organism.getGenome().getNodeGeneElement(k).getID());
						geneCopy.setLayer(organism.getGenome().getNodeGeneElement(k).getLayer());
						geneCopy.isOutput = organism.getGenome().getNodeGeneElement(k).isOutput;
						geneCopy.isInput = organism.getGenome().getNodeGeneElement(k).isInput;
						offspring.addNodeGene(geneCopy);//addNodeGene(NodeGene node)
					}
					offspring_list[counter].setGenome(offspring);
					counter++;

				}

				--producePopulation;
			}

		}

		return offspring_list;
	}

	public static Organism[] generateNewPopulation()
	{
		int i;
		int j;
		int k;
		int counter;
		int leftPopulation;

		double averageFitness = totalFitness/totalPopulation;
		
		Organism[] offspring_list;
		counter = 0;

		offspring_list = new Organism[totalPopulation];
		leftPopulation = totalPopulation;

		double minFit = Double.MIN_VALUE;
		double maxFit = Double.MAX_VALUE;

		for(i=0;i<totalFitnessList.length;i++)
		{
			if(minFit > totalFitnessList[i]){
				minFit = totalFitnessList[i];
			}
			if(maxFit < totalFitnessList[i]){
				maxFit = totalFitnessList[i];
			}
		}
		

		//Work on whether optimization does not progress within 6 generations

		for(i=0;i<population.size();i++)
		{

			double speciesCrossoverRate;
			double speciesMutationRate;

			speciesCrossoverRate = softRange(totalFitnessList[i], minFit, maxFit);
			speciesMutationRate = softRange(totalFitnessList[i], minFit, maxFit);
			if(leftPopulation < 0)
			{
				break;
			}

			int position = 0;

			double percentage = (totalFitnessList[i]/totalFitness);
			int producePopulation = (int) Math.rint(totalPopulation*percentage);
			leftPopulation -= producePopulation;

			if(leftPopulation < 0)
			{
				producePopulation -= Math.abs(leftPopulation);
			}

			if(i==population.size()-1 && leftPopulation>0)
			{
				double max = Double.MIN_VALUE;
				int candidate = 0;
				for(j=0;j<totalFitnessList.length;j++)
				{
					if(totalFitnessList[j] > max)
					{
						max = totalFitnessList[j];
						candidate = j;
					}
				}
				while(leftPopulation > 0)
				{
				    System.out.println("Offspring "+counter);
					offspring_list[counter] = new Organism();
					double randomChoice = randomDouble(1.0, 0);
					for(j=0;j<population.get(candidate).getSpeciesSize();j++)
					{
						if(randomChoice < population.get(candidate).getSpeciesElement(j).probability)
						{
							position = j; //Select first position
							break;
						}
					}

					if(randomDouble(1.0,0) < speciesCrossoverRate)
					{
						randomChoice = randomDouble(1.0,0);
						int position_2 = 0;
						for(j=0;j<population.get(candidate).getSpeciesSize();j++)
						{
							if(j != position)
							{
								if(randomChoice < population.get(candidate).getSpeciesElement(j).probability)
								{
									position_2 = j; //Select second position
									break;
								}
							}
						}

						Crossover cross = new Crossover();
						offspring_list[counter].setGenome(cross.CrossOver(population.get(candidate).getSpeciesElement(position), population.get(i).getSpeciesElement(position_2)));
						counter++;

					}
					else if(randomDouble(1.0,0) < speciesMutationRate){
						Mutation mutate = new Mutation();

						Genome offspring = new Genome();
						offspring.setInputCount(population.get(candidate).getSpeciesElement(position).getGenome().getInputCount());
						offspring.setOutputCount(population.get(candidate).getSpeciesElement(position).getGenome().getOutputCount());

						for(k=0;k<population.get(candidate).getSpeciesElement(position).getGenome().getConnectionGenesSize();k++)
						{
							ConnectionGene geneCopy = new ConnectionGene();
							geneCopy.setActive(population.get(candidate).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).ifActive());
							geneCopy.setIn_ID(population.get(candidate).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getIn_ID());
							geneCopy.setOut_ID(population.get(candidate).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getOut_ID());
							geneCopy.setInnovation(population.get(candidate).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getInnovation());
							geneCopy.setWeight(population.get(candidate).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getWeight());
							offspring.addConnectionGene(geneCopy);
						}
						for(k=0;k<population.get(candidate).getSpeciesElement(position).getGenome().getNodeGeneSize();k++)
						{
							NodeGene geneCopy = new NodeGene();
							geneCopy.setID(population.get(candidate).getSpeciesElement(position).getGenome().getNodeGeneElement(k).getID());
							geneCopy.setLayer(population.get(candidate).getSpeciesElement(position).getGenome().getNodeGeneElement(k).getLayer());
							geneCopy.isOutput = population.get(candidate).getSpeciesElement(position).getGenome().getNodeGeneElement(k).isOutput;
							geneCopy.isInput = population.get(candidate).getSpeciesElement(position).getGenome().getNodeGeneElement(k).isInput;
							offspring.addNodeGene(geneCopy);//addNodeGene(NodeGene node)
						}

						if(randomBoolean())
						{
							mutate.addNode(offspring);
							offspring_list[counter].setGenome(offspring);
							counter++;
						}else {
							mutate.addConnection(offspring);
							offspring_list[counter].setGenome(offspring);
							counter++;
						}
					}
					else {
						Genome offspring = new Genome();
						offspring.setInputCount(population.get(candidate).getSpeciesElement(position).getGenome().getInputCount());
						offspring.setOutputCount(population.get(candidate).getSpeciesElement(position).getGenome().getOutputCount());

						for(k=0;k<population.get(candidate).getSpeciesElement(position).getGenome().getConnectionGenesSize();k++)
						{
							ConnectionGene geneCopy = new ConnectionGene();
							geneCopy.setActive(population.get(candidate).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).ifActive());
							geneCopy.setIn_ID(population.get(candidate).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getIn_ID());
							geneCopy.setOut_ID(population.get(candidate).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getOut_ID());
							geneCopy.setInnovation(population.get(candidate).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getInnovation());
							geneCopy.setWeight(population.get(candidate).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getWeight());
							offspring.addConnectionGene(geneCopy);
						}
						for(k=0;k<population.get(candidate).getSpeciesElement(position).getGenome().getNodeGeneSize();k++)
						{
							NodeGene geneCopy = new NodeGene();
							geneCopy.setID(population.get(candidate).getSpeciesElement(position).getGenome().getNodeGeneElement(k).getID());
							geneCopy.setLayer(population.get(candidate).getSpeciesElement(position).getGenome().getNodeGeneElement(k).getLayer());
							geneCopy.isOutput = population.get(candidate).getSpeciesElement(position).getGenome().getNodeGeneElement(k).isOutput;
							geneCopy.isInput = population.get(candidate).getSpeciesElement(position).getGenome().getNodeGeneElement(k).isInput;
							offspring.addNodeGene(geneCopy);//addNodeGene(NodeGene node)
						}
						System.out.println("ConnectionGenes Size: "+offspring.getConnectionGenesSize());
                        System.out.println("NodeGenes Size: "+offspring.getNodeGeneSize());
						System.out.println("------------------------------");
						offspring_list[counter].setGenome(offspring);
						counter++;

					}

					--leftPopulation;
				}
			}

			while(producePopulation > 0)
			{
                System.out.println("Offspring "+counter);
				offspring_list[counter] = new Organism();
				double randomChoice = randomDouble(1.0, 0);
				for(j=0;j<population.get(i).getSpeciesSize();j++)
				{
					if(randomChoice < population.get(i).getSpeciesElement(j).probability)
					{
						position = j;
						break;
					}
				}

				if(randomDouble(1.0,0) < speciesCrossoverRate)
				{
					randomChoice = randomDouble(1.0,0);
					int position_2 = 0;
					for(j=0;j<population.get(i).getSpeciesSize();j++)
					{
						if(j != position)
						{
							if(randomChoice < population.get(i).getSpeciesElement(j).probability)
							{
								position_2 = j;
								break;
							}
						}
					}

					Crossover cross = new Crossover();
					offspring_list[counter].setGenome(cross.CrossOver(population.get(i).getSpeciesElement(position), population.get(i).getSpeciesElement(position_2)));
                    System.out.println("Crossover ConnectionGenes Size: "+offspring_list[counter].getGenome().getConnectionGenesSize());
                    System.out.println("Crossover NodeGenes Size: "+offspring_list[counter].getGenome().getNodeGeneSize());
                    System.out.println("------------------------------");
					counter++;

				}
				else if(randomDouble(1.0,0) < speciesMutationRate){
					Mutation mutate = new Mutation();

					Genome offspring = new Genome();
					offspring.setInputCount(population.get(i).getSpeciesElement(position).getGenome().getInputCount());
					offspring.setOutputCount(population.get(i).getSpeciesElement(position).getGenome().getOutputCount());

					for(k=0;k<population.get(i).getSpeciesElement(position).getGenome().getConnectionGenesSize();k++)
					{
						ConnectionGene geneCopy = new ConnectionGene();
						geneCopy.setActive(population.get(i).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).ifActive());
						geneCopy.setIn_ID(population.get(i).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getIn_ID());
						geneCopy.setOut_ID(population.get(i).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getOut_ID());
						geneCopy.setInnovation(population.get(i).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getInnovation());
						geneCopy.setWeight(population.get(i).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getWeight());
						offspring.addConnectionGene(geneCopy);
					}
					for(k=0;k<population.get(i).getSpeciesElement(position).getGenome().getNodeGeneSize();k++)
					{
						NodeGene geneCopy = new NodeGene();
						geneCopy.setID(population.get(i).getSpeciesElement(position).getGenome().getNodeGeneElement(k).getID());
						geneCopy.setLayer(population.get(i).getSpeciesElement(position).getGenome().getNodeGeneElement(k).getLayer());
						geneCopy.isOutput = population.get(i).getSpeciesElement(position).getGenome().getNodeGeneElement(k).isOutput;
						geneCopy.isInput = population.get(i).getSpeciesElement(position).getGenome().getNodeGeneElement(k).isInput;
						offspring.addNodeGene(geneCopy);//addNodeGene(NodeGene node)
					}

					if(randomBoolean())
					{
						mutate.addNode(offspring);
						offspring_list[counter].setGenome(offspring);
						counter++;
					}else {
						mutate.addConnection(offspring);
						offspring_list[counter].setGenome(offspring);
						counter++;
					}
                    System.out.println("Mutation ConnectionGenes Size: "+offspring.getConnectionGenesSize());
                    System.out.println("Mutation NodeGenes Size: "+offspring.getNodeGeneSize());
                    System.out.println("------------------------------");
				}
				else {
					Genome offspring = new Genome();
					offspring.setInputCount(population.get(i).getSpeciesElement(position).getGenome().getInputCount());
					offspring.setOutputCount(population.get(i).getSpeciesElement(position).getGenome().getOutputCount());

					for(k=0;k<population.get(i).getSpeciesElement(position).getGenome().getConnectionGenesSize();k++)
					{
						ConnectionGene geneCopy = new ConnectionGene();
						geneCopy.setActive(population.get(i).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).ifActive());
						geneCopy.setIn_ID(population.get(i).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getIn_ID());
						geneCopy.setOut_ID(population.get(i).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getOut_ID());
						geneCopy.setInnovation(population.get(i).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getInnovation());
						geneCopy.setWeight(population.get(i).getSpeciesElement(position).getGenome().getConnectionGeneElement(k).getWeight());
						offspring.addConnectionGene(geneCopy);
					}
					for(k=0;k<population.get(i).getSpeciesElement(position).getGenome().getNodeGeneSize();k++)
					{
						NodeGene geneCopy = new NodeGene();
						geneCopy.setID(population.get(i).getSpeciesElement(position).getGenome().getNodeGeneElement(k).getID());
						geneCopy.setLayer(population.get(i).getSpeciesElement(position).getGenome().getNodeGeneElement(k).getLayer());
						geneCopy.isOutput = population.get(i).getSpeciesElement(position).getGenome().getNodeGeneElement(k).isOutput;
						geneCopy.isInput = population.get(i).getSpeciesElement(position).getGenome().getNodeGeneElement(k).isInput;

						offspring.addNodeGene(geneCopy);//addNodeGene(NodeGene node)
					}
					offspring_list[counter].setGenome(offspring);
                    System.out.println("Normal ConnectionGenes Size: "+offspring.getConnectionGenesSize());
                    System.out.println("Normal NodeGenes Size: "+offspring.getNodeGeneSize());
                    System.out.println("------------------------------");
					counter++;

				}

				--producePopulation;
			}

		}

		return offspring_list;
	}

	public void producePopulation(int producePopulation, Organism[] offspring_list)
	{

	}

	public static double softRange(double x, double lowBound, double highBound){
		double squish = 2*((x-lowBound)/(highBound-lowBound)) - 1;

		return Math.exp(-squish)/14;
	}

	public static boolean randomBoolean()
	{
		return (((int)(Math.random()*2))==1)?true:false;
	}
	public static double randomDouble(double high, double low)
	{
		return ((high)-low) * Math.random() + low; //x<high
	}
	public static void addSpecies(Species species)
	{
		population.add(species);
	}
	public static void removeSpecies(int index)
	{
		population.remove(index);
	}
}
