package dm.core;

import java.util.ArrayList;

public class Instance 
{
	private String id;
	private ArrayList<Double> doubleFeatures;
	private ArrayList<String> stringFeatures;
	private ArrayList<Enum> enumFeatures;

	public Instance(){
		this.doubleFeatures= new ArrayList<Double>();
		this.enumFeatures= new ArrayList<Enum>();
		this.stringFeatures = new ArrayList<String>();
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
}
