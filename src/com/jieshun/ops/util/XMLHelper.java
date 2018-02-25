package com.jieshun.ops.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLHelper {
	private Document doc = null;

	public XMLHelper(String xmlFile) throws Exception {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();

		// 这个Document就是一个XML文件在内存中的镜像
		doc = db.parse(this.getClass().getClassLoader()
				.getResourceAsStream(xmlFile));

	}

	public String getLatestVersion() throws XMLFormatException {
		// 在xml文件里,只有一个根元素,先把根元素拿出来看看
		Element element = doc.getDocumentElement();
		if (element != null) {
			return element.getAttribute("latestVersion");
		}

		throw new XMLFormatException(-3, "XML文件格式正确！");
	}

	public String getUpdateContent(String lastVersion)
			throws XMLFormatException {
		NodeList nodeList = doc.getElementsByTagName("version");
		int length = nodeList.getLength();
		for (int i = 0; i < length; i++) {
			Node node = nodeList.item(i);
			Element elVersion = (Element) node;
			String id = elVersion.getAttribute("id");
			if (id.equals(lastVersion)) {
				NodeList contentNodeList = elVersion
						.getElementsByTagName("updateContent");
				return contentNodeList.item(0).getTextContent();
			}
		}
		throw new XMLFormatException(-3, "XML文件格式正确！");
	}

	public String getUpdateUrl(String lastVersion) throws XMLFormatException {
		NodeList nodeList = doc.getElementsByTagName("version");
		int length = nodeList.getLength();
		for (int i = 0; i < length; i++) {
			Node node = nodeList.item(i);
			Element elVersion = (Element) node;
			String id = elVersion.getAttribute("id");
			if (id.equals(lastVersion)) {
				NodeList contentNodeList = elVersion
						.getElementsByTagName("updateUrl");
				return contentNodeList.item(0).getTextContent();
			}
		}
		throw new XMLFormatException(-3, "XML文件格式正确！");
	}
}
