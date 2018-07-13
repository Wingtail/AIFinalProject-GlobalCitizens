package NEATImplementation;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.*;
import java.text.DecimalFormat;
public class Backpropagation {

	public double[][] input = {{2,4,2,2},{3,6},{25,50},{40,80},{1,2},{5,10},{8,9},{9,18},{98,99},{98,865},{98,196},{600,85},{86,98},{65,130}}; //The input data matrix(2x2)
	public double[][] target = {{1,1,1,1,1,1,0,1,0,0,1,0,0,1},{1,1,1,1,1,1,0,1,0,0,1,0,0,1}};// Target value relative to input data matrix
	double[][] inputsV = {{8,16},{986,1972},{68,136},{7,14},{98,100},{89,600},{600,1200},{98,120}};
	double[][] targetV = {{1,1,1,1,0,0,1,0},{1,1,1,1,0,0,1,0}};

	public double[][] weights = new double[0][0];			//Weight Matrix(2x2)
	public double learnRate = 0.01;							//The Learning rate of the neural network.
	public double momentum = 0.6;

	public int inputNodes = 4;								//Number of input nodes for algorithm to allocate
	public int[][] hiddenNodes = {{2,2,2,2},{2,2,2,2,2,2,2,2},{2,2},{2,2,2,2},{2,2,2},{2,2,2,2,2,2,2}}; 					//hidden layer matrix(2x2) to represent the number of layers: {},{},... and the number of nodes in each individual layer: ex) {2},{3},...  No hiddenNodes = {{1}}
	public int outputNodes = 2;								//Number of output nodes for the algorithm to allocate
	public int phase = 0;									//Current input matrix that algorithm is evaluating
	public int choose = 0;
	public double netError = 0;								//NetError
	public double[] error = {0};								//Current error of this phase

	public int trainPercent = 70;							//Amount of data to be used for training(Percentage wise)

	public ArrayList<Double> recordErrors = new ArrayList<Double>(); //temp data for error values
	public ArrayList<Double> inputLayer = new ArrayList<Double>();	//input matrix of the current layer (Useful for reading big data input information; when input matrix is not in the source code, but in a seperate file)
	public ArrayList<ArrayList<Node>> hiddenLayer = new ArrayList<ArrayList<Node>>(); //hidden layer matrix(2x2) that consists of the number of layers and the number of nodes in the individual layers. 
	public ArrayList<Node> outputLayer = new ArrayList<Node>(); // output layer matrix for storing the number of final nodes for summation and activation.
	double[] historicalgrad = {0};


	public ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> connections = new ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>();
	int i;
	int j;
	int k;
	File file = new File("NetError.txt");
	File file2 = new File("Output.txt");
	FileWriter writer;
	FileWriter writer2;


	Random rand = new Random();

	int count = 0;
	private double[] gradientS = {0.0};
	private double[] ndS = null;
	private boolean flag = false;
	double[] output;										//Final Output
	//Training T;
	//TrainSGD T;
	public static void main(String[] args)
	{
		Backpropagation main = new Backpropagation(); //Warning value considered to be null.
	}
	public Backpropagation(){
		// creates the file
		try {
			file.createNewFile();
			file.createNewFile();
			writer = new FileWriter(file); 
			writer2 = new FileWriter(file2);
			// creates a FileWriter Object

			// Writes the content to the file
			//Train();	
			writer.flush();
			writer.close();
			writer2.flush();
			writer2.close();
		}catch(IOException e){

		}


	}
	public void Start()
	{
		//Train();
		Propagate();
	}
	public void Propagate()
	{
		initInputProp();
		PassNodes();
		removeInput();
	}

