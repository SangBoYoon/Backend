package com.sangboyoon.accounter.application;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class XMLParsing {
    public String getTagValue(String tag, Element element) {
        String result = "";

        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        result = nodeList.item(0).getTextContent();

        return result;
    }

    public List<List<String>> getXML() {
        List<List<String>> corpList = new ArrayList<List<String>>();

        try{
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();

            InputStream inputStream = new ClassPathResource("/CORPCODE.xml").getInputStream();
            Document doc = dBuilder.parse(inputStream);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("list");

            for(int temp = 0; temp < nList.getLength(); temp++){
                Node nNode = nList.item(temp);
                Element eElement = (Element) nNode;

                if(!getTagValue("stock_code", eElement).equals(" ") && !getTagValue("corp_code", eElement).equals(" ")) {
                    List<String> aLine = new ArrayList<String>();
                    aLine.add(getTagValue("corp_code", eElement));
                    aLine.add(getTagValue("corp_name", eElement));

                    corpList.add(aLine);
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return corpList;
    }
}
