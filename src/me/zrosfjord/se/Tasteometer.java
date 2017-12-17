package me.zrosfjord.se;

import java.util.ArrayList;

import de.umass.lastfm.Artist;
import de.umass.lastfm.CallException;

public class Tasteometer implements Runnable {
	
	private final Main main;
	
	private Artist startArtist, endArtist;
	private ArrayList<Artist> begToEnd = new ArrayList<>();
	private int rIndex = 0, rLimit, sLimit;
	
	/**
	 * Constructor for Tasteometer class
	 * 
	 * @param sArtist	the starting artist
	 * @param eArtist	the ending artist
	 * @param rLimit	the minimum number of steps
	 * @param sLimit	number of top similar artists to look through
	 * @param main		instance of main class which calls this method
	 */
	public Tasteometer(Artist sArtist, Artist eArtist, int rLimit, int sLimit, final Main main) {
		this.startArtist = sArtist;
		this.endArtist = eArtist;
		
		this.rLimit = rLimit;
		this.sLimit = sLimit;
		
		this.main = main;
	}
	
	/**
	 * Runs the solve function and incrementally increase the rLimit until solve returns true.
	 */
	public void run() {
		while(!solve(startArtist)) {
			begToEnd.clear();
			rIndex = 0;
			rLimit++;
		}
		
		main.results(begToEnd);
	}
	
	/**
	 * RECURSIVE FUNCTION
	 * Used to determine the path from one artist to another using only Last FM's similar artists
	 * 
	 * 
	 * @param artist	the artist that is currently being considered for the list
	 * @return			whether or not it is solved
	 */
	public boolean solve(Artist artist) {
		String artistName = artist.getName();
		
		// Checks to make sure the artist isn't already in the list.
		for(Artist a : begToEnd) {
			if(a.getName().equalsIgnoreCase(artistName)) {
				rIndex--;
				return false;
			}
		}
		
		begToEnd.add(artist);
		
		// Insures the list reaches the rLimit
		if(rIndex >= rLimit) {
			if(artistName.equalsIgnoreCase(this.endArtist.getName())) {
				return true;
			} else {	
				begToEnd.remove(rIndex);
				rIndex--;
				return false;
			}
		}
		
		// sIndex makes sure that it isn't going too far in the similarities.
		int sIndex = 0;
		
		// tries and catches CallException on broken pages
		try {
			for(Artist a : Artist.getSimilar(artistName, sLimit, Main.FM_API_KEY)) {
				if(sIndex >= sLimit)
					break;
				
				rIndex++;
				if(solve(a))
					return true;
				
				sIndex++;
			}
		} catch (CallException e) {
			return false;
		}
		
		if(rIndex < 0)
			return false;
		
		begToEnd.remove(rIndex);
		rIndex--;
		return false;	
	}
	
}
