/**
 *  EventWarper.java
 *  Created on Jan 23, 2015 1:17:51 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.bacon01.maps.BaconLevel;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.AnimationStrip;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.maps.objects.Picture;
import net.wombatrpgs.mgneschema.graphics.AnimationMDO;
import net.wombatrpgs.mgneschema.io.data.InputButton;
import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;

/**
 * Warps reality!
 */
public class EventWarper extends MapEvent {
	
	protected BaconLevel level;
	
	protected AnimationStrip light;
	protected Picture pic;
	protected float totalElapsed;
	protected float period;
	protected float r1, r2;
	protected float origWidth;
	protected float startX, startY;
	protected float tween;

	public EventWarper(EventMDO mdo, TiledMapObject object) {
		super(mdo);
		
		light = new AnimationStrip(MGlobal.data.getEntryFor("anim_light", AnimationMDO.class));
		assets.add(light);
		
		period = Float.valueOf(object.getString("period"));
		r1 = Float.valueOf(object.getString("radius1"));
		r2 = Float.valueOf(object.getString("radius2"));
		
		pic = new Picture("pull.png", 1);
		assets.add(pic);
		tween = 0;
		
		totalElapsed = 0;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onAddedToMap(net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onAddedToMap(Level map) {
		super.onAddedToMap(map);
		this.level = (BaconLevel) map;
		startX = x;
		startY = y;
		if (appearance != null) {
			MGlobal.levelManager.getScreen().addChild(pic);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#postProcessing(net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		origWidth = light.getWidth();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		
		level.getLightBuffer().begin();
		int screenX = getCenterX() + (getAppearance() == null ? 16 : 0);
		int screenY = getCenterY();
		screenX -= light.getWidth() / 2;
		screenY -= light.getHeight() / 2;
		light.renderAt(batch, screenX, screenY);
		level.getLightBuffer().end();
		parent.getScreen().resumeNormalBuffer();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		
		light.update(elapsed);
		totalElapsed += elapsed;
		float ratio = (float) Math.cos(totalElapsed / period * Math.PI*2);
		ratio = 1f - (ratio/2f + .5f);
		float newRadius = (float) (r1 + ratio * (r2 - r1));
		light.setScale(2 * newRadius / origWidth);
		
		float distance = distanceToCenter(MGlobal.getHero());
		if (distance < 20) {
			tween += elapsed;
		} else {
			tween -= elapsed;
		}
		if (tween < 0) tween = 0;
		if (tween > .5) tween = .5f;
		if (appearance != null) {
			if (!MGlobal.screens.peek().containsChild(pic)) {
				MGlobal.screens.peek().addChild(pic);
			}
			pic.setX(pic.getWidth()/2);
			pic.setY(pic.getHeight() * (tween*2f - .5f));
		}
		
		setVelocity(0, 0);
		if (distance < 20 && appearance != null && MGlobal.keymap.isButtonDown(InputButton.BUTTON_A)) {
			MGlobal.getHero().faceToward(this);
			MGlobal.getHero().dirFix = true; // super janky ughhhh
			int dx = MGlobal.getHero().getCenterX() - getCenterX();
			int dy = MGlobal.getHero().getCenterY() - getCenterY();
			if (Math.abs(dx) > Math.abs(dy)) {
				setFacing(Math.signum(dx) > 0 ? OrthoDir.EAST : OrthoDir.WEST);
				if (Math.abs(dx) > 15) {
					MGlobal.getHero().maxVelocity = 51;
					setVelocity(getFacing().getVector().x * 51, getFacing().getVector().y * 102);
				}
			} else {
				setFacing(Math.signum(dy) > 0 ? OrthoDir.NORTH : OrthoDir.SOUTH);
				if (Math.abs(dy) > 15) {
					MGlobal.getHero().maxVelocity = 51;
					setVelocity(getFacing().getVector().x * 51, getFacing().getVector().y * 102);
				}
			}
		} else {
			MGlobal.getHero().maxVelocity = 102;
			MGlobal.getHero().dirFix = false;
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#isMovable()
	 */
	@Override
	public boolean isMovable() {
		return true;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#onMapFocusLost(net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onMapFocusLost(Level map) {
		super.onMapFocusLost(map);
		if (appearance != null && MGlobal.screens.size() > 0) {
			MGlobal.screens.peek().removeChild(pic);
		}
		x = startX;
		y = startY;
	}

}
