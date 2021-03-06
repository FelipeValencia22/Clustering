package dm.clustering.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class InputHandler 
{
	private static InputHandler miHandler = new InputHandler();
	private int K;
	private double exp;
	private String dataPath;
	private int iterations;
	private double diff;
	private int initialize;
	private String fileExtension;
	private int dataIndexStart;	
	private String delimiter;
	private int normalize;
	private double ratioMax;

	private final String LOG_TAG = InputHandler.class.getSimpleName();

	private InputHandler() {
		K = 1;
		this.exp = 2;
		this.iterations = 0;
		this.diff = 0.0;
		this.initialize= 0;
		this.fileExtension="csv";
		this.dataIndexStart=0;
		this.delimiter=" ";
		this.normalize=0;
		this.ratioMax=0;
	}		

	public void setRatioMax(double ratioMax) {
		this.ratioMax = ratioMax;
	}

	public static InputHandler getMiHandler(){
		return miHandler;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	public double getExp() {
		return exp;
	}

	public void setExp(double exp) {
		this.exp = exp;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public int getIterations() {
		return iterations;
	}

	public int getNormalize(){
		return normalize;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public double getDiff() {
		return diff;
	}

	public void setDiff(double diff) {
		this.diff = diff;
	}

	private void setIni(Integer pIni) {
		this.initialize=pIni;
	}

	public int getIni(){
		return this.initialize;
	}

	public int getDataIndexStart() {
		return dataIndexStart;
	}

	public void setDataIndexStart(int dataIndexStart) {
		this.dataIndexStart = dataIndexStart;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public void setNormalize(int i)
	{
		this.normalize=i;
	}

	public double getRatioMax() {
		return this.ratioMax;
	}

	public void loadArgs(String filePath){
		BufferedReader reader;
		try 
		{
			String linea;
			ArrayList<String> args = new ArrayList<String>();
			reader=new BufferedReader(new FileReader(filePath));
			while(reader.ready())
			{
				String line = reader.readLine();
				String[] arg = line.split("=");
				if(arg.length>0)args.add(arg[1]);
			}
			try 
			{
				this.setDataPath(args.get(0));
			} 
			catch (Exception e) 
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Revise el path del fichero de datos en el archivo de configuración");;
			}
			try 
			{
				this.setK(Integer.valueOf(args.get(1)));
			} 
			catch (NumberFormatException e) 
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Revise el parámetro k en el fichero de configuración");

			}
			try 
			{
				this.setIterations(Integer.valueOf(args.get(2)));
			} 
			catch (NumberFormatException e) 
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Revise el el parámetro iteraciones en el archivo de configuración");

			}
			try 
			{
				this.setDiff(Double.valueOf(args.get(3)));
			} 
			catch (NumberFormatException e) 
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Revise el parámetro difference en el archivo de configuración");

			}
			try 
			{
				this.setExp(Double.valueOf(args.get(4)));
			} 
			catch (NumberFormatException e) 
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Revise el parámetro distance en el archivo de configuración");

			}
			try
			{
				this.setIni(Integer.valueOf(args.get(5)));

			}
			catch(NumberFormatException e)
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Revise el parámetro initialize en el archivo de configuración");

			}
			try
			{
				this.setFileExtension(args.get(6));

			}
			catch(Exception e)
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Revise el parámetro file_extension en el archivo de configuración");

			}
			try
			{
				this.setDataIndexStart(Integer.valueOf(args.get(7)));

			}
			catch(NumberFormatException e)
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Revise el parámetro data_index_start en el archivo de configuración");

			}
			try
			{
				this.setDelimiter(args.get(8));
				System.out.println(this.getDelimiter());
			}
			catch(NumberFormatException e)
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Revise el parámetro delimiter en el archivo de configuración");

			}			
			try
			{
				this.setNormalize(Integer.parseInt(args.get(9)));
			}
			catch(NumberFormatException e)
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Revise el parámetro normalize en el archivo de configuración");

			}
			try
			{
				this.setRatioMax(Double.valueOf(args.get(10)));
			}
			catch(NumberFormatException e)
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Revise el parámetro ratio_max en el archivo de configuración");

			}	
		} 
		catch (FileNotFoundException e1) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, "No es posible encontrar el archivo de configuración");
		} 
		catch (IOException e1) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, "No es posible encontrar el archivo de configuración");
		}
	}
}
