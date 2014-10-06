package org.packmodelo;

public class MinkowskyDistance {
	
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
