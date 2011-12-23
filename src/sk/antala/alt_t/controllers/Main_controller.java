package sk.fiit.antala.alt_t.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import sk.fiit.antala.alt_t.helpers.GpxException;
import sk.fiit.antala.alt_t.helpers.GoogleException;
import sk.fiit.antala.alt_t.models.Map;
import sk.fiit.antala.alt_t.models.Trkpt;
import sk.fiit.antala.alt_t.views.Window_view;

/***********************************************************************************
 *  	Application Logic Controller
 */

final class Main_controller extends Thread
{
	private File file;
	private Window_view win_v;
	private List <Trkpt> trkseg;
	private List <Trkpt> track;
	private BufferedImage image;
	private double ascent = 0;
	private double descent = 0;
	private double distance = 0;
	
	public Main_controller(Window_view win_v, File file)
	{
		this.win_v = win_v;
		this.file = file;
	}
	
	public void run()
	{
		try 
		{
			this.win_v.hideButtons();
			this.win_v.nullComponents();
			this.win_v.showProgress();
			Main.logger.log(	Level.INFO, 
								"Gpx file: {0}", 
								new Object[]{this.file});
			Main.logger.log(	Level.INFO, 
								"Max distance bewteen points: {0}", 
								new Object[]{win_v.getSpinnerValue()});
			analyseTrack(win_v.getSpinnerValue());
			drawImage();		
			this.win_v.setDistance((int) this.distance);
			this.win_v.setAscent((int) this.ascent);
			this.win_v.setDescent((int) this.descent);
		} 
		catch (GoogleException e) 
		{
			Main.logger.log(Level.WARNING, "Problem with Google Api.");
			win_v.showError("Something is wrong with Google Api");
			this.win_v.nullComponents();
		}	
		catch (GpxException e) 
		{
			Main.logger.log(Level.WARNING, "Problem with GPX file.");
			win_v.showError("Something is wrong with Gpx file");
			this.win_v.nullComponents();
		}	
		catch (IOException e) 
		{
			Main.logger.log(Level.WARNING, "Problem with new Image.");
			win_v.showError("Something is wrong with new image");	
			this.win_v.nullComponents();
		}
		finally
		{
			this.win_v.showButtons();
		}		
	}
	
	/*******************************************************************************
	 * 	PRIVATE METHODS
	 */
	
	/*******************************************************************************
	 * 	ANALYSE TRACK
	 */
	
	private void analyseTrack(double maxdistance) throws GoogleException, GpxException
	{			
		Main.logger.log(Level.INFO, "Starting analysing Track");
		
		/***************************************************************************
		 * 		POINTS FROM GPX FILE
		 */
		
		this.trkseg = new ArrayList<Trkpt>();
		Gpx gpx = new Gpx(this.file, this.trkseg);
		try 
		{
			gpx.parseGpx();
		} 
		catch (ParserConfigurationException e1) 
		{
			throw new GpxException();
		} 
		catch (SAXException e1) 
		{
			throw new GpxException();
		} 
		catch (IOException e1) 
		{
			throw new GpxException();
		}
		
		/***************************************************************************
		 * 		GOOGLE QUERY
		 */
		
		this.track = new ArrayList<Trkpt>();
		try 
		{
			queryGoogle(maxdistance);
		} 
		catch (SAXException e) 
		{
			throw new GoogleException();
		}
		
		/***************************************************************************
		 * 		Set distance and climb for each point
		 * 		Calc total distance, ascent and descent
		 */
				
		setDistanceAndClimb();
		calc();
		
		/***************************************************************************
		 * 		PRINT POINTS
		 */
		
		//printPoints();		
	}
	
	/*******************************************************************************
	 * 		Calc total distance, ascent and descent
	 */
	
	private void calc() 
	{
		for (int i=1; i<this.track.size(); i++)
		{
			double delta_elevation = this.track.get(i).getElevation() - this.track.get(i-1).getElevation();
			if (delta_elevation > 0)
				this.ascent += delta_elevation;
			else
				this.descent += Math.abs(delta_elevation);
		}
		this.distance = this.track.get(this.track.size()-1).getDistance();
	}
	
