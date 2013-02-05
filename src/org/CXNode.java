package org;

public class CXNode extends CXTreeNode<CXNode>{
	
	java.util.Map<String, String> attributes; 
			//= new java.util.HashMap<String, String>();

	String name;
	@Override
    public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append("CXNode [attributes=");
	    builder.append(attributes);
	    builder.append(", name=");
	    builder.append(name);
	    builder.append(", content=");
	    builder.append(content);
	    builder.append(", children=");
	    builder.append(children);
	    builder.append(", parent=");
	    builder.append(parent == null ? "null" : parent.name);
	    builder.append("]");
	    return builder.toString();
    }

	String content;
	
	public CXNode(String name)
	{
		this.name = name;
	}
	
	public void addAttribute(String key, String val)
	{
		if (attributes == null)
			attributes = new java.util.HashMap<String, String>();
		attributes.put(key, val);
	}
	
	public String removeAttribute(String key)
	{
		throw new RuntimeException("Method not implemented.");
	}

	public java.util.Map<String, String> getAttributes() {
    	return attributes;
    }

	public String getName() {
    	return name;
    }

	public String getContent() {
    	return content;
    }

	public void setAttributes(java.util.Map<String, String> attributes) {
    	this.attributes = attributes;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public void setContent(String content) {
    	this.content = content;
    }

}

