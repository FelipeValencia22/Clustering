package dm.clustering.kmeans;

import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.FastScatterPlot;
import dm.clustering.utils.CSVDataLoader;
import dm.clustering.utils.InputHandler;
import dm.clustering.utils.Minkowski;
import dm.core.Cluster;
import dm.core.Instance;


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
		float[][] B = matrixMemberShipInitialize(nrow, nCol);

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

		while(!condParada)
		{
			for (int i=0;i<instances.size();i++)
			{
				Double dist = Minkowski.getMinkowski()
						.calculateDistance(instances.get(i)
								, codebook.get(0), 2);
				int codewordIndex = 0;

				for (int j = 0; j<k; j++)
				{
					Double distAux = Minkowski.getMinkowski().calculateDistance(instances.get(i), codebook.get(j), 2);
					System.out.println(distAux);
					if(dist>=distAux-difference)
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
						clusters[j].removeInstance(instances.get(i));
					}
				}
			}					

			for (int s=0; s< clusters.length;s++)
			{
				codebookAux = (ArrayList<Instance>)codebook.clone();
				codebook.set(s, clusters[s].calcCentroid());
				if (compareCodeBooks(codebookAux,codebook))
				{
					//TODO apañar bien donde meter la función comparar
					condParada = true;
				}					
			}			
		}	
		//TODO plot exit and data exit.
		final FastScatterPlotDemo clustersChar = new FastScatterPlotDemo("Fast Scatter Plot Demo",B);
        clustersChar.pack();
        clustersChar.setVisible(true);
		//TODO test and evaluation
	}

	/**
	 * @param k
	 * @param instances
	 */
	private static ArrayList<Instance> startCodebook(int k, ArrayList<Instance> instances) {
		ArrayList<Instance> codebook = new ArrayList<Instance>();
		for(int i=0;i<k;i++)
		{
			Random rand = new Random(100);
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
				System.out.println(B[j][s]);
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
		boolean rdo = true, aux = false;
		int i = 0, j=0;

		while (i<a.size() && rdo)
		{
			while (j<b.size())
			{
				if (a.get(i).equals(b.get(j)))
					aux = true;
				j++;
			}

			if (!aux)
				rdo=false;
			else
				aux=false;
			i++;
		}
		return rdo;
	}
	public static class FastScatterPlotDemo extends JFrame {

	    /** A constant for the number of items in the sample dataset. */

	    /** The data. */
	    private float[][] data;

	    /**
	     * Creates a new fast scatter plot.
	     *
	     * @param title  the frame title.
	     */
	    public FastScatterPlotDemo(final String title,float[][] pData) {

	        super(title);
	        data = pData;
	        final NumberAxis domainAxis = new NumberAxis("X");
	        domainAxis.setAutoRangeIncludesZero(false);
	        final NumberAxis rangeAxis = new NumberAxis("Y");
	        rangeAxis.setAutoRangeIncludesZero(false);
	        final FastScatterPlot plot = new FastScatterPlot(this.data, domainAxis, rangeAxis);
	        final JFreeChart chart = new JFreeChart("Fast Scatter Plot", plot);

	        // force aliasing of the rendered content..
	        chart.getRenderingHints().put
	            (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	        final ChartPanel panel = new ChartPanel(chart, true);
	        panel.setPreferredSize(new java.awt.Dimension(500, 270));
	  //      panel.setHorizontalZoom(true);
	    //    panel.setVerticalZoom(true);
	        panel.setMinimumDrawHeight(10);
	        panel.setMaximumDrawHeight(2000);
	        panel.setMinimumDrawWidth(20);
	        panel.setMaximumDrawWidth(2000);	        
	        setContentPane(panel);

	    }
	}
}
