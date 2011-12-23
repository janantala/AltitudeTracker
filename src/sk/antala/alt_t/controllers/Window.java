package sk.fiit.antala.alt_t.controllers;

import java.awt.event.ActionEvent;
import java.io.File;

import sk.fiit.antala.alt_t.views.Chooser_view;
import sk.fiit.antala.alt_t.views.Window_view;

/***********************************************************************************
 *  	GUI Controller
 */

public final class Window
{
	private File file;
	private String filename;
	private Window_view win_v;
	
	public void addView(Window_view win_v) 
	{
		this.win_v = win_v;
	}	
	
    public void chooseButtonActionPerformed(ActionEvent evt) 
    {
    	Chooser_view c = new Chooser_view();
    	String choosed = c.chooseFile();
    	
    	if (choosed != null)
    	{
    		this.filename = choosed;
        	this.file = new File(this.filename);
    	}
    	
    	if (this.filename == null)
    	{
    		win_v.setFileNameText("Choose your gpx file...");
    	}
    	else
    	{
    		win_v.setFileNameText(this.filename);
    	}
    }
    
	public void magicButtonActionPerformed(ActionEvent evt) 
	{
		if (this.file != null)
		{
			Main_controller mc = new Main_controller(this.win_v, this.file);
			mc.start();
			
		}
		else
			win_v.setFileNameText("Choose your gpx file...");
	}
}
