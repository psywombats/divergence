/**
 *  GmeTester.java
 *  Created on Oct 4, 2014 2:46:32 PM for project gme-java
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.gme;

import gme.EmuPlayer;
import gme.GbsEmu;
import gme.MusicEmu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Entry point instantiation thing.
 */
public class GmeTester {
	
	protected MusicEmu emu;
	protected int track;
	
	/**
	 * Constructs a tester for the given gbs file and track.
	 * @param	filename		The path to the file to test
	 * @param	track			The track to test on
	 * @throws	IOException 	404 file not found
	 */
	public GmeTester(String filename, int track) throws IOException {
		this.track = track;
		
		emu = new GbsEmu();
		emu.setSampleRate(44100);
		Path path = Paths.get(filename);
		emu.loadFile(Files.readAllBytes(path));
	}
	
	/**
	 * Tests the swing track playback.
	 */
	public void testJava() {
		EmuPlayer player = new EmuPlayer();
		try {
			player.setEmu(emu, 44100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			player.setVolume(1);
			player.startTrack(track, 30);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
