/**
 *  EnemyVenustron.java
 *  Created on Mar 14, 2013 4:37:37 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.instances;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.enemies.EnemyEvent;
import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.FacesAnimation;
import net.wombatrpgs.rainfall.graphics.FacesAnimationFactory;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.Positionable;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.physics.RectHitbox;
import net.wombatrpgs.rainfallschema.characters.enemies.EnemyEventMDO;
import net.wombatrpgs.rainfallschema.maps.data.Direction;

/**
 * The level 1 boss, obviously.
 */
public class EnemyVenustron extends EnemyEvent {
	
	private static final String KEY_4DIR_VERT = "venustron_vert_4dir";
	private static final String KEY_4DIR_HORIZ = "venustron_horiz_4dir";
	private static final String KEY_4DIR_NORTHEAST = "venustron_northeast_4dir";
	private static final String KEY_4DIR_NORTHWEST = "venustron_northwest_4dir";
	
	
	private List<Queueable> assets;
	private FacesAnimation appearanceVert, appearanceHoriz;
	private FacesAnimation turnNortheast, turnNorthwest;
	
	private Hitbox offBox;
	
	private static final float SWIVEL_TIME = .25f; // in s
	private Direction travelDir;
	private float swiveled;
	private boolean swiveling;

	public EnemyVenustron(EnemyEventMDO mdo, TiledObject object, Level parent, int x, int y) {
		super(mdo, object, parent, x, y);
		assets = new ArrayList<Queueable>();
		appearanceHoriz = FacesAnimationFactory.create(KEY_4DIR_HORIZ, this);
		appearanceVert = FacesAnimationFactory.create(KEY_4DIR_VERT, this);
		turnNortheast = FacesAnimationFactory.create(KEY_4DIR_NORTHEAST, this);
		turnNorthwest = FacesAnimationFactory.create(KEY_4DIR_NORTHWEST, this);
		assets.add(appearanceHoriz);
		assets.add(appearanceVert);
		assets.add(turnNorthwest);
		assets.add(turnNortheast);
		final Positionable me = this;
		offBox = new RectHitbox(new Positionable() {
			@Override public int getX() { return me.getX() - 16; }
			@Override public int getY() { return me.getY(); }
		}, 16, 0, 48, 32);
		zero();
	}

	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		zero();
	}

	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		
		if (targetVX != 0 && targetVY != 0) {
			float deltaX = Math.abs(getX() - targetX);
			float deltaY = Math.abs(getY() - targetY);
			if (deltaX > 16 || deltaX > deltaY) {
				targetVY = 0;
			} else {
				targetVX = 0;
			}
		}
		if (targetVX == 0 || targetVY != 0) vx = 0;
		if (targetVY == 0 || targetVX != 0) vy = 0;
		
		// HORIZONTAL SWIVELING
		if (Math.abs(targetVX) > 0) {
			if (travelDir == Direction.RIGHT || travelDir == Direction.LEFT) {
				vx = targetVX;
				setAppearance(appearanceHoriz);
				if (targetVX > 0) travelDir = Direction.RIGHT;
				else travelDir = Direction.LEFT;
			} else {
				if (swiveling) {
					if (swiveled > SWIVEL_TIME) {
						swiveling = false;
						if (targetVX > 0) travelDir = Direction.RIGHT;
						else travelDir = Direction.LEFT;
					} else {
						swiveled += elapsed;
					}
				} else {
					swiveling = true;
					swiveled = 0;
					if (targetVX > 0) {
						if (travelDir == Direction.DOWN) setAppearance(turnNorthwest);
						else setAppearance(turnNortheast);
					} else {
						if (travelDir == Direction.UP) setAppearance(turnNorthwest);
						else setAppearance(turnNortheast);
					}
				}
			}
		}
		
		// VERTICAL SWIVEL
		if (Math.abs(targetVY) > 0) {
			if (travelDir == Direction.UP || travelDir == Direction.DOWN) {
				vy = targetVY;
				setAppearance(appearanceVert);
				if (targetVY > 0) travelDir = Direction.UP;
				else travelDir = Direction.DOWN;
			} else {
				if (swiveling) {
					if (swiveled > SWIVEL_TIME) {
						swiveling = false;
						if (targetVY > 0) travelDir = Direction.UP;
						else travelDir = Direction.DOWN;
					} else {
						swiveled += elapsed;
					}
				} else {
					swiveling = true;
					swiveled = 0;
					if (targetVY > 0) {
						if (travelDir == Direction.LEFT) setAppearance(turnNorthwest);
						else setAppearance(turnNortheast);
					} else {
						if (travelDir == Direction.RIGHT) setAppearance(turnNorthwest);
						else setAppearance(turnNortheast);
					}
				}
			}
		}
		
		x += vx * elapsed;
		y += vy * elapsed;
		if (tracking && ((lastX < targetX && x > targetX) || (lastX > targetX && x < targetX))) {
			x = targetX;
		}
		if (tracking && ((lastY < targetY && y > targetY) || (lastY > targetY && y < targetY))) {
			y = targetY;
		}
		if ((x == targetX && y == targetY) || (lastX == x && lastY == y)) {
			tracking = false;
		}
		lastX = x;
		lastY = y;
		
		setFacing(directionTo(RGlobal.hero));
		//targetLocation(RGlobal.hero.getX(), RGlobal.hero.getY());
	}

	@Override
	public void setFacing(Direction dir) {
		appearanceHoriz.setFacing(dir);
		appearanceVert.setFacing(dir);
		turnNorthwest.setFacing(dir);
		turnNortheast.setFacing(dir);
	}

	@Override
	public boolean onCharacterCollide(CharacterEvent other, CollisionResult result) {
		return super.onCharacterCollide(other, result);
	}

	@Override
	public int getRenderX() {
		return super.getRenderX() - 16;
	}
	
	@Override
	public Hitbox getHitbox() {
		return offBox;
	}

	@Override
	protected void integrate(float elapsed) {
		// yeah no
	}

	private void zero() {
		travelDir = Direction.UP;
		swiveled = 0;
		swiveling = false;
	}

}
