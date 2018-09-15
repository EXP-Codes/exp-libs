package exp.libs.utils.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exp.libs.utils.format.ESCUtils;

public class TestEscUtils {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToXmlEsc() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnXmlEsc() {
		fail("Not yet implemented");
	}

	@Test
	public void testToJavaEsc() {
		fail("Not yet implemented");
	}

	@Test
	public void testToJsonEsc() {
		fail("Not yet implemented");
	}

	@Test
	public void testTestJson() {
		fail("Not yet implemented");
	}

	@Test
	public void testToJson() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnJson() {
		fail("Not yet implemented");
	}

	@Test
	public void testToBCPListOfListOfObjectStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testToBCPListOfObjectString() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnBCPStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnBCPStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testToCSV() {
		fail("Not yet implemented");
	}

	@Test
	public void testToCsv() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnCSV() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnCsv() {
		fail("Not yet implemented");
	}

	@Test
	public void testToTSV() {
		fail("Not yet implemented");
	}

	@Test
	public void testToTsv() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnTSV() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnTsv() {
		fail("Not yet implemented");
	}

	@Test
	public void testToTXT() {
		System.out.println(ESCUtils.toTXT(null, true));
		
		List<List<Object>> table = new ArrayList<List<Object>>();
		List<Object> head = new ArrayList<Object>();
		List<Object> row1 = new ArrayList<Object>();
		List<Object> row2 = new ArrayList<Object>();
		System.out.println(ESCUtils.toTXT(table, true));
		
		table.add(head);
		table.add(row1);
		table.add(row2);
		System.out.println(ESCUtils.toTXT(table, true));
		
		head.add("id");
		head.add("name");
		head.add("address");
		head.add("time");
		System.out.println(ESCUtils.toTXT(table, true));
		
		row1.add(2);
		row1.add("exp");
		row1.add("广东省");
		row1.add(new Date());
		row1.add("test");
		System.out.println(ESCUtils.toTXT(table, false));
		
		row2.add(1);
		row2.add("sky");
		row2.add("测试");
		row2.add(new Date(0));
		
		System.out.println(ESCUtils.toTXT(table, true));
	}

}
