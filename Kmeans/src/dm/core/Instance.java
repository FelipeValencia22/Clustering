package dm.core;

import java.util.ArrayList;

public class Instance 
{
	private String id;
	private ArrayList<Double> doubleFeatures;
	
	private ArrayList<String> features;

	public Instance(){
		this.doubleFeatures= new ArrayList<Double>();
		this.features = new ArrayList<String>();
	}
	public String getId() 
	{
		return id;
	}
	public void setId(String id) 
	{
		this.id = id;
	}
	
	public void addDobFeature(Double feature){
		doubleFeatures.add(feature);
	}
	
	/**
	 * pruebas
	 * @param feature
	 */
	public void addFeature(String feature){
		features.add(feature);
	}
	
	public int numFeatures(){
		return this.features.size();
	}
	public ArrayList<Double> getDobFeatures() {
		return this.doubleFeatures;
		
	}
	
	public boolean equals(Instance x)
	{
		boolean rdo = false;
		
		//TODO ssls
		
		return rdo;
	}
	
	
	public boolean compareCodeBooks(ArrayList<Instance> a, ArrayList<Instance> b)
	{
		boolean rdo = true;
		int i = 0, j=0;
		
		while (i<a.size() && rdo)
		{
			while (j<b.size() && rdo)
			{
				if (!a.get(i).equals(b.get(j)))
					rdo = false;
			}
		}
		return rdo;
	}

}
