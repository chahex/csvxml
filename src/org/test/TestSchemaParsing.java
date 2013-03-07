package org.test;

import static junit.framework.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.CXCSVParser;
import org.CXNode;
import org.CXSchema;
import org.CXSchemaNode;
import org.CXSchemaParser;
import org.CXmlFormatter;
import org.Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test that schema can work with
 *
 * @author xinkaihe
 *
 */
public class TestSchemaParsing {

	CXSchemaParser schemaParser = new CXSchemaParser();
	CXCSVParser csvParser = new CXCSVParser();
	CXmlFormatter formatter = new CXmlFormatter();

	@Before
	public void setUp()
	{
	}

	@After
	public void tearDown()
	{
	}

	@Test
	public void testCXSchemaParsing()
	throws Exception
	{
		/**
		 * 1. Generate a schema
		 * 2. Schema to CXNode
		 * 3. CXNode to XML element
		 * 4. Output the XML
		 * 5. Parse the XML - test the parsing process
		 * 6. Compare the schema parsed with the original schema
		 */
		for (int i = 1; i < 1000; i+=10)
		{
			SchemaGenConf conf = new SchemaGenConf(i);
			CXSchemaNode schemaNode = Reuse.generateRandomSchemaNode(conf);
			CXSchema schema = new CXSchema();
			schema.setSchemaNode(schemaNode);
			schema.setRootName(Util.randomString());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(bos);
			outputSchema2XML(schema, formatter, writer);
			writer.close();
			byte[] obytes = bos.toByteArray();
			ByteArrayInputStream bis = new ByteArrayInputStream(obytes);
			CXSchema schema2 = schemaParser.parseSchema(bis);
			assertEquals(schema, schema2);
			bis.close();
		}
	}

	private void outputSchema2XML(CXSchema schema,
			CXmlFormatter formatter,
			OutputStreamWriter writer)
	throws IOException
	{
		assert(schema != null);
		assert(writer != null);

		CXNode node =
				schemaParser.generateCXNodeFromSchema(schema);
		writer.append(formatter.formatNode2XML(node));
	}

}








