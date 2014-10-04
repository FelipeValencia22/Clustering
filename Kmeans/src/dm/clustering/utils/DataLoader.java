package dm.clustering.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import dm.core.Instance;
import au.com.bytecode.opencsv.CSVReader;

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
	public ArrayList<Instance> loadNumericData(String pFilePath,int pIndexini){

		
		ArrayList<Instance> instances = new ArrayList<Instance>();
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(pFilePath));
			List<String[]> features = reader.readAll();	
			for(int i = pIndexini;i<features.size();i++){
				Instance ins = new Instance();
				for(int j = 0;j<features.get(i).length;j++)
				{					
						try 
						{
							ins.addDobFeature(Double.valueOf(features.get(i)[j]));
							System.out.println(features.get(i)[j]);
						} catch (NumberFormatException e) 
						{
							Logger.getLogger(LOG_TAG).log(Level.INFO, "El atributo no es numérico");
						}					
				}
				instances.add(ins);
			}
		} 
		catch (FileNotFoundException e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.INFO, "El archivo no existe");
		} 
		catch (IOException e) {
			Logger.getLogger(LOG_TAG).log(Level.INFO, "Imposible acceder al archivo");
		}
		return instances;
	}

}
