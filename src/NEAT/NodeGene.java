package NEAT;

public class NodeGene {
	private int ID;
	private int Layer;
	public boolean isOutput = false;
	public boolean isInput = false;
	
	public int getID()
	{
		return ID;
	}
	public int getLayer()
	{
		return Layer;
	}
	
	public void setID(int id)
	{
		ID = id;
	}
	public void setLayer(int layer)
	{
		Layer = layer;
	}
	
	public void incrementLayer()
	{
		++Layer;
	}
}
