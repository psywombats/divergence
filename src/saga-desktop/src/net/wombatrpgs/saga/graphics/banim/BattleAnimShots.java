/**
 *  BattleAnimShots.java
 *  Created on Jul 30, 2014 1:16:12 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.graphics.banim;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.sagaschema.graphics.banim.BattleAnimShotsMDO;
import net.wombatrpgs.sagaschema.graphics.banim.BattleAnimStripMDO;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Animation comprised of lots of simultaneous gunshots.
 */
public class BattleAnimShots extends BattleAnim {
	
	protected BattleAnimShotsMDO mdo;
	protected BattleAnimStripMDO stripMDO;
	
	protected List<LocatedAnim> anims;
	protected int startedCount, finishedCount;
	protected float gainX, gainY;
	protected boolean mirrorHoriz, mirrorVert;
	protected int rows;
	protected int startX, startY;
	protected int width, height;

	/**
	 * Creates a new animation from data.
	 * @param	mdo				The animation to create from
	 */
	public BattleAnimShots(BattleAnimShotsMDO mdo) {
		super(mdo);
		this.mdo = mdo;
		stripMDO = MGlobal.data.getEntryFor(mdo.anim, BattleAnimStripMDO.class);
		anims = new ArrayList<LocatedAnim>();
		for (int i = 0; i < mdo.count; i += 1) {
			BattleAnimStrip strip = new BattleAnimStrip(stripMDO);
			anims.add(new LocatedAnim(strip));
			assets.add(strip);
		}
	}

	/** @see net.wombatrpgs.saga.graphics.banim.BattleAnim#getWidth() */
	@Override public int getWidth() { return width; }

	/** @see net.wombatrpgs.saga.graphics.banim.BattleAnim#getHeight() */
	@Override public int getHeight() { return height; }

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.PosRenderable#renderAt
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch, float, float)
	 */
	@Override
	public void renderAt(SpriteBatch batch, float x, float y) {
		if (!isDone()) {
			for (LocatedAnim anim : anims) {
				anim.strip.renderAt(batch, x + anim.x, y + anim.y);
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
	 * @see net.wombatrpgs.saga.graphics.banim.BattleAnim#start()
	 */
	@Override
	public void start() {
		super.start();
		
		for (LocatedAnim anim : anims) {
			anim.strip.reset();
		}
		
		finishedCount = 0;
		startedCount = 0;
		gainX = (MGlobal.rand.nextFloat() * mdo.gainX * 2) - mdo.gainX;
		gainY = (MGlobal.rand.nextFloat() * mdo.gainY * 2) - mdo.gainY;
		mirrorHoriz = MGlobal.rand.nextBoolean();
		mirrorVert = MGlobal.rand.nextBoolean();
		
		BattleAnimStrip strip = anims.get(0).strip;
		rows = (int) Math.ceil((float) mdo.count / (float) mdo.cols);
		width = (int) (mdo.cols * (strip.getWidth() + mdo.padX) + (rows * gainX));
		height = (int) (rows * (strip.getHeight() + mdo.padY) + (mdo.cols * gainY));
		startX = -1 * width / 2;
		startY = -1 * height / 2;
		
		update(0);
	}

	/**
	 * @see net.wombatrpgs.saga.graphics.banim.BattleAnim#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		for (int i = 0; i < mdo.count; i += 1) {
			LocatedAnim anim = anims.get(i);
			if (anim.strip.isStarted()) {
				anim.strip.update(elapsed);
				if (anim.strip.isDone() && anim.strip.isPlaying()) {
					finishedCount += 1;
					anim.strip.pauseAt(anim.strip.getDuration() - .1f);
				}
			} else if (sinceStart >= mdo.delay * i) {
				anim.strip.start();
				int col = i % mdo.cols;
				int row = (i - col) / rows;
				anim.x = (int) (startX + col * (anim.strip.getWidth() + mdo.padX) +
						(gainX * row) +
						(MGlobal.rand.nextFloat() * mdo.jitterX * 2) - mdo.jitterX);
				anim.y = (int) (startY + row * (anim.strip.getHeight() + mdo.padY) +
							(gainY * col) +
							(MGlobal.rand.nextFloat() * mdo.jitterY * 2) - mdo.jitterY);
				if (mirrorHoriz) {
					anim.x *= -1;
				}
				if (mirrorVert) {
					anim.y *= -1;
				}
			}
		}
	}

	/**
	 * @see net.wombatrpgs.saga.graphics.banim.BattleAnim#dispose()
	 */
	@Override
	public void dispose() {
		for (LocatedAnim anim : anims) {
			anim.strip.dispose();
		}
	}

}
