/**
 *  Ability.java
 *  Created on Oct 18, 2013 4:16:28 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.abil;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.FinishListener;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.graphics.effects.AbilFX;
import net.wombatrpgs.mrogue.graphics.effects.AbilFxFactory;
import net.wombatrpgs.mrogue.io.CommandListener;
import net.wombatrpgs.mrogue.io.command.CMapDirections;
import net.wombatrpgs.mrogue.maps.MapThing;
import net.wombatrpgs.mrogue.maps.events.Cursor;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.GameUnit;
import net.wombatrpgs.mrogue.rpg.CharacterEvent.RayCheck;
import net.wombatrpgs.mrogue.rpg.act.Action;
import net.wombatrpgs.mrogue.rpg.travel.Step;
import net.wombatrpgs.mrogueschema.characters.AbilityMDO;
import net.wombatrpgs.mrogueschema.characters.data.AbilityTargetType;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;
import net.wombatrpgs.mrogueschema.maps.data.EightDir;

/**
 * An ability is a special sort of action. It can be used by a character or a
 * hero, and it's not necessarily part of an AI routine. Actually it's kind of
 * a typical thing then... it's just constructed from a special ability MDO.
 */
public class Ability extends Action implements Queueable, CommandListener {
	
	protected AbilityMDO mdo;
	protected AbilEffect effect;
	protected RayCheck check;
	protected MapEvent firstHit;
	protected List<GameUnit> targets;
	protected List<GameUnit> lastTargets;
	protected AbilFX fx;
	protected List<Queueable> assets;
	protected Graphic icon;
	protected Cursor targetCursor;
	protected boolean blocking;
	
	/**
	 * Creates a new ability for a particular actor from data.
	 * @param actor
	 */
	public Ability(CharacterEvent actor, AbilityMDO mdo) {
		super(actor);
		this.mdo = mdo;
		this.effect = AbilEffectFactory.createEffect(mdo.effect, this);
		this.assets = new ArrayList<Queueable>();
		blocking = false;
		
		targetCursor = new Cursor();
		assets.add(targetCursor);
		
		if (MapThing.mdoHasProperty(mdo.fx) && MGlobal.graphics.isShaderEnabled()) {
			fx = AbilFxFactory.createFX(mdo.fx, this);
			assets.add(fx);
		}
		if (MapThing.mdoHasProperty(mdo.icon)) {
			icon = new Graphic(mdo.icon);
			assets.add(icon);
		}
		
		final CharacterEvent actor2 = actor;
		switch (mdo.target) {
		case PROJECTILE:
			check = actor.new RayCheck() {
				@Override public boolean bad(int tileX, int tileY) {
					if (!actor2.getParent().isTilePassable(actor2, tileX, tileY)) {
						return false;
					}
					for (MapEvent event : actor2.getParent().getEventsAt(tileX, tileY)) {
						if (!event.isPassable() && event != actor2) {
							firstHit = event;
							return true;
						}
					}
					return false;
				}
			};
			break;
		case BALL: case BEAM:
			check = actor.new RayCheck() {
				@Override public boolean bad(int tileX, int tileY) {
					if (!actor2.getParent().isTilePassable(actor2, tileX, tileY)) {
						return true;
					}
					return false;
				}
			};
			break;
		default:
			// it's fine, we don't need raycasting
		}
	}
	
	/** @return The MP cost of this ability */
	public int getMP() { return mdo.mpCost; }
	
	/** @return The in-game name of this ability */
	public String getName() { return mdo.name; }
	
	/** @return The in-game desc of this ability */
	public String getDesc() { return mdo.abilDesc; }
	
	/** @return The step animation of this ability */
	public Step getStep() { return effect.getStep(); }
	
	/** @return All the units currently targeted by this ability */
	public List<GameUnit> getTargets() { return targets; }
	
	/** @return The range of this ability, in fractional tiles (radius) */
	public Float getRange() { return mdo.range; }
	
	/** @return This ability's targeting type */
	public AbilityTargetType getType() { return mdo.target; }
	
	/** @return How this ability should look in the UI */
	public Graphic getIcon() { return icon; }
	
