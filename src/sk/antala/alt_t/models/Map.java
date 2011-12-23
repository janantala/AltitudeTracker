package sk.fiit.antala.alt_t.models;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import sk.fiit.antala.alt_t.controllers.Main;

/***********************************************************************************
 *  	Map class for creating and saving track image
 */

public final class Map
{
	private BufferedImage image;
	
	/*******************************************************************************
	 * 		Creates new track image from trackpoints
	 */	
	
	public BufferedImage createImage(List <Trkpt> track)
	{
		Main.logger.log(Level.INFO, "Creating Map");
		int paddingHorizontal = 125;
		int paddingVertical = 200;
		
		double maxelevation = track.get(0).getElevation();
		double maxdistance = track.get(track.size()-1).getDistance();
		
		for (int i=1; i<track.size(); i++)
		{
			if (track.get(i).getElevation() > maxelevation)
				maxelevation = track.get(i).getElevation();
		}
				
		int width = 2160;	
		int maxheight = 2310;
		int minheight = 150;
		
		double k_w = (maxdistance / width);
		
		int height;
		height = ((int) (maxelevation/k_w)) * 2;
		
		if (height < minheight) height = minheight;
		if (height > maxheight) height = maxheight;

		double k_h = (maxelevation / height);
		
		this.image = new BufferedImage(
				width + 2*paddingVertical, 
				height + 2*paddingHorizontal, 
				BufferedImage.TYPE_INT_RGB);
					
		Graphics2D g2 = (Graphics2D)this.image.createGraphics();
		
		this.paintBackground
			(paddingHorizontal, paddingVertical, width, height, g2);	
		this.paintElevationLines
			(paddingHorizontal, paddingVertical, maxelevation, width, height, k_h, g2);			
		this.paintTrack
			(track, paddingHorizontal, paddingVertical, k_w, height, k_h, g2);	
		this.paintNames
			(track, paddingHorizontal, paddingVertical, k_w, height, k_h, g2);
		this.paintStartLine
			(paddingHorizontal, paddingVertical, height, g2);		
		this.paintDistanceLines
			(paddingHorizontal, paddingVertical, maxdistance, width, height, g2);				
		this.paintFinishLine
			(paddingHorizontal, paddingVertical, maxdistance, width, height, g2);
		
		return this.image;
	}
	
	/**************************************************************************
	 * 		SAVES IMAGE at disc
	 */
	
	public void saveImage(File file) throws IOException
	{		
		File f = new File(file.getParent()+ File.separator + file.getName() + ".png");
		Main.logger.log(Level.INFO, "Saving map");
		ImageIO.write(this.image, "png", f);

	}
	
	/**************************************************************************
	 * 		RESIZES IMAGE to view it in application
	 */
	
	public BufferedImage resizeImage(int width, int height)
	{
		BufferedImage resizedImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(this.image, 0, 0, width, height, null);

		graphics2D.dispose();
	
		return resizedImage;
	}
	
	/*******************************************************************************
	 * 	PRIVATE METHODS
	 *
	 *
	 */	
	
	/*******************************************************************************
	 * 		PAINT DISTANCE LINES
	 */

	private void paintFinishLine(int paddingHorizontal, int paddingVertical,
			double maxdistance, int width, int height, Graphics2D g2) 
	{
		g2.setColor(Color.BLACK);
		DecimalFormat df = new DecimalFormat("#.##");
		Line2D linDistMax = new Line2D.Float
			(paddingVertical + width, height + paddingHorizontal + 50,
				paddingVertical + width, height + paddingHorizontal);
		g2.draw(linDistMax);
		g2.drawString(df.format((maxdistance / 1000)) + " km", 
			paddingVertical + width - 10, height + paddingHorizontal + 100);
	}

	/**************************************************************************/

