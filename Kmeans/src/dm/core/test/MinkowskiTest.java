package dm.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import dm.clustering.utils.Minkowski;
import dm.core.Instance;

public class MinkowskiTest {

	@Test
	public void testCalculateDistance() {
		Instance x = new Instance();
		Instance y = new Instance();
		
		x.addDobFeature(1.0);
		x.addDobFeature(3.0);
		
		y.addDobFeature(2.0);
		y.addDobFeature(5.0);


		double res = Minkowski.getMinkowski().calculateDistance(x, y, 2.0);
		Double resD = new Double(res);
		
		double calc = Math.pow(Math.pow(Math.abs((1.0-2.0)),2.0) + Math.pow(Math.abs((3.0-5.0)),2.0), 0.5);
		Double calcD = new Double(calc);
		
		assertEquals(calcD,resD);
	}

}
