/**
 *  ScreenMovable.java
 *  Created on Jan 17, 2014 10:52:05 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.PositionSetable;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.screen.ScreenObject;

/**
 * Somewhere between a picture and an object. This is so text objects can get
 * the picture positioning stuff. Also has a batch because uh yeah you're going
 * to be rendering things.
 */
public abstract class ScreenGraphic extends ScreenObject implements PositionSetable {
	
	protected Screen parent;
	protected Color currentColor;
	protected float x, y;
	protected boolean fadingOut;
	
	protected boolean tweening;
	protected float tweenTime;
	protected float tweenEnd;
	protected Color tweenTargetColor;
	protected Color tweenBaseColor;
	protected float tweenTargetX, tweenTargetY;
	protected float tweenBaseX, tweenBaseY;

	/**
	 * Creates a new screen object at x, y.
	 * @param	x				The x-coord to start at (in virtual px)
	 * @param	y				The y-coord to start at (in virtual px)
	 */
	public ScreenGraphic(float x, float y) {
		this.x = x;
		this.y = y;
		this.tweening = false;
		this.currentColor = new Color(1, 1, 1, 1);
		this.tweenBaseColor = new Color(1, 1, 1, 1);
		this.tweenTargetColor = new Color(1, 1, 1, 1);
	}
	
	/**
	 * Creates a new screen object at 0, 0.
	 */
	public ScreenGraphic() {
		this(0, 0);
	}

	/** @see net.wombatrpgs.mgne.maps.Positionable#getX() */
	@Override public float getX() { return this.x; }
	
	/** @see net.wombatrpgs.mgne.maps.Positionable#getY() */
	@Override public float getY() {	return this.y; }
	
	/** @see net.wombatrpgs.mgne.maps.PositionSetable#setX(int) */
	@Override public void setX(float x) { this.x = x; }

	/** @see net.wombatrpgs.mgne.maps.PositionSetable#setY(int) */
	@Override public void setY(float y) { this.y = y; }
	
	/** @return True if we're currently moving to another place/color */
	public boolean isTweening() { return tweening; }
	
	/** @return The width of this object, in virtual px */
	public abstract int getWidth();
	
	/** @return The height of this object, in virtual px */
	public abstract int getHeight();
	
	/**
	 * We do this final so that subclasses don't override the color code.
	 * @see net.wombatrpgs.mgne.screen.ScreenObject#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public final void render(SpriteBatch batch) {
		Color old = batch.getColor();
		batch.setColor(currentColor);
		super.render(batch);
		coreRender(batch);
		batch.setColor(old);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (fadingOut && !isTweening()) {
			parent.removeChild(this);
		}
		if (tweening) {
			tweenTime += elapsed;
			float r;
			if (tweenTime > tweenEnd) {
				r = 1;
				tweening = false;
			} else {
				r = tweenTime / tweenEnd;
			}
			
			// color
			currentColor.a = tweenBaseColor.a * (1.f-r) + r * tweenTargetColor.a;
			currentColor.r = tweenBaseColor.r * (1.f-r) + r * tweenTargetColor.r;
			currentColor.g = tweenBaseColor.g * (1.f-r) + r * tweenTargetColor.g;
			currentColor.b = tweenBaseColor.b * (1.f-r) + r * tweenTargetColor.b;
			
			// location
			x = tweenBaseX * (1.f-r) + r * tweenTargetX;
			y = tweenBaseX * (1.f-r) + r * tweenTargetX;
		}
	}
	
	/**
	 * The equivalent to the graphics interface renderable.
	 * @param	batch			The batch to render with
	 */
	public abstract void coreRender(SpriteBatch batch);
	
	/**
	 * Gets the default screen batch. This is usually what's used to render,
	 * although if some special thing is going on, it'd be best to use a
	 * different batch.
	 * @return					The current screen's sprite batch
	 */
	public SpriteBatch getBatch() {
		return MGlobal.screens.peek().getUIBatch();
	}
	
	/**
	 * Tweens to a new color. Undefined behavior if already tweening. This is a
	 * smooth transition from one color to another.
	 * @param 	target			The color to end up as
	 * @param 	time			How long to take
	 */
	public void tweenTo(Color target, float time) {
		tweenTargetColor.set(target);
		tweenBaseColor.set(currentColor);
		this.tweenEnd = time;
		this.tweenTime = 0;
		this.tweening = true;
	}
	
	/**
	 * Tweens to a new location. Undefined behavior if already tweening.
	 * @param	targetX			The location to move to (in screen px)
	 * @param	targetY			The location to move to (in screen px)
	 * @param	time			How long to take (in s)
	 */
	public void tweenTo(float targetX, float targetY, float time) {
		tweenTargetX = targetX;
		tweenTargetY = targetY;
		tweenEnd = time;
		tweenBaseX = x;
		tweenBaseY = y;
		tweening = true;
		tweenTime = 0;
	}
	
	/**
	 * Causes the text box to fade in to the current screen. Clears any text on
	 * the box.
	 * @param	screen			The screen to fade in on
	 * @param	fadeTime		How long the transition should take
	 */
	public void fadeIn(Screen screen, float fadeTime) {
		this.parent = screen;
		setColor(new Color(1, 1, 1, 0));
		if (!screen.containsChild(this)) {
			screen.addChild(this);
		}
		tweenTo(new Color(1, 1, 1, 1), fadeTime);
	}
	
	/**
	 * Gracefully exits from the screen.
	 * @param	fadeTime		How long the transition should take
	 */
	public void fadeOut(float fadeTime) {
		tweenTo(new Color(1, 1, 1, 0), fadeTime);
		fadingOut = true;
	}
	
	/**
	 * Sets the picture's rendering color.
	 * @param 	newColor
	 */
	public void setColor(Color newColor) {
		tweening = false;
		currentColor.set(newColor);
	}

}
