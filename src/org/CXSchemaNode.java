package org;

import java.util.List;

/**
 * CXSchemaNode defines a structure template for XML output.
 * It includes following features:<br>
 * 
 * 	* One CXSchemaNode is mapped to one XML documet tag (maybe extended to 
 * 		attributes as well)
 *  * name field indicates the name of the tag
 *  * contentSrc field indicates the column number from the source CSV file
 *  * children are children tags that have the same structure
 *
 * @author xinkaihe
 *
 */
public class CXSchemaNode extends CXTreeNode<CXSchemaNode> {

	// can't be null or empty
	private String name = "";
	// must have a valid value that related to a specific column in the CSV
	// file
	private Integer contentSrc;

	/**
	 * The overriden method will sort the children array while inserting.
	 * However, the CXTreeNode(super)'s addChild method will easily 
	 * disrupt this order if called upon this object using reflection.
	 */
	@Override
	public void addChild(CXSchemaNode node) {
		assert(node != null);
		int i = 0;
		int chldSize = this.children.size();
		boolean found = false;
		for (i = 0; i < chldSize; i++)
		{
			CXSchemaNode child = children.get(i);
			if (child.getName().compareTo(node.getName()) > 0)
			{
				found = true;
				break;
			}
		}
		if (found)
			children.add(i, node);
		else
			children.add(node);
		node.parent = this;
	}

	public String getName() {
		return name;
	}

	public Integer getContentSrc() {
		return contentSrc;
	}

	public void setName(String name) {
		if (name == null)
			throw new NullPointerException("Name of schema node can't be null.");
		this.name = name;
	}

	public void setContentSrc(Integer contentSrc) {
		this.contentSrc = contentSrc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CXSchemaNode [name=");
		builder.append(name);
		builder.append(", contentSrc=");
		builder.append(contentSrc);
		builder.append(", parent=");
		builder.append(parent == null ? "null" : parent.name);
		builder.append(", children=");
		builder.append(children);
		builder.append("]");
		return builder.toString();
	}

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result
	            + ((contentSrc == null) ? 0 : contentSrc.hashCode());
	    result = prime * result + ((name == null) ? 0 : name.hashCode());
	    return result;
    }

	/**
	 * The children list must be sorted based on name attribute
	 * The parent field of the child are not compared. This comparasion
	 * is based on assumption all children nodes are inserted based on
	 * 
	 *
	 * @param l1
	 * @param l2
	 * @return
	 */
	private boolean compareChildren(List<CXSchemaNode> l1,
			List<CXSchemaNode> l2)
	{
		if (l1.size() != l2.size())
		{
			return false;
		}
		for (int i = 0; i < l1.size(); i++)
		{
			CXSchemaNode n1 = l1.get(i);
			CXSchemaNode n2 = l2.get(i);
			if (!n1.equals(n2))
				return false;
		}
		return true;
	}

	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    CXSchemaNode other = (CXSchemaNode) obj;
	    if (contentSrc == null) {
		    if (other.contentSrc != null)
			    return false;
	    } else if (!contentSrc.equals(other.contentSrc))
		    return false;
	    if (name == null) {
		    if (other.name != null)
			    return false;
	    } else if (!name.equals(other.name))
		    return false;
	    return compareChildren(this.children, other.children);
    }
}
