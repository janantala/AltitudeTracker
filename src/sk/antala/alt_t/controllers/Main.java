package sk.fiit.antala.alt_t.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import sk.fiit.antala.alt_t.views.Window_view;

/*
 * 	ALTITUDE TRACKER
 * 	@author Jan Antala
 * 	@version 1.00
 */

//TODO Refactor
public class Main 
{
	public static Logger logger;
	
	public static void main(String[] args) 
	{
		prepareLogging();
		logger.log(Level.INFO, "Starting application");
		Window_view win_v;
		Window win_c = new Window();		
		win_v = new Window_view(win_c);
		win_c.addView(win_v);
		win_v.setComponents();
	}
	
	/*************************************************************************************
	 * PROPERTY FILE FROM 
	 * http://www.hildeberto.com/2009/04/using-java-logging-configuration-file.html
	 */
	
	public static void prepareLogging() 
	{
		File loggingConfigurationFile = new File("logging.properties");
		logger = Logger.getLogger("Logger");

		// it only generates the configuration file
		// if it really doesn't exist.
		if(!loggingConfigurationFile.exists()) 
		{
			Writer output = null;
			try 
			{
				output = new BufferedWriter(new FileWriter(loggingConfigurationFile));
		        
				// The configuration file is a property file.
				// The Properties class gives support to
				// define and persist the logging configuration.
				Properties logConf = new Properties();
				logConf.setProperty("handlers",
						"java.util.logging.FileHandler,"+
						"java.util.logging.ConsoleHandler");
				logConf.setProperty(".level", "INFO");
				logConf.setProperty(
						"java.util.logging.ConsoleHandler.level",
						"INFO");
				logConf.setProperty(
						"java.util.logging.ConsoleHandler.formatter",
						"java.util.logging.SimpleFormatter");
				logConf.setProperty(
						"java.util.logging.FileHandler.level",
						"ALL");
				logConf.setProperty(
						"java.util.logging.FileHandler.pattern",
						".log");
				logConf.setProperty(
						"java.util.logging.FileHandler.limit",
						"50000");
				logConf.setProperty(
						"java.util.logging.FileHandler.count", "1");
				logConf.setProperty(
						"java.util.logging.FileHandler.formatter",
						"java.util.logging.XMLFormatter");
				logConf.store(output, "Generated");
			}
			catch (IOException ex) 
			{
				logger.log(Level.WARNING, "Logging configuration file not created", ex);
			}
			finally 
			{
				try 
				{
					output.close();
				}
				catch (IOException ex) 
				{
					logger.log(Level.WARNING, 
						"Problems to save the logging configuration file in the disc", ex);
				}
			}
		}
		// This is the way to define the system
		// property without changing the command line.
		// It has the same effect of the parameter
		// -Djava.util.logging.config.file
		Properties prop = System.getProperties();
		prop.setProperty(
				"java.util.logging.config.file",
				"logging.properties");

		// It overwrites the current logging configuration
		// to the one in the configuration file.
		try 
		{
			LogManager.getLogManager().readConfiguration();
		}
		catch (IOException ex) 
		{
			logger.log(Level.WARNING, "Problems to load the logging configuration file", ex);
		}
	}
}
