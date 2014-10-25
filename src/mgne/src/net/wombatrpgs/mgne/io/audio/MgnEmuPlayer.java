/**
 *  MgnEmuPlayer.java
 *  Created on Oct 9, 2014 6:42:52 PM for project gme-java
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.badlogic.gdx.audio.AudioDevice;

import gme.MusicEmu;
import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MAssets;

/**
 * The MGN version of EmuPlayer. Not a manager in itself. Handles all track from
 * a specific gbs file.
 */
public class MgnEmuPlayer extends AssetQueuer {
	
	protected static final int BUFFER_LENGTH = 8192; // in samples
	
	protected String fileName;
	protected MusicEmu emu;
	protected SoundManager manager;
	protected boolean playing;
	
	/**
	 * Creates a new player for a specific music package file.
	 * @param	gbsFileName		The path of the .gbs file to load
	 */
	public MgnEmuPlayer(String gbsFileName, SoundManager manager) {
		this.fileName = Constants.AUDIO_DIR + gbsFileName;
		this.manager = manager;
		
		playing = false;
	}
	
	/** @return True if a track is playing on this emulator */
	public boolean isPlaying() { return playing; }

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#queueRequiredAssets
	 * (net.wombatrpgs.mgne.core.MAssets)
	 */
	@Override
	public void queueRequiredAssets(MAssets manager) {
		manager.load(fileName, MusicEmu.class);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		emu = manager.get(fileName, MusicEmu.class);
	}

	/**
	 * Blocking IO call to play some samples from this emulator to the sound
	 * manager audio device.
	 */
	public void play() {
		AudioDevice device = manager.getDevice();
		if (playing && !emu.trackEnded()) {
			byte [] buffer = new byte [BUFFER_LENGTH * 2];
			short[] shorts = new short[BUFFER_LENGTH];
			int count = emu.play(buffer, BUFFER_LENGTH);
			ByteBuffer.wrap(buffer).order(ByteOrder.BIG_ENDIAN).asShortBuffer().get(shorts);
			device.writeSamples(shorts, 0, count);
		}
	}
	
	/**
	 * Plays the given track in this file.
	 * @param	track			The index of the track to play
	 */
	public void playTrack(int track) {
		emu.startTrack(track);
		playing = true;
	}
	
	/**
	 * Fades out the BGM in the given amount of seconds.
	 * @param	seconds			The amount of seconds to take to fade
	 */
	public void fadeout(int seconds) {
		emu.setFade(emu.currentTime(), seconds);
	}

}
