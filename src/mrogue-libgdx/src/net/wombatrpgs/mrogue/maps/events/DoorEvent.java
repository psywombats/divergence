/**
 *  DoorEvent.java
 *  Created on Oct 24, 2013 5:43:42 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.events;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.FacesAnimation;
import net.wombatrpgs.mrogue.graphics.FacesAnimationFactory;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogueschema.maps.data.DoorSetMDO;
import net.wombatrpgs.mrogueschema.maps.data.OrthoDir;

/**
 * creaaaaaaak... TENSION ROOM OF RATS AUGHHHH!!!
 */
public class DoorEvent extends MapEvent {
	
	protected DoorSetMDO mdo;
	protected FacesAnimation openAnim, closedAnim;
	protected boolean open, opening;
	protected MapEvent child1, child2, child3, child4, child5;

	/**
	 * Creates a closed door.
	 * @param	parent			The parent map to generate on
	 */
	public DoorEvent(DoorSetMDO mdo, Level parent) {
		super(parent);
		this.mdo = mdo;
		open = false;
		opening = false;
		openAnim = FacesAnimationFactory.create(mdo.openAnim, this);
		closedAnim = FacesAnimationFactory.create(mdo.closeAnim, this);
		assets.add(openAnim);
		assets.add(closedAnim);
		
		final DoorEvent d = this;
		child1 = new MapEvent() {
			@Override public boolean isTransparent() { return open; }
		};
		child2 = new MapEvent() {
			@Override public boolean isTransparent() { return open; }
		};
		child3 = new MapEvent() {
			@Override public boolean isTransparent() { return open || MGlobal.hero.getTileY() < d.getTileY(); }
		};
		child4 = new MapEvent() {
			@Override public boolean isTransparent() { return open || MGlobal.hero.getTileY() < d.getTileY(); }
		};
		child5 = new MapEvent() {
			@Override public boolean isTransparent() { return open || MGlobal.hero.getTileY() > d.getTileY(); }
		};
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#isPassable()
	 */
	@Override
	public boolean isPassable() {
		return open;
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#isTransparent()
	 */
	@Override
	public boolean isTransparent() {
		return open;
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#collideWith
	 * (net.wombatrpgs.mrogue.rpg.CharacterEvent)
	 */
	@Override
	public void collideWith(CharacterEvent character) {
		super.collideWith(character);
		if (opening) {
			open = true;
			return;
		}
		if (character != MGlobal.hero) {
			return;
		}
		if (MGlobal.hero.inLoS(this)) {
			MGlobal.ui.getNarrator().msg(character.getUnit().getName() + 
					" opened the door.");
		} else {
			MGlobal.ui.getNarrator().msg("Something creaked open in the distance...");
		}
		opening = true;
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		if (!MGlobal.graphics.isShaderEnabled() && !MGlobal.hero.inLoS(this)) {
			return;
		}
		if (open) {
			openAnim.render(camera);
		} else {
			closedAnim.render(camera);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (!parent.isMoving() && opening && !open) {
			open = true;
			MGlobal.hero.refreshVisibilityMap();
		}
//		if (openAnim.getFacing() == OrthoDir.NORTH || openAnim.getFacing() == OrthoDir.SOUTH) {
//			if (MGlobal.hero.getTileY() >= this.getTileY()) {
//				child1.setTileY(getTileY());
//				child1.setTileY(getTileY());
//				child1.setTileY(getTileY());
//			} else {
//				child1.setTileY(getTileY()+1);
//				child1.setTileY(getTileY()+1);
//				child1.setTileY(getTileY()+1);
//			}
//		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#mouseoverMessage()
	 */
	@Override
	public String mouseoverMessage() {
		return ("A " + (open ? "open" : "closed") + " door.");
	}

	/**
	 * Sets this door's facing. Should really only be called once, ever.
	 * @param	dir				The direction to face
	 */
	public void setFacing(OrthoDir dir) {
		if (parent.contains(child1)) {
			parent.removeEvent(child1);
			parent.removeEvent(child2);
			parent.removeEvent(child3);
			parent.removeEvent(child4);
			parent.removeEvent(child5);
		}
		if (dir == OrthoDir.EAST || dir == OrthoDir.WEST) {
			parent.addEvent(child1, getTileX(), getTileY() + 1);
			parent.addEvent(child2, getTileX(), getTileY() + 2);
		} else {
			
			parent.addEvent(child1, getTileX()-1, getTileY() + 1);
			parent.addEvent(child2, getTileX()+1, getTileY() + 1);
			parent.addEvent(child3, getTileX()-1, getTileY());
			parent.addEvent(child4, getTileX()+1, getTileY());
			parent.addEvent(child5, getTileX(), getTileY() + 1);
		}
		openAnim.setFacing(dir);
		closedAnim.setFacing(dir);
	}
}
