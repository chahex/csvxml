package org;

import java.util.Collections;
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
public class CXSchemaNode extends CXTreeNode<CXSchemaNode> 
implements Comparable<CXSchemaNode> {

	// can't be null or empty
	private String name = CXSchema.DEFAULT_NODE_NAME;
	// must have a valid value that related to a specific column in the CSV
	// file
	private Integer contentSrc;

	public String getName() {
		return name;
	}

	public Integer getContentSrc() {
		return contentSrc;
	}

	/**
	 * 
	 * @param name
	 * @throws <code>NullPointerException</code> If name is null
	 * @throws <code>IllegalArgumentException</code> If name is empty string
	 */
	public void setName(String name) {
		if (name == null)
			throw new 
			NullPointerException("Name of schema node can't be null.");
		if (name.length() == 0)
			throw new 
			IllegalArgumentException("Name of schema node can't be empty.");
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
	 * Compare equality of children, a sort is executed thus sequence of child
	 * is not considered.
	 * 
	 * @param n1
	 * @param n2
	 * @return
	 */
	private boolean compareChildren(CXSchemaNode n1, CXSchemaNode n2)
	{
		int size = n1.childrenCount();
		if (size != n2.childrenCount())
		{
			return false;
		}
		
		List<CXSchemaNode> cl1 = n1.getChildren();
		List<CXSchemaNode> cl2 = n2.getChildren();
		
		Collections.sort(cl1);
		Collections.sort(cl2);

		for (int i = 0; i < size; i++)
		{
			CXSchemaNode sn1 = cl1.get(i);
			CXSchemaNode sn2 = cl2.get(i);
			if (sn1.compareTo(sn2) != 0)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Two schema nodes are equal if and only if
	 * * They are the same object
	 * * The contentSrc fields are same
	 * * The name fields are same
	 * * The children schemas are same
	 */
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
	    return compareChildren(this, other);
    }

	/**
	 * Comparison of two schema nodes are solely 
	 * based on name and contentSrc attributes.<br>
	 * <br>
	 * if this.name.compareTo(n.name) does not equal to 0
	 * return the comparsion result<br>
	 * 
	 * if one of the contentSrc attribute is null, it is smaller
	 * if both are null, return 0
	 * Otherwise return this.contentSrc.compareTo(n.contentSrc)
	 * 
	 * @return comparsion result
	 */
	@Override
    public int compareTo(CXSchemaNode n) {
		int result = this.name.compareTo(n.name);

		if (result != 0)
		{
			return result;
		}
		if (this.contentSrc != null && n.contentSrc != null)
		{
			return this.contentSrc.compareTo(n.contentSrc);
		}
		if (this.contentSrc != null)
			return 1;
		if (n.contentSrc != null)
			return -1;
	    return 0;
    }
}
