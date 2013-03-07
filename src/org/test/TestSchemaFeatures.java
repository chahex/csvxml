package org.test;

import org.CXSchemaNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestSchemaFeatures {

	@Test
	public void testCompare() {

		String name = "default";

		CXSchemaNode n1 = new CXSchemaNode();
		CXSchemaNode n2 = new CXSchemaNode();

		n1.setName(name);
		n2.setName(name);

		assertEquals(n1, n2);

		n1.setContentSrc(1);

		assertTrue(n1.compareTo(n2) > 0);

		n2.setContentSrc(0);
		assertTrue(n1.compareTo(n2) > 0);

		n1.setContentSrc(null);
		assertTrue(n1.compareTo(n2) < 0);
		
		n1.setName("f");
		
		assertTrue(n1.compareTo(n2) > 0);
		
	}

}
