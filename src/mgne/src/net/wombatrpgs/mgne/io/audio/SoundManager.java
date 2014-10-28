/**
 *  SoundManager.java
 *  Created on Sep 14, 2014 9:58:31 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.audio;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgneschema.audio.SoundManagerMDO;
import net.wombatrpgs.mgneschema.audio.data.EmuMusicEntryMDO;
import net.wombatrpgs.mgneschema.audio.data.LoadedMusicEntryMDO;
import net.wombatrpgs.mgneschema.audio.data.SoundEffectEntryMDO;

/**
 * General purpose sfx queuer and player. Lives in global land and plays a file
 * based on short ID string.
 */
public class SoundManager extends AssetQueuer implements	Disposable,
															Updateable {
	
	public static final int SAMPLE_RATE = 44100;
	public static final float FADE_TIME = 0;
	
	protected static final String KEY_SOUND_DEFAULT = "soundmanager_default";
	
	protected SoundManagerMDO mdo;
	protected Map<String, SoundObject> sounds;
	protected Map<String, MgnEmuPlayer> players;
	protected Map<String, EmuMusicEntryMDO> emuEntries;
	protected Map<String, LoadedMusicEntryMDO> loadedEntries;
	protected BackgroundMusic current, fadeOut;
	protected boolean emuThreadRunning;
	protected transient AudioDevice out;
	protected transient Thread emuThread;
	
	/**
	 * Creates a sound manager from data.
	 * @param	mdo				The data to create from
	 */
	public SoundManager(SoundManagerMDO mdo) {
		this.mdo = mdo;
		
		sounds = new HashMap<String, SoundObject>();
		for (SoundEffectEntryMDO entryMDO : mdo.soundEffectEntries) {
			SoundObject sound = new SoundObject(entryMDO);
			sounds.put(entryMDO.key, sound);
			assets.add(sound);
		}
		
		players = new HashMap<String, MgnEmuPlayer>();
		emuEntries = new HashMap<String, EmuMusicEntryMDO>();
		for (EmuMusicEntryMDO entryMDO : mdo.emuMusicEntries) {
			emuEntries.put(entryMDO.refKey, entryMDO);
			MgnEmuPlayer player = players.get(entryMDO.gbsPath);
			if (player == null) {
				player = new MgnEmuPlayer(entryMDO.gbsPath, this);
				players.put(entryMDO.gbsPath, player);
				assets.add(player);
			}
		}
		
		for (LoadedMusicEntryMDO entryMDO : mdo.loadedMusicEntries) {
			loadedEntries.put(entryMDO.refKey, entryMDO);
		}
		
		emuThreadRunning = false;
		emuThread = new Thread(new Runnable() {
			@Override public void run() {
				// only one should be playing at once
				while (emuThreadRunning) {
					boolean playing = false;
					for (MgnEmuPlayer player : players.values()) {
						if (player.isPlaying()) {
							player.play();
							playing = true;
						}
					}
					if (!playing) {
						emuThreadRunning = false;
					}
				}
			}
		});
	}
	
	/**
	 * Creates a sound manager from the default MDO.
	 */
	public SoundManager() {
		this(MGlobal.data.getEntryFor(KEY_SOUND_DEFAULT, SoundManagerMDO.class));
	}
	
	/** @return The audio device associated with this manager */
	public AudioDevice getDevice() { return out; }
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		for (SoundObject sound : sounds.values()) {
			sound.dispose();
		}
		out.dispose();
		if (emuThreadRunning) {
			emuThreadRunning = false;
			try {
				emuThread.join();
			} catch (InterruptedException e) {
				MGlobal.reporter.err(e);
			}
		}
		if (current != null) {
			current.fadeOutBGM(0);
			current.dispose();
		}
		if (fadeOut != null) {
			fadeOut.fadeOutBGM(0);
			fadeOut.dispose();
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (current != null) current.update(elapsed);
		if (fadeOut != null) fadeOut.update(elapsed);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.AssetQueuer#postProcessing
	 * (net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		out = Gdx.audio.newAudioDevice(SAMPLE_RATE, false);
		out.setVolume(1f);
	}

	/**
	 * Plays the sound effect with the given key.
	 * @param	key				The arbitrary tag string given in data for sfx
	 */
	public void playSFX(String key) {
		SoundObject sound = sounds.get(key);
		if (sound == null) {
			MGlobal.reporter.warn("Sound not found for key: " + key);
		} else {
			sound.play();
		}
	}
	
	/**
	 * Transition from the current BGM into a new track.
	 * @param	bgm				The new music to play, or null for none
	 */
	public void playBGM(BackgroundMusic bgm) {
		boolean matches = false;
		if (bgm == null || current == null) {
			matches = (current == null && bgm == null);
		} else {
			matches = bgm.equals(current);
		}
		if (!matches) {
			if (fadeOut != null) {
				fadeOut.dispose();
			}
			fadeOut = current;
			if (fadeOut != null) {
				fadeOut.fadeOutBGM(FADE_TIME);
			}
			current = bgm;
			if (current != null) {
				current.fadeInBGM(FADE_TIME);
			}
		}
	}
	
	/**
	 * Plays the emu background music with the given key. Probably shouldn't be
	 * called by anything but the emulated music object itself.
	 * @param	key				The arbitrary tag string given in data for bgm
	 */
	public void playEmuBGM(String key) {
		BgmLookup bgm = lookupBGM(key);
		bgm.player.playTrack(bgm.entryMDO.track);
		if (!emuThreadRunning) {
			emuThreadRunning = true;
			emuThread.start();
		}
	}
	
	/**
	 * Fades out the background music in the given amount of time. Only accepts
	 * seconds because the underlying emulator is a little inflexible.
	 * @param	seconds			The amount of time to fade in, in seconds
	 */
	public void fadeoutEmuBGM(int seconds) {
		for (MgnEmuPlayer player : players.values()) {
			player.fadeout(seconds);
		}
	}
	
	/**
	 * Creates a background music object for the given reference key. The refkey
	 * is arbitrarily defined in the MDO and can be used to generate the music
	 * from map or event. It's fine to call this multiple times with the same
	 * key, as the actual music objects are only loaded once. The returned
	 * object will need asset loading.
	 * @param	refKey			The reference key to use to lookup bgm in mdo
	 * @return					A background music object for that key
	 */
	public BackgroundMusic generateMusicForKey(String refKey) {
		if (emuEntries.containsKey(refKey)) {
			return new EmuBGM(emuEntries.get(refKey));
		} else if (loadedEntries.containsKey(refKey)) {
			return new LoadedBGM(loadedEntries.get(refKey));
		} else {
			MGlobal.reporter.err("No music found for key " + refKey);
			return null;
		}
	}
	
	/**
	 * Looks up the given key and returns the information needed to play it.
	 * Seperated out here to unify lookups.
	 * @param	lookupKey		The arbitary BGM lookup tag
	 * @return					The necessary player for that bgm
	 */
	protected BgmLookup lookupBGM(String lookupKey) {
		EmuMusicEntryMDO entryMDO = emuEntries.get(lookupKey);
		if (entryMDO == null) {
			MGlobal.reporter.err("No entry found for bgm key: " + lookupKey);
			return null;
		}
		MgnEmuPlayer player = players.get(entryMDO.gbsPath);
		if (player == null) {
			MGlobal.reporter.err("No player found for: " + lookupKey);
			return null;
		}
		return new BgmLookup(entryMDO, player);
	}
	
	/**
	 * Simple lookup data structure.
	 */
	protected class BgmLookup {
		public EmuMusicEntryMDO entryMDO;
		public MgnEmuPlayer player;
		public BgmLookup(EmuMusicEntryMDO entryMDO, MgnEmuPlayer player) {
			this.entryMDO = entryMDO;
			this.player = player;
		}
	}

}
