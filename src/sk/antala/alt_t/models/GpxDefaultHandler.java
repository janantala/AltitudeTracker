package sk.fiit.antala.alt_t.models;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import sk.fiit.antala.alt_t.controllers.Gpx;

/***********************************************************************************
 *  	SAX parser for parse Points (lat, lon) and Names from GPX file
 */

public final class GpxDefaultHandler extends DefaultHandler
{
	private boolean bname = false;
	private boolean btrkseg = false;
	private double lat;
	private double lon;
	private String name;
	private Gpx gpx;

	public GpxDefaultHandler(Gpx gpx)
	{
		this.gpx = gpx;
	}
	
	public void startElement(String uri, String localName,
			String qName, Attributes attributes)
			throws SAXException {
		
		if (qName.equalsIgnoreCase("TRKPT")) {
			this.lat = Double.parseDouble(attributes.getValue("lat"));
			this.lon = Double.parseDouble(attributes.getValue("lon"));
		}
		
		if (qName.equalsIgnoreCase("TRKSEG")) {
			btrkseg = true;
		}

		if (qName.equalsIgnoreCase("NAME")) {
			bname = true;
		}
	}

	public void endElement(String uri, String localName,
			String qName) throws SAXException 
	{
		if (qName.equalsIgnoreCase("TRKPT"))
		{
			Trkpt p = new Trkpt(lat, lon);
			p.setName(this.name);
			this.gpx.addTrkpt(p);
			this.name = null;
		}
		
		if (qName.equalsIgnoreCase("TRKSEG")) {
			btrkseg = false;
		}
	}

	public void characters(char ch[], int start, int length)
			throws SAXException 
	{
		if (btrkseg)
		{
			if (bname) 
			{
				this.name = new String(ch, start, length);
				bname = false;
			}
		}
	}
}
