package org;

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
		 * the CXCSVSchemaNode parsed using to the first element of the array;
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
				System.out.printf("StartPt:%d, endPt%d", startPt, thisEnd);
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
	
}

