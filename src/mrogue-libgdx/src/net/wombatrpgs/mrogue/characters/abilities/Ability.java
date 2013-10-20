/**
 *  Ability.java
 *  Created on Oct 18, 2013 4:16:28 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.abilities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.characters.Action;
import net.wombatrpgs.mrogue.characters.CharacterEvent;
import net.wombatrpgs.mrogue.characters.CharacterEvent.RayCheck;
import net.wombatrpgs.mrogue.characters.GameUnit;
import net.wombatrpgs.mrogue.characters.travel.Step;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.graphics.effects.AbilFX;
import net.wombatrpgs.mrogue.graphics.effects.AbilFxFactory;
import net.wombatrpgs.mrogue.maps.MapThing;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogueschema.characters.AbilityMDO;
import net.wombatrpgs.mrogueschema.characters.data.AbilityTargetType;

/**
 * An ability is a special sort of action. It can be used by a character or a
 * hero, and it's not necessarily part of an AI routine. Actually it's kind of
 * a typical thing then... it's just constructed from a special ability MDO.
 */
public class Ability extends Action implements Queueable {
	
	protected AbilityMDO mdo;
	protected AbilEffect effect;
	protected RayCheck check;
	protected MapEvent firstHit;
	protected List<GameUnit> targets;
	protected AbilFX fx;
	protected List<Queueable> assets;
	protected Graphic icon;
	
	/**
	 * Creates a new ability for a particular actor from data.
	 * @param actor
	 */
	public Ability(CharacterEvent actor, AbilityMDO mdo) {
		super(actor);
		this.mdo = mdo;
		this.effect = AbilEffectFactory.createEffect(mdo.effect, this);
		this.assets = new ArrayList<Queueable>();
		
		if (MapThing.mdoHasProperty(mdo.fx)) {
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
	
	/** @return The step animation of this ability */
	public Step getStep() { return effect.getStep(); }
	
	/** @return All the units currently targeted by this ability */
	public List<GameUnit> getTargets() { return targets; }
	
	/** @return The range of this ability, in fractional tiles (radius) */
	public Float getRange() { return mdo.range; }
	
	/** @return This ability's targeting type */
	public AbilityTargetType getType() { return mdo.target; }
	
	public Graphic getIcon() { return icon; }
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName() + "(" + mdo.key + ")";
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
	 * @see net.wombatrpgs.mrogue.characters.Action#act()
	 */
	@Override
	final public void act() {
		actor.getUnit().onAbilityUsed(this);
		effect.act(acquireTargets());
		actor.addStep(getStep());
		
		if (MapThing.mdoHasProperty(mdo.fx) && MGlobal.hero.inLoS(actor)) {
			if (fx == null) {
				fx = AbilFxFactory.createFX(mdo.fx, this);
				fx.postProcessing(MGlobal.assetManager, 0);
			}
			fx.spawn();
			fx = null;
		}
	}
	
	/**
	 * Switches on the ability targeting type and gets all victims affected by
	 * this attack.
	 * @return					The characters affected by this ability
	 */
	protected List<GameUnit> acquireTargets() {
		List<GameUnit> targets = new ArrayList<GameUnit>();
		switch (mdo.target) {
		case USER:
			targets.add(actor.getUnit());
			break;
		case BALL:
			for (CharacterEvent chara : actor.getParent().getCharacters()) {
				if (actor.euclideanTileDistanceTo(chara) <= mdo.range &&
						actor.rayExistsTo(chara, check) &&
						chara != actor) {
					targets.add(chara.getUnit());
				}
			}
			break;
		default:
			MGlobal.reporter.warn("Unknown ability target type " + mdo.target +
					" for ability + " + mdo.key);
		}
		return targets;
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.characters.Action#baseCost()
	 */
	@Override
	protected int baseCost() {
		return mdo.energyCost;
	}
	
}
