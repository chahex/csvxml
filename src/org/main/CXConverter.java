package org.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.CXCSVParser;
import org.CXCSVSchemaNode;
import org.CXSchemaExParser;
import org.CXmlFormatter;
import org.Util;

public class CXConverter {
	
	private static int FLUSH_THRESHOLD = 1024 * 10;

	
	private static void printf(String format, Object ... args)
	{
		System.out.printf(format, args);
	}
	
	private static void usage()
	{
		printf("CSV to XML converter\n");
		printf("Usage:\n");
		printf("cxconverter [src filename] [dst filename] [root element name] [schema expression]\n");
		printf("It is not necessary to specify schema expression, as the converter will output each line as an entity and each column as its child.\n");
		printf("When specifying expression, must also specify root element name.\n");
	}

	public static void main(String[] args) {
		
		if (args.length < 2)
		{
			usage();
			System.exit(0);
		}
		
		if (args.length == 3)
		{
			usage();
			System.exit(1);
		} 

		BufferedReader in = null;
		PrintWriter out = null;
		String rootName = null;
		String expression = null;
		CXCSVSchemaNode schemaNode = null;
		CXCSVParser parser = new CXCSVParser();
		CXmlFormatter formatter = new CXmlFormatter();
		CXSchemaExParser schemaParser = new CXSchemaExParser();
		String[] colNames = null;
		
		int charsWritten = 0;
		
		if (args.length == 2)
			rootName = "root";
		else
		{
			rootName = Util.sanityCheck(args[2]);
			expression = args[3];
			schemaNode = schemaParser.parseCXSchema(expression);
		}
		
		try{
			in = new BufferedReader(new FileReader(args[0]));
			out = new PrintWriter(new FileWriter(args[1]));
			// the first line should be header
			String str = null;
			str = in.readLine();
			colNames = str.split(",");

			// ignore writing XML headers now
			out.printf("<%s>\n", rootName);
			while ((str = in.readLine()) != null)
			{
				String outStr = formatter.formatNode2XML(
						parser.parseCSV(str, schemaNode, colNames)); 
				out.print(outStr);
				charsWritten += outStr.length();
				if (charsWritten >= FLUSH_THRESHOLD)
					out.flush();
			}
			out.printf("</%s>", rootName);
			out.flush();

		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(2);
		} catch (IOException e) {
	        e.printStackTrace();
	        System.exit(3);
        } finally
        {
        	try {
        		if (in != null)  in.close();
        		if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		
    }
}