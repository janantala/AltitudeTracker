package sk.fiit.antala.alt_t.helpers;

/***********************************************************************************
 *  	Calculates climb in %
 */

public final class Climb 
{
	public static double getClimb(double elevation, double distance)
	{
		return Math.toDegrees(Math.atan2(elevation, distance));
	}
}
