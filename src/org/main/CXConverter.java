package org.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.CXCSVParser;
import org.CXSchema;
import org.CXSchemaParser;
import org.CXmlFormatter;
import org.xml.sax.SAXException;

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
		printf("cxconverter [src filename] [dst filename] [schema filename]\n");
	}
	
	/**
	 * This method takes three file names, then convert the source CSV file
	 * to XML document and write to file specified by destination file name,
	 * using the schema that defines mapping between CSV and XML.
	 * 
	 * @param sfn source file name, source should be a CSV file
	 * @param dfn destination file name
	 * @param schmfn schema file name
	 * @return status code
	 */
	public static int convert(String sfn, String dfn, String schmfn)
	{
		BufferedReader in = null;
		PrintWriter out = null;
		String rootName = null;
		CXSchema schema = null;
		CXCSVParser parser = new CXCSVParser();
		CXmlFormatter formatter = new CXmlFormatter();
		CXSchemaParser schemaParser = new CXSchemaParser();

		int charsWritten = 0;
		
		try{
		schema = schemaParser.parseSchema(schmfn);
		rootName = schema.getRootName();
		}catch(IOException e)
		{
			printf("Error opening schema file.\n");
	        e.printStackTrace();
			return -1;
		} catch (SAXException e) {
			printf("Parsing schema error.\n");
	        e.printStackTrace();
	        return -2;
        }

		try{

			in = new BufferedReader(new FileReader(sfn));
			out = new PrintWriter(new FileWriter(dfn));

			// assume the first line is not header
			String str = null;

			// ignore writing XML headers now
			out.printf("<%s>\n", rootName);
			while ((str = in.readLine()) != null)
			{
				String outStr = formatter.formatNode2XML(
						parser.parseCSV(str, schema.getSchemaNode()));
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
			return -3;
		} catch (IOException e) {
	        e.printStackTrace();
	        return -4;
        } finally
        {
        	try {
        		if (in != null)  in.close();
        		if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		return 0;

	}

	public static void main(String[] args) {
		
		// System.exit(100);

		if (args.length < 3)
		{
			usage();
			System.exit(0);
		}
		
		convert(args[0], args[1], args[2]);
	}


}