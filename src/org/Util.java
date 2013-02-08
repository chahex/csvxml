package org;

public class Util {
	/**
	 * Replace <, >, ",  
	 * with &lt; &gt; &quot;
	 * 
	 * ??? "/" may also be problematic appeared in tag name)
	 * @param str
	 * @return
	 */
	public static String sanityCheck(String str)
	{
		if (str == null || str.length() == 0)
			return str;
		return str.replace("&", "&amp;").replace("<", "&lt;")
				.replace(">", "&gt;").replace("\"", "&quot;");
	}
	
}

