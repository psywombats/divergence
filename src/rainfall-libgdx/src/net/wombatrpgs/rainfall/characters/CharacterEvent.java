/**
 *  CharacterEvent.java
 *  Created on Nov 12, 2012 11:13:21 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.collisions.NoHitbox;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.FacesAnimation;
import net.wombatrpgs.rainfall.graphics.FacesAnimationFactory;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.maps.objects.TimerListener;
import net.wombatrpgs.rainfall.maps.objects.TimerObject;
import net.wombatrpgs.rainfall.moveset.MovesetAct;
import net.wombatrpgs.rainfallschema.characters.CharacterEventMDO;
import net.wombatrpgs.rainfallschema.characters.MobilityMDO;
import net.wombatrpgs.rainfallschema.characters.data.CollisionResponseType;
import net.wombatrpgs.rainfallschema.graphics.DirMDO;
import net.wombatrpgs.rainfallschema.maps.data.DirVector;
import net.wombatrpgs.rainfallschema.maps.data.Direction;

/**
 * A character event is an event with an MDO and an animation that looks kind of
 * like a character.
 */
public class CharacterEvent extends MapEvent {
	
	protected Map<Direction, Boolean> directionStatus;
	protected List<MovesetAct> activeMoves;
	protected CharacterEventMDO mdo;
	protected MobilityMDO mobilityMDO;
	protected FacesAnimation appearance;
	protected FacesAnimation walkAnim;
	
	protected boolean stunned;

	/**
	 * Creates a new char event with the specified data at the specified coords.
	 * @param 	mdo				The data to create the event with
	 * @param	object			The tiled object that created the chara
	 * @param	parent			The parent level of the event
	 * @param 	x				The x-coord of the event (in pixels)
	 * @param 	y				The y-coord of the event (in pixels)
	 */
	public CharacterEvent(CharacterEventMDO mdo, TiledObject object, Level parent, int x, int y) {
		super(parent, object, x, y, true, true);
		init(mdo);
	}
	
