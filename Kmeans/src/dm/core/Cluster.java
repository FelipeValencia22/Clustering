/**
 * 
 */
package dm.core;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *  
 *
 */
public class Cluster {

	
	private ArrayList<Instance> instances;
	
	
	public Cluster() {
		instances = new ArrayList<Instance>();
	}
	
	public ArrayList<Instance> getListaInstances(){
		return this.instances;
	}

	/**
	 * 
	 * @return the instances Iterator<Instance> 
	 */
	public Iterator<Instance> getInstances(){
		return this.getListaInstances().iterator();
	}
	
	/**
	 * Delete instance
	 * @param ins
	 * @return true if the instance is removed, otherwise false.
	 */
	public boolean removeInstance(Instance ins){
		return this.getListaInstances().remove(ins);
	}
	/**
	 * Add instance
	 * @param ins
	 * @return true if the instance is added, otherwise false.
	 */
	public boolean addInstance(Instance ins){
		 return this.getListaInstances().add(ins);
	}

	/**
	 * 
	 * @return cluster's average vector.
	 */
	public Instance calcCentroid()
	{
		Instance centroid = new Instance();
		int numInstances = this.getListaInstances().size();
		int numFeatures =0;
		if(!getInstances().hasNext())numFeatures = this.getInstances().next().getDobFeatures().size(); 
		double[] codeword = new double[numFeatures];
		
		//Initialize array for calculus
		for (int i = 0; i < numFeatures; i++) 
		{
			codeword[i] = (this.getListaInstances().get(0).getDobFeatures().get(i))/numInstances;
		}
		
		//Calculate centroid
		for(int j=1;j<numInstances;j++)
		{	
			Instance ins = this.getListaInstances().get(j);
			for(int i=0;i<numFeatures;i++)
			{
				double feature = ins.getDobFeatures().get(i);
				codeword[i] = codeword[i]+feature/numInstances;
			}
		}
		
		//Return centroid
		for(int s=0;s<codeword.length;s++)
		{
			centroid.getDobFeatures().add(codeword[s]);
		}
		return centroid;	
		}
	/**
	 * Clear the List of instances.
	 */
	public void reset() {
			this.getListaInstances().clear();		
	}
}
