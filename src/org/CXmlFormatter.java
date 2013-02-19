package org;

import java.util.List;

/**
 * Format XML output based on document structure defined by CXNode tree.
 * Currently does not support attribute output.
 *
 * @author xinkaihe
 *
 */
public class CXmlFormatter {

	private StringBuilder formatXML(CXNode node, StringBuilder sb)
	{
		assert(sb != null);
		assert(node != null);

		String name = Util.sanityCheck(node.getName());
		sb.append(String.format("<%s", name));
		boolean firstTagEnded = false;

		String content = node.getContent();
		if (content != null && content.length() != 0)
		{
			content = Util.sanityCheck(content);
			sb.append(">").append(content);
			firstTagEnded = true;
		}

		List<CXNode> children = node.getChildren();
		if (children != null && children.size() > 0)
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