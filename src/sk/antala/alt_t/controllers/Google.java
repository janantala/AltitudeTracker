package sk.fiit.antala.alt_t.controllers;

import java.util.List;
import java.util.logging.Level;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import sk.fiit.antala.alt_t.models.GoogleDefaultHandler;
import sk.fiit.antala.alt_t.models.Trkpt;

/***********************************************************************************
 *  	Google Controller
 *  	creates new query and SAX parser
 */

public final class Google
{
	private String query;
	private List<Trkpt> track;
	
	public Google(Trkpt p1, Trkpt p2, int n, List<Trkpt> track)
	{
		this.track = track;
		this.setQuery(p1, p2, n);
	}
	
	public void addTrkpt(Trkpt p)
	{
		this.track.add(p);
	}
	
	public void query() throws SAXException
	{
		try 
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			GoogleDefaultHandler handler = new GoogleDefaultHandler(this);
			saxParser.parse(this.query, handler);
		} 
		catch (Exception e) 
		{
			Main.logger.log(Level.WARNING, "Google query: {0}", new Object[]{this.query});
			throw new SAXException();
		}
	}
	
	private void setQuery(Trkpt p1, Trkpt p2, int n)
	{
		this.query =
					"http://maps.googleapis.com/maps/api/elevation/xml?path=" + 
					p1.getLat() +"," + p1.getLon() + "|" + p2.getLat() +"," + p2.getLon() + 
					"&samples=" + n + "&sensor=false";
	}

}
