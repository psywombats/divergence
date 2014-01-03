/**
 *  Narrator.java
 *  Created on Oct 10, 2013 3:44:25 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Turnable;
import net.wombatrpgs.mrogue.ui.text.FontHolder;
import net.wombatrpgs.mrogue.ui.text.TextBoxFormat;
import net.wombatrpgs.mrogueschema.ui.NarratorMDO;

/**
 * A UI element that happily spits out game info as it happens.
 */
public class Narrator extends UIElement implements Turnable {
	
	protected NarratorMDO mdo;
	protected FontHolder font;
	protected TextBoxFormat format;
	protected boolean stale;
	protected float x, y;
	
	protected List<Line> lines;
	
	/**
	 * Creates a new narrator from data. Does not deal with loading its font.
	 * @param	mdo				The data to create the narrator from
	 */
	public Narrator(NarratorMDO mdo, FontHolder font) {
		this.mdo = mdo;
		this.font = font;
		stale = false;
		lines = new ArrayList<Line>();
		format = new TextBoxFormat();
		format.x = mdo.offsetX;
		format.y = MGlobal.window.getHeight() - mdo.offsetY;
		format.width = mdo.width;
		format.height = MGlobal.window.getHeight();
		format.align = HAlignment.LEFT;
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Turnable#onTurn()
	 */
	@Override
	public void onTurn() {
		stale = true;
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			if (line.ttl < mdo.fadeout) {
				font.setAlpha(line.ttl/mdo.fadeout);
			} else {
				font.setAlpha(1.f);
			}
			font.draw(getBatch(), format, line.mutant, (int) (font.getLineHeight() * -i));
		}
		font.setAlpha(1);
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (MGlobal.stasis) return;
		for (Line line : lines) {
			line.ttl -= elapsed;
			line.mutant = mutate(line.line);
		}
		while (lines.size() > 0) {
			Line line = lines.get(0);
			if (line.ttl <= 0) {
				lines.remove(0);
			} else {
				break;
			}
		}
	}
	
	/**
	 * Reports something to the player.
	 * @param	msg				What to say to the player.
	 */
	public void msg(String msg) {
		msg = Character.toUpperCase(msg.charAt(0)) + msg.substring(1, msg.length());
		if (stale) {
			lines.clear();
			stale = false;
		}
		String thisLine;
		int split;
		if (msg.length() > mdo.chars) {
			for (split = mdo.chars-1; msg.charAt(split) != ' '; split -= 1);
			thisLine = msg.substring(0, split);
		} else {
			thisLine = msg;
			split = 0;
		}
		Line line = new Line(thisLine, mdo.ttl + mdo.fadeout);
		lines.add(Math.min(lines.size(), mdo.lines), line);
		if (lines.size() > mdo.lines) {
			lines.remove(0);
		}
		if (msg.length() != thisLine.length()) {
			msg(msg.substring(split+1));
		}
		
	}
	
	/**
	 * Creates a mutant string based on an input string. Character substitution
	 * for cthulu mode.
	 * @param	line			The line to mutate
	 * @return					The mutated version of the input
	 */
	protected String mutate(String line) {
		if (!line.contains("*")) return line;
		char out[] = new char[line.length()];
		for (int i = 0; i < line.length(); i += 1) {
			out[i] = line.charAt(i);
			if (out[i] == '*') {
				out[i] = (char)(MGlobal.rand.nextInt(26) + 'a');
			}
		}
		return new String(out);
	}
	
	class Line {
		public String line;
		public String mutant;
		public float ttl;
		public Line(String line, float ttl) {
			this.line = line;
			this.ttl = ttl;
			this.mutant = line;
		}
	}
	
}
