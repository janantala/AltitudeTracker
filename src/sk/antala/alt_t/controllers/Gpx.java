package sk.fiit.antala.alt_t.controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import sk.fiit.antala.alt_t.models.GpxDefaultHandler;
import sk.fiit.antala.alt_t.models.Trkpt;

/***********************************************************************************
 *  	GPX Controller
 *  	validates GPX file and creates SAX parser
 */

public final class Gpx 
{
	private List<Trkpt> trkseg;
	private File file;
	
	public void addTrkpt(Trkpt p)
	{
		this.trkseg.add(p);
	}
	
	public Gpx(File file, List<Trkpt> trkseg)
	{
		this.trkseg = trkseg;
		this.file = file;
	}
	
	public void parseGpx() throws ParserConfigurationException, SAXException, IOException
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
	    SchemaFactory schemafactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	    Source schemaFile = new StreamSource("http://www.topografix.com/gpx/1/1/gpx.xsd");
	    Schema schema = schemafactory.newSchema(schemaFile);

	    factory.setSchema(schema);
		SAXParser saxParser = factory.newSAXParser();

		GpxDefaultHandler handler = new GpxDefaultHandler(this);
		saxParser.parse(this.file, handler);
	}	
}
