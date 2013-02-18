package org.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.CXCSVParser;
import org.CXNode;
import org.CXSchemaNode;
import org.CXmlFormatter;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TestCSVParsing {
	
	CXCSVParser parser = new CXCSVParser();
	CXmlFormatter formatter = new CXmlFormatter();
	
	DocumentBuilderFactory dbf;
	DocumentBuilder db;
	
	{
		dbf = DocumentBuilderFactory
				.newInstance();
		try {
	        db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
	        e.printStackTrace();
        }
	}

	//@Test
	public void testCSVParsing()
	{
		/**
		 * 1. Generate columns
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
			compareTest(parsedNode, schemaNode, csvLine);
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
		Document doc = db.parse(bis);
		compareTest(doc.getDocumentElement(), schemaNode, csvLine);
		}
	}
	
	private void compareTest(Node node, 
			CXSchemaNode schema, String csvLine)
	{
		String[] cols = csvLine.split(",");
		Assert.assertEquals(schema.getName(), node.getNodeName());
		Integer csrc = schema.getContentSrc();

		NodeList cxlistnl = node.getChildNodes();
		int cxlSize = cxlistnl.getLength();
		List<Node> cxlist = new ArrayList<Node>();

		String nodeCt = null;

		for (int i = 0; i < cxlSize; i++)
		{
			Node cxlNode = cxlistnl.item(i);
			// TODO TODO TODO TODO
			if (cxlNode.getNodeType() == Node.TEXT_NODE)
			{
				String content = cxlNode.getTextContent().trim();
				if (content.length() != 0)
					nodeCt = content;
				continue;
			}
			if (cxlNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			int j = 0;
			int esize = cxlist.size();
			for (; j < esize; j++)
			{
				if (cxlist.get(j).getNodeName().
						compareTo(cxlNode.getNodeName()) > 0)
				{
					break;
				} else
				{
					if (j == esize - 1)
						j = esize;
				}
				cxlist.add(j, cxlNode);
			}
		}

		if (csrc != null)
		{
			Assert.assertTrue(nodeCt == null && cols[csrc].length() == 0 
					|| cols[csrc].equals(nodeCt));
		}else{
			Assert.assertTrue(nodeCt == null || nodeCt.length() == 0);
		}

		List<CXSchemaNode> sclist = schema.getChildren();
		
		Collections.sort(sclist, new Comparator<CXSchemaNode>() {
			@Override
			public int compare(CXSchemaNode o1, CXSchemaNode o2) {
				String name1 = o1.getName();
				String name2 = o2.getName();
				
			    return name1.compareTo(name2);
			}
		});
		
		for (int i = 0; i<cxlist.size(); i++)
		{
			compareTest(cxlist.get(i), sclist.get(i), csvLine);
		}
	}

	
	
	private void compareTest(CXNode node, CXSchemaNode schema, String csvLine)
	{
		String[] cols = csvLine.split(",");
		Assert.assertEquals(schema.getName(), node.getName());
		Integer csrc = schema.getContentSrc();
		String nodeCt = node.getContent();
		if (csrc != null)
		{
			Assert.assertEquals(cols[csrc], nodeCt);
		}else{
			Assert.assertTrue(nodeCt == null || nodeCt.length() == 0);
		}

		Assert.assertEquals(node.childrenCount(), schema.childrenCount());

		List<CXNode> cxlist = node.getChildren();
		List<CXSchemaNode> sclist = schema.getChildren();
		
		Collections.sort(cxlist, new Comparator<CXNode>() {
			@Override
			public int compare(CXNode o1, CXNode o2) {
				String name1 = o1.getName();
				String name2 = o2.getName();
				
			    return name1.compareTo(name2);
			}
		});

		Collections.sort(sclist, new Comparator<CXSchemaNode>() {
			@Override
			public int compare(CXSchemaNode o1, CXSchemaNode o2) {
				String name1 = o1.getName();
				String name2 = o2.getName();
				
			    return name1.compareTo(name2);
			}
		});
		
		for (int i = 0; i<cxlist.size(); i++)
		{
			compareTest(cxlist.get(i), sclist.get(i), csvLine);
		}
	}
}
