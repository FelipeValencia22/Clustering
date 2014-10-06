package dm.clustering.utils;

import java.util.ArrayList;

import dm.core.Instance;

public class Minkowski 
{
	private static Minkowski miMinkowski;
	
	private Minkowski(){
		
	}
	
	public static Minkowski getMinkowski(){
		return miMinkowski;
	}
	
	
	
	
	
	
	
	public double calculateDistance(Instance x, Instance y, double p)
	{
		double res = 0.0;
		ArrayList<Double> attX = x.getDobFeatures();
		ArrayList<Double> attY = y.getDobFeatures();
		
		
		for (int i = 0; i<attX.size(); i++) //para el nº de atributos
		{
			res = res + Math.pow(Math.abs((attX.get(i).doubleValue()) - attY.get(i).doubleValue()), p); //aqui x e y
		}
		
		res = Math.pow(res, 1/p);
		
		return res;
	}

}
