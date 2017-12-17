package me.zrosfjord.se;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;

public class Canvas {
	
	private final Main main;
	
	private JFrame frame;
	private JComponent currentPanel;
	private State currentState;
	
	
	public Canvas(String string, final Main main) {
		this.main = main;
		
		this.frame = new JFrame(string);
		this.currentPanel = new JPanel();
		this.currentState = State.STARTUP;
		
		update(currentState);
	}
	
	public void update(State state) {
		update(state, null);
	}
	
	public void update(State state, ArrayList<Artist> artists) {
		currentState = state;
		
		frame.remove(currentPanel);
		currentPanel = new JPanel();
		currentPanel.setBackground(Color.white);
		
		switch(currentState) {
		case LOADING:
			loadingScreen();
			break;
		case STARTUP:
			startupScreen();
			break;
		case TASTEOMETER:
			tasteScreen(artists);
			break;
		default:
			break;
		}
		
		
        frame.add(currentPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
	}
	
	private void startupScreen() {
		currentPanel.setLayout(new GridBagLayout());
		
		final JLabel sArtistLabel = new JLabel("Starting Artist:");
		final JTextField sArtistField = new JTextField(30);
		final JButton sArtistButton = new JButton("Check");
		final JCheckBox sArtistCheckbox = new JCheckBox();
		
		sArtistField.setText("Weezer");
		
		sArtistCheckbox.setEnabled(false);
		sArtistCheckbox.setSelected(false);
		
		sArtistButton.addActionListener((arg) -> {
			String artistText = sArtistField.getText();
			Artist a = Artist.getInfo(artistText, Main.FM_API_KEY);
			if(a != null) {
				sArtistCheckbox.setSelected(true);
			} else {
				sArtistCheckbox.setSelected(false);
			}
		});
		
		final JLabel eArtistLabel = new JLabel("Ending Artist:");
		final JTextField eArtistField = new JTextField(30);
		final JButton eArtistButton = new JButton("Check");
		final JCheckBox eArtistCheckbox = new JCheckBox();
		
		eArtistField.setText("Tame Impala");
		
		eArtistCheckbox.setEnabled(false);
		eArtistCheckbox.setSelected(false);
		
		eArtistButton.addActionListener((arg) -> {
			String artistText = eArtistField.getText();
			Artist a = Artist.getInfo(artistText, Main.FM_API_KEY);
			if(a != null) {
				eArtistCheckbox.setSelected(true);
			} else {
				eArtistCheckbox.setSelected(false);
			}
		});
		
		final JLabel rLimitLabel = new JLabel("Minimum Amount of Steps:");
		final JTextField rLimitField = new JTextField(30);
		
		rLimitField.setText("5");
		
		final JLabel sLimitLabel = new JLabel("Similarity (20 < x < 75):");
		final JTextField sLimitField = new JTextField(30);
		
		sLimitField.setText("20");
		
		final JButton enterButton = new JButton("Enter");
		
		enterButton.addActionListener((arg) -> {
			if(sArtistCheckbox.isSelected() && eArtistCheckbox.isSelected()) {
				String sArtist = sArtistField.getText();
				String eArtist = eArtistField.getText();
				Integer rLimit = Integer.parseInt(rLimitField.getText());
				Integer sLimit = Integer.parseInt(sLimitField.getText());
				
				main.run(sArtist, eArtist, rLimit, sLimit);
			}
		});
		
		GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(10, 10, 10, 10);

        gc.gridx = 0;
        gc.gridy = 0;
        currentPanel.add(sArtistLabel, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        currentPanel.add(sArtistField, gc);
        
        gc.gridx = 2;
        gc.gridy = 0;
        currentPanel.add(sArtistButton, gc);
        
        gc.gridx = 3;
        gc.gridy = 0;
        currentPanel.add(sArtistCheckbox, gc);


        gc.gridx = 0;
        gc.gridy = 1;
        currentPanel.add(eArtistLabel, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        currentPanel.add(eArtistField, gc);
        
        gc.gridx = 2;
        gc.gridy = 1;
        currentPanel.add(eArtistButton, gc);
        
        gc.gridx = 3;
        gc.gridy = 1;
        currentPanel.add(eArtistCheckbox, gc);
        
        
        gc.gridx = 0;
        gc.gridy = 2;
        currentPanel.add(rLimitLabel, gc);
        
        gc.gridx = 1;
        gc.gridy = 2;
        currentPanel.add(rLimitField, gc);
        
        
        gc.gridx = 0;
        gc.gridy = 3;
        currentPanel.add(sLimitLabel, gc);
        
        gc.gridx = 1;
        gc.gridy = 3;
        currentPanel.add(sLimitField, gc);
        
        
        gc.gridx = 2;
        gc.gridy = 4;
        currentPanel.add(enterButton, gc);
	}
	
	private void loadingScreen() {
		currentPanel.setLayout(new BorderLayout());
		currentPanel.setPreferredSize(new Dimension(600, 400));
		
		ImageIcon icon = new ImageIcon("rsc/ajax-loader.gif");
		JLabel label = new JLabel(icon);
		
		currentPanel.add(label, BorderLayout.CENTER);
		frame.setTitle(frame.getTitle() + " - This May Take Awhile");
	}
	
	private void tasteScreen(ArrayList<Artist> artists) {
		currentPanel.setLayout(new BorderLayout());
		
		int size = artists.size();
		int square = (int) Math.ceil(Math.sqrt(size));
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(square, square));
		
		try {
			for(Artist a : artists) {
				String artistName = a.getName();
				
				Album topAlbum = (Album) Artist.getTopAlbums(artistName, Main.FM_API_KEY).toArray()[0];
				ImageIcon icon = new ImageIcon(ImageIO.read(new URL(topAlbum.getImageURL(ImageSize.LARGE))));
				
				JButton button = new JButton(icon);
				button.setBorder(BorderFactory.createEmptyBorder());
				button.setToolTipText(artistName);
				
				grid.add(button);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		currentPanel.add(grid, BorderLayout.NORTH);
		
		// Restart button
		
		JButton restartBtn = new JButton("Restart?");
		restartBtn.addActionListener((arg) -> {main.restart();});
		
		Font fnt = new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 22);
		restartBtn.setFont(fnt);
		
		restartBtn.setBorder(BorderFactory.createEmptyBorder());
		restartBtn.setForeground(Color.white);
		restartBtn.setBackground(Color.black);
		
		currentPanel.add(restartBtn, BorderLayout.SOUTH);
	}
	
	
	public void createAndShow() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public enum State {
		STARTUP, LOADING, TASTEOMETER;
	}

}
