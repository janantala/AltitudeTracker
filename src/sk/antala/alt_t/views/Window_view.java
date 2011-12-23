package sk.fiit.antala.alt_t.views;

import java.awt.Font;
import java.awt.Label;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import sk.fiit.antala.alt_t.controllers.Window;

@SuppressWarnings("serial")
public final class Window_view extends JFrame
{
	private Window mc;
	private JSpinner spinner;
	private Font font;
	private Label fileLabel;
	private Label distanceLabel;
	private Label ascentLabel;
	private Label descentLabel;
	private Label l;
	private JLabel image;
	private JButton magicButton;
	private JButton chooseButton;
	
	public Window_view(Window mc)
	{
		this.mc = mc;
	}
	
	public void showError(String error_msg)
	{
		JOptionPane.showMessageDialog(null, error_msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void showImage(BufferedImage track)
	{
		ImageIcon icon = new ImageIcon(track);
		this.image.setIcon(icon);
	}
	
	public void showProgress()
	{
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/progressbar.gif"));
		this.image.setIcon(icon);
	}
	
	public void showDefaultImg()
	{
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/climb.png"));
		this.image.setIcon(icon);
	}
	
	public void showButtons()
	{
		this.magicButton.setVisible(true);
		this.chooseButton.setVisible(true);	
		this.spinner.setVisible(true);
		this.l.setVisible(true);
		this.fileLabel.setVisible(true);
	}
	
	public void hideButtons()
	{
		this.magicButton.setVisible(false);
		this.chooseButton.setVisible(false);
		this.spinner.setVisible(false);
		this.l.setVisible(false);
		this.fileLabel.setVisible(false);
	}

	public void setComponents()
	{
		JPanel contentPane;
		
		/***************************************************************************
		 * 		GUI
		 */
		
		this.setTitle("Altitude Tracker");
		this.font = new Font(Font.SANS_SERIF, Font.BOLD, 14);
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	
		this.chooseButton = new JButton("Choose File");
		this.chooseButton.setBounds(25, 500, 200, 40);
		contentPane.add(this.chooseButton);	
		
		SpinnerNumberModel model = new SpinnerNumberModel(50, 50, 500, 50);		
		this.spinner = new JSpinner(model);
		this.spinner.setBounds(250, 500, 50, 40);
		this.spinner.setFont(this.font);
		contentPane.add(this.spinner);	
		
		this.l = new Label();
		this.l.setBounds(310, 500, 400, 40);
		this.l.setText("Choose maximal distance between points");
		this.l.setFont(this.font);
		contentPane.add(this.l);	
		
		this.magicButton = new JButton("Do Magic");
		this.magicButton.setBounds(875, 500, 200, 40);
		contentPane.add(this.magicButton);
		
		this.fileLabel = new Label();
		this.fileLabel.setBounds(25, 460, 685, 30);
		this.fileLabel.setText("Choose your gpx file...");
		this.fileLabel.setFont(this.font);
		contentPane.add(this.fileLabel);
		
		this.distanceLabel = new Label();
		this.distanceLabel.setBounds(25, 430, 300, 30);
		this.distanceLabel.setText("Distance: 0 m");
		this.distanceLabel.setFont(this.font);
		contentPane.add(this.distanceLabel);
		
		this.ascentLabel = new Label();
		this.ascentLabel.setBounds(400, 430, 300, 30);
		this.ascentLabel.setText("Ascent: 0 m");
		this.ascentLabel.setFont(this.font);
		contentPane.add(this.ascentLabel);
		
		this.descentLabel = new Label();
		this.descentLabel.setBounds(775, 430, 300, 30);
		this.descentLabel.setText("Descent: 0 m");
		this.descentLabel.setFont(this.font);
		contentPane.add(this.descentLabel);
		
		this.image = new JLabel();
		this.image.setBounds(25, 25, 1050, 400);
		this.image.setHorizontalAlignment(JLabel.CENTER);
		this.showDefaultImg();
		contentPane.add(this.image);	
		
		this.setVisible(true);
		
		/***************************************************************************
		 * 		ACTION LISTENERS
		 */
		
		this.chooseButton.addActionListener
		(new java.awt.event.ActionListener() 
		{
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
            	mc.chooseButtonActionPerformed(evt);
            }
        });
		
		this.magicButton.addActionListener
		(new java.awt.event.ActionListener() 
		{
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
            	mc.magicButtonActionPerformed(evt);
            }
        });
	}
	
	public int getSpinnerValue()
	{
		return (Integer) this.spinner.getModel().getValue();
	}
	
	public void setFileNameText(String file)
	{
		this.fileLabel.setText(file);
	}
	
	public void setDistance(int distance)
	{
		this.distanceLabel.setText("Distance: " + distance + " m");
	}
	
	public void setAscent(int ascent)
	{
		this.ascentLabel.setText("Ascent: " + ascent + " m");
	}
	
	public void setDescent(int descent)
	{
		this.descentLabel.setText("Descent: " + descent + " m");
	}
	
	public void nullComponents()
	{
		this.setFileNameText("Choose your gpx file...");
		this.setDistance(0);
		this.setAscent(0);
		this.setDescent(0);
		this.showDefaultImg();
	}
}
