/**
 *  CharacterEvent.java
 *  Created on Nov 12, 2012 11:13:21 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Turnable;
import net.wombatrpgs.mrogue.graphics.FacesAnimation;
import net.wombatrpgs.mrogue.graphics.FacesAnimationFactory;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogue.maps.layers.EventLayer;
import net.wombatrpgs.mrogue.rpg.act.ActWait;
import net.wombatrpgs.mrogue.rpg.act.Action;
import net.wombatrpgs.mrogue.rpg.travel.Step;
import net.wombatrpgs.mrogue.rpg.travel.StepBump;
import net.wombatrpgs.mrogue.rpg.travel.StepMove;
import net.wombatrpgs.mrogueschema.characters.data.CharacterMDO;
import net.wombatrpgs.mrogueschema.graphics.DirMDO;
import net.wombatrpgs.mrogueschema.maps.data.EightDir;
import net.wombatrpgs.mrogueschema.maps.data.OrthoDir;

/**
 * A character event is an event with an MDO and an animation that looks kind of
 * like a character. It's also a character in an RPG-like game, meaning it
 * should probably be split into RPG-chara and RM-chara at some point soon. It
 * can act on a turn, unlike a map event.
 */
public class CharacterEvent extends MapEvent implements Turnable {
	
	protected static Action defaultWait;
	protected static RayCheck rayLoS;
	
	protected CharacterMDO mdo;
	
	protected FacesAnimation appearance;
	protected boolean pacing;
	protected List<Turnable> turnChildren;
	protected List<Step> travelPlan;
	protected Step lastStep;
	protected int ticksRemaining;
	
	protected GameUnit unit;

	/**
	 * Creates a new char event with the specified data.
	 * @param 	mdo				The data to create the event with
	 * @param	parent			The parent level of the event
	 */
	public CharacterEvent(CharacterMDO mdo, Level parent) {
		super(parent);
		// TODO: CharacterEvent
		init(mdo);
	}
	
	/**
	 * Creates a new char event with the specified data at the specified coords.
	 * @param	mdo				The data to create the event with
	 * @param	parent			The parent level of the event
	 * @param	tileX			The x-coord of the event (in tiles)
	 * @param	tileY			The y-coord of the event (in tiles)
	 */
	public CharacterEvent(CharacterMDO mdo, Level parent, int tileX, int tileY) {
		this(mdo, parent);
		this.tileX = tileX;
		this.tileY = tileY;
		this.x = tileX * parent.getTileWidth();
		this.y = tileY * parent.getTileHeight();
	}
	
	/**
	 * Creates a new character event associated with no map from the MDO.
	 * @param 	mdo				The MDO to create the event from
	 */
	public CharacterEvent(CharacterMDO mdo) {
		super();
		init(mdo);
	}
	
	/**
	 * Creates a new character event with the specified level at the origin.
	 * @param 	parent			The parent level of the event
	 */
	protected CharacterEvent(Level parent) {
		super(parent);
	}
	
	/** @return The cardinal direction the character is facing */
	public OrthoDir getFacing() {
		return appearance.getFacing();
	}
	
	/**
	 * Tells the animation to face a specific direction.
	 * @param 	dir				The direction to face
	 */
	public void setFacing(OrthoDir dir) {
		if (appearance != null) {
			appearance.setFacing(dir);
		}
	}
	
	/**
	 * Gives this character a new appearance with a four-dir anim.
	 * @param 	appearance		The new anim for this character
	 */
	public void setAppearance(FacesAnimation appearance) {
		this.appearance = appearance;
		if (pacing) {
			appearance.startMoving();
		}
	}
	
	/**
	 * @return The current appearance of this character */
	public FacesAnimation getAppearance() { return appearance; }
	
	/** @return The RPG-like stats of this character */
	public Stats getStats() { return unit.getStats(); }
	
	/** @return The RPG representation of this character */
	public GameUnit getUnit() { return unit; }
	
	/** @param s Another step on the ol' block */
	public void addStep(Step s) { travelPlan.add(s); }
	
