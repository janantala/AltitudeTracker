package sk.fiit.antala.alt_t.models;

import sk.fiit.antala.alt_t.helpers.Climb;
import sk.fiit.antala.alt_t.helpers.Distance;

/***********************************************************************************
 *  	Track point class
 *  	with gettesrs and setters for lat, lon, elevation, distance, climb and name
 */

public final class Trkpt 
{
	private double lat;
	private double lon;
	private double elevation;
	private double distance;
	private double climb;
	private String name = null;
	
	public Trkpt(double lat, double lon) 
	{
		this.lat = lat;
		this.lon = lon;
	}
	
	public void setClimb(double climb)
	{
		this.climb = climb;
	}

	public double getClimb(Trkpt p)
	{
		double elevation = (this.getElevation() - p.getElevation());
		double distance = (this.getDistance() - p.getDistance());

		return Climb.getClimb(elevation, distance);
	}
	
	public double getClimb()
	{
		return this.climb;
	}

	public void setElevation(double elevation) 
	{
		this.elevation = elevation;
	}

	public double getLat() 
	{
		return this.lat;
	}

	public double getLon() 
	{
		return this.lon;
	}

	public double getElevation() 
	{
		return this.elevation;
	}
	
	public void setDistance(double distance)
	{
		this.distance = distance;
	}
	
	public double getDistance()
	{
		return this.distance;
	}
	
	public double getDistance(Trkpt p)
	{
		if (p == null)
			return 0;
		
		double 	lat1 = this.getLat(),
				lon1 = this.getLon(),
				lat2 = p.getLat(),
				lon2 = p.getLon();
				
		return Distance.distVincenty(lat1, lon1, lat2, lon2);
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
}
