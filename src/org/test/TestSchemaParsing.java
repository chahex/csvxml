package org.test;

import static junit.framework.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

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
	public void testCXNodeGeneration()
	{
		/**
		 *  1. generate column size
		 *  2. generate string array with that size
		 *  3. generate random CXSchemaNode based on column size
		 *  4. generate a CXNode based on the CXSchemaNode - 1 and the columns
		 *  5. extract CXSchemaNode - 2 from CXNode generated
		 *  6. - 1 must be equal to -2
		 */
		for (int i = 1; i <= 1000; i++)
		{
			testCXNodeGeneration(i);
		}
	}

	@Test
	public void testCXSchema()
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

	/**
	 * The structure of CXNode should be defined by CXSchemaNode.
	 * Which means:
	 	The name of the 
	 * 
	 * 
	 * @param colSize
	 */
	private void testCXNodeGeneration(int colSize)
	{
		String[] columns = Reuse.generateStringColumn(colSize);
		SchemaGenConf conf = new SchemaGenConf(colSize);
		CXSchemaNode schema = Reuse.generateRandomSchemaNode(conf);
		CXNode cxnode = csvParser.parseCSV(columns, schema);
		CXSchemaNode schema2 = inferSchemaNode(cxnode);
		assertEquals(schema, schema2);
	}

	private CXSchemaNode inferSchemaNode(CXNode node)
	{
		int maxContentSrc = 0;
		int nodeChildrenSize = node.childrenCount();
		int contentSrc = Integer.parseInt(node.getContent());

		if (contentSrc > maxContentSrc)
			maxContentSrc = contentSrc;

		String name = node.getName();
		CXSchemaNode schema = new CXSchemaNode();

		schema.setName(name);
		schema.setContentSrc(contentSrc);

		if (nodeChildrenSize > 0)
		{
			List<CXNode> nodeChildren = node.getChildren();
			for (int i = 0; i < nodeChildrenSize; i++)
			{
				schema.addChild(inferSchemaNode(nodeChildren.get(i)));
			}
		}

		return schema;
	}



}








