package util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.Track;
import model.Waypoint;

public class KmlLineExporter {

	public static final void saveRoute(File gpxFile, Track track) {
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("kml");
			doc.appendChild(rootElement);

			Attr attr = doc.createAttribute("xmlns");
			attr.setValue("http://www.opengis.net/kml/2.2");
			rootElement.setAttributeNode(attr);

			attr = doc.createAttribute("xmlns:gx");
			attr.setValue("http://www.google.com/kml/ext/2.2");
			rootElement.setAttributeNode(attr);

			attr = doc.createAttribute("xmlns:kml");
			attr.setValue("http://www.opengis.net/kml/2.2");
			rootElement.setAttributeNode(attr);

			attr = doc.createAttribute("xmlns:atom");
			attr.setValue("http://www.w3.org/2005/Atom");
			rootElement.setAttributeNode(attr);


			// Intro
			
			Element documentElement = doc.createElement("Document");
			rootElement.appendChild(documentElement);

			Element nameElement = doc.createElement("name");
			documentElement.appendChild(nameElement);
			nameElement.appendChild(doc.createTextNode(gpxFile.getName()));
			
			Element openElement = doc.createElement("open");
			documentElement.appendChild(openElement);
			openElement.appendChild(doc.createTextNode("1"));
			
			Element styleElement = doc.createElement("Style");
			documentElement.appendChild(styleElement);
			attr = doc.createAttribute("id");
			attr.setValue("line-style");
			styleElement.setAttributeNode(attr);
			
			Element lineStyleElement = doc.createElement("LineStyle");
			styleElement.appendChild(lineStyleElement);
			
			Element colorElement = doc.createElement("color");
			lineStyleElement.appendChild(colorElement);
			colorElement.appendChild(doc.createTextNode(track.getLineKmlColor()));
			
			Element widthElement = doc.createElement("width");
			lineStyleElement.appendChild(widthElement);
			widthElement.appendChild(doc.createTextNode(track.getLineWidth().toString()));

			Element polyStyleElement = doc.createElement("PolyStyle");
			styleElement.appendChild(polyStyleElement);
			
			colorElement = doc.createElement("color");
			polyStyleElement.appendChild(colorElement);
			colorElement.appendChild(doc.createTextNode(track.getPolyKmlColor()));

			// Placemarks
			
			styleElement = doc.createElement("Style");
			documentElement.appendChild(styleElement);
			
			Element listStyleElement = doc.createElement("ListStyle");
			styleElement.appendChild(listStyleElement);
			
			Element itemTypeElement = doc.createElement("listItemType");
			listStyleElement.appendChild(itemTypeElement);
			itemTypeElement.appendChild(doc.createTextNode("checkHideChildren"));

			Element placemarkElement = doc.createElement("Placemark");

			nameElement = doc.createElement("name");
			placemarkElement.appendChild(nameElement);
			nameElement.appendChild(doc.createTextNode(track.getTitle()));

			Element visibilityElement = doc.createElement("visibility");
			placemarkElement.appendChild(visibilityElement);
			visibilityElement.appendChild(doc.createTextNode("1"));
			
			Element styleUrlElement = doc.createElement("styleUrl");
			placemarkElement.appendChild(styleUrlElement);
			styleUrlElement.appendChild(doc.createTextNode("#line-style"));
			
			Element lineStringElement = doc.createElement("LineString");
			placemarkElement.appendChild(lineStringElement);

			
			Element tessellateElement = doc.createElement("tessellate");
			Element behaviorElement = doc.createElement("altitudeMode");

			switch (track.getBehavior()) {
				case RELATIVE: 
					lineStringElement.appendChild(tessellateElement);
					tessellateElement.appendChild(doc.createTextNode("1"));

					behaviorElement.appendChild(doc.createTextNode("relativeToGround"));
					lineStringElement.appendChild(behaviorElement);
					break;
				case ABSOLUTE:
				case TRACK:
					behaviorElement.appendChild(doc.createTextNode("absolute"));
					lineStringElement.appendChild(behaviorElement);
					break;
				case GROUND:
					lineStringElement.appendChild(tessellateElement);
					tessellateElement.appendChild(doc.createTextNode("1"));
					break;
			}
			
			if (track.getExtrude()) {
				Element extrudeElement = doc.createElement("extrude");
				lineStringElement.appendChild(extrudeElement);
				extrudeElement.appendChild(doc.createTextNode("1"));
			}
			
			Element coordinatesElement = doc.createElement("coordinates");
			lineStringElement.appendChild(coordinatesElement);
			
			String lineCoords = "";
			String altitude = "";
			for (Waypoint waypoint : track.getWaypoints()) {
				switch (track.getBehavior()) {
					case RELATIVE:
					case ABSOLUTE:
						altitude = track.getAltitude().toString();
						break;
					case GROUND:
						altitude = "0";
						break;
					case TRACK:
						altitude = waypoint.getEle(); 
						break;
					default:
						altitude = "0";
						break;
					
				}
				lineCoords += waypoint.getLon() + "," + waypoint.getLat() + "," + altitude + " ";
			}

			
			coordinatesElement.appendChild(doc.createTextNode(lineCoords));
			
			documentElement.appendChild(placemarkElement);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(gpxFile);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} 
		catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		catch (TransformerException tfe) {
				tfe.printStackTrace();
		}
	}
		
}
