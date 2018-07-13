package NEAT;

public class Crossover {

	public Genome CrossOver(Organism organism_1, Organism organism_2)
	{

		int element_genome_1 = 0;
		int element_genome_2 = 0;
		int innovation_1 = 0;
		int innovation_2 = 0;
		double E = 0.05;

		Genome genome_1 = organism_1.getGenome();
		Genome genome_2 = organism_2.getGenome();

		Genome alpha = null;

		double inheritanceProb = 0.6;


		if(organism_1.fitness>organism_2.fitness)
		{
			alpha = genome_1;
		}else
		{
			alpha = genome_2;
		}


		Genome offspring = new Genome();

		offspring.setInputCount(organism_1.getGenome().getInputCount());
		offspring.setOutputCount(organism_2.getGenome().getOutputCount());

        int omegaSize_connection = ((Math.abs(organism_1.getGenome().getConnectionGenesSize()-organism_2.getGenome().getConnectionGenesSize())));
        int i;
        /*for(i=0;i<omegaSize_connection;i++)
        {
            if(genome_1.getConnectionGenesSize() > genome_2.getConnectionGenesSize())
            {
                offspring.addConnectionGene(genome_1.getConnectionGeneElement(genome_2.getConnectionGenesSize()+i));
            }else{
                offspring.addConnectionGene(genome_2.getConnectionGeneElement(genome_1.getConnectionGenesSize()+i));
            }
        }

        while(genome_1.getConnectionGenesSize() > element_genome_1 && genome_2.getConnectionGenesSize() > element_genome_2)
        {

            System.out.println("Evaluating");
                innovation_1 = genome_1.getConnectionGeneElement(element_genome_1).getInnovation();
                innovation_2 = genome_2.getConnectionGeneElement(element_genome_2).getInnovation();

            if(innovation_1 == innovation_2)
            {
                if(randomDouble(0,1)<inheritanceProb)
                {
                    if(alpha.equals(genome_1))
                    {
                        offspring.addConnectionGene(genome_1.getConnectionGeneElement(element_genome_1));
                    }else{
                        offspring.addConnectionGene(genome_2.getConnectionGeneElement(element_genome_2));
                    }
                }else{
                    if(!alpha.equals(genome_1))
                    {
                        offspring.addConnectionGene(genome_1.getConnectionGeneElement(element_genome_1));
                    }else{
                        offspring.addConnectionGene(genome_2.getConnectionGeneElement(element_genome_2));
                    }
                }

                if(element_genome_1 < genome_1.getConnectionGenesSize())
                {
                    element_genome_1++;
                }
                if(element_genome_2 < genome_2.getConnectionGenesSize())
                {
                    element_genome_2++;
                }
            }else if(innovation_1 > innovation_2)
            {
                if(randomDouble(0,1)<inheritanceProb)
                {
                    if(alpha.equals(genome_2))
                    {
                        offspring.addConnectionGene(genome_1.getConnectionGeneElement(element_genome_2));
                    }
                }

                if(element_genome_2<genome_2.getConnectionGenesSize())
                {
                    element_genome_2++;
                }
            }else{
                if(randomDouble(0,1)<inheritanceProb)
                {
                    if(alpha.equals(genome_1))
                    {
                        offspring.addConnectionGene(genome_1.getConnectionGeneElement(element_genome_1));
                    }
                }

                if(element_genome_1<genome_1.getConnectionGenesSize())
                {
                    element_genome_1++;
                }
            }
        }*/
		while(genome_1.getConnectionGenesSize() > element_genome_1 || genome_2.getConnectionGenesSize() > element_genome_2)
		{
			//System.out.println("Iteration");
			int index_1 = element_genome_1;
			int index_2 = element_genome_2;
			if(element_genome_1 >= genome_1.getConnectionGenesSize())
			{
				index_1 = genome_1.getConnectionGenesSize()-1;
				innovation_1 = Integer.MAX_VALUE;
			}else {
				innovation_1 = genome_1.getConnectionGeneElement(index_1).getInnovation();
			}

			if(element_genome_2 >= genome_2.getConnectionGenesSize())
			{
				index_2 = genome_2.getConnectionGenesSize()-1;
				innovation_2 = Integer.MAX_VALUE;
			}else {
				innovation_2 = genome_2.getConnectionGeneElement(index_2).getInnovation();
			}



			if(innovation_1 == innovation_2)
			{
				if(randomBoolean())
				{
					offspring.checkConnectionOverlap(genome_1.getConnectionGeneElement(index_1));
					element_genome_1++;
				}else {
					offspring.checkConnectionOverlap(genome_2.getConnectionGeneElement(index_2));
					element_genome_2++;
				}
			}else if(innovation_1 > innovation_2)
			{
				if(randomBoolean())
				{
					offspring.checkConnectionOverlap(genome_2.getConnectionGeneElement(index_2));
					element_genome_2++;
				}
			}else {
				if(randomBoolean())
				{
					offspring.checkConnectionOverlap(genome_1.getConnectionGeneElement(index_1));
					element_genome_1++;
				}
			}
		}


		///********************************************


		/*int connectionDifference = Math.abs(organism_1.getGenome().getConnectionGenesSize() - organism_2.getGenome().getConnectionGenesSize());

		for(i=omegaSize_connection - connectionDifference;i<omegaSize_connection;i++)
        {
            if(alpha.getConnectionGenesSize() == omegaSize_connection)
            {
                offspring.addConnectionGene(alpha.getConnectionGeneElement(i));
            }
        } //Puts excess elements into the offspring network

		while(genome_1.getConnectionGenesSize()-1 > element_genome_1 && genome_2.getConnectionGenesSize()-1 > element_genome_2)
        {
            System.out.println("Evaluating Crossover");
            innovation_1 = genome_1.getConnectionGeneElement(element_genome_1).getInnovation();
            innovation_2 = genome_2.getConnectionGeneElement(element_genome_2).getInnovation();

            if(innovation_1 > innovation_2)
            {
                if(genome_2.equals(alpha))
                {
                    offspring.addConnectionGene(genome_2.getConnectionGeneElement(element_genome_2));
                }
                if(element_genome_2 < genome_2.getConnectionGenesSize()-1)
                {
                    element_genome_2++;
                }
            }else if(innovation_1 < innovation_2)
            {
                if(genome_1.equals(alpha))
                {
                    offspring.addConnectionGene(genome_1.getConnectionGeneElement(element_genome_1));
                }
                if(element_genome_1<genome_2.getConnectionGenesSize()-1)
                {
                    element_genome_1++;
                }
            }else{
                if(randomDouble(1,0) < inheritanceProb)
                {
                    if(alpha.equals(genome_1)) {
                        offspring.addConnectionGene(genome_1.getConnectionGeneElement(element_genome_1));
                    }else{
                        offspring.addConnectionGene(genome_2.getConnectionGeneElement(element_genome_2));
                    }
                }
            }
            //if(innovation_1)

        }*/

		/*for(i=0;i<omegaSize_connection;i++)
		{
			innovation_1 = genome_1.getConnectionGeneElement(element_genome_1).getInnovation();
			innovation_2 = genome_2.getConnectionGeneElement(element_genome_2).getInnovation();

			if(element_genome_1 == genome_1.getConnectionGenesSize()-1 && element_genome_2 < genome_2.getConnectionGenesSize()-1 && (innovation_2 >= innovation_1 || innovation_2 < innovation_1))
			{
				if(alpha!=null)
				{
					if(alpha.equals(genome_2)&&innovation_2!=innovation_1)
					{
						offspring.addConnectionGene(genome_2.getConnectionGeneElement(element_genome_2));
					}else {
						if(randomBoolean())
						{
							offspring.addConnectionGene(genome_2.getConnectionGeneElement(element_genome_2));
						}
					}
				}else {

					offspring.addConnectionGene(genome_2.getConnectionGeneElement(element_genome_2));

				}

				element_genome_2++;

			}else if(element_genome_1 < genome_1.getConnectionGenesSize()-1 && element_genome_2 == genome_2.getConnectionGenesSize()-1 && (innovation_2 <= innovation_1 || innovation_2 > innovation_1))
			{
				if(alpha!=null)
				{
					if(alpha.equals(genome_1)&&innovation_2!=innovation_1)
					{
						offspring.addConnectionGene(genome_1.getConnectionGeneElement(element_genome_1));
					}else {
						if(randomBoolean())
						{
							offspring.addConnectionGene(genome_2.getConnectionGeneElement(element_genome_2));
						}
					}
				}else {

					offspring.addConnectionGene(genome_1.getConnectionGeneElement(element_genome_1));

				}

				element_genome_1++;

			}else if(innovation_2 < innovation_1)
			{
				if(alpha!=null)
				{
					if(alpha.equals(genome_2))
					{
						offspring.addConnectionGene(genome_2.getConnectionGeneElement(element_genome_2));
					}
				}else {

					offspring.addConnectionGene(genome_2.getConnectionGeneElement(element_genome_2));

				}

				element_genome_2++;

			}else if(innovation_2 > innovation_1)
			{
				if(alpha!=null)
				{
					if(alpha.equals(genome_1))
					{
						offspring.addConnectionGene(genome_1.getConnectionGeneElement(element_genome_1));
					}
				}else {

					offspring.addConnectionGene(genome_1.getConnectionGeneElement(element_genome_1));

				}

				element_genome_1++;

			}else if(innovation_2 == innovation_1)
			{
				if(randomBoolean())
				{
					offspring.addConnectionGene(genome_1.getConnectionGeneElement(element_genome_1));
				}else {
					offspring.addConnectionGene(genome_2.getConnectionGeneElement(element_genome_2));
				}

				element_genome_1++;
				element_genome_2++;
			}

		}*/
		addNodes(offspring);
		return offspring;

	}

	private void addNodes(Genome offspring)
	{
		int i;
		int max = 0;

		offspring.getNodeGeneList().clear();

		for(i=0;i<offspring.getConnectionGenesSize();i++) {
			offspring.addNodeGene(offspring.getConnectionGeneElement(i));
		}

	}

	public int randomInteger(int high, int low)
	{
		return (int)(Math.random()*((high+1)-low))+low;  //(+1) since x<high, want to make it x<=high
	}

	public boolean randomBoolean()
	{
		return ((int) (Math.random() * 2)) == 1;
	}

	public double randomDouble(double high, double low)
	{
		return ((high)-low) * Math.random() + low; //x<high
	}
}
