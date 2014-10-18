package dm.clustering.kmeans;

import java.util.ArrayList;
import java.util.Random;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import dm.clustering.utils.CSVDataLoader;
import dm.clustering.utils.InputHandler;
import dm.clustering.utils.Minkowski;
import dm.clustering.utils.Normalizer;
import dm.core.Cluster;
import dm.core.Instance;
import dm.plots.Plot;



public class Kmeans {



	public static void main(String[] args) {
		String LOG_TAG = Kmeans.class.getSimpleName();

		//Get configuration from the configuration file.
		InputHandler.getMiHandler().loadArgs("config/kmeans.conf");

		//Let k be the number of clusters to partition the data set
		//TODO k<<num instances and other args
		int k = InputHandler.getMiHandler().getK();

		//Let X = {x_{1},x_{2}, ..., x_{n}} be the data set to be analyzed
		//TODO another data formats.

		ArrayList<Instance> instances;
		instances = CSVDataLoader.getMiLoader()
				.loadNumericData(InputHandler
						.getMiHandler()
						.getDataPath(), 2);

		//TODO Normalize Data
		Normalizer norm = new Normalizer();
		
		instances = norm.normalize(instances);
		//Let seed be the seed for the random number to get the codebook.
		//Let M = {m_{1}, m_{2}, ..., m_{k}} be the code-book associated to the clusters. Random instances.
		ArrayList<Instance> codebook = startCodebook(k, instances);
		ArrayList<Instance> codebookAux;

		//B  membership matrix.
		int nrow = instances.size();
		int nCol = k;
		//TODO select initialize matrix or centroid.
		//float[][] B = matrixMemberShipInitialize(nrow, nCol);
		int[][] B = new int[instances.size()][k];
		// Instance k clusters
		Cluster[] clusters = new Cluster[k];
		for(int index=0;index<k;index++){
			clusters[index]=new Cluster();
		}
		//Let distance exponent
		double distExp = InputHandler.getMiHandler().getExp(); 
		boolean condParada = false;

		//Iterations		
		int itera = InputHandler.getMiHandler().getIterations();		
		if(itera<=0)
		{
			itera=instances.size();
		}

		//Disim
		double difference = InputHandler.getMiHandler().getDiff();
		if(difference<0.0)
		{
			difference=0.0;
		}

		
		while(!condParada)
		{
		for(int numIter=0;numIter<itera;numIter++){
			for (int p = 0; p<clusters.length;p++)
			{
				clusters[p].reset();
			}
			
			for (int i=0;i<instances.size();i++)
			{		
				Double dist = Minkowski.getMinkowski()
						.calculateDistance(instances.get(i)
								, codebook.get(0), distExp);
				int codewordIndex = 0;
				clusters[0].addInstance(instances.get(i));
				for (int j=0;j<k; j++)
				{
					Double distAux = Minkowski.getMinkowski().calculateDistance(instances.get(i), codebook.get(j), distExp);
					if(dist>distAux-difference)
					{
						dist = distAux;
						//update Matrix membership
						B[i][j]=1;
						B[i][codewordIndex]=0;
						//update Cluster list
						clusters[codewordIndex].removeInstance(instances.get(i));
						codewordIndex=j;
						clusters[j].addInstance(instances.get(i));						
					}
					else
					{
						//B[i][j]=0;
						//clusters[j].removeInstance(instances.get(i));
						
					}
				}
				for (int s=0; s< clusters.length;s++)
				{
					codebookAux = (ArrayList<Instance>)codebook.clone();
					if(clusters[s].getInstances().hasNext())codebook.set(s, clusters[s].calcCentroid());					
					if (compareCodeBooks(codebookAux,codebook,distExp))
					{
						condParada = true;
					}					
				}
				
			}		
			}
		}	
		
		Plot.getMiPlot().setMatrixMembership(B);
		JFreeChart scatter = Plot.getMiPlot().plotting("Memberships", "Clusters", "Instances");
		// create and display a frame...
				ChartFrame frame = new ChartFrame("First", scatter);
				frame.pack();
				frame.setVisible(true);

		//TODO test and evaluation
		 	
		
		for(int j=0;j<clusters.length;j++){
			System.out.println("CLUSTER: "+j);
			System.out.println("---------------------------------------");
			System.out.println("Número de instancias en el cluster: "+clusters[j].getListaInstances().size());
			System.out.println("===========================================================================");

		}
	}

	/**
	 * @param k
	 * @param instances
	 */
	private static ArrayList<Instance> startCodebook(int k, ArrayList<Instance> instances) {
		ArrayList<Instance> codebook = new ArrayList<Instance>();
		for(int i=0;i<k;i++)
		{
			Random rand = new Random();
			int cWordIndex = rand.nextInt(instances.size());
			codebook.add(instances.get(cWordIndex));
		}
		return codebook;
	}

	/**
	 * @param nrow
	 * @param nCol
	 */
	private static float[][]  matrixMemberShipInitialize(int nrow, int nCol) {
		float[][] B = new float[nrow][nCol];
		for(int j=0;j<nrow;j++)
		{
			for (int s=0;s<nCol;s++)
			{
				Random rand = new Random();				
				B[j][s]= rand.nextInt(2);
			}
		}
		return B;
	}

	/**
	 * @pre Equal sized codebooks (Instance Arraylists)
	 * @param a Codebook 1
	 * @param b Codebook 2
	 * @return true if both codebooks do have the same codewords (not taking into account
	 *  the order), false if not.
	 */

	public static boolean compareCodeBooks(ArrayList<Instance> a, ArrayList<Instance> b, double p)
	{
		int i = 0, j=0;
		
		while (i<a.size())
		{
			while (j<b.size())
			{
				double dist = Minkowski.getMinkowski().calculateDistance(a.get(i), b.get(i), p);
				if (!a.get(i).equals(b.get(j)) && dist>0.10)
				{
					return false;
				}
				j++;
			}
			i++;
		}
		return true;	
	}
}
