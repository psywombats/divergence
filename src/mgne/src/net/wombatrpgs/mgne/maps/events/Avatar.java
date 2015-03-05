/**
 *  Hero.java
 *  Created on Nov 25, 2012 8:33:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.events;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.core.interfaces.Timer;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.graphics.FacesAnimationFactory;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.physics.CollisionResult;
import net.wombatrpgs.mgneschema.io.data.InputButton;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;

/**
 * The physical representation of the player on the world map.
 */
public class Avatar extends MapEvent implements CommandListener {
	
	protected static final String HERO_DEFAULT = "event_hero";
	
	protected List<FinishListener> stepListeners;
	protected String parentName;
	protected boolean paused;
	public boolean dying;
	protected boolean fragging;

	/**
	 * For real hero constructor. Looks up the avatar in the database and
	 * uses it to set up a map event. Takes an argument so that kryo doesn't try
	 * to call this.
	 */
	public Avatar() {
		super(MGlobal.data.getEntryFor(HERO_DEFAULT, EventMDO.class));
		stepListeners = new ArrayList<FinishListener>();
		addStepTracker();
	}
	
	/**
	 * Creates a new avatar from a stored snapshot.
	 * @param	memory			The memory to restore from
	 */
	public Avatar(AvatarMemory memory) {
		this();
		setTileX(memory.tileX);
		setTileY(memory.tileY);
		appearance = FacesAnimationFactory.create(memory.animKey);
		appearance.startMoving();
		assets.add(appearance);
		setFacing(memory.dir);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#reset()
	 */
	@Override
	public void reset() {
		// oh hell no we ain't dyin
		// ^^ p sure that's been around since the Blockbound days
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#getName()
	 */
	@Override
	public String getName() {
		return "hero";
	}
	
	public boolean isPaused() { return paused; }

	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (MapEvent.PIXEL_MOVE) {
			if (!tracking && !paused) {
				switch (command) {
				case WORLD_INTERACT:	interact();				break;
				default:				return false;
				}
			}
			return true;
		} else {
			if (!tracking && !paused) {
				switch (command) {
				case MOVE_LEFT:			move(OrthoDir.WEST);	break;
				case MOVE_UP:			move(OrthoDir.NORTH);	break;
				case MOVE_RIGHT:		move(OrthoDir.EAST);	break;
				case MOVE_DOWN:			move(OrthoDir.SOUTH);	break;
				case WORLD_INTERACT:	interact();				break;
				default:				return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (MapEvent.PIXEL_MOVE) {
			if (paused) {
				halt();
			} else if (!isTracking() && !MGlobal.memory.getSwitch("finale_stop")) {
				int targetVX = 0;
				int targetVY = 0;
				if (MGlobal.keymap.isButtonDown(InputButton.DOWN)) targetVY -= 1;
				if (MGlobal.keymap.isButtonDown(InputButton.UP)) targetVY += 1;
				if (MGlobal.keymap.isButtonDown(InputButton.LEFT)) targetVX -= 1;
				if (MGlobal.keymap.isButtonDown(InputButton.RIGHT)) targetVX += 1;
				targetVX *= maxVelocity;
				targetVY *= maxVelocity;
				setVelocity(targetVX, targetVY);
			}
		}
		if (appearance != null && !dying && !fragging) {
			if (vx != 0 || vy != 0) {
				appearance.startMoving();
			} else {
				appearance.stopMoving();
			}
		}
	}

	/**
	 * Sets the paused state. Paused heroes can't move or interact.
	 * @param	paused			True to pause, false to unpause
	 */
	public void pause(boolean paused) {
		this.paused = paused;
		if (paused) {
			halt();
		}
	}
	
	/**
	 * Adds a callback for when the hero finishes taking a step. Make sure to
	 * remove it when no longer necessary.
	 * @param	listener		The listener to call
	 */
	public void addStepListener(FinishListener listener) {
		stepListeners.add(listener);
	}
	
	/**
	 * Removes a callback for hero stepping.
	 * @param	listener		The listener to remove
	 */
	public void removeStepListener(FinishListener listener) {
		if (!stepListeners.contains(listener)) {
			MGlobal.reporter.warn("Tried to remove non-added stepper");
		}
		stepListeners.remove(listener);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		if (fragging) {
			x -= 12;
			y -= 1;
		}
		super.render(batch);
		if (fragging) {
			x += 12;
			y += 1;
		}
	}

	/**
	 * Called when this avatar is loaded from memory.
	 */
	public void onUnloaded() {
		this.vx = 0;
		this.vy = 0;
		this.tracking = false;
	}
	
	/**
	 * bacon
	 */
	public void respawn() {
		MGlobal.levelManager.getTele().teleportRaw(MGlobal.memory.levelKey,
				MGlobal.memory.heroMemory.tileX,
				MGlobal.memory.heroMemory.tileY);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#setTileLocation(int, int)
	 */
	@Override
	public void setTileLocation(int tileX, int tileY) {
		super.setTileLocation(tileX, tileY);
		x -= (getHitbox().getX() - getX());
		y -= (getHitbox().getY() - getY());
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapMovable#isMovable()
	 */
	@Override
	public boolean isMovable() {
		return true;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapMovable#resolveWallCollision(net.wombatrpgs.mgne.physics.CollisionResult)
	 */
	@Override
	public void resolveWallCollision(CollisionResult result) {
		if (!paused) {
			super.resolveWallCollision(result);
		}
	}
	
	public void frag() {
		FacesAnimation anim = FacesAnimationFactory.create("anim_fragged");
		MGlobal.assets.loadAsset(anim, "frag anim");
		setAppearance(anim);
		anim.startMoving();
		new Timer(.5f, new FinishListener() {
			@Override public void onFinish() {
				setAppearance(null);
			}
		});
		fragging = true;
	}

	/**
	 * Moves in a certain dir on the map?
	 * @param	dir				The direction to move
	 */
	protected void move(OrthoDir dir) {
		if (attemptStep(dir)) {
			parent.onTurn();
		} else {
			int targetX = (int) (getTileX() + dir.getVector().x);
			int targetY = (int) (getTileY() + dir.getVector().y);
			for (MapEvent event : parent.getEventsAt(targetX, targetY)) {
				if (event == this) continue;
				event.onCollide(this, null);
			}
		}
	}
	
	/**
	 * Interact with whatever we're standing on or facing.
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onInteract()
	 */
	protected void interact() {
		for (MapEvent event : parent.getEventsAt(getTileX(), getTileY())) {
			if (event == this) continue;
			if (!event.isPassable()) continue;
			if (event.isHidden()) continue;
			if (event.onInteract()) return;
		}
		OrthoDir facing = getFacing();
		int tileX = (int) (getTileX() + facing.getVector().x);
		int tileY = (int) (getTileY() + facing.getVector().y);
		for (MapEvent event : parent.getEventsAt(tileX, tileY)) {
			if (event.isPassable()) continue;
			if (event.onInteract()) return;
		}
		if (parent.tileHasProperty(tileX, tileY, Constants.PROPERTY_COUNTER)) {
			tileX += (int) facing.getVector().x;
			tileY += (int) facing.getVector().y;
			for (MapEvent event : parent.getEventsAt(tileX, tileY)) {
				if (event.isPassable()) continue;
				if (event.onInteract()) return;
			}
		}
	}
	
	/**
	 * Adds the step tracker for when steps finish.
	 */
	protected void addStepTracker() {
		addTrackingListener(new FinishListener() {
			@Override public void onFinish() {
				for (FinishListener listener : stepListeners) {
					listener.onFinish();
				}
				addStepTracker();
			}
		});
	}
}
