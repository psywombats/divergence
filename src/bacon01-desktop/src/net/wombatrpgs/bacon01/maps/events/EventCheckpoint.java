/**
 *  EventCheckpoint.java
 *  Created on Jan 24, 2015 3:36:44 PM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import net.wombatrpgs.bacon01.core.BMemory;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.core.interfaces.Timer;
import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.maps.objects.Picture;
import net.wombatrpgs.mgne.physics.Hitbox;
import net.wombatrpgs.mgne.physics.RectHitbox;
import net.wombatrpgs.mgneschema.maps.EventMDO;

/**
 * bacon
 */
public class EventCheckpoint extends MapEvent {
	
	protected RectHitbox rect;
	protected Picture pic;
	protected boolean collidedLast;
	protected boolean tweening;

	public EventCheckpoint(EventMDO mdo, TiledMapObject object) {
		super(mdo);
		rect = object.getRectHitbox();
		rect.setParent(this);
		
		pic = new Picture("checkpoint.png", 1);
		assets.add(pic);
		tweening = false;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#getHitbox()
	 */
	@Override
	public Hitbox getHitbox() {
		return rect;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		boolean colliding = MGlobal.getHero().getHitbox().isColliding(getHitbox()).isColliding;
		if (colliding && !collidedLast) {
			MGlobal.memory.save(BMemory.FILE_NAME);
			if (!tweening) {
				tweening = true;
				MGlobal.screens.peek().addChild(pic);
				pic.setX(pic.getWidth()/2);
				pic.setY(-pic.getHeight());
				pic.tweenTo(pic.getWidth()/2, pic.getHeight()/2, .5f, new FinishListener() {
					@Override public void onFinish() {
						new Timer(1.5f, new FinishListener() {
							@Override public void onFinish() {
								pic.tweenTo(pic.getWidth()/2, -pic.getHeight(), .5f, new FinishListener() {
									@Override public void onFinish() {
										tweening = false;
										MGlobal.screens.peek().removeChild(pic);
									}
								});
							}
						});
					}
				});
//				pic.setX(pic.getWidth()/2f);
//				pic.setY(pic.getHeight()/2f);
//				pic.fadeIn(MGlobal.screens.peek(), .5f);
//				new Timer(2f, new FinishListener() {
//					@Override public void onFinish() {
//						pic.fadeOut(.5f);
//						tweening = false;
//					}
//				});
			}
		}
		collidedLast = colliding;
	}

}
