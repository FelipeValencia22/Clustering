package dm.clustering.kmeans;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import dm.clustering.utils.CSVDataLoader;
import dm.clustering.utils.Minkowski;
import dm.core.Cluster;
import dm.core.Instance;

public class Kmeans {
	

	public static void main(String[] args) {
		String LOG_TAG = Kmeans.class.getSimpleName();
		Cluster cluster = new Cluster();
		ArrayList<Cluster> clusterArray = new ArrayList<Cluster>();
		
		//Let seed be the seed for the random number to get the codebook.
		//Let k be the number of clusters to partition the data set
		int k = 1;
		try
		{
			k = Integer.valueOf(args[1]);
		} 
		catch (NumberFormatException e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, "El número de clusters k debe ser un entero");
		}
		//Let X = {x_{1},x_{2}, ..., x_{n}} be the data set to be analyzed
		ArrayList<Instance> instances;
		instances = CSVDataLoader.getMiLoader().loadNumericData(args[0], 2);
		
		//TODO Normalize Data
		
		//Let M = {m_{1}, m_{2}, ..., m_{k}} be the code-book associated to the clusters. Random instances.
		ArrayList<Instance> codebook = startCodebook(k, instances);
		
		//B Matrix membership.	Capturar de los argumentos inicialización escogida.
		int nrow = instances.size();
		int nCol = instances.get(0).getDobFeatures().size();
		int[][] B = matrixMemberShipInitialize(nrow, nCol);
		
		// Primera iteración, antes del while
		
		for (int i=0;i<instances.size();i++)
		{
			for (int j = 0; j<codebook.size(); j++)
			{
				Minkowski.getMinkowski().calculateDistance(instances.get(i), codebook.get(j), 2);
				//TODO guardar las que minimizen la distancia
			}
		}
				
		
		for (int i=0; i< clusterArray.size();i++)
		{
			codebook.set(i, clusterArray.get(i).calcCentroid());
		}
		
		// Iteraciones sucesivas
		boolean condParada = false;
		
		while(!condParada)
		{

			for (int i=0;i<instances.size();i++)
			{
				for (int j = 0; j<codebook.size(); j++)
				{
					Minkowski.getMinkowski().calculateDistance(instances.get(i), codebook.get(j), 2);
					//TODO guardar las que minimizen la distancia
				}
			}
					
			
			for (int i=0; i< clusterArray.size();i++)
			{
				codebook.set(i, clusterArray.get(i).calcCentroid());
			}
			
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
	private static int[][]  matrixMemberShipInitialize(int nrow, int nCol) {
		int[][] B = new int[nrow][nCol];
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

}
