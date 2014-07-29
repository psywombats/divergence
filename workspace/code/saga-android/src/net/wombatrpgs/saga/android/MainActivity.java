package net.wombatrpgs.saga.android;

import net.wombatrpgs.mrogue.core.MRogueGame;
import net.wombatrpgs.mrogue.core.Platform;
import net.wombatrpgs.mrogue.core.Reporter;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * Auuuuutogeeeeeen
 */
public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        initialize(new MRogueGame(new Platform() {
			@Override public Reporter getReporter() {
				return new AndroidReporter();
			}
        }), cfg);
    }
}