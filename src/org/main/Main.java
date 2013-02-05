package org.main;

import java.util.StringTokenizer;

import org.CXCSVSchemaNode;

public class Main {

	private static void printf(String format, Object ... args)
	{
		System.out.printf(format, args);
	}
	
	// (a,0,10,((b,1,5,((d,2,4))),(c,6,7)))
	// the output should be the CXNode that have multiple layer of 
	// child nodes
	// (name,startIndex,endIndex,(child nodes))
	private CXCSVSchemaNode parseCXSchema(String in)
	{
		assert(in!=null);
		return null;
	}
	
	/* Return the parsed length
	 * The startPt should be right after the "(" and is the place 
	 * the name start 
	 * */
	private int parseCXSchema(String in, int startPt, CXCSVSchemaNode parent)
	{
		assert(parent != null);
		int len = 0;
		int thisEnd = in.indexOf(",(", startPt);

		// no children nodes found
		if (thisEnd == -1)
		{
			thisEnd = in.length() - 1;
		}
		String[] vals = in.substring(startPt, thisEnd).split(",");
		CXCSVSchemaNode node = new CXCSVSchemaNode(Integer.parseInt(vals[1]), 
				Integer.parseInt(vals[2]), vals[0], null);
		return -1;
	}

	private void usage()
	{
		printf("CSV to XML converter\n");
	}

	public static void main(String[] args) {
	    
    }
}