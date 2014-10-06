package org.packmodelo;

public class MinkowskyDistance {
	
	public MinkowskyDistance(){}
	
	/*
	 * Nota: podemos hacerlo de dos formas, que se cree la clase solo para calcular, o crear una clase con setP y set instancias y que después tenga
	 * una función sin parámetros, con solo la ejecución y lo que devuelva.
	 */
	
	public double calculateDistance(Instance x, Instance y, double p)
	{
		double res = 0.0;
		
		for (int i = 0; i<10; i++) //para el nº de atributos
		{
			res = res + Math.pow(Math.abs(1-1),p); //aqui x e y
		}
		
		res = Math.pow(res, 1/p);
		
		return res;
	}
}
