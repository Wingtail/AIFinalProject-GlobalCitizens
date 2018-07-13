package NEAT;

import java.io.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import java.util.ArrayList;

public class TestForwardProp {
    Genome testGenome = new Genome();


    File testOutput;
    FileWriter testWriter;
    public TestForwardProp(){
        try
        {
            testOutput = new File("testOutput.txt");
            testWriter = new FileWriter(testOutput);
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        int i;
        TestForwardProp test = new TestForwardProp();
        test.testGenome.setInputCount(2);
        test.testGenome.setOutputCount(2);

        /*for(i=0;i<test.testGenome.getInputCount()+test.testGenome.getOutputCount();i++)
        {
            test.testGenome.addNodeGene(new NodeGene());
        }

        for(i=0;i<3;i++)
        {
            test.testGenome.addNodeGene(new NodeGene());
        }*/

        test.readJson();

        /*test.addConnection(4,1,1,true,test);
        test.addConnection(5,1,1,true,test);
        test.addConnection(4,2,1,true,test);
        test.addConnection(5,2,1,true,test);
        test.addConnection(6,5,1,true,test);
        test.addConnection(5,3,1,true,test);
        test.addConnection(7,4,1,true,test);
        test.addConnection(6,4,1,true,test);
        test.addConnection(7,6,1,true,test);
        test.addConnection(8,6,1,true,test);
        test.addConnection(7,8,1,true,test);*/



		/*test.addConnection(5, 1, 0.1, true, test);
		test.addConnection(1, 5, 0.1, true, test);
		test.addConnection(6, 1, 0.2, true, test);
		test.addConnection(4, 1, 0.3, true, test);
		test.addConnection(5, 1, 0.1, true, test);
		test.addConnection(6, 2, 0.1, true, test);
		test.addConnection(8, 2, 0.1, true, test);
		test.addConnection(7, 2, 0.1, true, test);
		test.addConnection(8, 3, 0.1, true, test);
		test.addConnection(8, 4, 0.1, true, test);
		test.addConnection(6, 5, 0.1, true, test);
		test.addConnection(4, 5, 0.1, true, test);
		test.addConnection(7, 6, 0.1, true, test);
		test.addConnection(9, 6, 0.1, true, test);
		test.addConnection(8, 7, 0.1, true, test);
		test.addConnection(4, 7, 0.1, true, test);
		test.addConnection(9, 8, 0.1, true, test);
		test.addConnection(4, 8, 0.1, true, test);
		test.addConnection(6, 8, 0.1, true, test);
		test.addConnection(5, 8, 0.1, true, test);
		test.addConnection(7, 9, 0.1, true, test);
		test.addConnection(4, 9, 0.1, true, test);*/



        for(ConnectionGene connectionGene : test.testGenome.getConnectionGenes())
        {
            test.testGenome.addNodeGene(connectionGene);
        }


		/*
		for(i=1;i<4;i++)
		{
			ConnectionGene cg = new ConnectionGene(4, i, 0.5, true, ConnectionGene.incrementGlobalInnovation());
			test.testGenome.addConnectionGene(cg);
		}


		//Network network = new Network();
		//network.constructNetwork(test.testGenome);
		//network.ForwardProp(1.0,2.0,3.0);


		//System.out.println(network.getFinalOutputListElement(0));



		Mutation mutation = new Mutation();

		mutation.addNode(test.testGenome);

		ConnectionGene geneC = new ConnectionGene(5,5,0.12,true,ConnectionGene.incrementGlobalInnovation());
		test.testGenome.addConnectionGene(geneC);

		mutation.addConnection(test.testGenome);

		System.out.println("This is network1 \n\n\n");

		for(i=0;i<4;i++)
		{
			mutation.addNode(test.testGenome);
			mutation.addConnection(test.testGenome);
			mutation.addConnection(test.testGenome);
		}
		*/
        Network network1 = new Network();
        network1.constructNetwork(test.testGenome);

        double[] inputs = {4.0,4.0};

        int counter = 0;
        for(double o : inputs)
        {
            counter++;
            try {
                test.testWriter.write("Input " + counter + ": " + o + "\n");
                test.testWriter.flush();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }


        network1.ForwardProp(4.0,4.0);

        counter = 0;
        /*for(double o : network1.output())
        {
            System.out.println("Output: "+o);
            counter++;
            try {
                test.testWriter.write("Output " + counter + ": " + o + "\n");
                test.testWriter.flush();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }*/

        NetworkJsonEncoder netJson = new NetworkJsonEncoder();
        netJson.exportNetwork(test.testGenome);

        try {
            test.testWriter.flush();
            test.testWriter.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        //network.ForwardProp(1.0,2.0,3.0);

        //System.out.println(network.getFinalOutputListElement(0));


        //System.out.println(network1.getFinalOutputListElement(0));


    }

    public void readJson()
    {
        Gson gson = new Gson();

        JsonObject jsonObject = new JsonObject();
        JsonArray jarr;
        try {
            JsonReader reader = new JsonReader(new FileReader("Network.json"));
            ArrayList<ArrayList<Double>> conObject = gson.fromJson(reader,ArrayList.class);

            for(ArrayList<Double> list : conObject){
                addConnection(list.get(1).intValue(),list.get(0).intValue(), list.get(2), true, this);
            }

        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addConnection(int in, int out, double weight, boolean flag, TestForwardProp test)
    {
        ConnectionGene cg = new ConnectionGene(in, out, weight, flag, ConnectionGene.incrementGlobalInnovation());
        test.testGenome.checkConnectionOverlap(cg);
    }

}
