package dm.clustering.utils.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dm.clustering.utils.DataLoader;
import dm.core.Instance;

public class DataLoaderTest {

	ArrayList<Instance> instances;
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
		instances = DataLoader.getMiLoader().loadData("data/bank-data.csv", 2);
		assertTrue(instances.size()>0);
		
	}

}
