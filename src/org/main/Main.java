package org.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.CXCSVParser;
import org.CXCSVSchemaNode;
import org.CXmlFormatter;
import org.Util;

public class Main {

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
		String[] colNames = null;
		
		if (args.length == 2)
			rootName = "root";
		else
		{
			rootName = Util.sanityCheck(args[2]);
			expression = args[3];
			CXCSVSchemaNode[] tmpNodes = new CXCSVSchemaNode[]{schemaNode};
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
				out.print(
						formatter.formatNode2XML(
								parser.parseCSV(str, schemaNode, colNames)));
			}
			out.printf("</%s>\n", rootName);

		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
	        e.printStackTrace();
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
