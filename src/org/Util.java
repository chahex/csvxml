package org;

import java.util.Random;

public class Util {

	static Random rdm = new Random();

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

	/**
	 * Generate a random string. The returned value wil be either a string
	 * represent a random integer value or an random integer value followed by
	 * "rdom_content_".
	 * 
	 * @return
	 */
	public static String randomString()
	{
		String ret = null;
		int val = rdm.nextInt();
		switch(val & 0x1)
		{
			case 1:
				ret = String.valueOf((val & (~(1<<31))) >> 5);break;
			default:
				ret = String.format("%s%d", "rdom_content_", val);
		}
		return ret;
	}

}

