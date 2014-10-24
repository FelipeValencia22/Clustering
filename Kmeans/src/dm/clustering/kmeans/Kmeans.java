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

import dm.clustering.io.DataLoader;
import dm.clustering.io.InputHandler;
import dm.core.Cluster;
import dm.core.Instance;


public class Kmeans {
	
	
	private double silhouette=0.0;
	public Kmeans()
	{
		
	}
	
	public void setSilhouette(double pCoefficient)
	{
		this.silhouette=pCoefficient;
	}
	
	public double getSilhouette()
	{
		return this.silhouette;
	}
	
	/**
	 * 
	 * @param clusters
	 * @return
	 */
	public  Cluster[] resetClusters(Cluster[] clusters) {
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
	public int[][] resetMatrixMembership(int[][] B){
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
	public ArrayList<Instance> loadInstances(String extension) {
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
	public ArrayList<Instance> startRandomCodebook(int k, ArrayList<Instance> instances) {
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
	public int[][]  matrixMemberShipInitialize(int nrow, int nCol) {
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

	public  boolean compareCodeBooks(ArrayList<Instance> a, ArrayList<Instance> b, double p, double ratioMax)
	{		
		for (int i=0;i<a.size();i++)
		{
				if (!a.get(i).equals(b.get(i),ratioMax))
				{
					return false;
				}
		}			
		return true;	
	}
	/**
	 * 
	 * @param clusters
	 * @param codebook
	 * @param path
	 */
	public  void report(Cluster[] clusters, ArrayList<Instance> codebook, String path){
		
		
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
				Paragraph titulo = new Paragraph("Datos de agrupamiento de la instancias obtenidas del archivo "+
				InputHandler.getMiHandler().getDataPath()+" en "+clusters.length+" Clusters",font);
				titulo.setAlignment(Paragraph.ALIGN_CENTER);
				docu.add(titulo); 

				font.setStyle(Font.ITALIC);
				font.setSize(font.getSize()-6);
				Paragraph autor = new Paragraph("Autores: Iñigo Sanchez Mendez y Josu Rodríguez",font);
				autor.setAlignment(Paragraph.ALIGN_CENTER);
				docu.add(autor);

				Paragraph fecha =  new Paragraph(datePDF);
				fecha.setAlignment(Paragraph.ALIGN_CENTER);
				fecha.getFont().setSize(10);
				docu.add(fecha);
				
				String distancia="";
				double exp=InputHandler.getMiHandler().getExp();
				if(exp==2.0)
				{
					distancia="Distancia Euclidea";
				}
				else if(exp==1.0)
				{
					distancia="Distancia Manhattan";
				}
				else
				{
					distancia="Distancia Minkowski"+" con exponente "+exp;
				}
				
				Paragraph args =  new Paragraph("Parámetros de la ejecución: \n"
						+ "Iteraciones: "+InputHandler.getMiHandler().getIterations()
						+ "\nPonderación de comparación de las distancias de la instancia al centroide actualizado: "
						+InputHandler.getMiHandler().getDiff()+
						"\nLa distancia utilizada: "+distancia
						+"\nInicialización: "+InputHandler.getMiHandler().getIni()
						+"\nNormalización: "+InputHandler.getMiHandler().getNormalize()
						+"\nDisimilitud codebooks: "+InputHandler.getMiHandler().getRatioMax());
				fecha.setAlignment(Paragraph.ALIGN_CENTER);
				fecha.getFont().setSize(10);
				docu.add(args);



				docu.newPage();

				Paragraph archivo = new Paragraph("Conjunto de instancias en el archivo "+InputHandler.getMiHandler().getFileExtension()+"\n\n");
				archivo.setAlignment(Paragraph.ALIGN_MIDDLE);
				docu.add(archivo);


				//Array que devuelve, para cada instancia el cluster al que pertenece, empezando por 0
				//Tiene tantos elementos, como número de instancias.
				for(int i=0;i<clusters.length;i++) 
				{
					if(!clusters[i].getListaInstances().isEmpty())
					{
						Paragraph clust =  new Paragraph("CLUSTER "+i+"\n\n",font);
						clust.setAlignment(Paragraph.ALIGN_CENTER);
						clust.getFont().setSize(15);
						clust.getFont().setStyle(Font.UNDERLINE);
						docu.add(clust);
						PdfPTable table = new PdfPTable(8);
						for(int j=0;j<clusters[i].getListaInstances().size();j++)
						{
							
							//System.out.printf("Instance %d -> Cluster %d \n", Integer.valueOf(clusters[i].getListaInstances().get(j).getId()), i);
							//String output = "Instance "+i+" -> Cluster "+ clusterNum+"\n";							
							String instance ="Instance "+clusters[i].getListaInstances().get(j).getId();
							//String cluster ="Cluster "+ i;
							PdfPCell cell = new PdfPCell();
							cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
							cell.addElement(new Phrase(instance));
							table.addCell(cell);
							//table.addCell(cluster);
							//docu.add(new Paragraph(output));
							//dl.SaveFile(filePath, output, false);
						}
						docu.add(table);
						docu.newPage();
					}
					
				}
				
				
				docu.newPage();
				
				Paragraph eval =  new Paragraph("Los resultados de la evaluación de la ejecución: \n"
						+ "Coeficiente silhouette: "+this.silhouette+"\n"
						);
				fecha.setAlignment(Paragraph.ALIGN_CENTER);
				fecha.getFont().setSize(10);
				docu.add(eval);
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

