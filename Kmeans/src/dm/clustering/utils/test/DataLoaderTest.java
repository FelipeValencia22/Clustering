package dm.clustering.utils.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dm.clustering.utils.DataLoader;
import dm.core.Instance;

public class DataLoaderTest {

	private ArrayList<Instance> instances;
	@Before
	public void setUp() throws Exception 
	{
		instances = new ArrayList<Instance>();
	}

	@After
	public void tearDown() throws Exception {
		instances = null;
	}

	@Test
	public void testLoadData() 
	{
		instances = DataLoader.getMiLoader().loadCSVNumericData("data/bank-data.csv", 2);
		Instance ins = instances.iterator().next();
		System.out.println(instances.size());
		assertTrue(instances.size()>0);
		assertFalse(ins.getDobFeatures().contains("YES"));
		assertFalse(ins.getDobFeatures().contains("FEMALE"));
		assertTrue(ins.getDobFeatures().contains(48.0));
		assertTrue(ins.getDobFeatures().contains(17546.0));
	}

}
