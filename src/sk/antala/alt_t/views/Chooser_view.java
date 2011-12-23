package sk.fiit.antala.alt_t.views;

import java.awt.Component;
import javax.swing.JFileChooser;

@SuppressWarnings("serial")
public final class Chooser_view extends Component 
{
	public String chooseFile()
    {	
        JFileChooser chooser = new JFileChooser();
        Filter gpx = new Filter();
        chooser.setFileFilter(gpx);
        chooser.setAcceptAllFileFilterUsed(false);
        
        int returnVal = chooser.showOpenDialog(new Chooser_view());
        
        if(returnVal == JFileChooser.APPROVE_OPTION) 
            return chooser.getSelectedFile().getPath();
        
        return null;
    }
}