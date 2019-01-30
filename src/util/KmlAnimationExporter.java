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

public class KmlAnimationExporter {

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
			colorElement.appendChild(doc.createTextNode("bf00aaff"));
			
			Element widthElement = doc.createElement("width");
			lineStyleElement.appendChild(widthElement);
			widthElement.appendChild(doc.createTextNode("5"));
			
			// Placemarks
			
			Element folderElement = doc.createElement("Folder");

			nameElement = doc.createElement("name");
			folderElement.appendChild(nameElement);
			nameElement.appendChild(doc.createTextNode("Path segments"));

			styleElement = doc.createElement("Style");
			folderElement.appendChild(styleElement);
			
			Element listStyleElement = doc.createElement("ListStyle");
			styleElement.appendChild(listStyleElement);
			
			Element itemTypeElement = doc.createElement("listItemType");
			listStyleElement.appendChild(itemTypeElement);
			itemTypeElement.appendChild(doc.createTextNode("checkHideChildren"));

			Integer placemarkCount = 0;
			Element placemarkElement;
			Waypoint startPoint;
			Waypoint endPoint = null;
			for (Waypoint waypoint : track.getWaypoints()) {
				if (endPoint == null) {
					endPoint = waypoint;
				}
				else {
					startPoint = endPoint;
					endPoint = waypoint;
					placemarkCount++;

					placemarkElement = doc.createElement("Placemark");
					attr = doc.createAttribute("id");
					attr.setValue(placemarkCount.toString());
					placemarkElement.setAttributeNode(attr);

					nameElement = doc.createElement("name");
					placemarkElement.appendChild(nameElement);
					nameElement.appendChild(doc.createTextNode(placemarkCount.toString()));

					Element visibilityElement = doc.createElement("visibility");
					placemarkElement.appendChild(visibilityElement);
					visibilityElement.appendChild(doc.createTextNode("0"));
					
					Element styleUrlElement = doc.createElement("styleUrl");
					placemarkElement.appendChild(styleUrlElement);
					styleUrlElement.appendChild(doc.createTextNode("#line-style"));
					
					Element lineStringElement = doc.createElement("LineString");
					placemarkElement.appendChild(lineStringElement);

					Element tessellateElement = doc.createElement("tessellate");
					lineStringElement.appendChild(tessellateElement);
					tessellateElement.appendChild(doc.createTextNode("1"));

					Element coordinatesElement = doc.createElement("coordinates");
					lineStringElement.appendChild(coordinatesElement);
					coordinatesElement.appendChild(doc.createTextNode(
							startPoint.getLon() + "," + 
							startPoint.getLat() + ",0 " +
							endPoint.getLon() + "," +
							endPoint.getLat() + ",0"));
					
					folderElement.appendChild(placemarkElement);
				};
			}
			
			
			// Tour
			
			Element tourElement = doc.createElement("gx:Tour");
			documentElement.appendChild(tourElement);
			
			nameElement = doc.createElement("name");
			tourElement.appendChild(nameElement);
			nameElement.appendChild(doc.createTextNode(gpxFile.getName()));

			Element playlistElement = doc.createElement("gx:Playlist");
			tourElement.appendChild(playlistElement);

			Element waitElement = doc.createElement("gx:Wait");
			playlistElement.appendChild(waitElement);
			
			Element durationElement = doc.createElement("gx:duration");
			waitElement.appendChild(durationElement);
			durationElement.appendChild(doc.createTextNode("1"));
			
			Element animationElement;
			Element updateElement;
			Element changeElement;
			Element visibilityElement;
			
			for (int placemarkNo = 1; placemarkNo <= placemarkCount; placemarkNo++) {
				
				animationElement = doc.createElement("gx:AnimationUpdate");
				
				updateElement = doc.createElement("Update");
				animationElement.appendChild(updateElement);
				
				changeElement = doc.createElement("Change");
				updateElement.appendChild(changeElement);
				
				placemarkElement = doc.createElement("Placemark");
				attr = doc.createAttribute("targetId");
				attr.setValue(new Integer(placemarkNo).toString());
				placemarkElement.setAttributeNode(attr);
				changeElement.appendChild(placemarkElement);
				
				visibilityElement = doc.createElement("visibility");
				visibilityElement.appendChild(doc.createTextNode("1"));
				placemarkElement.appendChild(visibilityElement);
				
				playlistElement.appendChild(animationElement);
				
				waitElement = doc.createElement("gx:Wait");
				durationElement = doc.createElement("gx:duration");
				waitElement.appendChild(durationElement);
				durationElement.appendChild(doc.createTextNode("0.02"));
				playlistElement.appendChild(waitElement);
			};
			
			
			// Finale 
			
			documentElement.appendChild(folderElement);
			documentElement.appendChild(tourElement);


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
