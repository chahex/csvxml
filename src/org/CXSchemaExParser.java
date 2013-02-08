package org;

import java.util.ArrayList;
import java.util.List;

/**
 * The converter uses a simple expression to specify mapping rule between
 * CSV columns and XML document objects. 
 * 
 * @author xinkaihe
 *
 */
public class CXSchemaExParser {
	
	// ([name],[start index],[end index],
		// (([child name],[child start index],[child end index]),([next child]))) 
		// (a,0,10,((b,1,5,((d,2,4))),(c,6,7)))
		// the output should be the CXNode that have multiple layer of 
		// child nodes
		// (name,startIndex,endIndex,(child nodes))
		public CXCSVSchemaNode parseCXSchema(String in)
		{
			assert(in!=null);
			CXCSVSchemaNode[] parent = new CXCSVSchemaNode[1];
			parseCXSchema(in, 0, parent);
			return parent[0];
		}
		
		/**
		 * Parse the expression to build CSVSchemaNode
		 * If the first element in the array passed in is null, then return
		 * the CXCSVSchemaNode parsed to the first element of the array;
		 * Otherwise add the parsed node as child node to the first element of the 
		 * array.
		 * 
		 * @param in The expression string needed to be parsed
		 * @param startPt The index the expression inside the string starts
		 * 		<br> The startPt should always be the index of a "("
		 * @param parent An CXCSVSchemaNode array with length of at least 1
		 * @return
		 */
		// (a,0,10,((b,1,5,((f,2,3))),(c,6,7),(d,8,9),(e,10,10)))
		private int parseCXSchema(String in, int startPt, CXCSVSchemaNode[] parent)
		{
			assert(parent != null);
			assert(parent.length >= 1);
			
			boolean hasChild = false;
			int thisEnd = in.indexOf(",(", startPt);
			
			// no children nodes found
			if (thisEnd == -1 || in.substring(startPt, thisEnd).contains(")"))
			{
				thisEnd = in.indexOf(")", startPt);
				// System.out.printf("StartPt:%d, endPt%d", startPt, thisEnd);
			} else {
				hasChild = true;
			}
			String[] vals = in.substring(startPt + 1, thisEnd).split(",");
			//System.out.println(Arrays.toString(vals));
			CXCSVSchemaNode node = new CXCSVSchemaNode(Integer.parseInt(vals[1]), 
					Integer.parseInt(vals[2]), vals[0]);
			if (hasChild)
			{
				// use a while loop in case multiple children
				thisEnd+=2;
				do{
					thisEnd += parseCXSchema(in, thisEnd, 
							new CXCSVSchemaNode[]{node});
					if (in.charAt((thisEnd)) == ',')
					{
						thisEnd += 1;
						continue;
					} else {
						break;
					}
				} while(true);
				// move the end to next ")"
				thisEnd += 1;
			}
			if (parent[0] == null)
			{
				parent[0] = node;
			} else
			{
				parent[0].addChild(node);
			}
			// return the length of the string already parsed
			return thisEnd - startPt + 1;
		}
		
		public String explain2Expression(CXCSVSchemaNode node)
		{
			assert(node != null);
			assert(node.name != null);
			StringBuilder sb = new StringBuilder();
			explainNode(node, sb);
			return sb.toString();
		}
		
		private void explainNode(CXCSVSchemaNode node, 
				StringBuilder sb)
		{
			assert(sb != null);
			// parent decides whether to explain or not
			// this node only print out the string
			sb.append(String.format("(%s,%d,%d", node.name, 
					node.startIdx, node.endIdx));
			List<CXCSVSchemaNode> children = node.getChildren();
			// append children
			if (children.size() != 0)
			{
				List<StringBuilder> csbList 
					= new ArrayList<StringBuilder>();
				// iterate through children
				for (CXCSVSchemaNode child : children)
				{
					// not append only when name is null and startIdx == endIdx
					// assumption: name can't be empty string
					if ((child.startIdx ==child.endIdx) && child.name == null)
					{
						continue;
					} else
					{
						StringBuilder csb = new StringBuilder();
						explainNode(child, csb);
						csbList.add(csb);
					}
				}
				int bound = csbList.size() - 1; // upper index of the array
				if (csbList.size() > 0)
				{
					sb.append(",(");
					for (int i = 0; i <= bound; i++)
					{
						sb.append(csbList.get(i));
						// append comma when not the last element
						if (i != bound)
						{
							sb.append(',');
						}
					}
					sb.append(")"); // print the ending parentheses of children
				}
			}
			// print the ending parentheses
			sb.append(")");
		}
}