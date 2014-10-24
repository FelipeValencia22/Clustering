package dm.clustering.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import dm.core.Instance;
import au.com.bytecode.opencsv.CSVReader;

/**
 * 
 * Fuentes consultadas: http://opencsv.sourceforge.net/
 *
 */
public class DataLoader 
{
	private static DataLoader miDataLoader = new DataLoader();
	private final String LOG_TAG = this.getClass().getSimpleName();

	private DataLoader(){};

	public static DataLoader getMiLoader(){
		return miDataLoader;
	}

	/**
	 * 
	 * pre: el path del archivo existe y el formato es csv, además contiene datos en formato numérico.Es necesario
	 * que se expecifique el índice de la fila a partir de la cual comienza los Datos.
	 * pos: devuelve los datos cargados en una matriz de dobles.
	 * @param filePath
	 * @return double[][]
	 */
	@SuppressWarnings("resource")
	public ArrayList<Instance> loadCSVNumericData(String pFilePath,int pIndexini){

		
		ArrayList<Instance> instances = new ArrayList<Instance>();
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(pFilePath));
			List<String[]> features = reader.readAll();	
			for(int i = pIndexini;i<features.size();i++){
				Instance ins = new Instance();
				ins.setId(String.valueOf(i));
				for(int j = 0;j<features.get(i).length;j++)
				{					
						try 
						{
							ins.addDobFeature(Double.valueOf(features.get(i)[j]));
						} catch (NumberFormatException e) 
						{
							Logger.getLogger(LOG_TAG).log(Level.INFO, "El atributo: "+j+ " no es numérico, se añade a features");
							ins.addFeature(features.get(i)[j]);
						}					
				}
				instances.add(ins);
			}
			reader.close();
		} 
		catch (FileNotFoundException e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.INFO, "El archivo no existe");
		} 
		catch (IOException e) {
			Logger.getLogger(LOG_TAG).log(Level.INFO, "Imposible acceder al archivo");
		}
		try
		{
			reader.close();
		} 
		catch (IOException e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Error al intentar cerrar CSVReader");
		}
		return instances;
	}
	/**
	 * 
	 * @param pFilePath
	 * @param delimiter 
	 * @return ArrayList<Instance>
	 */
	public ArrayList<Instance> loadARFF(String pFilePath, String delimiter)
	{
		ArrayList<Instance> instances = new ArrayList<Instance>();
		File file = new File(pFilePath);
		if(file.exists())
		{
			BufferedReader reader;
			boolean enc=false;
			try
			{
				reader = new BufferedReader(new FileReader(pFilePath));
				String linea;
				while(reader.ready() && !enc)
				{
					linea = reader.readLine();
					if(linea.equalsIgnoreCase("@data")){
						enc=true;
					}      
				}
				//Lines are written in array
				if(enc != true) 
				{
					//the file isn't ARFF
					new Exception();
				}else 
				{
					int j=0;
					while(reader.ready())
					{
						linea=reader.readLine();
						String[] features = linea.split(delimiter);
						Instance ins = new Instance();
						ins.setId(String.valueOf(j));
						for(int i=0;i<features.length;i++)
						{
							try 
							{
								ins.addDobFeature(Double.valueOf(features[i]));
							} catch (NumberFormatException e) 
							{
								Logger.getLogger(LOG_TAG).log(Level.INFO, "El atributo: "+i+ " no es numérico, se añade a features");
								ins.addFeature(features[i]);
							}	
						}
						instances.add(ins);
						j++;
					}
					reader.close();
				}
					
			}
			catch(IOException e)
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Error de entrada en el archivo");
			}
			catch(Exception e)
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Algo va mal en tu archivo arff, no se encuentra @data");
			}
		}
		else
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, "El archivo no existe");
		}
		return instances;
	}
	
	public ArrayList<Instance> loadData(String pFilePath,int pIndexIni,String pDelimiter)
	{
		ArrayList<Instance> instances = new ArrayList<Instance>();
		File file = new File(pFilePath);
		if(file.exists()){
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(pFilePath));
				String linea;
				int j=0;
				while(reader.ready())
				{
					linea=reader.readLine();
					String[] features = linea.split(pDelimiter);
					Instance ins = new Instance();
					ins.setId(String.valueOf(j));
					for(int i=0;i<features.length;i++)
					{
						try 
						{
							ins.addDobFeature(Double.valueOf(features[i]));
						} catch (NumberFormatException e) 
						{
							Logger.getLogger(LOG_TAG).log(Level.INFO, "El atributo: "+i+ " no es numérico, se añade a features");
							ins.addFeature(features[i]);
						}	
					}
					instances.add(ins);
					j++;
				}
			reader.close();	
			} 
			catch (FileNotFoundException e1) 
			{

				e1.printStackTrace();
			} catch (IOException e) 
			{

				e.printStackTrace();
			}
						
		}
		return instances;
	}

}
