package com.tiza.datest.util;

import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 配置文件读取类
 * 
 * @author yanghj
 *
 */
public class DaConfig {

	private String thefile;
	public Properties theproperties;

	public DaConfig(String cfgFile) {
		this.theproperties = new Properties();
		this.thefile = cfgFile;
	}

	public void load() throws IOException {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setNamespaceAware(true);
		try {
			DocumentBuilder documentbuilder = dbf.newDocumentBuilder();
			doc = documentbuilder.parse(this.thefile);
		} catch (ParserConfigurationException pce) {
			throw new IOException(pce.getMessage());
		} catch (IOException ioe) {
			throw ioe;
		} catch (SAXException se) {
		}
		if (doc != null)
			getDOMTree(doc);
	}

	private void getDOMTree(Node node) {
		int type = node.getNodeType();
		switch (type) {
		case 9:
			getDOMTree(((Document) node).getDocumentElement());
			break;
		case 1:
			if (node.getNodeName().compareToIgnoreCase("item") == 0) {
				String strKey = getNodeAttrValue(node, "name");
				String strType = getNodeText(node, "type");
				if (strType.compareToIgnoreCase("tree") != 0) {
					String strValue = getNodeText(node, "value");
					this.theproperties.setProperty(strKey, strValue);
					return;
				}
			}

			NodeList children = node.getChildNodes();
			if (children != null) {
				int len = children.getLength();
				for (int j = 0; j < len; j++)
					getDOMTree(children.item(j));
			}
			break;
		}
	}

	private String getNodeText(Node node, String strNodeName) {
		NodeList nlChilds = node.getChildNodes();
		for (int i = 0; i < nlChilds.getLength(); i++) {
			Node subNode = nlChilds.item(i);
			if (subNode.getNodeName().compareToIgnoreCase(strNodeName) == 0) {
				Node ssubNode = subNode.getFirstChild();
				return ssubNode.getNodeValue();
			}
		}

		return "";
	}

	private String getNodeAttrValue(Node node, String key) {
		String strRet = "";

		NamedNodeMap attrs = node.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			if (attr.getNodeName().compareToIgnoreCase(key) == 0) {
				strRet = attr.getNodeValue();
				return strRet;
			}
		}

		return strRet;
	}

	public void closeCfg() throws IOException {
	}
}