	public void Mixinput()
	{
		int high = input.length-1;
		int low = 0;
		for(i=0;i<input.length;i++)
		{
			Random rand = new Random();
			int num = rand.nextInt((high-low)+1)+low;
			double[] imsi = input[num];
			input[num] = input[i];
			input[i] = imsi;
			for(j=0;j<target.length;j++)
			{
				double imsi1 = target[j][num];
				target[j][num] = target[j][i];
				target[j][i] = imsi1;
			}
		}
	}
	public void randConnection()
	{
		int layer = (1+hiddenNodes.length);

		for(i=0;i<layer;i++)
		{
			ArrayList<ArrayList<ArrayList<Double>>> layers = new ArrayList<ArrayList<ArrayList<Double>>>();
			ArrayList<ArrayList<Double>> nodes = null;
			if(i==0)
			{
				for(j=0;j<inputNodes;j++)
				{
					nodes = new ArrayList<ArrayList<Double>>();
					ArrayList<Double> connect = new ArrayList<Double>();
					connect.add(inputLayer.get(j));
					nodes.add(connect);
					layers.add(nodes);
				}
			}else
			{
				for(j=0;j<hiddenNodes[i-1].length;j++)
				{
					int Max = hiddenNodes[i-1].length;
					int Min = 3;
					Random rand = new Random();
					int ra = Min+rand.nextInt(Max-Min);

				}
			}
			layers.add(nodes);
			connections.add(layers);
		}
	}
	public int ratioPercent(int input, int percent)
	{
		int percent1 = (int)(Math.round(input*input*(percent/100)*100.0)/100.0);
		return percent1;
	}
	public void setWConstruct()
	{
		int i;
		int j;
		if(/*hiddenNodes[0][0]==1 || */hiddenNodes == null) // If there are no hidden nodes...
		{
			weights = new double[(1+0+1)-1][0]; //Set weights[i] as 1 input layer + 1 output layer - 1 layer since we only need 2 matrix layers weights[2] = {0, 1, 2} weights[2-1] = {0, 1}.
			for(i=0;i<weights.length;i++)
			{
				weights[i] = new double[(inputNodes+1)*outputNodes]; //+1 for the bias value
			}
		}else{ // If there are hidden nodes...
			weights = new double[(1+hiddenNodes.length+1)-1][0]; //Set weights[i] as 1 input layer + number of hidden Layers + 1 output layer  - 1 layer since we only need 2+number of hidden layer matrix layers weights[2] = {0, 1, 2} weights[2-1] = {0, 1}.
			for(i=0;i<weights.length;i++)
			{
				if(i==weights.length-1){
					weights[i] = new double[(hiddenNodes[i-1].length+1)*outputNodes];//+1 for the bias value
				}else{
					if(i-1>=0)
					{
						weights[i] = new double[(hiddenNodes[i-1].length+1)*hiddenNodes[i].length]; //+1 for bias value (Need to edit on this part..)
					}else{
						weights[i] = new double[(inputNodes+1)*hiddenNodes[i].length]; //+1 for bias value
					}
				}
			}

		}
		for(i=0;i<weights.length;i++){
			for(j=0;j<weights[i].length;j++)
			{
				weights[i][j] = 0.0;
			}
		}
	}
	public void setWeights()
	{

		if(weights.length <= 0) // Set the weight matrix based on number of inputNodes, outputNodes, and hiddenNodes
		{
			int i;
			int j;
			if(hiddenNodes[0][0]==1) // If there are no hidden nodes...
			{
				weights = new double[(1+0+1)-1][0]; //Set weights[i] as 1 input layer + 1 output layer - 1 layer since we only need 2 matrix layers weights[2] = {0, 1, 2} weights[2-1] = {0, 1}.
				for(i=0;i<weights.length;i++)
				{
					weights[i] = new double[(inputNodes+1)*outputNodes]; //+1 for the bias value
				}
			}else{ // If there are hidden nodes...
				weights = new double[(1+hiddenNodes.length+1)-1][0]; //Set weights[i] as 1 input layer + number of hidden Layers + 1 output layer  - 1 layer since we only need 2+number of hidden layer matrix layers weights[2] = {0, 1, 2} weights[2-1] = {0, 1}.
				for(i=0;i<weights.length;i++)
				{
					if(i==weights.length-1){
						weights[i] = new double[(hiddenNodes[i-1].length+1)*outputNodes];//+1 for the bias value
					}else{
						if(i-1>=0)
						{
							weights[i] = new double[(hiddenNodes[i-1].length+1)*hiddenNodes[i].length]; //+1 for bias value (Need to edit on this part..)
						}else{
							weights[i] = new double[(inputNodes+1)*hiddenNodes[i].length]; //+1 for bias value
						}
					}
				}

			}

			for(i=0;i<weights.length;i++){
				for(j=0;j<weights[i].length;j++)
				{
					weights[i][j] = 0.0;
				}
			}

			Random rand = new Random();
			double high = 1.000;
			double low = -1.000;
			for(i=0;i<weights.length;i++)
			{
				for(j=0;j<weights[i].length;j++)
				{
					weights[i][j] = roundDouble(high+(rand.nextDouble()*(low-high)),10);
					//weights[i][j] = high+(rand.nextDouble()*(low-high));
				}
			}
		}
	}
	public void initInputTrain() // Set the values to the inputLayer ArrayList from the input matrix
	{


		for(i=0;i<input[phase].length;i++)
			inputLayer.add(input[phase][i]);
		/*for(i=0;i<inputLayer.size();i++)
			{
				/*String s = Double.toString(inputLayer.get(i));
				try {
					//writer2.write("Contents of input: "+s+"\n");
					s = Double.toString(target[phase]);
					//writer2.write("Target: "+s+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Contents of input: " +s+"\n");
				}

			}*/
	}
	public void initInputProp() // Set the values to the inputLayer ArrayList from the input matrix
	{


		for(i=0;i<input[0].length;i++)
			inputLayer.add(input[0][i]);
		/*for(i=0;i<inputLayer.size();i++)
			{
				/*String s = Double.toString(inputLayer.get(i));
				try {
					//writer2.write("Contents of input: "+s+"\n");
					s = Double.toString(target[phase]);
					//writer2.write("Target: "+s+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Contents of input: " +s+"\n");
				}

			}*/
	}
	public void initInputVali()
	{
		for(i=0;i<inputsV[phase].length;i++)
			inputLayer.add(inputsV[phase][i]);
	}
	public void initInputTest()
	{
		for(i=0;i<input[phase].length;i++)
			inputLayer.add(input[phase][i]);
		for(i=0;i<inputLayer.size();i++)
		{
			//String s = Double.toString(inputLayer.get(i));
			/*try {
				//writer2.write("Contents of inputTest: "+s+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Contents of inputTest: " +s+"\n");
			}*/
			System.out.println("Contents of input: " +inputLayer.get(i)+"\n");
		}
	}
	public void removeInput() // Remove the input for new input feed
	{
		int i;
		for(i=0;i<input[0].length;i++)
		{
			inputLayer.remove(0);
		}
	}
	public void PassNodes()
	{
		setNodes();
		passOutput();
		//CalcError();
		//roundFinal();
		//calcNewWeight();

		//		flushOutLayer();
	}

