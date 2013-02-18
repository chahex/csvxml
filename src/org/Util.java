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
	
	public static String randomString()
	{
		String ret = null;
		int val = rdm.nextInt(); 
		switch(val & 0x1)
		{
			case 1:
				ret = String.valueOf((val & (~(1<<31))) >> 5);break;
			default:
				ret = "random_content"; 
		}
		return ret;
	}
	
}

