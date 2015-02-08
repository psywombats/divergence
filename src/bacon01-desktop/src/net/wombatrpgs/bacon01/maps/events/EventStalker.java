/**
 *  EventStalker.java
 *  Created on Jan 25, 2015 12:55:46 PM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import net.wombatrpgs.bacon01.maps.BaconLevel;
import net.wombatrpgs.bacon01.screens.ScreenGameOver;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.core.interfaces.Timer;
import net.wombatrpgs.mgne.graphics.AnimationStrip;
import net.wombatrpgs.mgne.graphics.ShaderFromData;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.events.Avatar;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.physics.CollisionResult;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.util.AStarPathfinder;
import net.wombatrpgs.mgneschema.graphics.AnimationMDO;
import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.mgneschema.maps.data.DirEnum;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;

/**
 * bacons stalker
 */
public class EventStalker extends MapEvent {
	
	protected float vision = 400;
	
	protected AStarPathfinder pather;
	protected AnimationStrip light;
	protected ShaderFromData shader;
	protected float totalElapsed;
	protected float startX, startY;

	public EventStalker(EventMDO mdo) {
		super(mdo);
		this.maxVelocity = 96;
		pather = new AStarPathfinder();
		
		light = new AnimationStrip(MGlobal.data.getEntryFor("anim_light", AnimationMDO.class));
		light.setScale(4);
		assets.add(light);
		
		shader = new ShaderFromData("shader_stalker");
	}

	protected int getPathTileX() {
		float x = getHitbox().getX();
		if (MGlobal.rand.nextBoolean()) x += getHitbox().getWidth();
		return (int) Math.floor(x / 16f);
	}