	private void paintDistanceLines(int paddingHorizontal, int paddingVertical,
			double maxdistance, int width, int height, Graphics2D g2) 
	{
		g2.setColor(Color.BLACK);
		int n = 16;
		int stage = (int)(width/n);

		for (int i=1; i < n; i++)
		{			
			Line2D linDistKm = new Line2D.Float
			(paddingVertical+i*stage, height + paddingHorizontal + 50,
					paddingVertical+i*stage, height + paddingHorizontal);
			g2.draw(linDistKm);
			
			DecimalFormat df = new DecimalFormat("#.##");
			g2.drawString(df.format(i*(maxdistance/n)/1000) + " km",
					paddingVertical + i*stage - 10, height + paddingHorizontal + 75);
		}
	}
	
	/**************************************************************************/

	private void paintStartLine(int paddingHorizontal, int paddingVertical,
			int height, Graphics2D g2) 
	{
		g2.setColor(Color.BLACK);
		Line2D linDist0 = new Line2D.Float
			(paddingVertical, height + paddingHorizontal + 50,
					paddingVertical, height + paddingHorizontal);
		g2.draw(linDist0);
		g2.drawString("Start", 
				paddingVertical - 10, height + paddingHorizontal + 100);
	}

	/***************************************************************************
	 * 		PAINT BACKGROUND
	 */
	
