package dm.clustering.evaluation;
import java.util.ArrayList;

import dm.clustering.utils.Minkowski;
import dm.clustering.utils.Statistics;
import dm.core.Cluster;
import dm.core.Instance;


public class Evaluation 
{
	public Evaluation(){
		
	}
	
	/**
	 * @param exponent for the minkowski distance
	 * @param clusters
	 * @return the mean of silhouette Coefficient 
	 */
	public double silhouetteCoefficient(Cluster[] clusters, double exp){
		ArrayList<Double> sForClusters=new ArrayList<Double>();
		for(Cluster clust:clusters)
		{
			if(!clust.getListaInstances().isEmpty())
			{
				double s=silhouetteCoefficientForCluster(clust, clusters, exp);
				sForClusters.add(s);
			}		
		}
		double rdo=Statistics.getMiStatistics().mean(sForClusters);
		return rdo;
	}
	
	/**
	 * 
	 * @param cluster
	 * @param clusters
	 * @param exp
	 * @return
	 */
	private double silhouetteCoefficientForCluster(Cluster cluster,Cluster[] clusters,double exp){
		ArrayList<Double> coefficients= new ArrayList<Double>();
		for(Instance ins:cluster.getListaInstances())
		{
			double s = silhouetteCoefficientForInstance(ins,cluster,clusters, exp);
			coefficients.add(s);
		}
		return Statistics.getMiStatistics().mean(coefficients);
	}
	/**
	 * 
	 * @param ins
	 * @return return the s coefficient for the ins
	 */
	private double silhouetteCoefficientForInstance(Instance ins,Cluster cluster,Cluster[] clusters,double exp) {
		ArrayList<Double>intraDissims= new ArrayList<Double>();
		ArrayList<Double>interDissims= new ArrayList<Double>();
		double intra =getIntraDissimilarity(ins,cluster,exp);
		intraDissims.add(intra);
		double inter =getInterDissimilarity(ins,cluster,clusters,exp);
		interDissims.add(inter);	
		if(intra<inter)
		{
			return 1-(intra/inter);
		}
		else if(intra > inter)
		{
			return (inter/intra)-1;
		}
		else
		{
			return 0;
		}
		//return  (inter-intra)/Math.max(intra, inter);
	}

	/**
	 * 
	 * @param ins
	 * @param cluster
	 * @param clusters
	 * @param exp
	 * @return the lowest mean of the distance between the ins and the another instance in not same cluster. 
	 * The cluster with this lowest average dissimilarity is said to be the "neighbouring cluster" of ins because it is the next best fit cluster for point ins.
	 */
	private double getInterDissimilarity(Instance ins,Cluster cluster,Cluster[] clusters,double exp) {
		double mean = Double.MAX_VALUE,meanAux;
		for(Cluster clust:clusters)
		{
			ArrayList<Double>distances=new ArrayList<Double>();
			if(clust!=cluster && !clust.getListaInstances().isEmpty())
			{
				for(Instance i:clust.getListaInstances())
				{
					//System.out.println(ins==i);
					distances.add(Minkowski.getMinkowski().calculateDistance(ins, i, exp));
				}
				meanAux=Statistics.getMiStatistics().mean(distances);
				if(mean>meanAux)mean=meanAux;
			}
		}
		return mean;
	}
	/**
	 * 
	 * @param ins
	 * @param cluster
	 * @param exp
	 * @return the mean of the distance between ins and the another instances in the clusters.
	 */
	private double getIntraDissimilarity(Instance ins,Cluster cluster,double exp) {
		ArrayList<Double>distances=new ArrayList<Double>();
		for(Instance i:cluster.getListaInstances())
		{
			if(i!=ins)
			{
				double dist=Minkowski.getMinkowski().calculateDistance(ins, i, exp);
				distances.add(dist);
			}
		}
		return Statistics.getMiStatistics().mean(distances);
	}	
}
