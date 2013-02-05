package org;

import java.util.Iterator;

/**
 * This class defines the internal structure of a line of CSV.
 * Each node will also have a list of children nodes and a parent node.
 * 
 * Initially, all the column will be mapped to nodes instead of attributes.
 * 
 * @author xinkaihe
 *
 */
public class CXCSVSchemaNode extends CXTreeNode<CXCSVSchemaNode>{

	/** Start point (column index) of the node */
	int startIdx;
	/** End point (column index) of the node */
	int endIdx;
	/** The name of this node */
	String name;
	
	/**
	 * The column at endIdx will not be included
	 * 
	 * @param startIdx
	 * @param endIdx
	 * @param name
	 * @param childNames might be null; should have more than endIdx children
	 *  if not null.
	 */
	public CXCSVSchemaNode (int startIdx, int endIdx, String name, 
			String[] childNames)
	{
		assert(startIdx <= endIdx);
		
		this.startIdx = startIdx;
		this.endIdx = endIdx;
		this.name = name;
		if (startIdx == endIdx)
			return;

		assert(childNames.length > endIdx);
		
		for (int i = startIdx; i <= endIdx; i++)
		{
			this.addChild(new CXCSVSchemaNode(i, i, childNames[i], null));
		}
	}
	
	@Override
	public void addChild(CXCSVSchemaNode node) {
		
		if (children != null)
		{
			// remove the children at column overlap the new child
			Iterator<CXCSVSchemaNode> ite = children.iterator();
		    for (CXCSVSchemaNode child = null; ite.hasNext();)
		    {
		    	child = ite.next();
		    	if (child.startIdx >= node.startIdx 
		    			&& child.endIdx <= node.endIdx)
		    	{
		    		ite.remove();
		    	}
		    }
		}
	    super.addChild(node);
	}

	@Override
    public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append("CXCSVSchemaNode [startIdx=");
	    builder.append(startIdx);
	    builder.append(", endIdx=");
	    builder.append(endIdx);
	    builder.append(", name=");
	    builder.append(name);
	    builder.append(", children=");
	    builder.append(children);
	    builder.append(", parent=");
	    builder.append(parent == null? "null":parent.name);
	    builder.append("]");
	    return builder.toString();
    }
}