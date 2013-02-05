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
		return parseCSV(line, null);
	}
	
	/**
	 * @precondition The length of the schema equals that columns contained
	 * 				in the line
	 * @param line
	 * @param schema
	 * @return
	 */
	public CXNode parseCSV(String line, CXCSVSchemaNode schema)
	{
		assert(line != null);
		String strs[] = splitLine(line);
		
		// for empty column names, generate column names based on 
		// the size of the line
		if (schema == null)
		{
			schema = new CXCSVSchemaNode(0, strs.length-1, 
					DEFAULT_ELEMENT_NAME, generateColNames(strs.length));
		} else
		{
			// check integrity
			assert(strs.length == (schema.endIdx - schema.startIdx + 1));
		}
		
		return generateSubNodes(strs, schema);
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
	private CXNode generateSubNodes(String[] strs, CXCSVSchemaNode schema)
	{
		CXNode root = new CXNode(schema.name);
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
