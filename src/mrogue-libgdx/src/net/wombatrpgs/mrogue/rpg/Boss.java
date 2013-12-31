/**
 *  Boss.java
 *  Created on Oct 27, 2013 3:27:02 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mrogue.core.FinishListener;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Turnable;
import net.wombatrpgs.mrogue.graphics.BossPicSet;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.graphics.effects.Effect;
import net.wombatrpgs.mrogue.graphics.effects.EffectFactory;
import net.wombatrpgs.mrogue.io.audio.MusicObject;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.layers.EventLayer;
import net.wombatrpgs.mrogue.maps.objects.Picture;
import net.wombatrpgs.mrogue.maps.objects.TimerListener;
import net.wombatrpgs.mrogue.maps.objects.TimerObject;
import net.wombatrpgs.mrogue.rpg.abil.Ability;
import net.wombatrpgs.mrogue.rpg.ai.BTAction;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogue.screen.instances.EndingScreen;
import net.wombatrpgs.mrogueschema.audio.MusicMDO;
import net.wombatrpgs.mrogueschema.characters.AbilityMDO;
import net.wombatrpgs.mrogueschema.characters.BossMDO;
import net.wombatrpgs.mrogueschema.characters.data.AbilityTargetType;

/**
 * Just the boss of the game, nbd.
 */
public class Boss extends Enemy {
	
	protected static final String BOSS_DEFAULT = "boss_default";
	
	protected BossMDO mdo;
	protected Effect effect;
	protected Ability fxAbil;
	protected SceneParser scene, winScene;
	protected MusicObject music;
	protected boolean seen;
	
	// rave mode
	protected MusicObject raveMusic;
	protected Ability raveAbil;
	protected SceneParser raveScene;
	protected Graphic bsod, bsodMessage;
	protected BossPicSet gos;
	protected Picture bsodPic, promptPic;
	protected boolean bsodMode;
	
	/**
	 * Creates a new boss character. I'm not sure how this'll work yet.
	 * @param	parent			The parent level to create on
	 * @param	tileX			The tile to spawn at
	 * @param	tileY			The tile to spawn at
	 */
	public Boss(Level parent, int tileX, int tileY) {
		super(MGlobal.data.getEntryFor(BOSS_DEFAULT, BossMDO.class), parent);
		this.mdo = MGlobal.data.getEntryFor(BOSS_DEFAULT, BossMDO.class);
		unit.setName(MGlobal.levelManager.getBossName());
		seen = false;
		
		if (mdoHasProperty(mdo.effect) && MGlobal.graphics.isShaderEnabled()) {
			effect = EffectFactory.create(parent, mdo.effect);
			assets.add(effect);
		}
		if (mdoHasProperty(mdo.abilFX)) {
			AbilityMDO abilMDO = new AbilityMDO();
			abilMDO.range = 3f;
			abilMDO.fx = mdo.abilFX;
			abilMDO.effect = "effect_physical";
			abilMDO.target = AbilityTargetType.BALL;
			fxAbil = new Ability(this, abilMDO);
			assets.add(fxAbil);
		}
		if (mdoHasProperty(mdo.sightedScene)) {
			scene = MGlobal.levelManager.getCutscene(mdo.sightedScene);
			assets.add(scene);
		}
		if (mdoHasProperty(mdo.music)) {
			music = new MusicObject(MGlobal.data.getEntryFor(mdo.music, MusicMDO.class));
			assets.add(music);
			
		}
		
		// rave mode
		raveScene = MGlobal.levelManager.getCutscene(mdo.deathScene);
		raveMusic = new MusicObject(MGlobal.data.getEntryFor(mdo.glitchMusic, MusicMDO.class));
		raveAbil = new Ability(this, MGlobal.data.getEntryFor(mdo.raveAbility, AbilityMDO.class));
		bsod = new Graphic(mdo.bsod);
		bsodMessage = new Graphic(mdo.bsodMessage);
		gos = new BossPicSet(mdo.gos);
		assets.add(raveScene);
		assets.add(raveAbil);
		assets.add(raveMusic);
		assets.add(bsod);
		assets.add(bsodMessage);
		assets.add(gos);
		
		winScene = MGlobal.levelManager.getCutscene(mdo.deadScene);
		assets.add(winScene);
	}
	
