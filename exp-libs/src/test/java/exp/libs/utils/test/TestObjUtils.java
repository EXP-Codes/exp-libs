package exp.libs.utils.test;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exp.libs.utils.other.ObjUtils;

public class TestObjUtils {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToObj() {
		fail("Not yet implemented");
	}

	@Test
	public void testToStrObjectClassOfQ() {
		fail("Not yet implemented");
	}

	@Test
	public void testToStrObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsSubclass() {
		fail("Not yet implemented");
	}

	@Test
	public void testCloneObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testInstanceClass() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllChildClass() {
		fail("Not yet implemented");
	}

	@Test
	public void testToBean() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("number", 1234);
		map.put("name", "sky");
		map.put("id", 22);
		Person p = (Person) ObjUtils.toBean(map, Person.class);
		
		System.out.println(p.getName());
		System.out.println(p.getNumber());
	}

}
