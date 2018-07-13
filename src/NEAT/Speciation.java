package NEAT;

public class Speciation {

    private int i;

    public static double coefficient_Excess = 1.0;
    public static double coefficient_Disjoint = 1.0;
    public static double coefficient_Weight = 0.4;
    public static double delta_threshold = 3;

    private double weightSum = 0;
    private int matchCount = 0;
    private int excessCount = 0;
    private int disjointCount = 0;
    private int element_subject = 0;
    private int element_candidate = 0;
    private int innovation_subject = 0;
    private int innovation_candidate = 0;
    private int omegaSize = 0;

    public Speciation()
    {

    }

    public void speciation(Organism organism)
    {
        for(i=0;i<Population.getPopulationSize();i++)
        {
            double delta = deltaCompatible(Population.getPopulationElement(i).getSpeciesElement(0).getGenome(), organism.getGenome());
            System.out.println("Delta Comparison: "+delta);
            if(deltaCompatible(Population.getPopulationElement(i).getSpeciesElement(0).getGenome(), organism.getGenome()) <= delta_threshold)
            {
                Population.getPopulationElement(i).addSpecies(organism);
                organism.addSpecies(Population.getPopulationElement(i));
                return;
            }

        }

        Species newSpecies = new Species();
        newSpecies.addSpecies(organism);
        organism.addSpecies(newSpecies);
        Population.addSpecies(newSpecies);
        return;
    }

    public double computeSharedFitness(Species species, double fitness)
    {
        return (fitness/species.getSpeciesSize());
    }

    public double deltaCompatible(Genome subject, Genome candidate)
    {
        int i; //local i
        element_subject = 0;
        element_candidate = 0;

        matchCount = 0;
        disjointCount = 0;
        weightSum = 0.0;
        excessCount = 0;

        omegaSize = ((subject.getConnectionGenesSize()>candidate.getConnectionGenesSize())?subject.getConnectionGenesSize():candidate.getConnectionGenesSize()); //omega is considered to be genome with the greater number of genes than the other

        excessCount = Math.abs(subject.getConnectionGenesSize() - candidate.getConnectionGenesSize());
        while(subject.getConnectionGenesSize()-1 != element_subject && candidate.getConnectionGenesSize()-1 != element_candidate)
        {
            innovation_subject = subject.getConnectionGeneElement(element_subject).getInnovation();
            innovation_candidate = candidate.getConnectionGeneElement(element_candidate).getInnovation();

            if(innovation_subject == innovation_candidate) {
                weightSum+=Math.abs(subject.getConnectionGeneElement(element_subject).getWeight() - candidate.getConnectionGeneElement(element_candidate).getWeight());
                matchCount++;
				if(element_subject < subject.getConnectionGenesSize()-1)
				{
					element_subject++;
				}
				if(element_candidate < candidate.getConnectionGenesSize()-1)
				{
					element_candidate++;
				}
            }else if(innovation_subject > innovation_candidate){
                disjointCount++;
                if(element_candidate < candidate.getConnectionGenesSize()-1)
                {
                    element_candidate++;
                }
            }else{
                disjointCount++;
                if(element_subject < subject.getConnectionGenesSize()-1)
                {
                    element_subject++;
                }
            }



        }
		
		
		/*for(i=0;i<omegaSize;i++)
		{
			innovation_subject = subject.getConnectionGeneElement(element_subject).getInnovation();
			innovation_candidate = candidate.getConnectionGeneElement(element_candidate).getInnovation();

			if(element_subject == subject.getConnectionGenesSize()-1 && element_candidate < candidate.getConnectionGenesSize()-1 && (innovation_candidate > innovation_subject || innovation_candidate == innovation_subject))
			{
				if(innovation_candidate != innovation_subject)
				{
					excessCount+=candidate.getConnectionGenesSize()-element_candidate-1; //(-1) since the element pointed by element_candidate is also an excess element; we should include that also
					break;
				}else {
					matchCount++;
					element_candidate++;
				}
				
			}else if(element_subject < subject.getConnectionGenesSize()-1 && element_candidate == candidate.getConnectionGenesSize()-1 && (innovation_candidate < innovation_subject || innovation_candidate == innovation_subject))
			{
				if(innovation_candidate != innovation_subject)
				{
					excessCount+=subject.getConnectionGenesSize()-element_subject-1; //(-1) since the element pointed by element_candidate is also an excess element; we should include that also
					break;
				}else {
					matchCount++;
					element_subject++;
				}
			}else if(innovation_candidate > innovation_subject)
			{
				disjointCount++;
				if(element_subject < subject.getConnectionGenesSize()-1)
				{
					element_subject++;
				}
			}else if(innovation_candidate < innovation_subject)
			{
				disjointCount++;
				if(element_candidate < candidate.getConnectionGenesSize()-1)
				{
					element_candidate++;
				}
			}else if(innovation_candidate == innovation_subject)
			{
				weightSum+=Math.abs(subject.getConnectionGeneElement(element_subject).getWeight() - candidate.getConnectionGeneElement(element_candidate).getWeight());
				
				matchCount++;
				element_candidate++;
				element_subject++;
				
			}
		}*/

        return (((coefficient_Excess*excessCount)/omegaSize + (coefficient_Disjoint*disjointCount))/omegaSize) + (coefficient_Weight*(weightSum/matchCount));

    }
}