	/** @return True if the hero has spotted us */
	public boolean hasBeenSighted() { return seen; }

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#getName()
	 */
	@Override
	public String getName() {
		return "boss";
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.CharacterEvent#onAdd
	 * (net.wombatrpgs.mrogue.maps.layers.EventLayer)
	 */
	@Override
	public void onAdd(EventLayer layer) {
		super.onAdd(layer);
		if (fxAbil != null) {
			fxAbil.fxSpawn();
		}
		if (MGlobal.graphics.isShaderEnabled()) {
			parent.getScreen().addObject(effect);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.CharacterEvent#onRemove
	 * (net.wombatrpgs.mrogue.maps.layers.EventLayer)
	 */
	@Override
	public void onRemove(EventLayer layer) {
		super.onRemove(layer);
		if (MGlobal.graphics.isShaderEnabled()) {
			MGlobal.levelManager.getScreen().removeObject(effect);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.CharacterEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (scene != null && !seen && MGlobal.hero.inLoS(this)) {
			scene.run();
			seen = true;
		}
		if (!MGlobal.raveMode && !raveScene.isRunning() && getStats().hp < getStats().mhp * .9f) {
			raveScene.run();
			raveScene.addListener(new FinishListener() {
				@Override public void onFinish() {
					MGlobal.hero.getStats().hp = MGlobal.hero.getStats().mhp;
					MGlobal.raveMode = true;
				}	
			});
			getStats().hp += 10000;
			getUnit().addTurnChild(new Turnable() {
				int deathcount = 6;
				@Override public void onTurn() {
					deathcount -= 1;
					if (deathcount == 0) {
						MGlobal.stasis = true;
						getUnit().removeTurnChild(this);
					}
				}
			});
			intelligence = new BTAction(this, raveAbil, true);
		}
	}
	
	/**
	 * Rave modeeee.
	 */
	public void onStasis() {
		MGlobal.screens.playMusic(raveMusic, true);
		final Boss boss = this;
		final TimerListener goTimer = new TimerListener() {
			@Override public void onTimerZero(TimerObject source) {
				MGlobal.levelManager.getScreen().addObject(gos);
				MGlobal.levelManager.getScreen().removeObject(bsodPic);
				MGlobal.levelManager.getScreen().removeObject(promptPic);
				MGlobal.won = true;
				parent.removeEvent(boss);
			}
		};
		final TimerListener bsodTimer = new TimerListener() {
			@Override public void onTimerZero(TimerObject source) {
				promptPic = new Picture(bsodMessage,
						MGlobal.window.getWidth()/2 - bsodMessage.getWidth()/2,
						16,
						1000) {
					float total = 0;
					@Override public void update(float elapsed) {
						super.update(elapsed);
						total += elapsed;
					}
					@Override public void render(OrthographicCamera camera) {
						if (Math.round(total*2f) % 2 == 0) {
							super.render(camera);
						}
					}
					
				};
				MGlobal.levelManager.getScreen().addObject(promptPic);
				new TimerObject(5.0f, MGlobal.levelManager.getScreen(), goTimer);
			}
		};
		new TimerObject(3.5f, MGlobal.levelManager.getScreen(), new TimerListener() {
			@Override public void onTimerZero(TimerObject source) {
				raveMusic.stop();
				bsodMode = true;
				bsodPic = new Picture(bsod, 999);
				MGlobal.levelManager.getScreen().addObject(bsodPic);
				new TimerObject(2.0f, MGlobal.levelManager.getScreen(), bsodTimer);
			}
		});
	}
	
	public void onStasisEnd() {
		MGlobal.hero.getStats().hp = MGlobal.hero.getStats().mhp;
		MGlobal.levelManager.getScreen().removeObject(gos);
		MGlobal.stasis = false;
		MGlobal.raveMode = false;
		MGlobal.screens.playMusic(music, false);
		if (!winScene.isRunning() && !winScene.hasExecuted()) {
			winScene.run();
			winScene.addListener(new FinishListener() {
				@Override public void onFinish() {
					MGlobal.screens.playMusic(null, false);
					MGlobal.won2 = true;
					MGlobal.screens.pop();
					MGlobal.screens.push(new EndingScreen());
				}
			});
		}
	}

}
