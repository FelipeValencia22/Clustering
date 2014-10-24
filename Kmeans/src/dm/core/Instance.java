package dm.core;

import java.util.ArrayList;

import dm.clustering.utils.*;

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
	public void setAtt(int j, double att)
	{		
		doubleFeatures.set(j, att);
	}
	
	public double getAtt(int index) {
		return this.getDobFeatures().get(index);
	}

	/**
	 * @pre x must be a instance of the same dataset
	 * @param x Instance to be compared with
	 * @return true if all attributes have the same values, false if not.
	 */

	public boolean equals(Instance x,double ratioMax)
	{
		int i = 0;
		double ratioMean=0;
		ArrayList<Double>ratios=new ArrayList<Double>();
			while (i<this.doubleFeatures.size()&&i<x.doubleFeatures.size())
			{
				double att=this.doubleFeatures.get(i);
				double attX=x.doubleFeatures.get(i);
				if(att>attX)
				{
					double ratio=attX/att;
					ratios.add(ratio);
				}
				else if(att<attX)
				{
					double ratio=att/attX;
					ratios.add(ratio);
				}
				else
				{
					double ratio=1.0;
					ratios.add(ratio);
				}
				i++;
			}		
			ratioMean = Statistics.getMiStatistics().mean(ratios);
		System.out.println(ratioMean);
		return ratioMean>=ratioMax;
	}	
	
}
