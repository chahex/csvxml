package org;

public class CXSchema {
	
	public static final String DEFAULT_ROOT_NAME = "root";
	public static final String CXROOT = "cx";
	public static final String ROOT_NAME = "root_name";
	public static final String CXNODE = "cxnode";
	public static final String CXNODE_NAME = "name";
	public static final String CHILDREN = "children";
	public static final String CONTENT_SRC = "contentsrc";

	private String rootName = DEFAULT_ROOT_NAME;

	private CXSchemaNode schemaNode;

	public CXSchemaNode getSchemaNode() {
    	return schemaNode;
    }

	public void setSchemaNode(CXSchemaNode schemaNode) {
    	this.schemaNode = schemaNode;
    }

	public String getRootName() {
    	return rootName;
    }

	public void setRootName(String rootName) {
		if (rootName == null || rootName.length()==0)
			throw new NullPointerException("Root name specified by schema can't be null or empty.");
    	this.rootName = rootName;
    }

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result
	            + ((rootName == null) ? 0 : rootName.hashCode());
	    result = prime * result
	            + ((schemaNode == null) ? 0 : schemaNode.hashCode());
	    return result;
    }

	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    CXSchema other = (CXSchema) obj;
	    if (rootName == null) {
		    if (other.rootName != null)
			    return false;
	    } else if (!rootName.equals(other.rootName))
		    return false;
	    if (schemaNode == null) {
		    if (other.schemaNode != null)
			    return false;
	    } else if (!schemaNode.equals(other.schemaNode))
		    return false;
	    return true;
    }
	
}