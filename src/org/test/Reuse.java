package org.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.CXCSVParser;
import org.CXNode;
import org.CXSchemaNode;
import org.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Reuse {

	static final int MAX_COLUMN_SIZE = 100;
	static final int MAX_SIBLINGS = 5;
	static final int MAX_TREE_DEPTH = 5;
	static final float FACTOR = 0.8f;

	static Random rdm = new Random();
	
	static DocumentBuilderFactory dbf;
	static DocumentBuilder db;

	static {
		dbf = DocumentBuilderFactory
				.newInstance();
		try {
	        db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
	        e.printStackTrace();
        }
	}

	/**
	 * Generate a random CXSchemaNode, based on the generation configuration.
	 *
	 * @param conf
	 * @return
	 */
	public static CXSchemaNode generateRandomSchemaNode(SchemaGenConf conf)
	{
		int depth = 0;
		CXSchemaNode schema = generateSchemaNode(conf, depth, null, 0);
		while (conf.colRemoved <= conf.colSize * FACTOR)
		{
			schema.addChild(generateSchemaNode(conf, depth,
					schema.getName(), schema.childrenCount()));
		}
		return schema;
	}
	
	/**
	 * Generate a String array from 0 to size-1.
	 * Where element at index i have value of String.valueOf(i)
	 * 
	 * @param size the size of the string array
	 * @return
	 */
	public static String[] generateStringColumn(int size)
	{
		String[] cols = new String[size];
		for (int i = 0; i < size; i++)
		{
			cols[i] = String.valueOf(i);
		}
		return cols;
	}

	/**
	 * Generate a line of CSV using random string.
	 * 2/3 of the probability there are null values between columns.
	 * However, the generation process guaranteed the first column's value
	 * shall never be null.
	 *
	 * @param size
	 * @return
	 */
	public static String generateRandomCSVLine(int size)
	{
		assert(size > 0);
		StringBuilder sb = new StringBuilder();
		int hasNull = rdm.nextInt(3);

		if (hasNull == 0 || size == 1)
		{
			for (int i = 0; i < (size - 1); i++)
			{
				String str = Util.randomString();
				sb.append(str);
				sb.append(CXCSVParser.CSV_SEPARATOR);
			}
			sb.append(Util.randomString());
		}
		else
		{
			int nullIdx = 0;
			while((nullIdx = rdm.nextInt(size)) == 0);

			for (int i = 0; i < nullIdx; i++)
			{
				sb.append(Util.randomString());
				sb.append(CXCSVParser.CSV_SEPARATOR);
			}

			if (nullIdx != size - 1)
				sb.append(CXCSVParser.CSV_SEPARATOR);

			for (int i = (nullIdx + 1); i < (size - 1) ; i++)
			{
				sb.append(Util.randomString());
				sb.append(CXCSVParser.CSV_SEPARATOR);
			}
			sb.append(Util.randomString());
		}

		return sb.toString();
	}

	private static CXSchemaNode generateSchemaNode(SchemaGenConf conf,
			int depth,
			final String parentNo, final int childNo)
	{
		if (depth > conf.maxDepth)
			return null;

		conf.nodeCount++;

		String name = parentNo == null ?
				String.format("%s", number2Word(childNo)) :
					String.format("%s_%s", parentNo, number2Word(childNo));
		CXSchemaNode schema = new CXSchemaNode();
		schema.setName(name);
		schema.setContentSrc(conf.nextColumnIndex());

		int childCount = rdm.nextInt(MAX_SIBLINGS);
		if (childCount != 0 && ++depth <= conf.maxDepth)
		{
			for (int i = 0; i < childCount; i++)
			{
				schema.addChild(generateSchemaNode(conf, depth, name, i));
			}
		}
		return schema;
	}

	/**
	 * Convert a number to 26based number using A-Z
	 * 
	 * @param number
	 * @return
	 */
	public static String number2Word(int number)
	{
		if (number < 0)
			throw new
			IllegalArgumentException("Negative number conversion unsupported.");
		StringBuilder sb = new StringBuilder();
		number2Word(number, sb);
		return sb.toString();
	}

	private static void number2Word(int number, StringBuilder sb)
	{
		final int factor = 26;
		int division = number / factor;
		if (division == 0)
		{
			sb.append((char)('A' + number));
			return;
		}
		number2Word(division, sb);
		sb.append((char)('A' + number % factor));
	}

	public static String number2WordOld(int number)
	{
		if (number < 0)
			throw new
			IllegalArgumentException("Negative number conversion unsupported.");
		StringBuilder sb = new StringBuilder();
		final int factor = 26; // length from A-Z
		int division = number;
		while((division /= factor) != 0){
			int residue = number -  division * 26;
			sb.append((char)('A' + residue));
			number = division;
		};
		sb.append((char)('A'+(number-division * 26)));
		return sb.reverse().toString();
	}

	/**
	 * Compare an XML node's content with the CSV line using CXSchemaNode
	 * 
	 * The comparision is assuming each tag output has distinct name.
	 * 
	 * @param node
	 * @param schema
	 * @param csvLine
	 */
	public static void compareTest(Node node,
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

	/**
	 * Compare an CXNode parsed with the CSV line using CXSchemaNode
	 * 
	 * The comparision is assuming each CXNode has distinct names.
	 * 
	 * @param node
	 * @param schema
	 * @param csvLine
	 */
	public static void 
	compareTest(CXNode node, CXSchemaNode schema, String csvLine)
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

	public static void main(String[] args) {
	    //System.out.println(generateRandomCSVLine(10));
		for (int i = 0; i< 26 * 26; i++)
			System.out.println(number2Word(i));
    }
	
}
