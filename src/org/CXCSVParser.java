package org;

import java.util.List;

/**
 * Provide functionalities to parse a line of CSV to CXNode, based on schema
 * provided or no schema. 
 *  
 * @author xinkaihe
 *
 */
public class CXCSVParser{
	
	public final static String CSV_SEPARATOR = ",";
	public final static String DEFAULT_ELEMENT_NAME = "element";
	public final static String DEFAULT_COL_NAME_PREFIX = "column";

	public CXNode parseCSV(String line)
	{
		assert(line != null);
		return parseCSV(line, null, null);
	}
	
	/**
	 * @precondition The length of the schema equals that columns contained
	 * 				in the line
	 * @param line
	 * @param schema
	 * @return
	 */
	public CXNode parseCSV(String line, CXCSVSchemaNode schema, 
			String[] colNames)
	{
		assert(line != null);
		String strs[] = splitLine(line);
		
		// for null column names, generate column names based on 
		// the size of the line
		if (schema == null)
		{
			schema = new CXCSVSchemaNode(0, strs.length-1, 
					DEFAULT_ELEMENT_NAME);
		} else
		{
			// check integrity
			assert(strs.length == (schema.endIdx - schema.startIdx + 1));
		}
		if (colNames == null)
		{
			colNames = generateColNames(strs.length);
		}
		
		return generateSubNodes(strs, schema, colNames);
	}
	
	/**
	 * Generate an output element using the more flexible schema
	 * 
	 * @param line
	 * @param schema
	 * @return
	 */
	public CXNode parseCSV(String line, CXSchemaNode schema)
	{
		assert(line != null);
		String strs[] = splitLine(line);
		return parseCSV(strs, schema);
	}

	public CXNode parseCSV(String[] cols, CXSchemaNode schema)
	{
		try{
			return generateSubNodes(cols, schema);
		}catch(IndexOutOfBoundsException e)
		{
			// warning or error
			throw e;
		}
	}

	private String[] generateColNames(int length)
	{
		assert(length >= 0);
		String[] colNames = new String[length];
		for (int i = 0; i < length; i++)
		{
			colNames[i] = String.format("%s_%d", DEFAULT_COL_NAME_PREFIX,
					i);
		}
		return colNames;
	}
	
	/**
	 * No content and child nodes at the same time
	 * 
	 * @precondition schema not null
	 * @param strs
	 * @param schema
	 * @return
	 */
	private CXNode generateSubNodes(String[] strs, CXCSVSchemaNode schema, 
			String[] colNames)
	{
		String nodeName = schema.name;
		// by default, if node name not specified by schema node, it will
		// be the name of the its initial column's name
		if (nodeName == null)
		{
			nodeName = colNames[schema.startIdx];
		}
			
		CXNode root = new CXNode(nodeName);
		List<CXCSVSchemaNode> childrenSchema = schema.children;
		if (childrenSchema == null)
		{
			// the leaf node
			root.content = strs[schema.startIdx];
		} else
		{
			for (int i = 0; i < childrenSchema.size(); i++)
			{
				CXCSVSchemaNode childSchema = childrenSchema.get(i);
				CXNode child = generateSubNodes(strs, childSchema, colNames);
				root.addChild(child);
			}
		}
		return root;
	}
	
	private CXNode generateSubNodes(String[] strs, CXSchemaNode schema)
	{
		String nodeName = schema.getName();
		Integer contentSrc = schema.getContentSrc(); 
		List<CXSchemaNode> childrenSchema = schema.getChildren();
		int childrenSize = childrenSchema.size();

		CXNode root = new CXNode(nodeName);
		// node that have content
		if (contentSrc != null)
			root.content = strs[contentSrc];

		if (childrenSize != 0)
		{
			for (int i = 0; i < childrenSize; i++)
			{
				CXSchemaNode childSchema = childrenSchema.get(i);
				CXNode child = generateSubNodes(strs, childSchema);
				root.addChild(child);
			}
		}
		return root;
	}
	
	/**
	 * Helper method to split the line. In the production version, will have
	 * full capability to process escape chars and quotation marks.
	 * (possibly through using appropriate regex)
	 * 
	 * @param line
	 * @return
	 */
	private String[] splitLine(String line)
	{
		return line.split(CSV_SEPARATOR);
	}

}
