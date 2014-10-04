package dm.core.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dm.core.Cluster;
import dm.core.Instance;

public class ClusterTest {
	
	private Instance ins1;
	private Instance ins2;
	private Instance ins3;
	private Cluster cluster1;
	@Before
	public void setUp() throws Exception 
	{
		ins1 = new Instance();
		ins2 = new Instance();
		ins3 = new Instance();
		cluster1 = new Cluster();
	}

	@After
	public void tearDown() throws Exception 
	{
		ins1 = null;
		ins2 = null;
		ins3 = null;
	}

	@Test
	public void testCalcCentroid() 
	{
		ins1.addDobFeature(2.0);
		ins1.addDobFeature(1.25);
		ins1.addDobFeature(1.75);
		ins1.addDobFeature(1.0);
		ins1.addDobFeature(0.05);
		ins2.addDobFeature(1.07);
		ins2.addDobFeature(1.14);
		ins2.addDobFeature(1.25);
		ins2.addDobFeature(0.08);
		ins2.addDobFeature(2.02);
		ins3.addDobFeature(1.5);
		ins3.addDobFeature(1.80);
		ins3.addDobFeature(3.0);
		ins3.addDobFeature(5.69);
		ins3.addDobFeature(1.40);
		cluster1.addInstance(ins1);
		cluster1.addInstance(ins2);
		cluster1.addInstance(ins3);
		Instance centroid = cluster1.calcCentroid();
		assertTrue(Math.round(centroid.getDobFeatures().get(0)*Math.pow(10, 2))/Math.pow(10, 2)==1.52 && Math.round(centroid.getDobFeatures().get(1)*Math.pow(10, 2))/Math.pow(10, 2)==1.4);
	}

}
