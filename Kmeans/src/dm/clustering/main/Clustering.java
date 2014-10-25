package dm.clustering.main;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;

import dm.clustering.evaluation.Evaluation;
import dm.clustering.io.InputHandler;
import dm.clustering.kmeans.Kmeans;
import dm.core.Cluster;
import dm.core.Instance;
import dm.core.distance.Minkowski;
import dm.core.filters.Normalizer;
import dm.plots.Plot;

public class Clustering {

	public static void main(String[] args) {
		String LOG_TAG = Kmeans.class.getSimpleName();
		
		File data = new File("data");
		if(!data.exists())
		{
			data.mkdirs();
		}
		
		File config = new File("config");
		if(!config.exists())
		{
			config.mkdirs();
		}
		File rdo = new File("Resultados");
		if(!rdo.exists())
		{
			rdo.mkdirs();
		}

		//Let kmeans object
		Kmeans kmeans= new Kmeans();

		//Get configuration from the configuration file.
		InputHandler.getMiHandler().loadArgs("config/kmeans.conf");

		//Let k be the number of clusters to partition the data set
		//TODO k<<num instances and other args
		int k = InputHandler.getMiHandler().getK();

		//Let X = {x_{1},x_{2}, ..., x_{n}} be the data set to be analyzed

		ArrayList<Instance> instances = null;
		String extension=InputHandler.getMiHandler().getFileExtension();
		instances = kmeans.loadInstances(extension);

		//Normalize data
		if(InputHandler.getMiHandler().getNormalize()==1)
		{
			Normalizer norm = new Normalizer();
			try 
			{
				instances = norm.normalize(instances);
			} 
			catch (Exception e) 
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE,"ERROR al normalizar las instancias");
			}
		}
		else if (InputHandler.getMiHandler().getNormalize()==0)
		{
			System.out.println("No se normalizarán las instancias");
		}
		else if (InputHandler.getMiHandler().getNormalize()==2)
		{
			Normalizer norm = new Normalizer();
			if (norm.shouldNormalize(instances))
			{
				instances = norm.normalize(instances);
			}
		}
		//B  membership matrix.
		int nrow = instances.size();
		int nCol = k;
		int[][] B = new int[nrow][nCol];

		//codebook
		ArrayList<Instance> codebook = new ArrayList<Instance>();

		// Instance k clusters
		Cluster[] clusters = new Cluster[k];
		for(int index=0;index<k;index++){
			clusters[index]=new Cluster();
		}

		if(InputHandler.getMiHandler().getIni()<1)
		{
			//Let seed be the seed for the random number to get the codebook.
			//Let M = {m_{1}, m_{2}, ..., m_{k}} be the code-book associated to the clusters. Random instances.
			codebook = kmeans.startRandomCodebook(k, instances);
		}
		else
		{
			B = kmeans.matrixMemberShipInitialize(nrow, nCol);
			for(int i=0;i<B.length;i++){
				for(int j=0;j<B[i].length;j++){
					if(B[i][j]==1)
					{
						clusters[j].addInstance(instances.get(i));
					}
				}
			}

			for(int l=0;l<clusters.length;l++)
			{
				codebook.add(clusters[l].calcCentroid());
			}
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
		if(itera>0)
		{
			for(int numIter=0;numIter<itera;numIter++)
			{
				System.out.println("Agrupando por número de iteraciones fijo..........................."+(numIter*100)/itera+"%");
				clusters=kmeans.resetClusters(clusters);
				B=kmeans.resetMatrixMembership(B);
				for (int i=0;i<instances.size();i++)
				{		
					Double dist = Minkowski.getMinkowski()
							.calculateDistance(instances.get(i)
									, codebook.get(0), distExp);
					int codewordIndex = 0;
					clusters[0].addInstance(instances.get(i));
					B[i][0]=1;
					for (int j=0;j<k; j++)
					{
						Double distAux = Minkowski.getMinkowski().calculateDistance(instances.get(i), codebook.get(j), distExp);
						if(dist>distAux+difference)
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
					}					
				}
				for (int s=0; s< clusters.length;s++)
				{
					if(clusters[s].getInstances().hasNext())codebook.set(s, clusters[s].calcCentroid());
				}
			}
		}
		else
		{
			while(!condParada)
			{
				double ratioMax=InputHandler.getMiHandler().getRatioMax();
				ArrayList<Instance> codebookAux;
				clusters=kmeans.resetClusters(clusters);
				B=kmeans.resetMatrixMembership(B);
				for (int i=0;i<instances.size();i++)
				{		
					Double dist = Minkowski.getMinkowski()
							.calculateDistance(instances.get(i)
									, codebook.get(0), distExp);
					int codewordIndex = 0;
					clusters[0].addInstance(instances.get(i));
					B[i][0]=1;
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
					}					
				}
				for (int s=0; s< clusters.length;s++)
				{
					codebookAux = (ArrayList<Instance>)codebook.clone();
					if(clusters[s].getInstances().hasNext())codebook.set(s, clusters[s].calcCentroid());
					if (kmeans.compareCodeBooks(codebookAux,codebook,distExp,ratioMax))
					{
						condParada = true;
					}
				}
			}
		}
		

		Plot.getMiPlot().setMatrixMembership(B);
		JFreeChart scatter = Plot.getMiPlot().plottingMatrix("Memberships", "Clusters", "Instances");
		// create and display a frame...
		ChartFrame frame = new ChartFrame("CLUSTERING:K-MEANS", scatter);
		frame.pack();
		frame.setVisible(true);
		
		for(int j=0;j<clusters.length;j++){
			System.out.println("CLUSTER: "+j);
			System.out.println("---------------------------------------");
			System.out.println("Número de instancias en el cluster: "+clusters[j].getListaInstances().size());
			
			
			String s="";
			for (int p=0;p<codebook.get(0).numFeatures();p++)
			{
				s=s+codebook.get(j).getAtt(p)+"\n";
			}
			System.out.println("Con el codeword: \n"+s);
			System.out.println("===========================================================================");
		}
		Evaluation eval= new Evaluation();
		double coef=eval.silhouetteCoefficient(clusters, 2);
		kmeans.setSilhouette(coef);
		System.out.println("Average Silhouette coefficient: "+coef);
		System.out.println("===========================================================================");		
		kmeans.report(clusters, codebook, InputHandler.getMiHandler().getDataPath());
	}

}
