package org.test;

import java.util.Random;

import org.CXCSVParser;
import org.CXSchemaNode;
import org.Util;

public class Reuse {
	
	static final int MAX_COLUMN_SIZE = 100;
	static final int MAX_SIBLINGS = 5;
	static final int MAX_TREE_DEPTH = 5;
	static final float FACTOR = 0.8f;
	
	static Random rdm = new Random();
	
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
	public static String[] generateStringColumn(int size)
	{
		String[] cols = new String[size];
		for (int i = 0; i < size; i++)
		{
			cols[i] = String.valueOf(i);
		}
		return cols;
	}
	
	public static String generateRandomCSVLine(int size)
	{
		assert(size > 0);
		StringBuilder sb = new StringBuilder();
		int hasNull = rdm.nextInt(3);

		if (hasNull == 0)
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
	
	private static String number2WordOld(int number)
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
	
	public static void main(String[] args) {
	    //System.out.println(generateRandomCSVLine(10));
		for (int i = 0; i< 26 * 26; i++)
			System.out.println(number2Word(i));
    }

}