	/** @return The class of effect this ability has */
	public Class<? extends AbilEffect> getEffectClass() { return effect.getClass(); }
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName() + "(" + mdo.key + ")";
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.rpg.act.Action#baseCost()
	 */
	@Override
	public int baseCost() {
		return mdo.energyCost;
	}
	
	/**
	 * Looks up the radius of this ability. This will be the same as the range
	 * in most cases, but beams and projectiles have radius 1.5. Right now only
	 * really suited for abilfx.
	 * @return					The radius of this ability
	 */
	public float getRadius() {
		switch (mdo.target) {
		case BALL:
			return mdo.range;
		case USER: case MELEE:
			return 1f;
		case BEAM: case PROJECTILE:
			return 1.5f;
		default:
			return 0;
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}

	/**
	 * This should only really be called by enemy AI or us from other methods.
	 * @see net.wombatrpgs.mrogue.rpg.act.Action#act()
	 */
	@Override
	final public void act() {
		actor.getUnit().onAbilityUsed(this);
		if (actor != MGlobal.hero) {
			acquireTargets();
		}
		effect.act(targets);
		actor.addStep(getStep());
		
		if (MapThing.mdoHasProperty(mdo.fx) &&
				MGlobal.hero.inLoS(actor) &&
				(targets.size() > 0 || mdo.target == AbilityTargetType.BALL)) {
			fxSpawn();
			fx = null;
		}
		
		targets = null;
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.io.CommandListener#onCommand
	 * (net.wombatrpgs.mrogueschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (!blocking) {
			MGlobal.reporter.warn("Ability got a command when not blocking: " + command);
			return false;
		}
		if (command == InputCommand.INTENT_CANCEL) {
			unblock();
			return true;
		}
		switch (mdo.target) {
		case MELEE:
			switch (command) {
			case MOVE_NORTH:		meleeTarget(EightDir.NORTH);		break;
			case MOVE_NORTHEAST:	meleeTarget(EightDir.NORTHEAST);	break;
			case MOVE_EAST:			meleeTarget(EightDir.EAST);			break;
			case MOVE_SOUTHEAST:	meleeTarget(EightDir.SOUTHEAST);	break;
			case MOVE_SOUTH:		meleeTarget(EightDir.SOUTH);		break;
			case MOVE_SOUTHWEST:	meleeTarget(EightDir.SOUTHWEST);	break;
			case MOVE_WEST:			meleeTarget(EightDir.WEST);			break;
			case MOVE_NORTHWEST:	meleeTarget(EightDir.NORTHWEST);	break;
			default:													break;
			}
			unblock();
			break;
		default:
			System.out.println("not yet implemented");
		}
		// we always consume commands on our command map
		return true;
	}

	/**
	 * The hero should call this to use this ability. Will check if the player
	 * has provided enough info for us to use this move, and if so, uses it.
	 * @return					True if this ability is no longer blocking
	 */
	public boolean useAndBlock() {
		acquireTargets();
		if (targets != null) {
			lastTargets = targets;
			if (targets.size() > 0 || mdo.target == AbilityTargetType.BALL) {
				MGlobal.hero.actAndWait(this);
			}
			targets = null;
			return true;
		}
		return !blocking;
	}
	
	/**
	 * Spawns a graphical representation of this ability.
	 */
	public void fxSpawn() {
		if (MGlobal.graphics.isShaderEnabled()) {
			if (fx == null) {
				fx = AbilFxFactory.createFX(mdo.fx, this);
				fx.postProcessing(MGlobal.assetManager, 0);
			}
			fx.spawn();
		}
	}
	
	/**
	 * Checks if this ability has any valid targets. Useful for AI.
	 * @return					True if any targets are in range
	 */
	public boolean anyInRange() {
		acquireTargets();
		if (targets == null || targets.size() == 0) return false;
		for (GameUnit target : targets) {
			if (actor.getUnit().getRelationTo(target).attackIfBored) return true;
		}
		return false;
	}
	
	/**
	 * Switches on the ability targeting type and gets all victims affected by
	 * this attack. Sets the resulting list to the corresponding field.
	 * @return					The characters affected by this ability
	 */
	protected void acquireTargets() {
		switch (mdo.target) {
		case USER:
			targets = new ArrayList<GameUnit>();
			targets.add(actor.getUnit());
			break;
		case BALL:
			targets = new ArrayList<GameUnit>();
			for (CharacterEvent chara : actor.getParent().getCharacters()) {
				if (actor.euclideanTileDistanceTo(chara) <= mdo.range &&
						actor.rayExistsTo(chara, check) &&
						chara != actor) {
					targets.add(chara.getUnit());
				}
			}
			break;
		case MELEE:
			if (actor == MGlobal.hero) {
				if (targets != null) {
					return;
				}
				if (!blocking) {
					targets = null;
					MGlobal.levelManager.getScreen().registerCommandListener(this);
					MGlobal.levelManager.getScreen().pushCommandContext(new CMapDirections());
					MGlobal.ui.getNarrator().msg("Enter a direction (012346789)...");
					blocking = true;
				}
			} else {
				targets = new ArrayList<GameUnit>();
				for (CharacterEvent chara : actor.getParent().getCharacters()) {
					if (actor.euclideanTileDistanceTo(chara) < 1.5 && actor != chara) {
						targets.add(chara.getUnit());
						break;
					}
				}
			}
			break;
		case PROJECTILE:
			if (actor == MGlobal.hero) {
				if (targets != null) {
					return;
				}
				if (!targetCursor.isActive()) {
					targets = null;
					CharacterEvent last = null;
					if (lastTargets != null && lastTargets.size() > 0) {
						last = lastTargets.get(0).getParent();
						if (!actor.inLoS(last) ||
								last.getUnit().isDead() ||
								actor.euclideanTileDistanceTo(last) > mdo.range) {
							last = null;
						}
					}
					if (last == null) {
						for (GameUnit unit : actor.getUnit().getVisibleEnemies()) {
							if (actor.euclideanTileDistanceTo(unit.getParent()) < mdo.range) {
								last = unit.getParent();
								break;
							}
						}
					}
					targetCursor.activate(true);
					targetCursor.setRange(mdo.range);
					if (last != null) {
						targetCursor.setTileX(last.getTileX());
						targetCursor.setTileY(last.getTileY());
						targetCursor.setX(last.getTileX() * last.getParent().getTileWidth());
						targetCursor.setY(last.getTileY() * last.getParent().getTileHeight());
						targetCursor.setLastTarget(last);
					}
					targetCursor.registerFinishListener(new FinishListener() {
						@Override public void onFinish() {
							targets = new ArrayList<GameUnit>();
							if (targetCursor.getLastTarget() != null) {
								targets.add(targetCursor.getLastTarget().getUnit());
							}
							blocking = false;
						}
					});
					MGlobal.ui.getNarrator().msg("Select a target...");
					blocking = true;
				}
			} else {
				targets = new ArrayList<GameUnit>();
				for (GameUnit enemy : actor.getUnit().getVisibleEnemies()) {
					if (actor.euclideanTileDistanceTo(enemy.getParent()) <= mdo.range &&
							actor != enemy.getParent() &&
							actor.getUnit().getRelationTo(enemy).attackIfBored) {
						targets.add(enemy);
						break;
					}
				}
			}
			break;
		default:
			MGlobal.reporter.warn("Unknown ability target type " + mdo.target +
					" for ability + " + mdo.key);
		}
	}
	
	/**
	 * Acquires a target in the specified direction.
	 * @param	dir				The direction to target
	 */
	protected void meleeTarget(EightDir dir) {
		int targetX = (int) (actor.getTileX() + dir.getVector().x);
		int targetY = (int) (actor.getTileY() + dir.getVector().y);
		targets = new ArrayList<GameUnit>();
		for (CharacterEvent chara : actor.getParent().getCharacters()) {
			if (chara.getTileX() == targetX && chara.getTileY() == targetY) {
				targets.add(chara.getUnit());
			}
		}
	}
	
	/**
	 * A fancy way to set blocking to false from true.
	 */
	protected void unblock() {
		MGlobal.levelManager.getScreen().popCommandContext();
		MGlobal.levelManager.getScreen().unregisterCommandListener(this);
		blocking = false;
	}
}
