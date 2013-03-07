package org.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.CXCSVParser;
import org.CXNode;
import org.CXSchemaNode;
import org.CXmlFormatter;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TestCSVParsing {

	CXCSVParser parser = new CXCSVParser();
	CXmlFormatter formatter = new CXmlFormatter();

	@Test
	public void testCSVParsing()
	{
		/**
		 * 1. Generate size of columns
		 * 2. Generate random schema
		 * 3. Generate a line of column content
		 * 4. Parse it using schema to CXNode
		 * 5. Compare the CXNode content
		 * 		generated with the columns using schema
		 *
		 * The content should be same since the uniqueness of node name
		 */
		for (int i = 0; i < 1000; i++)
		{
			int colSize = 100;

			SchemaGenConf conf = new SchemaGenConf(colSize);
			CXSchemaNode schemaNode = Reuse.generateRandomSchemaNode(conf);

			String csvLine = Reuse.generateRandomCSVLine(colSize);

			CXNode parsedNode = parser.parseCSV(csvLine, schemaNode);
			Reuse.compareTest(parsedNode, schemaNode, csvLine);
		}
	}

	@Test
	public void testXMLFormatter()
	throws IOException, SAXException
	{
		/**
		 * 1. Generate CXNode, format and output as XML
		 * 2. Parse the XML buid CXNode structure
		 * 3. Call compare test with the CXNode parsed from XML
		 */

		int colSize = 1000;
		for (int i = 0; i < 100; i++)
		{
		SchemaGenConf conf = new SchemaGenConf(colSize);
		CXSchemaNode schemaNode = Reuse.generateRandomSchemaNode(conf);

		String csvLine = Reuse.generateRandomCSVLine(colSize);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(bos);
		writer.write(formatter.formatNode2XML(
				parser.parseCSV(csvLine, schemaNode)));
		writer.close();
		byte[] obytes = bos.toByteArray();
		ByteArrayInputStream bis = new ByteArrayInputStream(obytes);
		Document doc = Reuse.db.parse(bis);
		Reuse.compareTest(doc.getDocumentElement(), schemaNode, csvLine);
		}
	}

}
