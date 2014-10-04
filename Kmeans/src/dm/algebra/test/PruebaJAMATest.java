package dm.algebra.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Jama.Matrix;

public class PruebaJAMATest {

	private double[][] M;
	private double[][] N;
	private Matrix A;
	private Matrix B;
	private double escalar;
	@Before
	public void setUp() throws Exception 
	{
		escalar = 1;
		M = new double[][]{{1,2,3},{1,2,3},{1,1,1}};
		N = new double[][]{{1,1,1}};
		A=new Matrix(M);
		B=new Matrix(N);
	}

	@After
	public void tearDown() throws Exception 
	{
		M=null;
		N=null;
		A=null;
		B=null;
	}

	@Test
	public void test() 
	{
		System.out.println(A.getColumnDimension()+"x"+A.getRowDimension());
		System.out.println(B.getColumnDimension()+"x"+B.getRowDimension());
		Matrix C=A.getMatrix(0, 0, 0, 2);		
		C.minus(B);
		C.timesEquals(escalar);
		System.out.println(C.getColumnDimension()+"x"+C.getRowDimension());

	}

}
