package dm.algebra;

import Jama.Matrix;

public class PruebaJAMA 
{
	private static PruebaJAMA miJAMA = new PruebaJAMA();
	
	private PruebaJAMA(){
		
	}
	
	public static PruebaJAMA getmiJAMA()
	{
		return miJAMA;
	}
	
	public Matrix getMatrix(double[][] a, double[][] b){
		Matrix A = new Matrix(a);
		Matrix B = new Matrix(b);
		
		return A.minus(B);
	}
	
	
}
