package org;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The CXSchemaParser read the schema as it is defined in XML format and parse
 * it into CXSchemaNode. The class also provides ways to convert a CXSchema to
 * a CXNode structure than can be output as XML document.
 * <br>
 * {@literal
 * The definition XML doc should be structurized as an XML document:<br>
 * <cxroot>								// root element
 * 		<root_name>RootName</root_name> // output XML root element tag name
 * 		<cxnode>						// output XML element structure
 * 			<name></name>				// name of the output tag name
 * 			<content_src></content_src>	// original CSV column number
 * 			<children>					// children nodes
 * 				<cxnode></cxnode>		// iterate over itself
 * 			</children>
 * 		</cxnode>
 * </cxroot>
 * }<br>
 * 
 * If the root name is not specified, then 
 * {@value org.CXSchema#DEFAULT_ROOT_NAME} will be used as the root node tag
 * name of the schema.
 * 
 * @author xinkaihe
 *
 */
public class CXSchemaParser {

	DocumentBuilderFactory dbf;
	DocumentBuilder db;

	{
		dbf = DocumentBuilderFactory
				.newInstance();
		try {
	        db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
	        e.printStackTrace();
        }
	}

	/**
	 * @pre e must not be null
	 * @param e
	 */
	private CXSchemaNode parseSchemaNode(Node e)
	{
		CXSchemaNode schema = new CXSchemaNode();
		NodeList nl = e.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++)
		{
			Node node = nl.item(i);
			String tagName = node.getNodeName();
			// parse node name
			if (CXSchema.CXNODE_NAME.equals(tagName))
			{
				schema.setName(node.getTextContent().trim());
				continue;
			}
			// should be a number indicate index of the source column in CSV
			if (CXSchema.CONTENT_SRC.equals(tagName))
			{
				schema.setContentSrc(Integer
						.parseInt(node.getTextContent().trim()));
				continue;
			}
			// child structure
			if (CXSchema.CHILDREN.equals(tagName))
			{
				NodeList childList = node.getChildNodes();
				for (int j = 0; j < childList.getLength(); j++)
				{
					Node child = childList.item(j);
					// children schema node should be cxnode only
					// however, any white space will cause #text to be inserted
					if (CXSchema.CXNODE.equals(child.getNodeName()))
						schema.addChild(parseSchemaNode(child));
					else
					{	// Log
						// System.out.printf("Unsupported child nodes:%s",
						//		child.getNodeName());
					}
				}
				continue;
			}
		}
		return schema;
	}

	private CXSchema parseSchema(Document document)
	{
		CXSchema schema = new CXSchema();
		CXSchemaNode schemaNode = null;

		Element root = document.getDocumentElement();
		// the children of root should be either configurations
		// or the CXSchema

		NodeList nl = root.getChildNodes();

		for (int i = 0; i<nl.getLength(); i++)
		{
			Node elem = nl.item(i);
			String nodeName = elem.getNodeName();

			if (CXSchema.CXNODE.equals(nodeName))
			{
				schemaNode = parseSchemaNode(elem);
			} else if (CXSchema.ROOT_NAME.equals(nodeName))
			{
				schema.setRootName(elem.getTextContent().trim());
			}
		}
		schema.setSchemaNode(schemaNode);
		return schema;
	}

	/**
	 * Parse the schema according to XML specified by fileName<br>
	 * The XML parsing is achieved using Java's default DocumentBuilder's
	 * parsing mehtod.
	 * 
	 * @param fileName file path of the XML that contains schema definition
	 * @return CXSchema object of the parsed schema
	 * @throws SAXException if XML parsig error
	 * @throws IOException if file operation error
	 */
	public CXSchema parseSchema(String fileName)
			throws SAXException, IOException
	{
		Document document = db.parse(new File(fileName));
		return parseSchema(document);
	}

	/**
	 * Parse the schema defined in the XML document that from the input stream.
	 * The XML parsing is acheived by calling Java's default DocumentBuilder's
	 * parsing method.
	 * 
	 * 
	 * @param in
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public CXSchema parseSchema(InputStream in)
			throws SAXException, IOException
	{
		Document document = db.parse(in);
		return parseSchema(document);
	}

	/**
	 * Generate the CXNode strucutre that can be used to output the schema
	 * as an XML document that equivalent to the schema passed in 
	 * in the form defined at {@link CXSchemaParser}. 
	 *
	 * @param schema
	 * @return
	 */
	public CXNode generateCXNodeFromSchema(CXSchema schema)
	{
		assert(schema != null);

		CXNode node = new CXNode(CXSchema.CXROOT);
		CXNode rootNameNode = new CXNode(CXSchema.ROOT_NAME);
		rootNameNode.setContent(schema.getRootName());

		node.addChild(rootNameNode);
		node.addChild(generateCXNodeFromSchemaNode(schema.getSchemaNode()));
		return node;
	}

	/**
	 * Generate the CXNode strucutre that can be used to output the 
	 * CXSchemaNode an XML document that equivalent to the schema passed in 
	 * in the form defined at {@link CXSchemaParser}. 
	  
	 * @param schemaNode
	 * @return
	 */
	public CXNode generateCXNodeFromSchemaNode(CXSchemaNode schemaNode)
	{
		assert(schemaNode != null);

		CXNode node = new CXNode(CXSchema.CXNODE);
		CXNode nameNode = new CXNode(CXSchema.CXNODE_NAME);
		CXNode ctsrcNode = new CXNode(CXSchema.CONTENT_SRC);

		nameNode.setContent(schemaNode.getName());
		ctsrcNode.setContent(String.valueOf(schemaNode.getContentSrc()));

		node.addChild(nameNode);
		node.addChild(ctsrcNode);

		CXNode childrenNode = new CXNode(CXSchema.CHILDREN);

		int schemaChildCount = schemaNode.childrenCount();
		if (schemaChildCount > 0)
		{
			node.addChild(childrenNode);
			List<CXSchemaNode> schemaChildren = schemaNode.getChildren();
			for (int i = 0; i < schemaChildCount; i++)
			{
				childrenNode.addChild(
						generateCXNodeFromSchemaNode(schemaChildren.get(i)));
			}
		}
		return node;
	}

	public static void main(String[] args) throws Exception{
	    CXSchemaParser parser = new CXSchemaParser();
	    System.out.println(parser.parseSchema("schema.xml"));
    }
}
