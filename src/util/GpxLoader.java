package util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import model.Waypoint;

public class GpxLoader {

	public static final List<Waypoint> loadWaypoints(File gpxFile) {
		
		List<Waypoint> list = null;
		
	    try {

	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    	Document doc = dBuilder.parse(gpxFile);

	    	doc.getDocumentElement().normalize();

	    	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

	    	NodeList nList = doc.getElementsByTagName("trkpt");

	    	System.out.println("----------------------------");


    		for (int pos = 0; pos < nList.getLength(); pos++) {
	    		
    			if (list == null) {
    	    		list = new ArrayList<Waypoint>();
    			}
    			
	    		Node nNode = nList.item(pos);

	    		System.out.println("\nCurrent Element :" + nNode.getNodeName() + " No. " + pos);

	    		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

	    			Element eElement = (Element) nNode;
	    			
	    			System.out.println("Lat : " + eElement.getAttribute("lat"));
	    			System.out.println("Lon : " + eElement.getAttribute("lon"));
	    			
	    			Waypoint waypoint = new Waypoint(eElement.getAttribute("lat"), eElement.getAttribute("lon"));

	    			NodeList eleList = eElement.getElementsByTagName("ele");
	    			
	    			if (eleList.getLength() > 0) {
	    				Node eleNode = eleList.item(0);
	    				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	    					Element eleElement = (Element) eleNode;
	    					System.out.println("Ele : " + eleElement.getTextContent());
	    					
	    					waypoint.setEle(eleElement.getTextContent());
	    				}
	    			}
	    			
	    			list.add(waypoint);
	    			
	    		}
	    	}
        } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    	return null;
	    }
		
		return list;
	}
}
