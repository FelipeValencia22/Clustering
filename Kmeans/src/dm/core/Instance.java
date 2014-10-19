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
		return this.doubleFeatures.size();
	}
	public ArrayList<Double> getDobFeatures() {
		return this.doubleFeatures;

	}

	/**
	 * @pre x must be a instance of the same dataset
	 * @param x Instance to be compared with
	 * @return true if all attributes have the same values, false if not.
	 */

	public boolean equals(Instance x)
	{
		int i = 0;
		while (i<this.doubleFeatures.size()&&i<x.doubleFeatures.size())
		{
			if(this.doubleFeatures.get(i)!=x.doubleFeatures.get(i))
			{
				return false;
			}
			i++;
		}		
		return true;
	}
	public double getAtt(int index) {

		return this.getDobFeatures().get(index);
	}
	public void setAtt(int j, double att)
	{
		
		doubleFeatures.set(j, att);

	}
}
