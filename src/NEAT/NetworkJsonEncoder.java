package NEAT;
import com.google.gson.*;
import org.json.*;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NetworkJsonEncoder {

    public File jsonEncode = new File("Network.json");
    public FileWriter encodeWriter;


    public NetworkJsonEncoder(){
        try {
            jsonEncode.createNewFile();
            encodeWriter = new FileWriter(jsonEncode);
        }catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    public void endProcess()
    {
        try {
            encodeWriter.flush();
            encodeWriter.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }


    public boolean exportNetwork(Genome genome)
    {
        ArrayList<ArrayList<Double>> encoder = new ArrayList<ArrayList<Double>>();
        for(ConnectionGene connectionGene: genome.getConnectionGenes())
        {
            ArrayList<Double> tuple = new ArrayList<Double>();
            tuple.add((double)connectionGene.getOut_ID());
            tuple.add((double)connectionGene.getIn_ID());
            tuple.add(connectionGene.getWeight());
            encoder.add(tuple);
        }
        String json = new Gson().toJson(encoder);
        try {
            encodeWriter.write(json);
            encodeWriter.flush();
            return true;
        }catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

    }

}
