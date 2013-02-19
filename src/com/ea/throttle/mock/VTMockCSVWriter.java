package com.ea.throttle.mock;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class VTMockCSVWriter {

	String fileName;
	PrintWriter writer;
	int count;
	SimpleDateFormat smft = new SimpleDateFormat("M/d/yy H:m");

	public VTMockCSVWriter(String arg) throws Exception {
		this.fileName = arg;
	}

	void begin() throws Exception {
		try {
			writer = new PrintWriter(new FileWriter(fileName));
		} catch (Exception e) {
			if (writer != null)
				writer.close();
			throw e;
		}
		count = 0;
	}

	void add2Batch(Transaction t) throws Exception {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(smft.format(t.timestamp));
			sb.append(',');
			sb.append(t.logId);
			sb.append(',');
			sb.append(t.ip.ip);
			sb.append(',');
			sb.append(t.ip.country);
			sb.append(',');
			sb.append(t.user.userId);
			sb.append(',');
			sb.append(t.user.email);
			sb.append(',');
			sb.append(t.product.prodName);
			sb.append(',');
			sb.append(t.product.prodId);
			sb.append(',');
			sb.append(t.finInst.transactionType.name());
			sb.append(',');
			sb.append(t.finInst.cardType.name());
			sb.append(',');
			sb.append(t.invoiceAmount);
			sb.append(',');
			sb.append(t.suspect);
			writer.println(sb.toString());
			if (++count % 10000 == 0) { // TODO
				                                                // Threshold on
				                                                // Insertion
				System.out.printf("Batch Executing...%d", count);
				writer.flush();
				System.out.print(":Executed.\n");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	void close() throws Exception {
		try {
			System.out.printf("Batch Executing...%d", count);
			writer.flush();
			System.out.print(":Executed.\nProcess ends.\n");
		} finally {
			writer.close();
		}
	}
}