package dm.clustering.utils;


import java.util.ArrayList;

import dm.core.Instance;

public class Normalizer {
	
	public ArrayList<Instance> normalize(ArrayList<Instance> Instances)
	{
		/*
		 * Create the array containing the mean and stdev for each feature
		 */
		int nfeat = Instances.get(0).getDobFeatures().size();
		
		double [][] arguments = new double[nfeat][2];
		double mean = 0.0;
		double stdev = 0.0;
		for (int i = 0; i < Instances.get(0).numFeatures();i++)
		{
			
			mean = mean(Instances, i);
			stdev= stdev(Instances, i, mean, true);
			arguments[i][0]= mean;
			arguments[i][1]= stdev;
		}
		double att=0.0;		
		/*
		 * Change each attribute of each instance to the normalized one 
		 */
		
		
		
		
		for (int i = 0; i < Instances.size(); i++)
		{			
			for (int j = 0; j < nfeat; j++)
			{
				// X - mean / stdev		
				att=Instances.get(i).getAtt(j);
				
				System.out.println("Jota: "+j+" Atributo: " + att + " media: "+ arguments[j][0]+ " desv: "+arguments[j][1]);
				
				att = (Instances.get(i).getAtt(j)-arguments[j][0])/ (arguments[j][1]);
				
				System.out.println("Atributo despues: "+att);
				
				Instances.get(i).setAtt(j,att);
			}
		}		
		return Instances;
	}
	
	/**
	 * 
	 * @param Instances
	 * @param index
	 * @return mean of the specified attribute of the dataset
	 */

	
	public double mean(ArrayList<Instance> Instances, int index)
	{
		double res = 0.0;
	
		for (int i = 0; i < Instances.size(); i++)
		{
			res=res+Instances.get(i).getAtt(index);
		}
		res = res/Instances.size();
		
		return res;
	}
	
	/**
	 * 
	 * @param Instances
	 * @param index index of the attribute which stdev has to be calculated
	 * @param mean 
	 * @param useMean specifies if the mean has been calculated or is not valid
	 * @return the stdev of the specified attribute in a dataset
	 */
	public double stdev(ArrayList<Instance> Instances, int index, double mean, boolean useMean)
	{
		double res = 0.0;		
		if (!useMean)
		{
			mean = mean(Instances, index);
		}
		
		for (int i = 0; i < Instances.size(); i++)
		{
			res = res + (Math.pow(Instances.get(i).getAtt(index)-mean, 2));
			
		}
		res = res /Instances.size();
		double rdo = Math.sqrt(res);
		return rdo;
	}
}
