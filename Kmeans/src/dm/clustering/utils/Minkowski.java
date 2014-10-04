package dm.clustering.utils;

import dm.core.Instance;

public class Minkowski 
{
	private static Minkowski miMinkowski;
	
	private Minkowski(){
		
	}
	
	public static Minkowski getMinkowski(){
		return miMinkowski;
	}
	
	//TODO implementar
	/**
	 * 
	 * @param A
	 * @param B
	 * @param exp
	 * @return
	 */
	public double distance(Instance A, Instance B, int exp){
		double distance = 0.0;
		return distance;
	}
}
