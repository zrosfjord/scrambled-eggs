package me.zrosfjord.se;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import de.umass.lastfm.Artist;
import me.zrosfjord.se.Canvas.State;
import me.zrosfjord.se.files.FileManager;

public class Main {
	
	private FileManager fileMan;
	public static String FM_API_KEY;
	
	
	private Canvas canvas;
	
	private Artist sArtist;
	private Artist eArtist;
	
	/**
	 * Constructor for the Main class
	 */
	public Main() {
		fileMan = new FileManager();
		fileMan.addConfigFile("apiKeys");
		fileMan.addConfigFile("config");
		FM_API_KEY = fileMan.getConfigFile("apiKeys").getStrValue("FM_API_KEY");
		
		
		canvas = new Canvas("Scrambled Eggs", this);
		SwingUtilities.invokeLater(() -> {canvas.createAndShow();});
	}
	
	/**
	 * Called by Canvas after the values are entered in the startup screen.
	 * 
	 * @param sStr		name of starting artist
	 * @param eStr		name of ending artist
	 * @param rLimit	minimum number of steps that must be taken
	 * @param sLimit	number of top similar artists to look through
	 */
	public void run(String sStr, String eStr, int rLimit, int sLimit) {
		sArtist = Artist.getInfo(sStr, FM_API_KEY);
		eArtist = Artist.getInfo(eStr, FM_API_KEY);
		
		Tasteometer taste = new Tasteometer(sArtist, eArtist, rLimit, sLimit, this);
		Thread tasteThread = new Thread(taste);
		tasteThread.start();
		
		canvas.update(State.LOADING);
	}
	
	/**
	 * Called by Canvas. Sets the values of the starting and ending artist
	 * to null and updates the canvas to startup mode.
	 * 
	 */
	public void restart() {
		sArtist = null;
		eArtist = null;
		
		canvas.update(State.STARTUP);
	}
	
	/**
	 * Called by Tasteometer when it has finished running.
	 * 
	 * @param artists	the linked Artist list
	 */
	public void results(ArrayList<Artist> artists) {
		canvas.update(State.TASTEOMETER, artists);
	}
	
	/**
	 * The method that starts it all.
	 * 
	 * @param args	ignored variable
	 */
	public static void main(String[] args) {
		new Main();
	}
	
	/**
	 * 
	 * @return	the fileManager who is already initiated by the constructor.
	 */
	public FileManager getFileManager() {
		return fileMan;
	}
	
}
