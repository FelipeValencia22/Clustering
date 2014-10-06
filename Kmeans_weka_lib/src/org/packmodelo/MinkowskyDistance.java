package org.packmodelo;

import java.util.ArrayList;

public class MinkowskyDistance {
	
	public MinkowskyDistance(){}
	
	/*
	 * TODO Nota: podemos hacerlo de dos formas, que se cree la clase solo para calcular, o crear una clase con setP y set instancias y que después tenga
	 * una función sin parámetros, con solo la ejecución y lo que devuelva.
	 */
	
	public double calculateDistance(Instance x, Instance y, double p)
	{
		double res = 0.0;
		ArrayList<Double> attX = x.getDoubleAtt();
		ArrayList<Double> attY = y.getDoubleAtt();
		
		
		for (int i = 0; i<attX.size(); i++) //para el nº de atributos
		{
			res = res + Math.pow(Math.abs((attX.get(i).doubleValue()) - attY.get(i).doubleValue()), p); //aqui x e y
		}
		
		res = Math.pow(res, 1/p);
		
		return res;
	}
}
