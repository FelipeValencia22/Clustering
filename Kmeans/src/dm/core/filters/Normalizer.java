package dm.core.filters;


import java.util.ArrayList;

import dm.core.Instance;

public class Normalizer {
	
	/**
	 * 
	 * @param Instances
	 * @return
	 */
	public ArrayList<Instance> normalize(ArrayList<Instance> Instances)
	{
		/*
		 * Create the array containing the max (args[j][0]) and min (args[j][1]) values for each feature
		 */
		int nfeat = Instances.get(0).getDobFeatures().size();
		double [][] arguments = new double[nfeat][2];
		double min = 0.0;
		double max = 0.0;
		for (int j = 0; j < nfeat; j++)
		{
			max = Instances.get(0).getAtt(j);
			min = Instances.get(0).getAtt(j);
			for (int i = 0; i < Instances.size();i++)
			{	
				if (Instances.get(i).getAtt(j)>max)
				{
					max = Instances.get(i).getAtt(j);
				}
				else if (Instances.get(i).getAtt(j)<min)
				{
					min = Instances.get(i).getAtt(j);
				}
			}
			arguments[j][0] = max;
			arguments[j][1] = min;
			
		}
		double att=0.0;		
		/*
		 * Change each attribute of each instance to the normalized one 
		 */
		
		
		
		
		for (int i = 0; i < Instances.size(); i++)
		{			
			for (int j = 0; j < nfeat; j++)
			{
				att=Instances.get(i).getAtt(j);
				att = (att - arguments[j][1])/(arguments[j][0]-arguments[j][1]);
				Instances.get(i).setAtt(j,att);
			}
		}		
		return Instances;
	}
	
	
	public boolean shouldNormalize(ArrayList<Instance> Instances)
	{
		boolean res = false;
		int nfeat = Instances.get(0).getDobFeatures().size();
		double [] arguments = new double[nfeat];
		
		// arguments holds the array of each atts stdev
		for (int i = 0; i < nfeat; i++)
		{
			arguments[i] = stdev(Instances, i, 0, false);
		}
		
		// mean of the stdevs
		double mean = 0.0;
		for (int i = 0; i < arguments.length; i++)
		{
			mean = mean + arguments[i];
		}
		mean = mean / arguments.length;
		
		// stdev of the stdevs
		double stdev = 0.0;
		for (int i = 0; i< arguments.length; i++)
		{
			stdev = stdev + (Math.pow(arguments[i]-mean, 2));
		}
		stdev = stdev / arguments.length;
		
		
		//coefficient of variation
		double CoV = stdev / mean;
		
		if (CoV>0.1)
		{
			res = true;
		}
		
		/*
		 * calculate the mean of distances between att values
		 * i: indes of each att
		 * j: index of each instance
		 
		double[] params = new double[nfeat];
		double difAcc=0.0;
		for (int i = 0; i < nfeat; i++)
		{
			for (int j = 0; j<Instances.size(); j++)
			{
				for (int k = j; k<Instances.size(); k++)
				{
					difAcc= Math.abs(Instances.get(j).getAtt(i)-Instances.get(k).getAtt(i));
				}
			}
			difAcc=difAcc/Instances.size();
			params[i]=difAcc;
			difAcc=0.0;
		}
		
		*/
		return res;
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
