/**
 *  Main.java
 *  Created on Nov 12, 2012 8:40:30 AM for project tmx-level-rename-hack
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Fixes all .tmx files in the supplied directory so libgdx works with mgne.
 */
public class Main {
	
	/**
	 * See above.
	 * @param args First arg is path to directory to parse
	 */
	public static void main(String args[]) {
		if (args.length != 1) {
			System.out.println("Usage: java -jar unhack.jar <dir>");
			System.exit(0);
		}
		File f = new File(args[0]);
		if (f.isFile()) {
			System.out.println(args[1] + " is a file, not a dir");
		}
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(1);
		}
		TransformerFactory tFactory = TransformerFactory.newInstance();
		
		for (File child : f.listFiles()) {
			if (child.getName().endsWith(".tmx")) {
				System.out.println("Unhacking " + child.getName());
				Document doc = null;
				try {
					doc = docBuilder.parse(child);
				} catch (SAXException e) {
					e.printStackTrace();
					System.exit(1);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
				Node map = doc.getFirstChild();
				NodeList items = map.getChildNodes();
				int layer = -1;
				for (int i = 0; i < items.getLength(); i++) {
					String name = items.item(i).getNodeName();
					if ("layer".equals(name) || "objectgroup".equals(name)) {
						layer++;
					}
				}
				for (int i = 0; i < items.getLength(); i++) {
					Node item = items.item(i);
					String name = item.getNodeName();
					if ("layer".equals(name) || "objectgroup".equals(name)) {
						Node properties = null;
						for (int j = 0; j < item.getChildNodes().getLength(); j++) {
							Node maybe = item.getChildNodes().item(j);
							if ("properties".equals(maybe.getNodeName())) {
								properties = maybe; break;
							}
						}
						if (properties == null) {
							properties = doc.createElement("properties");
							if (item.getChildNodes().getLength() == 0) {
								item.appendChild(properties);
							} else {
								item.insertBefore(properties, item.getFirstChild());
							}
						}
						Element property = doc.createElement("property");
						property.setAttribute("name", "layer");
						property.setAttribute("value", ""+layer);
						properties.appendChild(property);
						layer--;
					}
				}
				try {
					Transformer transformer = tFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new FileOutputStream(child));
					transformer.transform(source, result); 
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			
		}
	}

}
