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
import java.util.Stack;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;

import net.wombatrpgs.rainfall.characters.moveset.ActAttack;
import net.wombatrpgs.rainfall.characters.moveset.MovesetAct;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.core.Updateable;
import net.wombatrpgs.rainfall.graphics.FacesAnimation;
import net.wombatrpgs.rainfall.graphics.FacesAnimationFactory;
import net.wombatrpgs.rainfall.io.audio.SoundObject;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.maps.objects.TimerListener;
import net.wombatrpgs.rainfall.maps.objects.TimerObject;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.physics.NoHitbox;
import net.wombatrpgs.rainfall.scenes.SceneParser;
import net.wombatrpgs.rainfallschema.audio.SoundMDO;
import net.wombatrpgs.rainfallschema.characters.CharacterEventMDO;
import net.wombatrpgs.rainfallschema.characters.MobilityMDO;
import net.wombatrpgs.rainfallschema.characters.data.CollisionResponseType;
import net.wombatrpgs.rainfallschema.cutscene.SceneMDO;
import net.wombatrpgs.rainfallschema.graphics.DirMDO;
import net.wombatrpgs.rainfallschema.maps.data.DirVector;
import net.wombatrpgs.rainfallschema.maps.data.Direction;

/**
 * A character event is an event with an MDO and an animation that looks kind of
 * like a character. It's also a character in an RPG-like game, meaning it
 * should probably be split into RPG-chara and RM-chara at some point soon.
 */
public class CharacterEvent extends MapEvent {
	
	protected static final String PROPERTY_FACING = "face";
	protected static final String PROPERTY_WALK_IN_PLACE = "pace";
	protected static final String PROPERTY_CONVO = "convo";
	
	protected static final String DIR_DOWN = "down";
	protected static final String DIR_UP = "up";
	protected static final String DIR_LEFT = "left";
	protected static final String DIR_RIGHT = "right";
	
	protected CharacterEventMDO mdo;
	protected Map<Direction, Boolean> directionStatus;
	protected List<MovesetAct> activeMoves;
	protected List<MovesetAct> toCancel;
	protected SceneParser convo;
	protected SoundObject soundHurt;
	protected MobilityMDO mobilityMDO;
	protected FacesAnimation appearance;
	protected Stack<FacesAnimation> walkStack, idleStack;
	protected FacesAnimation walkAnim, idleAnim, stunAnim;
	
	protected boolean stunned;
	protected boolean dead;
	protected boolean pacing;
	
	protected Stats stats;
	protected int hp;

