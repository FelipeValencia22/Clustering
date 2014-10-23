package dm.clustering.utils;

import java.util.ArrayList;

public class Statistics 
{
	private static Statistics miStatistics=new Statistics();
	
	private Statistics(){}
	
	public static Statistics getMiStatistics()
	{
		return miStatistics;
	}
	
	/**
	 * 
	 * @param index
	 * @return mean of the values
	 */

	
	public double mean(ArrayList<Double> values)
	{
		double res = 0.0;	
		for (int i = 0; i < values.size(); i++)
		{
			res=res+values.get(i);
		}
		res = res/values.size();
		
		return res;
	}
	
	/**
	 * 
	 * @param values
	 * @param mean 
	 * @param useMean specifies if the mean has been calculated or is not valid
	 * @return the stdev of the specified values.
	 */
	public double stdev(ArrayList<Double> values,double mean, boolean useMean)
	{
		double res = 0.0;		
		if (!useMean)
		{
			mean = mean(values);
		}
		
		for (int i = 0; i < values.size(); i++)
		{
			res = res + (Math.pow(values.get(i), 2));
			
		}
		res = res /values.size();
		double rdo = Math.sqrt(res);
		return rdo;
	}

}