	/**
	 * @see net.wombatrpgs.mrogue.maps.MapThing#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (hidden()) return;
		super.update(elapsed);
		if (pacing && appearance != null) {
			appearance.update(elapsed);
		}
		if (parent.isMoving()) {
			if (travelPlan.size() > 0 ) {
				int step = (int) Math.floor((float) travelPlan.size() *
						(parent.getMoveTimeElapsed() / MGlobal.constants.getDelay()));
				if (step > travelPlan.size()-1) step = travelPlan.size()-1;
				Step toStep = travelPlan.get(step);
				if (lastStep != toStep && lastStep != null) {
					lastStep.onEnd();
				}
				toStep.update(elapsed);
				lastStep = toStep;
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		if (hidden()) return;
		super.render(camera);
		if (appearance != null && MGlobal.hero.inLoS(this)) {
			appearance.render(camera);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.PreRenderable#getRegion()
	 */
	@Override
	public TextureRegion getRegion() {
		if (hidden() || appearance == null) return null;
		return appearance.getRegion();
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#onAdd
	 * (net.wombatrpgs.mrogue.maps.layers.EventLayer)
	 */
	@Override
	public void onAdd(EventLayer layer) {
		super.onAdd(layer);
		layer.addChara(this);
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#onRemove
	 * (net.wombatrpgs.mrogue.maps.layers.EventLayer)
	 */
	@Override
	public void onRemove(EventLayer layer) {
		super.onRemove(layer);
		layer.removeCharacter(this);
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#mouseoverMessage()
	 */
	@Override
	public String mouseoverMessage() {
		return unit.getName() + " is standing here.";
	}

	/**
	 * Overrides the pacing action of this character.
	 * @param 	pacing			True if character should pace, false otherwise
	 */
	public void setPacing(boolean pacing) {
		this.pacing = pacing;
		if (pacing && appearance != null) {
			appearance.startMoving();
		}
	}

	/**
	 * Makes this event face towards an object on the map.
	 * @param 	event			The object to face
	 */
	public void faceToward(MapEvent event) {
		faceToward(event.getTileX(), event.getTileY());
	}
	
	/**
	 * Makes this event face towards a tile location on the map.
	 * @param	tileX			The x-coord of the tile to face (in tiles)
	 * @param	tileY			The y-coord of the tile to face (in tiles)
	 */
	public void faceToward(int tileX, int tileY) {
		setFacing(directionTo(tileX, tileY).toOrtho(getFacing()));
	}
	
	/**
	 * Face away from a particular map event.
	 * @param	event			The object to face away from
	 */
	public void faceAway(MapEvent event) {
		setFacing(EightDir.getOpposite(directionTo(event)).toOrtho(getFacing()));
	}
	
	/**
	 * Adds itself to its parent map in a position not viewable by the hero.
	 * This is kind of a dumb implementation that relies on rand, be warned.
	 */
	public void spawnUnseen() {
		// 100 tries max
		for (int i = 0; i < 100; i++) {
			int tileX = MGlobal.rand.nextInt(parent.getWidth());
			int tileY = MGlobal.rand.nextInt(parent.getHeight());
			if (MGlobal.hero != null &&
					MGlobal.hero.getParent() == parent &&
					MGlobal.hero.inLoS(tileX, tileY)) {
				continue;
			}
			if (!parent.isTilePassable(this, tileX, tileY)) continue;
			parent.addEvent(this, tileX, tileY);
			return;
		}
		MGlobal.reporter.warn("Waited 100 turns to spawn a " + this);
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#halt()
	 */
	@Override
	public void halt() {
		super.halt();
		if (!pacing) {
			appearance.stopMoving();
			appearance.update(0);
		}
		targetLocation(getX(), getY());
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#onMoveStart()
	 */
	@Override
	public void onMoveStart() {
		for (Step step : travelPlan) {
			step.setTime(MGlobal.constants.getDelay() / travelPlan.size());
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#isPassable()
	 */
	@Override
	public boolean isPassable() {
		return false;
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#stopMoving()
	 */
	@Override
	public void stopMoving() {
		super.stopMoving();
		if (lastStep != null) {
			lastStep.onEnd();
			lastStep = null;
			travelPlan.clear();
		}
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.core.Turnable#onTurn()
	 */
	@Override
	public void onTurn() {
		for (Turnable t : turnChildren) {
			t.onTurn();
		}
	}

	/**
	 * Faces this event towards its current tile target.
	 */
	public void faceTarget() {
		float dx = tileX*parent.getTileWidth() - x;
		float dy = tileY*parent.getTileHeight() - y;
		if (Math.abs(dx) > Math.abs(dy)) {
			if (dx > 0) {
				setFacing(OrthoDir.EAST);
			} else if (dx < 0) {
				setFacing(OrthoDir.WEST);
			}
		} else {
			if (dy < 0) {
				setFacing(OrthoDir.SOUTH);
			} else if (dy > 0) {
				setFacing(OrthoDir.NORTH);
			}
		}
	}
	
	/**
	 * Attempts to move to a specified tile location next to this character. If
	 * there's nothing there, the character will step to that location. If
	 * there his something there, this will hit it. Also adds an appropriate
	 * travel step.
	 * @param	targetX			The target location x-coord, in pixels
	 * @param	targetY			The target location y-coord, in pixels
	 */
	public void attemptStep(int targetX, int targetY) {
		faceToward(targetX, targetY);
		if (parent.isTilePassable(this, targetX, targetY)) {
			List<MapEvent> events = parent.getEventsAt(targetX, targetY);
			boolean colliding = false;
			for (MapEvent event : events) {
				event.collideWith(this);
				if (!event.isPassable()) {
					travelPlan.add(new StepBump(this, directionTo(targetX, targetY)));
					colliding = true;
				}
			}
			if (!colliding) {
				travelPlan.add(new StepMove(this, targetX, targetY));
				tileX = targetX;
				tileY = targetY;
			}
		} else {
			travelPlan.add(new StepBump(this, directionTo(targetX, targetY)));
			List<MapEvent> events = parent.getEventsAt(getTileX(), getTileY());
			for (MapEvent event : events) {
				if (event != this) {
					event.collideWith(this);
				}
			}
		}
	}
	
	/**
	 * Attempts to step in a particular direction
	 * @param	dir				The direction to step.
	 */
	public void attemptStep(OrthoDir dir) {
		int targetX = (int) (tileX + dir.getVector().x);
		int targetY = (int) (tileY + dir.getVector().y);
		attemptStep(targetX, targetY);
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#collideWith
	 * (net.wombatrpgs.mrogue.rpg.CharacterEvent)
	 */
	@Override
	public void collideWith(CharacterEvent character) {
		if (getUnit().getRelationTo(character).attackIfBored) {
			character.getUnit().attack(getUnit());
		}
	}

	/**
	 * Calculates the ticks until we next move.
	 * @return					The remaining ticks, in game ticks
	 */
	public int ticksToAct() {
		return ticksRemaining;
	}

	/**
	 * Simulates the passage of time in the game world.
	 * @param	ticks			The number of ticks elapsed, in game ticks
	 */
	public void simulateTime(int ticks) {
		ticksRemaining -= ticks;
	}
	
	/**
	 * Flashes our appearance a certain color for a certain time.
	 * @param	c				The color to flash
	 * @param	duration		How long the flash should take in total
	 */
	public void flash(Color c, float duration) {
		appearance.flash(c, duration);
	}

	/**
	 * Called every time we make a turn. Default selects an action then waits
	 * for its duration.
	 */
	public void act() {
		actAndWait(selectAction());
	}
	
	/**
	 * Performs an action and then waits an appropriate time for recovery, based
	 * on the act's cost. Should be called from act, almost always.
	 * @param	act				The action to perform
	 */
	public void actAndWait(Action act) {
		act.setActor(this);
		act.act();
		ticksRemaining += act.getCost();
	}
	
	/**
	 * Checks to see if a given location is in the hero's line of sight.
	 * @param	tileX			The x-coord of the tile to check, in tiles
	 * @param	tileY			The y-coord of the tile to check, in tiles
	 * @return					True if that tile is visible, false otherwise
	 */
	public boolean inLoS(int targetX, int targetY) {
		if (euclideanTileDistanceTo(targetX, targetY) > getStats().vision) return false;
		return rayExistsTo(targetX, targetY, rayLoS);
	}
	
	/**
	 * Runs the raycasting routine to an event instead of a point.
	 * @param	event			The event to raycast at
	 * @param	chk				The checking routeine to use
	 * @return
	 */
	public boolean rayExistsTo(MapEvent event, RayCheck chk) {
		return rayExistsTo(event.getTileX(), event.getTileY(), chk);
	}
	
	/**
	 * A tile-based raycasting algorithm. Checks whether a ray can be cast from
	 * this character to some other point on the map. The data parameters mean
	 * this can be used for line attacks or sight. To avoid hogging memory, the
	 * last object hit by the raycast is stored in the character's memory and
	 * can be queried there.
	 * @param	targetX			The x-coord to raycast to, in tiles
	 * @param	targetY			The y-coord to raycast to, in tiles
	 * @param	chk				The class with the failure condition
	 * @return					True if an unobstructed ray exists to there
	 */
	public boolean rayExistsTo(int targetX, int targetY, RayCheck chk) {
		// This algo copied from 2011SDRL
		boolean good = true;
		double m;
		double bend = .3; // 0-1. Higher values mean stricter sight around corners.
		double atX = getTileX();
		double atY = getTileY();
		int dx = -(getTileX()-targetX);
		int dy = -(getTileY()-targetY);
		if (dx==0 && dy==0) return true;
		if (dx == 0) m = 999; // HUGE_VALUE
		else m = (double)dy / (double)dx;
		// This is so we increment by no more than 1 tile on the path at a time
		if (Math.abs(m) < 1) {
			while (Math.abs(atX-targetX) > 1.1) {
				atX += (dx > 0) ? 1.0 : -1.0;
				atY += ((dx > 0) ? 1.0 : -1.0) * m;
				if (chk.bad((int)Math.floor(atX), (int)Math.floor(atY+bend))) {
					good = false;
					break;
				}
			}
			if (good) return true;
			atX = getTileX();
			atY = getTileY();
			while (Math.abs(atX-targetX) > 1.1) {
				atX += (dx > 0) ? 1.0 : -1.0;
				atY += ((dx > 0) ? 1.0 : -1.0) * m;
				if (chk.bad((int)Math.floor(atX), (int)Math.floor(atY+bend))) {
					return false;
				}
			}
		} else {
			while (Math.abs(atY-targetY) > 1.1) {
				atY += (dy > 0) ? 1.0 : -1.0;
				if (m!=999) atX += ((dy > 0) ? 1.0 : -1.0)/m;
				if (chk.bad((int)Math.floor(atX+bend), (int)Math.floor(atY))) {
					good=false;
					break;
				}
			}
			if (good) return true;
			atX = getTileX();
			atY = getTileY();
			while (Math.abs(atY-targetY) > 1.1) {
				atY += (dy > 0) ? 1.0 : -1.0;
				if (m!=999) atX += ((dy > 0) ? 1.0 : -1.0)/m;
				if (chk.bad((int)Math.ceil(atX-bend), (int)Math.floor(atY))) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks if a given event is visible by this character.
	 * @param	event			The event to check if visible
	 * @return					True if event is visible, false otherwise
	 */
	public boolean inLoS(MapEvent event) {
		return inLoS(event.getTileX(), event.getTileY());
	}
	
	/**
	 * Automated way of selecting an action to use each turn. Enemies should
	 * follow some AI, heroes will never call this, etc.
	 * @return					The action to use
	 */
	public Action selectAction() {
		return defaultWait;
	}

	/**
	 * Creates this event from an MDO.
	 * @param 	mdo			The MDO to create the event from
	 */
	protected void init(CharacterMDO mdo) {
		this.mdo = mdo;
		this.turnChildren = new ArrayList<Turnable>();
		if (mdoHasProperty(mdo.appearance)) {
			DirMDO dirMDO = MGlobal.data.getEntryFor(mdo.appearance, DirMDO.class);
			appearance = FacesAnimationFactory.create(dirMDO, this);
			assets.add(appearance);
		}
		setPacing(true);
		
		travelPlan = new ArrayList<Step>();
		
		initUnit();
		assets.add(unit);
		turnChildren.add(unit);
		ticksRemaining = 0;
		
		if (defaultWait == null) {
			defaultWait = new ActWait();
		}
		if (rayLoS == null) {
			rayLoS = new RayCheck() {
				@Override public boolean bad(int tileX, int tileY) {
					return !MGlobal.hero.getParent().isTransparentAt(tileX, tileY);
				}
			};
		}
	}
	
	/**
	 * Creates the game unit. Separate for overriding purposes.
	 */
	protected void initUnit() {
		unit = new GameUnit(mdo, this);
	}
	
	public abstract class RayCheck {
		/**
		 * A very messy inner condition for raycasting from 2011.
		 * @param	tileX			The current x-coord, in tiles
		 * @param	tileY			The current y-coord, in tiles
		 * @return					True if raycasting has hit an obstruction
		 */
		public abstract boolean bad(int tileX, int tileY);
	}

}
