package dm.clustering.utils;

import java.io.File;
import java.io.FileNotFoundException;
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

	private final String LOG_TAG = InputHandler.class.getSimpleName();
	
	private InputHandler() {
		K = 1;
		this.exp = 2;
		this.iterations = 0;
		this.diff = 0.0;
		this.initialize= 0;
		this.fileExtension="csv";
		this.dataIndexStart=0;
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
	
	public void loadArgs(String filePath){
		try 
		{
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(new File(filePath));
			sc.useDelimiter(";");
			ArrayList<String> args = new ArrayList<String>(); 
			while(sc.hasNext()){
				String line = sc.next();
				String[] arg = line.split("=");
				args.add(arg[1]);
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
		} 
		catch (FileNotFoundException e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, "No es posible encontrar el archivo de configuración");
		}
	}
}