	/**
	 * Creates a new char event with the specified data at the specified coords.
	 * @param 	mdo				The data to create the event with
	 * @param	object			The tiled object that created the chara
	 * @param	parent			The parent level of the event
	 */
	public CharacterEvent(CharacterEventMDO mdo, MapObject object, Level parent) {
		super(parent, object, extractX(parent, object), extractY(parent, object), true, true);
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
	 * @param 	dir				The direction to face
	 */
	public void setFacing(Direction dir) {
		if (appearance != null) this.appearance.setFacing(dir);
		for (FacesAnimation anim : walkStack) {
			anim.setFacing(dir);
		}
		for (FacesAnimation anim : idleStack) {
			anim.setFacing(dir);
		}
	}
	
	/**
	 * Gives this character a new idling appearance with a four-dir anim.
	 * @param 	appearance		The new anim for this character
	 */
	public void setIdleAppearance(FacesAnimation appearance) {
		if (this.appearance == this.idleAnim) this.appearance = appearance;
		this.idleAnim = appearance;
		if (stunAnim == null) appearance.setFlicker(stunned);
	}
	
	/**
	 * Gives this character a new walking appearance with a four-dir anim.
	 * @param 	appearance		The new anim for this character
	 */
	public void setWalkingAppearance(FacesAnimation appearance) {
		if (this.appearance == this.walkAnim) this.appearance = appearance;
		this.walkAnim = appearance;
		if (stunAnim == null) appearance.setFlicker(stunned);
	}
	
	/**
	 * Gets the current appearance of this character event.
	 * @return					The current appearance of this character event
	 */
	public FacesAnimation getAppearance() {
		return appearance;
	}
	
	/**
	 * Gets the RPG-like stats bundle of this character, up to date.
	 * @return					The stats of the character
	 */
	public Stats getStats() {
		return stats;
	}
	
	/**
	 * Gets the current health of this character.
	 * @return					The health of the character, in hp
	 */
	public int getHP() {
		return hp;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.MapThing#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (hidden()) return;
		super.update(elapsed);
		for (Updateable move : activeMoves) {
			move.update(elapsed);
		}
		for (MovesetAct move : toCancel) {
			internalStopAction(move);
		}
		toCancel.clear();
		if (!pacing && appearance != null) {
			appearance.update(elapsed);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapThing#vitalUpdate(float)
	 */
	@Override
	public void vitalUpdate(float elapsed) {
		super.vitalUpdate(elapsed);
		if (pacing && appearance != null) {
			appearance.update(elapsed);
		}
		// TODO: moonwalk flag
		if (!pacing && walkAnim != null && !stunned)
			if (Math.abs(vx) < .1 && Math.abs(vy) < .1 &&
				Math.abs(targetVX) < .1 && Math.abs(targetVY) < .1) {
				if (walkAnim.isMoving()) {
					walkAnim.stopMoving();
				}
				if (appearance == walkAnim) {
					idleAnim.setFacing(appearance.getFacing());
					appearance = idleAnim;
				}
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		if (hidden()) return;
		super.render(camera);
		if (appearance != null) {
			appearance.render(camera);
		}
	}

	/**
	 * I'm not sure why this doesn't correspond to a queue method but oh well.
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		if (idleAnim == null) {
			idleAnim = walkAnim;
		}
		if (object != null) {
			String dir = getProperty(PROPERTY_FACING);
			if (dir != null) {
				if (dir.equals(DIR_DOWN)) {
					appearance.setFacing(Direction.DOWN);
				} else if (dir.equals(DIR_UP)) {
					appearance.setFacing(Direction.UP);
				} else if (dir.equals(DIR_RIGHT)) {
					appearance.setFacing(Direction.RIGHT);
				} else if (dir.equals(DIR_LEFT)) {
					appearance.setFacing(Direction.LEFT);
				} else {
					RGlobal.reporter.warn("Not a valid direction on char " + this + 
							" : " + dir);
				}
			}
			if (getProperty(PROPERTY_WALK_IN_PLACE) != null) {
				appearance.startMoving();
				pacing = true;
			}
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapThing#getHitbox()
	 */
	@Override
	public Hitbox getHitbox() {
		if (appearance == null || hidden()) return NoHitbox.getInstance();
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
	 * @see net.wombatrpgs.rainfall.maps.MapThing#isOverlappingAllowed()
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
		boolean moveLimited = false;
		for (MovesetAct act : activeMoves) {
			if (act.disablesMovement()) {
				moveLimited = true;
				break;
			}
		}
		return !moveLimited && !stunned && !dead && super.canMove();
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#onCollide
	 * (net.wombatrpgs.rainfall.maps.MapThing, net.wombatrpgs.rainfall.physics.CollisionResult)
	 */
	@Override
	public boolean onCollide(MapEvent other, CollisionResult result) {
		if (dead) return true;
		return other.onCharacterCollide(this, result);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapThing#onCharacterCollide
	 * (net.wombatrpgs.rainfall.characters.CharacterEvent, 
	 * net.wombatrpgs.rainfall.physics.CollisionResult)
	 */
	@Override
	public boolean onCharacterCollide(CharacterEvent other, CollisionResult result) {
		if (dead) return true;
		if (other == RGlobal.hero && convo != null) {
			convo.run(getLevel());
		}
		return false;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.MapThing#resolveCollision
	 * (net.wombatrpgs.rainfall.maps.MapThing, net.wombatrpgs.rainfall.physics.CollisionResult)
	 */
	@Override
	public void resolveCollision(MapEvent other, CollisionResult result) {
		other.resolveCharacterCollision(this, result);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapThing#resolveCharacterCollision
	 * (net.wombatrpgs.rainfall.characters.CharacterEvent, 
	 * net.wombatrpgs.rainfall.physics.CollisionResult)
	 */
	@Override
	public void resolveCharacterCollision(CharacterEvent other, CollisionResult result) {
		if (other.mdo.response == CollisionResponseType.IMMOBILE &&
			this.mdo.response == CollisionResponseType.IMMOBILE) {
			//RGlobal.reporter.warn("Two immobile objects collided? wtf?");
			applyMTV(other, result, 1f);
			return;
		}
		if (other.mdo.response == CollisionResponseType.IMMOBILE) {
			applyMTV(other, result, 1f);
			return;
		}
		if (this.mdo.response == CollisionResponseType.IMMOBILE) {
			applyMTV(other, result, 0f);
			return;
		}
		if (other.mdo.response == CollisionResponseType.PUSHABLE &&
				this.mdo.response == CollisionResponseType.PUSHABLE) {
				applyMTV(other, result, .5f);
				return;
		} else if (other.mdo.response == CollisionResponseType.PUSHABLE) {
			applyMTV(other, result, 0f);
			return;
		} else if (this.mdo.response == CollisionResponseType.PUSHABLE) {
			applyMTV(other, result, 1f);
			return;
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.PreRenderable#getRegion()
	 */
	@Override
	public TextureRegion getRegion() {
		if (hidden() || appearance == null) return null;
		return appearance.getRegion();
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		setY(getY() - parent.getTileHeight()); // TODO: awfuil hack idk bug reset pos
		if (parent.contains(soundHurt)) {
			parent.removeObject(soundHurt);
		}
		dead = false;
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#fallIntoHole(int, int)
	 */
	@Override
	public void fallIntoHole(int tileX, int tileY) {
		if (mdo.response != CollisionResponseType.ETHEREAL) {
			super.fallIntoHole(tileX, tileY);
		}
	}

	/**
	 * Finalized, override the internal equivalent instead.
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#respondToAttack
	 * (net.wombatrpgs.rainfall.characters.moveset.ActAttack)
	 */
	@Override
	public final void respondToAttack(ActAttack attack) {
		super.respondToAttack(attack);
		if (attack.getActor() == this) {
			return;
		} else {
			attack.applySpecialEffects(this);
			internalAttackResponse(attack);
		}
	}

	/**
	 * Determines if the hero is currently in a state to act, based on the
	 * actions the hero is currently carrying out and status conditions.
	 * @return					True if we can act, false otherwise.
	 */
	public boolean canAct() {
		return canMove() && !hidden();
	}
	
	/**
	 * Overrides the pacing action of this character.
	 * @param 	pacing			True if character should pace, false otherwise
	 */
	public void setPacing(boolean pacing) {
		this.pacing = pacing;
		if (pacing) {
			appearance.startMoving();
		}
	}
	
	/**
	 * The power of life and death!
	 * @param 	isDead			True if character is out of action
	 */
	public void setDead(boolean isDead) {
		this.dead = isDead;
	}
	
	/**
	 * Gets the speed from the mobility mdo.
	 * @return					Normal speed of this event
	 */
	public float getSpeed() {
		return mobilityMDO.walkVelocity;
	}

	/**
	 * Makes this event face towards an object on the map.
	 * @param 	event			The object to face
	 */
	public void faceToward(MapEvent event) {
		setFacing(directionTo(event));
	}
	
	/**
	 * Face away from a particular map event.
	 * @param	event			The object to face away from
	 */
	public void faceAway(MapEvent event) {
		setFacing(Direction.getOpposite(directionTo(event)));
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#halt()
	 */
	@Override
	public void halt() {
		super.halt();
		if (!pacing) {
			appearance.stopMoving();
			appearance.update(0);
		}
		targetLocation(getX(), getY());
		for (Direction dir : directionStatus.keySet()) {
			directionStatus.put(dir, false);
		}
	}

	/**
	 * Stuns the character to prevent action and voluntary movement. This
	 * happens for most attacks.
	 * @param	duration		How long this stun should last
	 */
	public void stun(float duration) {
		setStunned(true);
		halt();
		if (stunAnim != null) {
			addWalkAnim(stunAnim);
			addIdleAnim(stunAnim);
		}
		if (soundHurt != null) {
			soundHurt.play();
		}
		List<MovesetAct> cancelledActs = new ArrayList<MovesetAct>();
		for (MovesetAct act : activeMoves) {
			cancelledActs.add(act);
		}
		for (MovesetAct act : cancelledActs) {
			act.cancel();
		}
		new TimerObject(duration, this, new TimerListener() {
			@Override
			public void onTimerZero(TimerObject source) {
				appearance.setFlicker(false);
				setStunned(false);
				if (stunAnim != null) {
					while (appearance == stunAnim) {
						removeIdleAnim(stunAnim);
						removeWalkAnim(walkAnim);
					}
				}
			}
		});
	}
	
	/**
	 * Sets this character to a "dead" state. Rather than being removed from the
	 * map it silently waits for the map to be reset. Useful for enemies.
	 */
	public void kill() {
		dead = true;
		if (soundHurt != null) {
			soundHurt.play();
		}
	}
	
	/**
	 * Bounces forcibly off of another event.
	 * @param 	other			The other event to bounce off of
	 * @param	velocity		The speed this event will be set to
	 */
	public void bounce(MapEvent other, int velocity) {
		float dx = getX() - other.getX();
		float dy = getY() - other.getY();
		float hypo = (float) Math.sqrt(dx*dx + dy*dy);
		dx /= hypo;
		dy /= hypo;
		setVelocity(dx * velocity, dy * velocity);
	}
	
	/**
	 * Does not actually do anything but registers this move as having started.
	 * @param 	act				The act that's being started
	 */
	public void startAction(MovesetAct act) {
		if (!canAct()) return;
		activeMoves.add(act);
		if (act.getIdleAppearance() != null) {
			addIdleAnim(act.getIdleAppearance());
		}
		if (act.getWalkingAppearance() != null) {
			addWalkAnim(act.getWalkingAppearance());
		}
	}
	
	/**
	 * Queues an act to stop.
	 * @param 	act				The act stop
	 */
	public void stopAction(MovesetAct act) {
		if (activeMoves.contains(act)) {
			toCancel.add(act);
		} else {
			RGlobal.reporter.warn("Removed an unperformed action 2: " + act);
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
	 * Sets movement in a particular direction based on our mobility.
	 * @param 	dir				The direction to move in
	 */
	public void targetDirection(Direction dir) {
		DirVector vec = dir.getVector();
		float targetVX = mobilityMDO.walkVelocity * vec.x;
		float targetVY = mobilityMDO.walkVelocity * vec.y;
		targetVelocity(targetVX, targetVY);
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.MapThing#internalTargetVelocity(float, float)
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
			setFacing(newDir);
			if (appearance == idleAnim) {
				appearance = walkAnim;
			}
			if (!walkAnim.isMoving()) {
				walkAnim.startMoving();
			}
		}
		super.internalTargetVelocity(targetVX, targetVY);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#hidden()
	 */
	@Override
	protected boolean hidden() {
		return super.hidden() || dead;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#isAnchored()
	 */
	@Override
	protected boolean isAnchored() {
		return super.isAnchored() && canAct();
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
	 * Registers a move as having stopped. Adjusts appearance back to normal.
	 * Internal so as to prevent concurrent mod exceptions.
	 * @param 	act				The act that's being stopped
	 */
	protected void internalStopAction(MovesetAct act) {
		if (activeMoves.contains(act)) {
			activeMoves.remove(act);
		} else {
			RGlobal.reporter.warn("Removed an unperformed action 1: " + act);
		}
		walkStack.get(0).setFacing(appearance.getFacing());
		idleStack.get(0).setFacing(appearance.getFacing());
		if (act.getIdleAppearance() != null) {
			removeIdleAnim(act.getIdleAppearance());
		}
		if (act.getWalkingAppearance() != null) {
			removeWalkAnim(act.getWalkingAppearance());
		}
		if (targetVX != 0 && targetVY != 0) {
			Direction newFace;
			if (Math.abs(targetVX) > Math.abs(targetVY)) {
				newFace = (targetVX > 0) ? Direction.RIGHT : Direction.LEFT;
			} else {
				newFace = (targetVY > 0) ? Direction.UP : Direction.DOWN;
			}
			setFacing(newFace);
		}
	}
	
	/**
	 * Internal response to attacks. Happens when this character is hit by
	 * something that they didn't launch. Should do kickback/damage etc even
	 * if this is ultimately shifted into the hands of the attack itself. The
	 * attack will probably delegate all the necessary commands like stunning
	 * for characters to override if they're immune or something.
	 * @param 	attack			The attack that's being responded to
	 */
	protected void internalAttackResponse(ActAttack attack) {
		stun(attack.getStunDuration());
		bounce(attack.getActor(), attack.getKnockback());
		// TODO: damage formula
		this.hp -= attack.getPower();
		if (hp <= 0) kill();
	}
	
	/**
	 * Sets the stun status of this character. Stunned characters are unable to
	 * act.
	 * @param 	stunned			True if this chara is stunned, false otherwise
	 */
	protected void setStunned(boolean stunned) {
		this.stunned = stunned;
		if (stunAnim == null) {
			for (FacesAnimation anim : walkStack) {
				anim.setFlicker(stunned);
			}
			for (FacesAnimation anim : idleStack) {
				anim.setFlicker(stunned);
			}
			appearance.setFlicker(stunned);
		}
	}
	
	/**
	 * Creates this event from an MDO.
	 * @param 	mdo			The MDO to create the event from
	 */
	protected void init(CharacterEventMDO mdo) {
		this.mdo = mdo;
		checkCollisions = true;
		activeMoves = new ArrayList<MovesetAct>();
		toCancel = new ArrayList<MovesetAct>();
		walkStack = new Stack<FacesAnimation>();
		idleStack = new Stack<FacesAnimation>();
		if (mdoHasProperty(mdo.appearance)) {
			DirMDO dirMDO = RGlobal.data.getEntryFor(mdo.appearance, DirMDO.class);
			walkAnim = FacesAnimationFactory.create(dirMDO, this);
			walkStack.push(walkAnim);
			appearance = walkAnim;
			assets.add(appearance);
		}
		if (mdoHasProperty(mdo.idleAnim)) {
			DirMDO idleMDO = RGlobal.data.getEntryFor(mdo.idleAnim, DirMDO.class);
			idleAnim = FacesAnimationFactory.create(idleMDO, this);
			idleStack.push(idleAnim);
			assets.add(idleAnim);
		}
		if (mdoHasProperty(mdo.soundHurt)) {
			SoundMDO soundMDO = RGlobal.data.getEntryFor(mdo.soundHurt, SoundMDO.class);
			soundHurt = new SoundObject(soundMDO);
			assets.add(soundHurt);
		}
		if (mdoHasProperty(mdo.flinchAnim)) {
			DirMDO flinchMDO = RGlobal.data.getEntryFor(mdo.flinchAnim, DirMDO.class);
			stunAnim = FacesAnimationFactory.create(flinchMDO, this);
			assets.add(stunAnim);
		}
		if (object != null) {
			String convoKey = getProperty(PROPERTY_CONVO);
			if (convoKey != null) {
				SceneMDO sceneMDO = RGlobal.data.getEntryFor(convoKey, SceneMDO.class);
				convo = new SceneParser(sceneMDO);
				assets.add(convo);
			}
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
		dead = false;
		pacing = false;
		
		stats = new Stats(mdo.stats);
		hp = stats.getMHP();
	}
	
	/**
	 * Properly adds an idle animation to the stack of playing animations.
	 * Starts playing the animation right away.
	 * @param	anim			The animation to add
	 */
	protected void addIdleAnim(FacesAnimation anim) {
		setIdleAppearance(anim);
		idleStack.push(idleAnim);
		anim.reset();
		setFacing(walkAnim.getFacing());
		anim.startMoving();
	}
	
	/**
	 * Properly adds a walk animation to the stack of playing animations.
	 * Starts playing the animation right away.
	 * @param	anim			The animation to add
	 */
	protected void addWalkAnim(FacesAnimation anim) {
		setWalkingAppearance(anim);
		walkStack.push(walkAnim);
		anim.reset();
		setFacing(idleAnim.getFacing());
		anim.startMoving();
	}
	
	/**
	 * Properly removes an idle animation from the stack of playing animations.
	 * @param 	anim			The animation to remove
	 */
	protected void removeIdleAnim(FacesAnimation anim) {
		idleStack.remove(anim);
		idleAnim = idleStack.peek();
		if (appearance == anim) appearance = idleAnim;
	}
	
	/**
	 * Propertly removes a walk animation from the stack of playing animations.
	 * @param	anim			The animation to remove
	 */
	protected void removeWalkAnim(FacesAnimation anim) {
		walkStack.remove(anim);
		walkAnim = walkStack.peek();
		if (appearance == anim) appearance = walkAnim;
	}

}
