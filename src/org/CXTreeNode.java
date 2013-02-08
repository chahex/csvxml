package org;

import java.util.ArrayList;
import java.util.List;

/**
 * The tree node used to implement both CXNode and CXSchemaNode
 * @author xinkaihe
 *
 * @param <T>
 */
public abstract class CXTreeNode<T extends CXTreeNode>{
	T parent;
	List<T> children = new ArrayList<T>();
	
	/**
	 * @side_effect will change the parent attribute of the new node to this
	 * 				tree node.
	 * @param node
	 */
	public void addChild(T node)
	{
		assert(node != null);
		
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
		return new ArrayList<T>(children);
    }

}