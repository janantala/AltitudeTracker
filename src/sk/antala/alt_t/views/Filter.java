package sk.fiit.antala.alt_t.views;

import java.io.File;
import javax.swing.filechooser.FileFilter;

final class Filter extends FileFilter 
{
	@Override
	public boolean accept(File file) 
	{
		String filename = file.getName();
		if (file.isDirectory())
			return true;
		
		return filename.endsWith(".gpx");
	}

	@Override
	public String getDescription() 
	{
		return "gpx files";
	}

}
