package dm.clustering.kmeans;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYCoordinate;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import dm.clustering.utils.CSVDataLoader;
import dm.clustering.utils.InputHandler;
import dm.clustering.utils.Minkowski;
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

		
		/*while(!condParada)
		{*/
		for(int numIter=0;numIter<itera;numIter++){
			//TODO			
			for (int p = 0; p<clusters.length;p++)
			{
				clusters[p].reset();
			}
			
			for (int i=0;i<instances.size();i++)
			{		
				Double dist = Minkowski.getMinkowski()
						.calculateDistance(instances.get(i)
								, codebook.get(0), 2);
				int codewordIndex = 0;
				clusters[0].addInstance(instances.get(i));
				for (int j=0;j<k; j++)
				{
					Double distAux = Minkowski.getMinkowski().calculateDistance(instances.get(i), codebook.get(j), 2);
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
						B[i][j]=0;
						//clusters[j].removeInstance(instances.get(i));
						
					}
				}
				for (int s=0; s< clusters.length;s++)
				{
					codebookAux = (ArrayList<Instance>)codebook.clone();
					if(clusters[s].getInstances().hasNext())codebook.set(s, clusters[s].calcCentroid());					
					if (compareCodeBooks(codebookAux,codebook))
					{
						//TODO apañar bien donde meter la función comparar
						condParada = true;
					}					
				}
				
			}		
			/*}*/
		}	
		
		/*JFreeChart chart = ChartFactory.createScatterPlot(
				"Membership", // chart title
				"Instances", // x axis label
				"Clusters", // y axis label
				createDataset(B), // data  ***-----PROBLEM------***
				PlotOrientation.VERTICAL,
				true, // include legend
				true, // tooltips
				false // urls
				);
		XYPlot plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)plot.getRenderer();
		renderer.setSeriesPaint(0, randomColor());
		// create and display a frame...
		ChartFrame frame = new ChartFrame("First", chart);
		frame.pack();
		frame.setVisible(true);*/
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

	public static boolean compareCodeBooks(ArrayList<Instance> a, ArrayList<Instance> b)
	{
		int i = 0, j=0;
		
		while (i<a.size())
		{
			while (j<b.size())
			{
				if (!a.get(i).equals(b.get(j)))
				{
					return false;
				}
				j++;
			}
			i++;
		}
		return true;	
	}
	
	/**
	 * 
	 * @param B
	 * @return dataset for ploting
	 */
	private static XYDataset createDataset(int[][] B) {
	    XYSeriesCollection result = new XYSeriesCollection();
	    XYSeries series = new XYSeries("Membership");
	    for (int i = 0; i < B.length; i++) {
	    	System.out.println(B.length);
	    	for(int j=0;j<B[i].length;j++){
	    		if(B[i][j]==1)
	    		{
	    			double x = i;
	    			double y = j;
	    			series.add(x, y);
	    		}
	    	}
	    }
	    result.addSeries(series);
	    return result;
	}
	
	/**
	 * 
	 * @return
	 */
	private static Color randomColor(){
		Random rand = new Random();
		
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		
		return new Color(r,g,b);
	}
}
