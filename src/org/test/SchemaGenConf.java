package org.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SchemaGenConf
{
	final int colSize;
	final int maxDepth;
	final int maxChildren;

	final List<Integer> availableColumns;

	int colRemoved;
	int nodeCount;

	public SchemaGenConf(int colSize) {
		if (colSize <= 0)
			throw new IllegalArgumentException("Column size should > 0");

		this.colSize = colSize;
		this.maxDepth = Reuse.rdm.nextInt(Reuse.MAX_TREE_DEPTH);
		this.maxChildren = Reuse.rdm.nextInt(Reuse.MAX_SIBLINGS);

		this.nodeCount = 0;
		this.colRemoved = 0;

		this.availableColumns = new ArrayList<Integer>(colSize);
		initAvailCols();
    }

	private void initAvailCols()
	{
		for (int i = 0; i < colSize; i++)
		{
			availableColumns.add(i);
		}
		Collections.shuffle(availableColumns);
	}

	public int nextColumnIndex()
	{
		if (availableColumns.size() > 0)
		{
			++colRemoved;
			return availableColumns.remove(0);
		} else
		{
			initAvailCols();
			return nextColumnIndex();
		}
	}
}