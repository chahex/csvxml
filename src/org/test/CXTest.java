package org.test;

import org.CXCSVParser;
import org.CXCSVSchemaNode;
import org.CXNode;
import org.CXmlFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CXTest {
	
	static String[] NAMES = {"<id>", "&firstname", "\"lastname\"", "weaponheld", 
		"battalian"};
	static String DATA = "1,xinkai,he,ak-47,m213";
	
	CXCSVSchemaNode schema;
	CXCSVParser parser;
	CXNode node;
	
	CXmlFormatter formatter;
	
	@Before
	public void setUp() throws Exception {
		schema = new CXCSVSchemaNode(0, 
				NAMES.length - 1, "root");
		
		CXCSVSchemaNode child = new CXCSVSchemaNode(1, 2, "knownas");
		schema.addChild(child);
		
		parser = new CXCSVParser();
		node = parser.parseCSV(DATA, schema, NAMES);
		
		formatter = new CXmlFormatter();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSchema() {
		
		System.out.println(schema);
	}
	
	@Test
	public void testParser() {
		CXNode node = parser.parseCSV(DATA, schema, NAMES);
		System.out.println(node);
	}
	
	@Test
	public void test2XML() {
		System.out.println(formatter.formatNode2XML(node));
	}
}
