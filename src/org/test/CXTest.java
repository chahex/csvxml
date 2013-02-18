package org.test;



import org.CXCSVParser;
import org.CXCSVSchemaNode;
import org.CXNode;
import org.CXSchemaExParser;
import org.CXmlFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.main.CXConverter;

public class CXTest {
	
	static String[] NAMES = {"<id>", "&firstname", "\"lastname\"", "weaponheld", 
		"battalian"};
	static String DATA = "1,xinkai,he,ak-47,m213";
	
	CXCSVSchemaNode schema;
	CXCSVParser parser;
	CXNode node;
	CXSchemaExParser schemaParser;
	
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
		
		schemaParser = new CXSchemaExParser();
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
	
	//@Test
	public void testConverter() {
		String[] args = {"wb1.csv","wb1.xml","roster","(people,0,3,((knownas,1,2)))"};
		CXConverter.main(args);
	}
	
	
	@Test
	public void testExplainer()
	{
		String exp =  "(a,0,10,((b,1,5,((d,6,7))),(c,2,4)))";
		CXCSVSchemaNode node = schemaParser.parseCXSchema(exp);
		System.out.println(schemaParser.explain2Expression(node));
	}
}
