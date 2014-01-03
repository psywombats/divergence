/**
 *  AbilFX.java
 *  Created on Oct 18, 2013 7:01:07 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics.effects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.graphics.Disposable;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogue.maps.objects.TimerListener;
import net.wombatrpgs.mrogue.maps.objects.TimerObject;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.GameUnit;
import net.wombatrpgs.mrogue.rpg.abil.Ability;
import net.wombatrpgs.mrogueschema.characters.data.AbilityTargetType;
import net.wombatrpgs.mrogueschema.graphics.effects.data.AbilFxMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.data.TrackingType;
import net.wombatrpgs.mrogueschema.graphics.effects.data.ZType;

/**
 * A special effect designed for use with an ability.
 */
public abstract class AbilFX extends MapEvent implements Disposable {
	
	protected Level parent;
	protected AbilFxMDO mdo;
	protected Ability abil;
	protected SpriteBatch privateBatch;
	protected List<CharacterEvent> targets;
	protected float totalElapsed;
	protected float done;
	protected boolean initialized;
	
	/**
	 * Creates an effect given data, parent.
	 * @param	mdo				The data to generate from
	 * @param	abil			The ability to generate for
	 */
	public AbilFX(AbilFxMDO mdo, Ability abil) {
		super();
		this.mdo = mdo;
		this.abil = abil;
		this.parent = abil.getActor().getParent();
		
		privateBatch = new SpriteBatch();
		assets = new ArrayList<Queueable>();
		initialized = false;
	}
	
	/**
	 * This is called by the ability every time it wants us to show, usually
	 * once per act. After that, we're on our own. What probably needs to happen
	 * is this thing gets added to the parent map so we can listen for updates,
	 * and then start a timer to remove when done. One instance is spawned per
	 * attack, so unlike Step animations, we don't have to worry about any time
	 * limit except the one in the MDO.
	 */
	public void spawn() {
		final AbilFX fx = this;
		final Level parent = abil.getActor().getParent();
		parent.addEvent(this);
		totalElapsed = 0;
		
		targets = new ArrayList<CharacterEvent>();
		if (abil.getType() == AbilityTargetType.BALL) {
			targets.add(abil.getActor());
		} else {
			for (GameUnit target : abil.getTargets()) {
				targets.add(target.getParent());
			}
		}
		
		update(0);
		new TimerObject(mdo.duration, parent.getScreen(), new TimerListener() {
			@Override public void onTimerZero(TimerObject source) {
				parent.removeEvent(fx);
				totalElapsed = mdo.duration;
			}
		});
	}
	
	/** @return True if this ability has finished playing and erased itself */
	public boolean isFinished() { return totalElapsed >= mdo.duration; }

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		// default is nothing
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.MapThing#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (MGlobal.stasis) return;
		super.update(elapsed);
		if (!initialized || mdo.tracking == TrackingType.TRACKS) {
			MapEvent target = targets.get(0);
			tileX = target.getTileX();
			tileY = target.getTileY();
			x = target.getX();
			y = target.getY();
		}
		totalElapsed += elapsed;
		done = (totalElapsed / mdo.duration);
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.MapThing#onRemovedFromMap
	 * (net.wombatrpgs.mrogue.maps.Level)
	 */
	@Override
	public void onRemovedFromMap(Level map) {
		super.onRemovedFromMap(map);
		dispose();
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		privateBatch.dispose();
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#getZ()
	 */
	@Override
	protected float getZ() {
		if (mdo.z == ZType.ABOVE) {
			return super.getZ() - parent.getHeightPixels();
		} else {
			return super.getZ() + parent.getHeightPixels();
		}
	}

}
