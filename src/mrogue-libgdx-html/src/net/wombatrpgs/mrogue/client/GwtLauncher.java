package net.wombatrpgs.mrogue.client;

import net.wombatrpgs.mrogue.core.MRogueGame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.utils.Clipboard;

public class GwtLauncher extends GwtApplication {
	
	public static final String WARMUP_NAME = "One moment please...";
	public static final int WARMUP_WIDTH = 640;
	public static final int WARMUP_HEIGHT = 2480;
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(320, 240);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new MRogueGame(null);
	}

	@Override
	public Net getNet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clipboard getClipboard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}
}