package org.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.CXSchema;
import org.CXSchemaNode;
import org.CXSchemaParser;
import org.CXmlFormatter;
import org.junit.Before;
import org.junit.Test;
import org.main.CXConverter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Automatic test for conversion between CSV and XML using a randomly generated
 * schema definition. 
 *
 *
 * @author xinkaihe
 *
 */
public class CXTest {
	
	static int CSV_SIZE = 100;
	static int COL_SIZE = 100;
	
	CXmlFormatter formatter = new CXmlFormatter();
	CXSchemaParser schemaParser = new CXSchemaParser();
	
	SchemaGenConf conf;
	CXSchema schema;
	
	@Before
	public void setUp()
	throws Exception
	{
		conf = new SchemaGenConf(COL_SIZE);
		CXSchemaNode snode = Reuse.generateRandomSchemaNode(conf);
		schema = new CXSchema();
		schema.setRootName("rdm_root_name");
		schema.setSchemaNode(snode);
	}
	

	/**
	 * Test for 100 times of conversions.
	 *
	 * @throws Exception
	 */
	//@Test
	public void test() throws Exception{
		for (int i = 0; i < 100; i++)
			test_whole_process();
	}

	/**
	 * Conversion test.
	 *
	 * @throws IOException
	 * @throws SAXException
	 */
	@Test
	public void test_whole_process()
			throws IOException, SAXException
	{
		/**
		 * 1. Generate a schema
		 * 2. Generate a CSV file
		 * 3. Parse the CSV file according to the schema to an XML file
		 * 4. Parse the XML file, (check the correctness)
		 * 
		 * Assume each line of the CSV mapped to an XML element output
		 */
		long curTime = System.currentTimeMillis();

		String schmfn = String.format("schm_%d.xml", curTime);
		String csvfn = String.format("csv_%d.csv", curTime);
		String xmlfn = String.format("xml_%d.xml", curTime);

		File schmf = new File(schmfn);
		File csvf = new File(csvfn);
		File xmlf = new File(xmlfn);

		// file not existed yet
		assertTrue((!schmf.exists()) && (!csvf.exists()) && (!xmlf.exists()));

		BufferedWriter out = null;

		// randomly generate a schema and write it to schema file
		try{
		out = new BufferedWriter(new FileWriter(schmf));
		out.write(formatter.formatNode2XML(schemaParser.
				generateCXNodeFromSchema(schema)));
		}finally{
			out.close();
		}

		// randomly generate a CSV document and write to CSV file
		try{
			out = new BufferedWriter(new FileWriter(csvfn));
			for (int i = 0; i < CSV_SIZE - 1; i++)
			{
				out.append(Reuse.generateRandomCSVLine(COL_SIZE));
				out.append('\n');
			}
			if (CSV_SIZE > 0)
				out.append(Reuse.generateRandomCSVLine(COL_SIZE));
		}finally{
			out.close();
		}

		// convert the CSV to XML and write to XML file using
		// schema generated. Return code should be 0 indicating success
		assertEquals(0, CXConverter.convert(csvfn, xmlfn, schmfn));

		// read the generated XML file
		Document doc = Reuse.db.parse(xmlf);
		Element root = doc.getDocumentElement();

		// root name of the XML generated should be as defined 
		assertEquals(schema.getRootName(), root.getTagName());

		/*
		 * Each child node of the root should be defined by the schema
		 * and the value should match the source CSV column of that line
		 * Assumptions:
		 * 		* Each XML tag generated has distinct tag names
		 * 		* The XML are output sequentially, with the same order of
		 * 			lines in the CSV file
		 */ 
		NodeList nl = root.getChildNodes();
		int i = 0;
		BufferedReader in = null;
		try{
				in = new BufferedReader(new FileReader(csvf));
		String str = null;
		while ((str = in.readLine()) != null)
		{
			Node child = null;
			while((child = nl.item(i++)).getNodeType() != Node.ELEMENT_NODE);
			Reuse.compareTest(child, schema.getSchemaNode(), str);
		}
		}finally{
			in.close();
		}
		//assertTrue(csvf.delete() & schmf.delete() & xmlf.delete());
		
	}

}