	/*******************************************************************************
	 * 		Print points
	 */

	/*private void printPoints() 
	{
		for (int i=0; i<this.track.size(); i++)
		{
			System.out.println(
					i + 
					" Lat: " +
					this.track.get(i).getLat() + 
					" Lon: " +
					this.track.get(i).getLon() + 
					" Distance: " +
					this.track.get(i).getDistance() + 
					" Elevation: " +
					this.track.get(i).getElevation() + 
					" Climb: " +
					this.track.get(i).getClimb() + "%" + 
					" Name: " +
					this.track.get(i).getName()
					);
		}
	}*/
	
	/*******************************************************************************
	 * 		Set distance and Climb for each point
	 */

	private void setDistanceAndClimb() 
	{
		this.track.get(0).setDistance(0);
		this.track.get(0).setClimb(0);
		
		for (int i=1; i<this.track.size(); i++)
		{
			this.track.get(i).setDistance(
					this.track.get(i-1).getDistance() + 
					this.track.get(i-1).getDistance(this.track.get(i)));
					
			this.track.get(i).setClimb(this.track.get(i).getClimb(this.track.get(i-1)));			
		}
	}
	
	/*******************************************************************************
	 * 		Query Google to get Elevation for each point
	 * 		//TODO REFACTOR
	 */

	private void queryGoogle(double maxdistance) throws SAXException
	{	
		double distance = 0;
		
		if (this.trkseg.size() > 0)
		{
			Google g = new Google(this.trkseg.get(0), this.trkseg.get(0), 2, this.track);
			try 
			{
				Thread.sleep(150);
			} 
			catch (InterruptedException e) 
			{
			}
			
			try 
			{
				g.query();
				if (this.trkseg.get(0).getName() != null)
				{
					this.track.get(0).setName(this.trkseg.get(0).getName());
				}
			} 
			catch (SAXException e) 
			{
					g.query();
					if (this.trkseg.get(0).getName() != null)
					{
						this.track.get(0).setName(this.trkseg.get(0).getName());
					}
			}
		}
		
		for (int i=1; i<this.trkseg.size(); i++)
		{
			int n;
			distance = this.trkseg.get(i-1).getDistance(this.trkseg.get(i));	

			if (distance <= maxdistance)
				n = 2;
			else
			{
				n = (int)(distance / maxdistance) + 2;
				if (n>512)
					n=100;
			}
			
			Google g = new Google(this.trkseg.get(i-1), this.trkseg.get(i), n, this.track);
			try 
			{
				Thread.sleep(150);
			} 
			catch (InterruptedException e) 
			{
			}
			
			try 
			{
				g.query();
				if (this.trkseg.get(i).getName() != null)
				{
					this.track.get(this.track.size() - 1).setName(this.trkseg.get(i).getName());
				}
			} 
			catch (SAXException e) 
			{
				try 
				{
					Thread.sleep(1000);
				} 
				catch (InterruptedException ie) 
				{
				}
				
				g.query();
				if (this.trkseg.get(i).getName() != null)
				{
					this.track.get(this.track.size() - 1).setName(this.trkseg.get(i).getName());
				}
			}
		}
	}
	
	/*******************************************************************************
	 * 		Draw image
	 * 		save image and show image
	 */
	
	private void drawImage() throws IOException
	{	
		Map m = new Map();
		this.image = m.createImage(this.track);
		m.saveImage(this.file);
		
		int maxwidth = 1050;
		int maxheight = 400;
		int width;
		int height;
		double k;
		
		if ((this.image.getWidth() / maxwidth) > (this.image.getHeight() / maxheight))
		{
			k = this.image.getWidth() / (double) maxwidth;
			width = (int) (this.image.getWidth() / k);
			height = (int) (this.image.getHeight() / k);
		}
		else
		{
			k = this.image.getHeight() / (double) maxheight;
			width = (int) (this.image.getWidth() / k);
			height = (int) (this.image.getHeight() / k);
		}
		
		this.win_v.showImage(m.resizeImage(width, height));
	}
}
