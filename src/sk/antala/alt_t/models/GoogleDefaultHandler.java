package sk.fiit.antala.alt_t.models;

import java.util.logging.Level;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import sk.fiit.antala.alt_t.controllers.Google;
import sk.fiit.antala.alt_t.controllers.Main;

/***********************************************************************************
 *  	SAX parser for new Points with Elevation from Google API
 */

final public class GoogleDefaultHandler extends DefaultHandler
{
	private boolean blat = false;
	private boolean blon = false;
	private boolean bele = false;
	private boolean status = false;
	private boolean result = false;
	private double lat;
	private double lon;
	private double elevation;
	private Google g;
	
	public GoogleDefaultHandler(Google g)
	{
		this.g = g;
	}

	public void startElement(String uri, String localName,
			String qName, Attributes attributes)
			throws SAXException {		

		if (qName.equalsIgnoreCase("LAT")) {
			blat = true;
		}

		if (qName.equalsIgnoreCase("LNG")) {
			blon = true;
		}

		if (qName.equalsIgnoreCase("ElEVATION")) {
			bele = true;
		}

		if (qName.equalsIgnoreCase("STATUS")) {
			status = true;
		}

	}

	public void endElement(String uri, String localName,
			String qName) throws SAXException
	{
		if (qName.equalsIgnoreCase("RESULT"))
		{
			if (!result)
				result = true;
			else
			{
				Trkpt p = new Trkpt(lat, lon);
				p.setElevation(elevation);
				g.addTrkpt(p);
			}
		}
	}

	public void characters(char ch[], int start, int length)
			throws SAXException
	{
			if (blat) 
			{
				lat = Double.parseDouble(new String(ch, start, length));
				blat = false;
			}

			if (blon) 
			{
				lon = Double.parseDouble(new String(ch, start, length));
				blon = false;
			}

			if (bele) 
			{
				elevation = Double.parseDouble(new String(ch, start, length));
				bele = false;
			}

			if (status) 
			{
				String strStatus = new String(ch, start, length);
				if (strStatus.equalsIgnoreCase("OK"))
				{
					status = false;
				}
				else
				{
					Main.logger.log(	Level.WARNING, 
										"Google status: {0}", 
										new Object[]{strStatus});
					throw new SAXException();
				}
			}
	}
}