	/**
	 * Creates a new character event associated with no map from the MDO.
	 * @param 	mdo				The MDO to create the event from
	 */
	public CharacterEvent(CharacterEventMDO mdo) {
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
	
	/**
	 * Gets the direction this character is currently facing from its animation
	 * @return					The direction currently facing
	 */
	public Direction getFacing() {
		return appearance.getFacing();
	}
	
	/**
	 * Tells the animation to face a specific direction.
	 * @param 	dir				The directiont to face
	 */
	public void setFacing(Direction dir) {
		this.appearance.setFacing(dir);
	}
	
	/**
	 * Gives this character a new (temporary?) appearance with a four-dir anim
	 * @param 	appearance		The new anim for this character
	 */
	public void setAppearance(FacesAnimation appearance) {
		this.appearance = appearance;
	}
	
	/**
	 * Gets the current appearance of this character event.
	 * @return					The current appearance of this character event
	 */
	public FacesAnimation getAppearance() {
		return appearance;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (appearance != null) {
			appearance.update(elapsed);
		}
		if (Math.abs(vx) < .1 && Math.abs(vy) < .1) {
			walkAnim.stopMoving();
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		if (appearance != null) {
			appearance.render(camera);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		appearance.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		appearance.postProcessing(manager, pass);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#getHitbox()
	 */
	@Override
	public Hitbox getHitbox() {
		switch (mdo.collision) {
		case ANIMATION_SPECIFIC_RECTANGLE:
			return appearance.getHitbox();
		case SOMETHING_ELSE_TELL_PSY_RIGHT_AWAY:
			RGlobal.reporter.warn("Got a hitbox for something totally weird");
			return NoHitbox.getInstance();
		case NONE:
			return NoHitbox.getInstance();
		default:
			RGlobal.reporter.warn("No hitbox setting found on " + this);
			return NoHitbox.getInstance();
		}
	}

	/** 
	 * @see net.wombatrpgs.rainfall.maps.MapObject#isOverlappingAllowed()
	 **/
	@Override
	public boolean isOverlappingAllowed() { 
		switch (mdo.response) {
		case ETHEREAL:
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#canMove()
	 */
	@Override
	public boolean canMove() {
		return !stunned && super.canMove();
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#onCollide
	 * (net.wombatrpgs.rainfall.maps.MapObject, net.wombatrpgs.rainfall.collisions.CollisionResult)
	 */
	@Override
	public boolean onCollide(MapEvent other, CollisionResult result) {
		return other.onCharacterCollide(this, result);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#onCharacterCollide
	 * (net.wombatrpgs.rainfall.characters.CharacterEvent, 
	 * net.wombatrpgs.rainfall.collisions.CollisionResult)
	 */
	@Override
	public boolean onCharacterCollide(CharacterEvent other, CollisionResult result) {
		return false;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#resolveCollision
	 * (net.wombatrpgs.rainfall.maps.MapObject, net.wombatrpgs.rainfall.collisions.CollisionResult)
	 */
	@Override
	public void resolveCollision(MapEvent other, CollisionResult result) {
		other.resolveCharacterCollision(this, result);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#resolveCharacterCollision
	 * (net.wombatrpgs.rainfall.characters.CharacterEvent, 
	 * net.wombatrpgs.rainfall.collisions.CollisionResult)
	 */
	@Override
	public void resolveCharacterCollision(CharacterEvent other, CollisionResult result) {
		if (other.mdo.response == CollisionResponseType.IMMOBILE &&
			this.mdo.response == CollisionResponseType.IMMOBILE) {
			RGlobal.reporter.warn("Two immobile objects collided? wtf?");
			applyMTV(other, result, 1f);
			return;
		}
		if (other.mdo.response == CollisionResponseType.IMMOBILE) {
			applyMTV(other, result, 1f);
		}
		if (this.mdo.response == CollisionResponseType.IMMOBILE) {
			applyMTV(other, result, 0f);
		}
		if (other.mdo.response == CollisionResponseType.PUSHABLE &&
				this.mdo.response == CollisionResponseType.PUSHABLE) {
				applyMTV(other, result, .5f);
		} else if (other.mdo.response == CollisionResponseType.PUSHABLE) {
			applyMTV(other, result, 0f);
		} else if (this.mdo.response == CollisionResponseType.PUSHABLE) {
			applyMTV(other, result, 1f);
		}
	}
	
	/**
	 * Determines if the hero is currently in a state to act, based on the
	 * actions the hero is currently carrying out and status conditions.
	 * @return					True if we can act, false otherwise.
	 */
	public boolean canAct() {
		return canMove();
	}

	/**
	 * Makes this event face towards an object on the map.
	 * @param 	object			The object to face
	 */
	public void faceToward(MapEvent object) {
		int dx = object.getX() - this.getX();
		int dy = object.getY() - this.getY();
		if (Math.abs(dx) > Math.abs(dy)) {
			if (dx > 0) {
				setFacing(Direction.RIGHT);
			} else {
				setFacing(Direction.LEFT);
			}
		} else {
			if (dy > 0) {
				setFacing(Direction.UP);
			} else {
				setFacing(Direction.DOWN);
			}
		}
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#halt()
	 */
	@Override
	public void halt() {
		super.halt();
		appearance.stopMoving();
		targetVX = 0;
		targetVY = 0;
		for (Direction dir : directionStatus.keySet()) {
			directionStatus.put(dir, false);
		}
	}

	/**
	 * Stuns the character to prevent action for some time or something?
	 */
	public void stun() {
		setStunned(true);
		halt();
		List<MovesetAct> cancelledActs = new ArrayList<MovesetAct>();
		for (MovesetAct act : activeMoves) {
			cancelledActs.add(act);
		}
		for (MovesetAct act : cancelledActs) {
			act.cancel();
		}
		new TimerObject(1.0f, this, new TimerListener() {
			@Override
			public void onTimerZero(TimerObject source) {
				appearance.setFlicker(false);
				setStunned(false);
			}
		});
	}
	
	/**
	 * Bounces forcibly off of another event.
	 * @param 	other			The other event to bounce off of.
	 */
	public void bounce(MapEvent other) {
		float dx = getX() - other.getX();
		float dy = getY() - other.getY();
		float hypo = (float) Math.sqrt(dx*dx + dy*dy);
		dx /= hypo;
		dy /= hypo;
		setVelocity(dx * mobilityMDO.walkVelocity * 2.5f, dy * mobilityMDO.walkVelocity * 2.5f);
	}
	
	/**
	 * Does not actually do anything but registers this move as having started.
	 * @param 	act				The act that's being started
	 */
	public void startAction(MovesetAct act) {
		if (!canAct()) return;
		activeMoves.add(act);
		if (act.getAppearance() != null) {
			setAppearance(act.getAppearance());
			act.getAppearance().reset();
			act.getAppearance().startMoving(walkAnim.getFacing());
		}
	}
	
	/**
	 * Registers a move as having stopped. Adjusts appearance back to normal.
	 * @param 	act				The act that's being stopped
	 */
	public void stopAction(MovesetAct act) {
		if (activeMoves.contains(act)) {
			activeMoves.remove(act);
		} else {
			RGlobal.reporter.warn("Removed an unperformed action: " + act);
		}
		if (activeMoves.size() == 0 && appearance != walkAnim) {
			appearance = walkAnim;
			if (targetVX != 0 && targetVY != 0) {
				Direction newFace;
				if (Math.abs(targetVX) > Math.abs(targetVY)) {
					newFace = (targetVX > 0) ? Direction.RIGHT : Direction.LEFT;
				} else {
					newFace = (targetVY > 0) ? Direction.UP : Direction.DOWN;
				}
				walkAnim.setFacing(newFace);
			}
		}
	}
	
	/**
	 * Determines if a move is currently being performed.
	 * @param 	act				The act to check
	 * @return					True if that act is active, false otherwise
	 */
	public boolean isMoveActive(MovesetAct act) {
		return activeMoves.contains(act);
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#internalTargetVelocity(float, float)
	 */
	@Override
	protected void internalTargetVelocity(float targetVX, float targetVY) {
		if (appearance != null && 
				(targetVX != this.targetVX || targetVY != this.targetVY) && 
				(Math.abs(targetVX) > .1 || Math.abs(targetVY) > .1)) {
			Direction newDir;
			if (Math.abs(targetVX) >= Math.abs(targetVY)) {
				if (targetVX * Direction.RIGHT.getVector().x > 0) {
					newDir = Direction.RIGHT;
				} else {
					newDir = Direction.LEFT;
				}
			} else {
				if (targetVY * Direction.DOWN.getVector().y > 0) {
					newDir = Direction.DOWN;
				} else {
					newDir = Direction.UP;
				}
			}
			appearance.startMoving(newDir);
		}
		super.internalTargetVelocity(targetVX, targetVY);
	}

	/**
	 * The character starts moving in the specified direction. Uses its built-in
	 * speed. (but right now it just takes it from the speed mdo)
	 * @param 	vector		The vector direction to start moving in
	 */
	protected void addMoveComponent(DirVector vector) {
		float newX = this.targetVX + vector.x * mobilityMDO.walkVelocity;
		float newY = this.targetVY + vector.y * mobilityMDO.walkVelocity;
		this.targetVelocity(newX, newY);
	}
	
	/**
	 * Sets the stun status of this character. Stunned characters are unable to
	 * act.
	 * @param 	stunned			True if this chara is stunned, false otherwise
	 */
	protected void setStunned(boolean stunned) {
		this.stunned = stunned;
		this.appearance = walkAnim;
		this.appearance.setFlicker(stunned);
	}
	
	/**
	 * Creates this event from an MDO.
	 * @param 	mdo			The MDO to create the event from
	 */
	protected void init(CharacterEventMDO mdo) {
		this.mdo = mdo;
		activeMoves = new ArrayList<MovesetAct>();
		if (mdo.appearance != null) {
			DirMDO dirMDO = RGlobal.data.getEntryFor(mdo.appearance, DirMDO.class);
			walkAnim = FacesAnimationFactory.create(dirMDO, this);
			appearance = walkAnim;
		}
		directionStatus = new HashMap<Direction, Boolean>();
		directionStatus.put(Direction.DOWN, false);
		directionStatus.put(Direction.UP, false);
		directionStatus.put(Direction.LEFT, false);
		directionStatus.put(Direction.RIGHT, false);
		mobilityMDO = RGlobal.data.getEntryFor(mdo.mobility, MobilityMDO.class);
		acceleration = mobilityMDO.acceleration;
		decceleration = mobilityMDO.decceleration;
		maxVelocity = mobilityMDO.walkVelocity;
	}

}
