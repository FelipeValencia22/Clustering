import java.util.ArrayList;

import dm.core.Cluster;
import dm.core.Instance;


public class Evaluation 
{
	public Evaluation(){
		
	}
	
	public double silhouetteCoefficient(Cluster[] clusters){
		ArrayList<Double> intraDissims=new ArrayList<Double>();
		ArrayList<Double> interDissims=new ArrayList<Double>();
		for(Cluster cluster:clusters)
		{
			double intra =getIntraDissimilarity(cluster);
			intraDissims.add(intra);
		}
		for(Cluster cluster:clusters)
		{
			double inter =getInterDissimilarity(cluster);
			intraDissims.add(inter);
		}
		return 0.0;
	}

	private double getInterDissimilarity(Cluster cluster) {
		// TODO Auto-generated method stub
		return 0;
	}

	private double getIntraDissimilarity(Cluster cluster) {
		// TODO Auto-generated method stub
		return 0;
	}	
}
