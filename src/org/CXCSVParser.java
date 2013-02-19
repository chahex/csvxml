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
