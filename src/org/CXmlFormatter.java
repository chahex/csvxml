package org;

import java.util.List;


public class CXmlFormatter {
	
	/**
	 * Replace <, >, ", /(??? maybe problematic appeared in tag name) 
	 * 
	 * @param str
	 * @return
	 */
	private String sanityCheck(String str)
	{
		if (str == null || str.length() == 0)
			return str;
		return str.replace("&", "&amp;").replace("<", "&lt;")
				.replace(">", "&gt;").replace("\"", "&quot;");
	}
	
	private StringBuilder formatXML(CXNode node, StringBuilder sb)
	{
		assert(sb != null);
		assert(node != null);
		
		String name = sanityCheck(node.getName());
		sb.append(String.format("<%s", name));
		boolean firstTagEnded = false;

		String content = node.getContent();
		if (content != null)
		{
			content = sanityCheck(content);
			sb.append(">").append(content)
				.append("");
			firstTagEnded = true;
		}

		List<CXNode> children = node.getChildren();
		if (children != null && children.size() >= 0)
		{
			if (!firstTagEnded)
			{
				sb.append(">\n");
				firstTagEnded = true;
			} else {
				sb.append("\n");
			}
			for (CXNode child : children)
			{
				sb = formatXML(child, sb);
			}
		}
		
		if (!firstTagEnded)
		{
			sb.append("/>\n");
		} else
		{
			sb.append(String.format("</%s>\n", name));
		}
		return sb;
	}
	
	public String formatNode2XML(CXNode node)
	{
		StringBuilder sb = new StringBuilder();
		return formatXML(node, sb).toString();
	}
}