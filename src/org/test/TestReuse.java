package org.test;

import org.CXCSVParser;
import org.junit.Assert;
import org.junit.Test;

public class TestReuse {

	//@Test
	/**
	 * Test randomly generated CSVLine.
	 */
	public void testRandomCSVLine()
	{
		for (int i = 1; i<=10000; i++)
		{
			String str = Reuse.generateRandomCSVLine(100);
			String[] strs = str.split(CXCSVParser.CSV_SEPARATOR);
			Assert.assertEquals(strs.length, 100);
		}
	}

	@Test
	public void testNumber2Word()
	{
		for (int i = 0; i < Integer.MAX_VALUE; i++)
		{
			String word = Reuse.number2Word(i);
			int number2 = word2Number(word.toCharArray());
			Assert.assertEquals(i, number2);
		}
	}

	private int word2Number(char[] word)
	{
		int result = 0;
		int len = word.length;
		int factor = 1;
		for (int i = 0; i < len; i++)
		{
			result += (word[len - i - 1] - 'A') * factor;
			factor *= 26;
		}
		return result;
	}
}

