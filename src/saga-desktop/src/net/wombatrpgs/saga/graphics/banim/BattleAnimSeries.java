/**
 *  BattleAnimSeries.java
 *  Created on Jul 29, 2014 12:51:21 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.graphics.banim;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.sagaschema.graphics.banim.BattleAnimSeriesMDO;
import net.wombatrpgs.sagaschema.graphics.banim.BattleAnimStripMDO;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Battle animation comprised of lots of little anims.
 */
public class BattleAnimSeries extends BattleAnim {
	
	protected BattleAnimSeriesMDO mdo;
	protected BattleAnimStripMDO stripMDO;
	
	protected List<LocatedAnim> strips;
	protected float stripDuration;
	protected int finishedCount;

	/**
	 * Creates a battle anim series from data.
	 * @param	mdo				The data to create from
	 */
	public BattleAnimSeries(BattleAnimSeriesMDO mdo) {
		super(mdo);
		this.mdo = mdo;
		
		finishedCount = 0;
		stripMDO = MGlobal.data.getEntryFor(mdo.anim, BattleAnimStripMDO.class);
		strips = new ArrayList<LocatedAnim>();
		for (int i = 0; i < mdo.concurrent; i += 1) {
			BattleAnimStrip strip = new BattleAnimStrip(stripMDO);
			strips.add(new LocatedAnim(strip));
			assets.add(strip);
		}
		
		stripDuration = strips.get(0).strip.getDuration();
	}
	
	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getWidth() */
	@Override public int getWidth() { return mdo.span; }

	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getHeight() */
	@Override public int getHeight() { return mdo.span; }

	/**
	 * The provided x/y should be the center of the target's portrait.
	 * @see net.wombatrpgs.mgne.graphics.interfaces.PosRenderable#renderAt
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch, float, float)
	 */
	@Override
	public void renderAt(SpriteBatch batch, float x, float y) {
		if (!isDone()) {
			for (LocatedAnim located : strips) {
				BattleAnimStrip strip = located.strip;
				if (strip.isPlaying() && !strip.isDone()) {
					strip.renderAt(batch, x + located.x, y + located.y);
				}
			}
		}
	}
	
	/**
	 * @see net.wombatrpgs.saga.graphics.banim.BattleAnim#isDone()
	 */
	@Override
	public boolean isDone() {
		return finishedCount >= mdo.count;
	}

	/**
	 * @see net.wombatrpgs.saga.graphics.banim.BattleAnim#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		for (int i = 0; i < mdo.concurrent; i += 1) {
			LocatedAnim located = strips.get(i);
			BattleAnimStrip strip = located.strip;
			strip.update(elapsed);
			if (!strip.isPlaying()) {
				float offset = stripDuration / (float) mdo.concurrent;
				if (sinceStart >= offset * i) {
					startStrip(located);
				}
			} else if (strip.isDone()) {
				startStrip(located);
			}
		}
	}

	/**
	 * @see net.wombatrpgs.saga.graphics.banim.BattleAnim#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		for (LocatedAnim located : strips) {
			located.strip.dispose();
		}
	}

	/**
	 * @see net.wombatrpgs.saga.graphics.banim.BattleAnim#start()
	 */
	@Override
	public void start() {
		super.start();
		update(0);
	}
	
	/**
	 * Starts an individual strip playing, or replaying as the case may be.
	 * @param	located			The strip to start and locate
	 */
	protected void startStrip(LocatedAnim located) {
		if (located.strip.isPlaying()) {
			located.strip.reset();
			finishedCount += 1;
		}
		int range = mdo.span / mdo.granularity;
		int randX = MGlobal.rand.nextInt(range) - range/2;
		int randY = MGlobal.rand.nextInt(range) - range/2;
		located.x = randX * mdo.granularity;
		located.y = randY * mdo.granularity;
		located.strip.start();
	}
	
	/**
	 * Struct for a battle animation and its location.
	 */
	protected class LocatedAnim {
		public BattleAnimStrip strip;
		public int x, y;
		public LocatedAnim(BattleAnimStrip strip) {
			this.strip = strip;
			this.x = 0;
			this.y = 0;
		}
	}

}
