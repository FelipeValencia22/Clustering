package org.packdatamaning;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import packdatahandlers.dm.DataLoader;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.MinkowskiDistance;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class KmeansFirstApproach 
{
	/**
	 * Fuentes consultadas:
	 * http://stackoverflow.com/questions/6685961/weka-simple-k-means-clustering-assignments
	 * http://weka.wikispaces.com/Remove+Attributes
	 * http://hashblogeando.wordpress.com/2013/07/22/generando-tablas-en-archivos-pdf-con-java-e-itext/
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception 
	{
		
		final int numbOfClusters = Integer.parseInt(args[1]);
		final int numbersOfIterations = Integer.parseInt(args[2]);
		//TODO Implementar tratamiento argumentos 
		DataLoader dl = new DataLoader(args[0]);
		Instances data = dl.instancesLoader();
		Instances dataNewInstances;

		//Para obtener el gráfico de los cluster
		//Plot2D p2d = new Plot2D();		
		if(args.length>4)
		{
			//Eliminamos el atributo de tipo string porque no es compatible con kmeans //TODO documentar
			//TODO si existen más atributos de tipo string???
			Remove remove = new Remove();
			remove.setAttributeIndices(args[4]);
			remove.setInvertSelection(false);
			remove.setInputFormat(data);
			dataNewInstances = Filter.useFilter(data, remove);
		}
		else
		{
			dataNewInstances=data;
		}
		dataNewInstances.setClassIndex(-1);
		MinkowskiDistance mDistance = new MinkowskiDistance(dataNewInstances);		
		mDistance.setOrder(2.0);
		
		if(args.length>3)
		{
				 mDistance.setOrder(Double.valueOf(args[3]));			
		}
		SimpleKMeans kMeans = new SimpleKMeans();
		try 
		{
			kMeans.setDistanceFunction(mDistance);
			kMeans.setPreserveInstancesOrder(true);
			kMeans.setNumClusters(numbOfClusters);
			kMeans.setMaxIterations(numbersOfIterations);
			kMeans.buildClusterer(dataNewInstances);
			
			Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm"); // Formato de la fecha.
			String dateS = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.		
			String datePDF = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());
			//String filePath = "Resultados/"+dateS+"resultados.txt";
			
			FileOutputStream file = new FileOutputStream(new File("Resultados/"+dateS+"_"+args[0].substring(5, args[0].length()-5)+"_resultados.pdf"));
			Document docu = new Document();
			PdfWriter.getInstance(docu, file);
			docu.open();
			docu.addTitle("ANALISIS DE RESULTADOS");
			docu.addCreationDate();
			docu.addAuthor("Iñigo Sánchez Méndez");
			docu.addCreator("Iñigo Sánchez");
			Font font =FontFactory.getFont("arial",   // fuente
	                22,                            // tamaño
	                Font.ITALIC,                   // estilo
	                BaseColor.BLACK);
			font.setStyle(Font.UNDERLINE);
			Paragraph titulo = new Paragraph("Datos de agrupamiento de la instancias en "+args[1]+" Clusters",font);
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
			
			Paragraph archivo = new Paragraph("Conjunto de instancias en el archivo "+args[0].substring(5)+"\n\n");
			archivo.setAlignment(Paragraph.ALIGN_MIDDLE);
			docu.add(archivo);
			
			PdfPTable table = new PdfPTable(6);

			//Array que devuelve, para cada instancia el cluster al que pertenece, empezando por 0
			//Tiene tantos elementos, como número de instancias.
			int[] asignaciones = kMeans.getAssignments();
			int i=0;
			for(int clusterNum : asignaciones) 
			{
			    System.out.printf("Instance %d -> Cluster %d \n", i, clusterNum);
			    //TODO args para guardar en archivo si el usuario lo pide.
			    //String output = "Instance "+i+" -> Cluster "+ clusterNum+"\n";
			    String instance ="Instance "+i;
			    String cluster ="Cluster "+ clusterNum;
			    PdfPCell cell = new PdfPCell();
			    cell.setBackgroundColor(BaseColor.ORANGE);
			    cell.addElement(new Phrase(instance));
			    table.addCell(cell);
			    table.addCell(cluster);
			    //docu.add(new Paragraph(output));
			    //dl.SaveFile(filePath, output, false);
			    i++;

			}
		    docu.add(table);
			docu.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
