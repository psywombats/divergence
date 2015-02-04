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

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.core.interfaces.Timer;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.events.Avatar;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.physics.CollisionResult;
import net.wombatrpgs.mgne.util.AStarPathfinder;
import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.mgneschema.maps.data.DirEnum;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;

/**
 * bacons stalker
 */
public class EventStalker extends MapEvent {
	
	protected AStarPathfinder pather;
	
	protected float vision = 500;

	public EventStalker(EventMDO mdo) {
		super(mdo);
		this.maxVelocity = 52;
		pather = new AStarPathfinder();
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
		
		Avatar hero = MGlobal.getHero();
		if (hero.isPaused()) {
			halt();
			return;
		}
		
		Vector2 delta = new Vector2(hero.getCenterX() - getCenterX(), hero.getCenterY() - getCenterY());
		float v = (1f - (delta.len() / vision)) * maxVelocity;
		if (delta.len() > vision) {
			halt();
			return;
		}
		
		vx = 0;
		vy = 0;
		int sources = 1;
		delta.nor();
//		vx += delta.x;
//		vy += delta.y;
		
		List<Vector2> checks = new ArrayList<Vector2>();
		int x1 = (int) Math.floor((float) getHitbox().getX() / 16f);
		int y1 = (int) Math.floor((float) (parent.getHeightPixels() - getHitbox().getY()) / 16f);
		int x2 = (int) Math.floor((float) (getHitbox().getX() + getHitbox().getWidth()) / 16f);
		int y2 = (int) Math.floor((float) (parent.getHeightPixels() - 
				(getHitbox().getY() + getHitbox().getHeight()) + 1) / 16f);
		checks.add(new Vector2(x1, y1));
		checks.add(new Vector2(x1, y2));
		checks.add(new Vector2(x2, y2));
		checks.add(new Vector2(x2, y1));
		
		AStarPathfinder pather = new AStarPathfinder();
		for (Vector2 check : checks) {
			pather.setInfo(getParent(), (int) check.x, (int) check.y, hero.getTileX(), hero.getTileY());
			List<? extends DirEnum> path = pather.getOrthoPath();
			if (path != null && path.size() > 0) {
				DirEnum dir = path.get(0);
				sources += 1;
				Vector2 dv = new Vector2(dir.getVector().x, dir.getVector().y);
				dv.nor();
				vx += dv.x;
				vy -= dv.y;
			}
		}
		
		vx *= v;
		vy *= v;
		vx /= sources;
		vy /= sources;
		
		if (Math.abs(vx) > Math.abs(vy)) {
			setFacing(vx > 0 ? OrthoDir.EAST : OrthoDir.WEST);
		} else {
			setFacing(vy > 0 ? OrthoDir.NORTH : OrthoDir.SOUTH);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onCollide
	 * (net.wombatrpgs.mgne.maps.events.MapEvent, net.wombatrpgs.mgne.physics.CollisionResult)
	 */
	@Override
	public boolean onCollide(MapEvent event, CollisionResult result) {
		if (event == MGlobal.getHero()) {
			new Timer(0f, new FinishListener() {
				@Override public void onFinish() {
					MGlobal.levelManager.getTele().teleport(
							MGlobal.memory.levelKey,
							MGlobal.memory.heroMemory.tileX,
							MGlobal.memory.heroMemory.tileY);
				}
			});
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
	}
	
	private float getOffX() { return -(getHitbox().getX() - getX()) / 2; }
	private float getOffY() { return -(getHitbox().getY() - getY()) / 2; }

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		
	}
	
	public void trueRender(SpriteBatch batch) {
		super.render(batch);
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
		//map.removeEvent(this);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onMapFocusGained(net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onMapFocusGained(Level map) {
		super.onMapFocusGained(map);
	}
	
	

}
