package dm.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;







import dm.clustering.utils.Normalizer;
import dm.core.Instance;

public class NormalizerTest {
	
	
	private Instance ins1;
	private Instance ins2;
	private Instance ins3;
	private ArrayList<Instance> instances;
	@Before
	public void setUp() throws Exception 
	{
		ins1 = new Instance();
		ins2 = new Instance();
		ins3 = new Instance();
		instances = new ArrayList <Instance>();
		
		ins1.addDobFeature(10.0);
		ins1.addDobFeature(3.0);
		ins1.addDobFeature(2.0);
		
		ins2.addDobFeature(9.0);
		ins2.addDobFeature(4.0);
		ins2.addDobFeature(4.0);
		
		ins3.addDobFeature(12.0);
		ins3.addDobFeature(5.0);
		ins3.addDobFeature(1.0);
		
		instances.add(ins1);
		instances.add(ins2);
		instances.add(ins3);
	}

	@After
	public void tearDown() throws Exception 
	{
		ins1 = null;
		ins2 = null;
		ins3 = null;
		instances = null;
	}
	
	@Test
	public void testNormalize() {
		boolean rdo = true;
		Normalizer norm = new Normalizer();
		norm.normalize(instances);
		
		for (int i =0; i<instances.size();i++)
		{
			for (int j=0; j<instances.get(0).numFeatures(); j++)
			{
				if (instances.get(i).getAtt(j)>1.0)
					rdo = false;
			}
		}
		
		assertTrue(rdo);
		
		
		
	}
	@Test
	public void testMean(){
		Normalizer norm = new Normalizer();
		double res = norm.mean(instances, 0);
		assertEquals(Double.valueOf(res),Double.valueOf(31.0/3.0));
		
		
	}
	@Test
	public void testStdev(){
		Normalizer norm = new Normalizer();
		double mean = norm.mean(instances, 0);
		double res = norm.stdev(instances, 0, mean,true);
		
		double calc =Math.sqrt(((Math.pow((10.0-mean),2) + Math.pow((9.0-mean),2) + Math.pow((12.0-mean),2))/3));
		
		assertEquals(Double.valueOf(res),Double.valueOf(calc));
		
	}

}