	protected int getPathTileY() {
		float y = getHitbox().getY();
		if (MGlobal.rand.nextBoolean()) y += getHitbox().getHeight();
		return (int) (parent.getHeight() - Math.floor(y / 16f) - 1);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		totalElapsed += elapsed;
		
		Avatar hero = MGlobal.getHero();
		if (hero.isPaused()) {
			halt();
			return;
		}
		
		Vector2 delta = new Vector2(hero.getCenterX() - getCenterX(), hero.getCenterY() - getCenterY());
		float v = (1f - (delta.len() / vision)) * maxVelocity;
		float len = delta.len();
		if (len > vision) {
			v = 0;
		}
		
		vx = 0;
		vy = 0;
		
		path(true);
		path(false);
		
		delta.nor();
		vx += delta.x;
		vy += delta.y;
		
		Vector2 vec = new Vector2(vx, vy);
		vec.nor();
		
		if (len < 85) v *= 1.2;
		if (len > 170) v *= .8;
		vx = v * vec.x;
		vy = v * vec.y;
		
		float dx = hero.getCenterX() - getCenterX();
		float dy = hero.getCenterY() - getCenterY();
		if (Math.abs(dx) > Math.abs(dy)) {
			setFacing(dx > 0 ? OrthoDir.EAST : OrthoDir.WEST);
		} else {
			setFacing(dy > 0 ? OrthoDir.NORTH : OrthoDir.SOUTH);
		}
		
		// SHADER
		
		float ratio = len / 150f;
		ratio *= ratio;
		
		shader.begin();
		shader.setUniformf("u_elapsed", totalElapsed);
		shader.setUniformf("u_thresh",  ratio);
		shader.end();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onCollide
	 * (net.wombatrpgs.mgne.maps.events.MapEvent, net.wombatrpgs.mgne.physics.CollisionResult)
	 */
	@Override
	public boolean onCollide(MapEvent event, CollisionResult result) {
		if (event == MGlobal.getHero()) {
			MGlobal.getHero().pause(true);
			MGlobal.levelManager.getTele().getPre().addListener(new FinishListener() {
				@Override public void onFinish() {
					Screen go = new ScreenGameOver();
					MGlobal.assets.loadAsset(go, "game over");
					MGlobal.screens.push(go);
				}
			});
			MGlobal.levelManager.getTele().getPre().run();
		}
		return super.onCollide(event, result);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onAddedToMap(net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onAddedToMap(Level map) {
		super.onAddedToMap(map);
		x += getOffX();
		y += getOffY();
		startX = x;
		startY = y;
	}
	
	private float getOffX() { return -(getHitbox().getX() - getX()) / 2; }
	private float getOffY() { return -(getHitbox().getY() - getY()) / 2; }

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		// aura
		BaconLevel level = (BaconLevel) getParent();
		level.getLightBuffer().begin();
		int screenX = getCenterX();
		int screenY = getCenterY();
		screenX -= light.getWidth() / 2;
		screenY -= light.getHeight() / 2;
		light.renderAt(batch, screenX, screenY);
		level.getLightBuffer().end();
		parent.getScreen().resumeNormalBuffer();
	}
	
	public void trueRender(SpriteBatch batch) {
		batch.setShader(shader);
		super.render(batch);
		batch.setShader(null);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#setFacing(net.wombatrpgs.mgneschema.maps.data.OrthoDir)
	 */
	@Override
	public void setFacing(OrthoDir dir) {
		if (getHitbox() != null) {
			x -= getOffX();
			y -= getOffY();
		}
		super.setFacing(dir);
		if (getHitbox() != null) {
			x += getOffX();
			y += getOffY();
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapMovable#resolveWallCollision(net.wombatrpgs.mgne.physics.CollisionResult)
	 */
	@Override
	public void resolveWallCollision(CollisionResult result) {
		super.resolveWallCollision(result);
		if (this.getHitbox() == result.collide2) {
			result.mtvX *= -1;
			result.mtvY *= -1;
		}
		
		Vector2 mtv = new Vector2(result.mtvX, result.mtvY);
		if (result.mtvX != 0) {
			y += Math.signum(vy) * mtv.len();
		}
		if (result.mtvY != 0) {
			x += Math.signum(vx) * mtv.len();
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#onMapFocusLost(net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onMapFocusLost(Level map) {
		super.onMapFocusLost(map);
		x = startX;
		y = startY;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onMapFocusGained(net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onMapFocusGained(Level map) {
		super.onMapFocusGained(map);
	}
	
	private int path(boolean version) {
		
		int sources = 0;
		
		List<Vector2> checks = new ArrayList<Vector2>();
		int x1 = (int) Math.floor((float) (getHitbox().getX()) / 16f);
		int y2 = (int) Math.floor((float) (parent.getHeightPixels() - getHitbox().getY()) / 16f);
		int x2 = (int) Math.floor((float) (getHitbox().getX() + getHitbox().getWidth()) / 16f);
		int y1 = (int) Math.floor((float) (parent.getHeightPixels() - 
				(getHitbox().getY() + getHitbox().getHeight())) / 16f);
		
		if (!parent.isChipPassable(x1, y1)) {
			if (version) x1 += 1;
			else y1 += 1;
		}
		if (!parent.isChipPassable(x2, y1)) {
			if (version) x2 -= 1;
			else y1 += 1;
		}
		if (!parent.isChipPassable(x2, y2)) {
			if (version) x2 -= 1;
			else y2 -= 1;
		}
		if (!parent.isChipPassable(x1, y2)) {
			if (version) x1 += 1;
			else y2 -= 1;
		}
		
		checks.add(new Vector2(x1, y1));
		checks.add(new Vector2(x1, y2));
		checks.add(new Vector2(x2, y2));
		checks.add(new Vector2(x2, y1));
		
		AStarPathfinder pather = new AStarPathfinder();
		for (Vector2 check : checks) {
			pather.setInfo(getParent(), (int) check.x, (int) check.y, 
					MGlobal.getHero().getTileX(), MGlobal.getHero().getTileY());
			List<? extends DirEnum> path = pather.getOrthoPath(this);
			if (path != null && path.size() > 0) {
				sources += 1;
				DirEnum dir = path.get(0);
				Vector2 dv = new Vector2(dir.getVector().x, dir.getVector().y);
				dv.nor();
				vx += dv.x;
				vy -= dv.y;
			}
		}
		
		return sources;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		light.dispose();
		shader.dispose();
	}

}