	/*public void TrainNode()
	{
		int i;
		/*if(phase > 1)
		{
			T = new TrainSGD();
			T.Getdval(ndS);
			T.Getgrad(gradientS);
			T.setLayers(inputLayer, hiddenLayer, outputLayer);
			T.setvalues(error, learnRate, momentum);
			T.setWeights(weights);
			ndS = new double[T.num];
			T.Train(1);
			ndS = T.deltaval;
			for(i=0;i<weights.length;i++)
			{
				for(j=0;j<weights[i].length;j++)
				{
					weights[i][j] = T.weights[i][j];
				}
			}
			gradientS = new double[0];
			recordErrors.add(error);

		}else{
			T = new TrainSGD();
			T.GethistoryGrad(historicalgrad);
			T.Getgrad(gradientS);
			T.setLayers(inputLayer, hiddenLayer, outputLayer);
			T.setvalues(error, learnRate, momentum);
			T.setWeights(weights);
			if(phase == 0)
			{
				T.Train(0);
				//----
				gradientS = T.gBatch; //Erase a gradient Batch
			}
			if(!flag)
			{
				T.deltaval = new double[T.num];
				ndS = T.deltaval;
				flag = true;
			}
			T.Getdval(ndS);
			ndS = new double[T.num];
			T.Train(1);
			historicalgrad = T.historicalgrad;
			ndS = T.deltaval;
			weights = T.weights;
			gradientS = new double[0];
			double imsi = 0;
			for(i=0;i<error.length;i++)
			{
				imsi += error[i];
			}
			imsi /= error.length;
			recordErrors.add(imsi);


		//}
	}*/
	public void TestNodes()
	{
		setNodes();
		passOutput();
		//flushOutLayer(); //We are not flushing the layer temporarily
	}
	public void setNodes(){
		int i;
		int j;
		if(/*hiddenNodes[0][0]==1*/ hiddenNodes == null)
		{

			flushOutLayer();
			Node node = setNodes1(0, 0); // fix this output error. It does not make sense/
			outputLayer.add(node);

		}else{

			flushOutLayer(); // Next edit, we put parameters for flushOutLayer(); Make the function have selective removals.
			for(i=0;i<hiddenNodes.length;i++)// suspicion error
			{
				ArrayList<Node> temp = new ArrayList<Node>(); //Marker for line 313
				for(j=0;j<hiddenNodes[i].length;j++)
				{
					/*Node node = what node..;
						temp.add(node);*/
					Node node = setNodes1(i,j);
					temp.add(node);
				}
				hiddenLayer.add(temp);
			}
			for(i=0;i<outputNodes;i++)
			{
				Node node = setNodes1(hiddenNodes.length, i);
				outputLayer.add(node);
			}

		}
	}
	public Node setNodes1(int curLayer, int curNode){
		int i;
		double[] tempIN = null;
		double[] tempW = null;
		Node node = null;
		//pre-set weights and output values 
		if(curLayer==0) //If it is the first hiddenLayer
		{
			tempIN = new double[inputLayer.size()+1];
			tempW = new double[inputLayer.size()+1];
			//double[] tempW = new double[weights[0].length];
			for(i=0;i<=inputLayer.size();i++)
			{
				if(i==inputLayer.size())
				{
					tempIN[i] = 1.0;
					tempW[i] = weights[curLayer][weights[curLayer].length-1-curNode];
				}else{
					tempIN[i] = inputLayer.get(i);
					tempW[i] = weights[curLayer][(i*hiddenNodes[curLayer].length)+curNode];
				}

			}
			node = new Node(tempIN, tempW);
		}else if(curLayer > 0 && curLayer<hiddenNodes.length-1){
			tempIN = new double[hiddenLayer.get(curLayer-1).size()+1]; // curLayer should start at 1; then -1 to make hiddenLayer[0]
			tempW = new double[hiddenLayer.get(curLayer-1).size()+1]; // So far correct
			for(i=0;i<=hiddenLayer.get(curLayer-1).size();i++) // -1 is because of the input.
			{
				if(i==hiddenLayer.get(curLayer-1).size())
				{
					tempIN[i] = 1.0;
					tempW[i] = weights[curLayer][weights[curLayer].length-1-curNode]; //???
				}else{
					tempIN[i] = (double)hiddenLayer.get(curLayer-1).get(i).eval;
					tempW[i] = weights[curLayer][(i*hiddenNodes[curLayer].length)+curNode]; //There is error whilst changing Hidden Node
				}

			}
			node = new Node(tempIN, tempW);

		}else{ //I am very very confused about this..
			tempIN = new double[hiddenLayer.get(curLayer-1).size()+1]; // -1 is for the input.
			tempW = new double[weights[curLayer].length];
			for(i=0;i<=hiddenLayer.get(curLayer-1).size();i++) // Stuck on 359 marker. Bias value
			{

				if(i==hiddenLayer.get(curLayer-1).size())
				{
					tempIN[i] = 1.0;
					tempW[i] = weights[curLayer][i*outputLayer.size()+curNode];
				}else{
					tempIN[i] = (double)hiddenLayer.get(curLayer-1).get(i).eval;
					tempW[i] = weights[curLayer][i*outputLayer.size()+curNode]; // Based on 1 outputvalue.
				}
			}
			node = new Node(tempIN, tempW);
		}
		return node;
	}
	public void passOutput()
	{
		int i;
		output = new double[outputLayer.size()];
		for(i=0;i<output.length;i++)
		{
			output[i] = outputLayer.get(i).eval;
		}

	}
	public void flushOutLayer() //Flushing created output and hidden layers for next evaluation
	{
		int i;
		int size = outputLayer.size();
		for(i=0;i<size;i++)
		{
			outputLayer.remove(0);
		}
		size = hiddenLayer.size();
		for(i=0;i<size;i++)
		{
			hiddenLayer.remove(0);
		}
	}
	public void CalcError()
	{
		double[] error;
		error = new double[output.length];
		for(i=0;i<output.length;i++)
		{
			error[i] =  output[i] - target[i][phase];
		}
		//error = (Math.round(error)*100.000)/100.000;
		this.error = error;
	}
	public void CalcErrorV()
	{
		double[] error;
		error = new double[output.length];
		for(i=0;i<output.length;i++)
		{
			error[i] =  output[i] - targetV[i][phase];
		}
		//error = (Math.round(error)*100.000)/100.000;
		this.error = error;
	}
	public void CalcNetError() //We calculate the Neterror by retreiving all the recorded errors from: ArrayList<Double> recordErrors
	{
		int i;
		double net = 0;
		for(i=0;i<recordErrors.size();i++)
		{
			net += ((recordErrors.get(i)*recordErrors.get(i)));
		}
		netError = (net/2);
		int size = recordErrors.size();
		for(i=0;i<size;i++)
		{
			recordErrors.remove(0);
		}

	}

	//We need gradient descent algorithm
	// + Basic optimization algorithm
	public int roundInt(double val, double restrict)
	{
		if(val>=restrict)
		{
			return (int)restrict;
		}else{
			val = (Math.round(val)*100.0)/100.0;
			return (int)val;
		}
	}
	public double roundDouble(double val, double restrict)
	{
		if(val>=restrict)
		{
			return (double)restrict;
		}else{
			val = (double)(Math.round(val*1000d))/1000d;
			return val;
		}
	}



}