	private void paintBackground(int paddingHorizontal, int paddingVertical,
			int width, int height, Graphics2D g2) 
	{
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, width + 2*paddingVertical,
							height + 2*paddingHorizontal);
	}
	
	/***************************************************************************
	 * 		PAINT TRACK
	 */

	private void paintTrack(List<Trkpt> track, int paddingHorizontal,
			int paddingVertical, double k_w, int height, double k_h,
			Graphics2D g2) 
	{
		for (int i=1; i<track.size(); i++)
		{
			int xPoints[] = {
					paddingVertical + (int) (track.get(i-1).getDistance() / k_w),
					paddingVertical + (int) (track.get(i).getDistance() / k_w),
					paddingVertical + (int) (track.get(i).getDistance() / k_w),
					paddingVertical + (int) (track.get(i-1).getDistance() / k_w)
							};
			
			int yPoints[] = {
					paddingHorizontal + height,
					paddingHorizontal + height,
					paddingHorizontal + height - (int) (track.get(i).getElevation() / k_h),
					paddingHorizontal + height - (int) (track.get(i-1).getElevation() / k_h),
							};
			
			int nPoints = 4;
			
			g2.setColor(getClimbColor(track.get(i).getClimb()));
			g2.fillPolygon(xPoints, yPoints, nPoints);
		}
	}
	
	/***************************************************************************
	 * 		PAINT NAMES
	 */
	
	private void paintNames(List<Trkpt> track, int paddingHorizontal,
			int paddingVertical, double k_w, int height, double k_h,
			Graphics2D g2) 
	{
		for (int i=0; i<track.size(); i++)
		{
			if (track.get(i).getName() != null)
			{
				int x = paddingVertical + (int) (track.get(i).getDistance() / k_w);
				int y = paddingHorizontal + height - (int) (track.get(i).getElevation() / k_h);
				g2.setColor(Color.BLACK);
				g2.drawString("*", x-3, y+6);
				g2.drawString(track.get(i).getName(), x-15, y-15);
			}
		}
	}
	
	/***************************************************************************
	 * 		PAINT ELEVATION LINES
	 */
	
	private void paintElevationLines(int paddingHorizontal,
			int paddingVertical, double maxelevation, int width, int height,
			double k_h, Graphics2D g2) 
	{
		g2.setColor(Color.BLACK);
		int v = 0;
		for (int i=(paddingHorizontal + height); 
					(( i>= paddingHorizontal - 100) && (v <= maxelevation + 250)); 
							i = (i - (int) (250 / k_h)))
		{
			Line2D lin = new Line2D.Float
						(paddingVertical - 100 , i, width + paddingVertical + 100, i);
	        g2.draw(lin);
	        g2.drawString(Integer.toString(v) + " m", paddingVertical / 4, i);
	        g2.drawString(Integer.toString(v) + " m", paddingVertical + width + 100, i);
			v = v+250;
		}
	}
	
	/***************************************************************************
	 * 		Get Color in track stage
	 */
	
	private static Color getClimbColor(double relativeclimb)
	{
		double climb = Math.abs(relativeclimb);
		
		if 		(climb == 0.0)						return Color.decode("#52d017");
		else if ((climb > 0.0) && (climb <= 0.5))	return Color.decode("#51cc17");
		else if ((climb > 0.5) && (climb <= 1.0))	return Color.decode("#4fc917");
		else if ((climb > 1.0) && (climb <= 1.5))	return Color.decode("#4ec417");
		else if ((climb > 1.5) && (climb <= 2.0))	return Color.decode("#4dc217");
		else if ((climb > 2.0) && (climb <= 2.5))	return Color.decode("#4cbe17");
		else if ((climb > 2.5) && (climb <= 3.0))	return Color.decode("#4bba17");
		else if ((climb > 3.0) && (climb <= 3.5))	return Color.decode("#4ab717");
		else if ((climb > 3.5) && (climb <= 4.0))	return Color.decode("#49b317");
		else if ((climb > 4.0) && (climb <= 4.5))	return Color.decode("#48af17");
		else if ((climb > 4.5) && (climb <= 5.0))	return Color.decode("#47ac17");
		else if ((climb > 5.0) && (climb <= 5.5))	return Color.decode("#46a817");
		else if ((climb > 5.5) && (climb <= 6.0))	return Color.decode("#45a517");
		else if ((climb > 6.0) && (climb <= 6.5))	return Color.decode("#43a117");
		else if ((climb > 6.5) && (climb <= 7.0))	return Color.decode("#439d17");
		else if ((climb > 7.0) && (climb <= 7.5))	return Color.decode("#419a17");
		else if ((climb > 7.5) && (climb <= 8.0))	return Color.decode("#409717");
		else if ((climb > 8.0) && (climb <= 8.5))	return Color.decode("#3f9317");
		else if ((climb > 8.5) && (climb <= 9.0))	return Color.decode("#3e9017");
		else if ((climb > 9.0) && (climb <= 9.5))	return Color.decode("#3d8c17");
		else if ((climb > 9.5) && (climb <= 10.0))	return Color.decode("#3b8917");
		else if ((climb > 10.0) && (climb <= 10.5))	return Color.decode("#3b8517");
		else if ((climb > 10.5) && (climb <= 11.0))	return Color.decode("#398217");
		else if ((climb > 11.0) && (climb <= 11.5))	return Color.decode("#387d17");
		else if ((climb > 11.5) && (climb <= 12.0))	return Color.decode("#377a17");
		else if ((climb > 12.0) && (climb <= 12.5))	return Color.decode("#367717");
		else if ((climb > 12.5) && (climb <= 13.0))	return Color.decode("#357317");
		else if ((climb > 13.0) && (climb <= 13.5))	return Color.decode("#346f17");
		else if ((climb > 13.5) && (climb <= 14.0))	return Color.decode("#326c17");
		else if ((climb > 14.0) && (climb <= 14.5))	return Color.decode("#326917");
		else if ((climb > 14.5) && (climb <= 15.0))	return Color.decode("#306517");
		else if ((climb > 15.0) && (climb <= 15.5))	return Color.decode("#2f6117");
		else if ((climb > 15.5) && (climb <= 16.0))	return Color.decode("#2e5e17");
		else if ((climb > 16.0) && (climb <= 16.5))	return Color.decode("#2d5a17");
		else if ((climb > 16.5) && (climb <= 17.0))	return Color.decode("#2c5717");
		else if ((climb > 17.0) && (climb <= 17.5))	return Color.decode("#2a5317");
		else if ((climb > 17.5) && (climb <= 18.0))	return Color.decode("#2a4f17");
		else if ((climb > 18.0) && (climb <= 18.5))	return Color.decode("#284b17");
		else if ((climb > 18.5) && (climb <= 19.0))	return Color.decode("#274817");
		else if ((climb > 19.0) && (climb <= 19.5))	return Color.decode("#264517");
		else if ((climb > 19.5) && (climb <= 20.0))	return Color.decode("#254117");
		else 										return Color.decode("#253d17");
		
	}
	
}
