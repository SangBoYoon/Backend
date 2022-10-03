package com.sangboyoon.accounter.application;

import lombok.RequiredArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

        // 본인이 받은 api키를 추가
        String key = "1d00d3d38aaeb4136245a7f8fc10b595c5d6dab0";
        List<List<String>> corpList = new ArrayList<List<String>>();

        try{
            // parsing할 url 지정(API 키 포함해서)
            String url = "https://opendart.fss.or.kr/api/corpCode.xml?crtfc_key=" + key;

            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
            Document doc = dBuilder.parse("src/main/resources/CORPCODE.xml");

            // 제일 첫번째 태그
            doc.getDocumentElement().normalize();

            // 파싱할 tag
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
