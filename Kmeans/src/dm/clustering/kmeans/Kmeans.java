package dm.clustering.kmeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import dm.clustering.evaluation.Evaluation;
import dm.clustering.utils.DataLoader;
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

		ArrayList<Instance> instances = null;
		String extension=InputHandler.getMiHandler().getFileExtension();
		instances = loadInstances(extension);

		//Normalize data
		if(InputHandler.getMiHandler().getNormalize())
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
			codebook = startRandomCodebook(k, instances);
		}
		else
		{
			B = matrixMemberShipInitialize(nrow, nCol);
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
		while(!condParada)
		{
			for(int numIter=0;numIter<itera;numIter++){
				clusters=resetClusters(clusters);
				B=resetMatrixMembership(B);
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
					ArrayList<Instance> codebookAux;
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
		JFreeChart scatter = Plot.getMiPlot().plottingMatrix("Memberships", "Clusters", "Instances");
		// create and display a frame...
		ChartFrame frame = new ChartFrame("CLUSTERING:K-MEDIAS", scatter);
		frame.pack();
		frame.setVisible(true);
		

		//TODO test and evaluation

		System.out.println(instances.size());
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
		System.out.println("===========================================================================");
		System.out.println("Average Silhouette coefficient: "+eval.silhouetteCoefficient(clusters, 2));
		
		
		//report(clusters, codebook, InputHandler.getMiHandler().getDataPath());
	}

	/**
	 * reset all clusters
	 * @param clusters
	 */
	private static Cluster[] resetClusters(Cluster[] clusters) {
		for (int p = 0; p<clusters.length;p++)
		{
			clusters[p].reset();
		}
		return clusters;
	}
	
	/**
	 * Put all members in zero
	 * @param B
	 * @return matrix with all members = 0.
	 */
	private static int[][] resetMatrixMembership(int[][] B){
		for(int i=0;i<B.length;i++){
			for(int j=0;j<B[i].length;j++){
				B[i][j]=0;
			}
		}
		return B;
	}

	/**
	 * Load data from the file with extension get by parameter
	 * @param extension
	 * @return
	 */
	private static ArrayList<Instance> loadInstances(String extension) {
		ArrayList<Instance> instances = null;
		if(extension.equals("csv"))
		{
			instances = DataLoader.getMiLoader()
					.loadCSVNumericData(InputHandler
							.getMiHandler()
							.getDataPath(), InputHandler.getMiHandler().getDataIndexStart());
		}
		else if(extension.equalsIgnoreCase("arff"))
		{
			instances = DataLoader.getMiLoader()
					.loadARFF(InputHandler
							.getMiHandler()
							.getDataPath(),
							InputHandler.getMiHandler()
							.getDelimiter());
		}
		else
		{
			instances = DataLoader.getMiLoader()
					.loadData(InputHandler.getMiHandler()
							.getDataPath(), 
							InputHandler.getMiHandler()
							.getDataIndexStart(),
							InputHandler.getMiHandler()
							.getDelimiter());
		}
		return instances;
	}

	/**
	 * @param k
	 * @param instances
	 */
	private static ArrayList<Instance> startRandomCodebook(int k, ArrayList<Instance> instances) {
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
	private static int[][]  matrixMemberShipInitialize(int nrow, int nCol) {
		int[][] B = new int[nrow][nCol];
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

	public static void report(Cluster[] clusters, ArrayList<Instance> codebook, String path){
		
		
		final String LOG_TAG = Kmeans.class.getSimpleName();
		Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm"); // Formato de la fecha.
		String dateS = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.		
		String datePDF = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());
		//String filePath = "Resultados/"+dateS+"resultados.txt";
		String parent = "Resultados";
		File directory =new File(parent);
		directory.mkdir();
		File myPath =new File(parent+"/"+dateS+"_"+path.substring(5, path.length()-5)+"_resultados.pdf");
			try {
				FileOutputStream file = new FileOutputStream(myPath);
				Document docu = new Document();
				PdfWriter.getInstance(docu, file);
				docu.open();
				docu.addTitle("ANALISIS DE RESULTADOS");
				docu.addCreationDate();
				docu.addAuthor("Iñigo Sánchez Méndez\n&\nJosu Rodríguez Azpeleta");
				docu.addCreator("Iñigo Sánchez & Josu Rodríguez");
				Font font =FontFactory.getFont("arial",   // fuente
						22,                            // tamaño
						Font.ITALIC,                   // estilo
						BaseColor.BLACK);
				font.setStyle(Font.UNDERLINE);
				Paragraph titulo = new Paragraph("Datos de agrupamiento de la instancias en "+clusters.length+" Clusters",font);
				titulo.setAlignment(Paragraph.ALIGN_CENTER);
				docu.add(titulo); 

				font.setStyle(Font.ITALIC);
				font.setSize(font.getSize()-6);
				Paragraph autor = new Paragraph("Autor: Iñigo Sanchez Mendez",font);
				autor.setAlignment(Paragraph.ALIGN_CENTER);
				docu.add(autor);

				Paragraph fecha =  new Paragraph(datePDF);
				fecha.setAlignment(Paragraph.ALIGN_CENTER);
				fecha.getFont().setSize(10);
				docu.add(fecha);



				docu.newPage();

				Paragraph archivo = new Paragraph("Conjunto de instancias en el archivo "+InputHandler.getMiHandler().getFileExtension()+"\n\n");
				archivo.setAlignment(Paragraph.ALIGN_MIDDLE);
				docu.add(archivo);

				PdfPTable table = new PdfPTable(6);

				//Array que devuelve, para cada instancia el cluster al que pertenece, empezando por 0
				//Tiene tantos elementos, como número de instancias.
				for(int i=0;i<clusters.length;i++) 
				{
					if(!clusters[i].getListaInstances().isEmpty())
					{
						for(int j=0;j<clusters[i].getListaInstances().size();j++)
						{
							System.out.printf("Instance %d -> Cluster %d \n", j, i);
							//TODO args para guardar en archivo si el usuario lo pide.
							//String output = "Instance "+i+" -> Cluster "+ clusterNum+"\n";
							String instance ="Instance "+j;
							String cluster ="Cluster "+ i;
							PdfPCell cell = new PdfPCell();
							cell.setBackgroundColor(BaseColor.ORANGE);
							cell.addElement(new Phrase(instance));
							table.addCell(cell);
							table.addCell(cluster);
							//docu.add(new Paragraph(output));
							//dl.SaveFile(filePath, output, false);
						i++;

						}
					}
				}
				docu.add(table);
				docu.close();
			} 
			catch (FileNotFoundException e) 
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "No se encuentra el archivo en el sistema");
			} 
			catch (DocumentException e) 
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "No se puede crear el reporte");
			}
	}
}

