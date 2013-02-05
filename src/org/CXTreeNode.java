package org;

import java.util.List;

public abstract class CXTreeNode<T extends CXTreeNode>{
	T parent;
	List<T> children;
	
	/**
	 * @side_effect will change the parent attribute of the new node to this
	 * 				tree node.
	 * @param node
	 */
	public void addChild(T node)
	{
		assert(node != null);
		
		if (children == null)
			children = new java.util.ArrayList<T>();
		
		children.add(node);
		node.parent = this;
	}
	
	public T removeChild(T node)
	{
		throw new RuntimeException("Method not implemented.");
	}
	
	public T removeChild(int idx)
	{
		throw new RuntimeException("Method not implemented.");
	}

	public T getParent() {
    	return parent;
    }

	public List<T> getChildren() {
    	return children;
    }

}