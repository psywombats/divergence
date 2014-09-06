/**
 *  ScreenName.java
 *  Created on Jun 10, 2014 1:50:24 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextFormat;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.rpg.chara.Chara;

/**
 * Screen for the player to enter hero names.
 */
// TODO: polish: get these in columns
public class ScreenName extends SagaScreen {
	
	protected static final int NAME_WIDTH = 240;
	protected static final int NAME_HEIGHT = 36;
	protected static final int LETTERS_WIDTH = 240;
	protected static final int LETTERS_PADDING_HORIZ = 8;
	protected static final int LETTERS_PADDING_VERT = 3;
	
	protected Chara chara;
	protected FinishListener listener;
	protected List<Character> chars;
	protected FacesAnimation sprite;
	protected String name, underName;
	protected boolean finished;
	protected int spot;
	protected int selectX, selectY;
	
	protected Nineslice nameBG, lettersBG;
	protected TextFormat nameFormat;
	protected int nameX, nameY;
	protected int lettersX, lettersY;
	protected int spriteX, spriteY;
	protected int letterRows, letterCols;
	protected int lettersHeight;
	
	/**
	 * Creates a new naming screen.
	 * @param	chara			The character to be named
	 */
	public ScreenName(Chara chara) {
		this.chara = chara;
		
		chars = new ArrayList<Character>();
		chars.add(SConstants.NBSP.charAt(0));
		for (char c = 'A'; c <= 'Z'; c += 1) {
			chars.add(c);
		}
		for (char c = 'a'; c <= 'z'; c += 1) {
			chars.add(c);
		}
		for (char c = '0'; c <= '9'; c += 1) {
			chars.add(c);
		}
		chars.add('-');
		
		selectX = 0;
		selectY = 0;
		
		// should there be a default name?
		// name = chara.getName();
		name = "";
		spot = 0;
		sprite = chara.createSprite();
		sprite.startMoving();
		assets.add(sprite);
		addUChild(sprite);
		
		FontHolder font = MGlobal.ui.getFont();
		letterCols = 0;
		int width = LETTERS_PADDING_HORIZ;
		while (width < LETTERS_WIDTH) {
			width += (font.getCharWidth() + LETTERS_PADDING_HORIZ);
			letterCols += 1;
		}
		letterCols -= 1;
		letterRows = (int) Math.ceil((float) chars.size() / (float) letterCols);
		lettersHeight = (int) (LETTERS_PADDING_VERT + letterRows *
				(font.getLineHeight() * LETTERS_PADDING_VERT));
		
		
		nameBG = new Nineslice(NAME_WIDTH, NAME_HEIGHT);
		assets.add(nameBG);
		
		lettersBG = new Nineslice(LETTERS_WIDTH, lettersHeight);
		assets.add(lettersBG);
		
		WindowSettings window = MGlobal.window;
		
		lettersX = (window.getViewportWidth() - lettersBG.getWidth()) / 2;
		lettersY = (window.getViewportHeight() - (lettersBG.getHeight() + nameBG.getHeight() -
				nameBG.getBorderWidth())) / 2;
		nameX = (window.getViewportWidth() - nameBG.getWidth()) / 2;
		nameY = lettersY + lettersBG.getHeight() - nameBG.getBorderHeight();
		
		spriteX = nameX + sprite.getWidth();
		spriteY = nameY + (nameBG.getHeight() - sprite.getHeight()) / 2;
		
		nameFormat = new TextFormat();
		nameFormat.x = spriteX + sprite.getWidth() / 2 + sprite.getWidth();
		nameFormat.y = (int) (spriteY + (sprite.getHeight() - font.getLineHeight()) / 2 +
				font.getLineHeight());
		nameFormat.align = HAlignment.LEFT;
		nameFormat.width = nameBG.getWidth();
		nameFormat.height = nameBG.getHeight();
		
		finished = false;
		reconstructUnderName();
	}
	
	/**
	 * Creates a new naming screen that calls a listener when done.
	 * @param	chara			The chara to name
	 * @param	listener		The listener to call when done
	 */
	public ScreenName(Chara chara, FinishListener listener) {
		this(chara);
		this.listener = listener;
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		lettersBG.renderAt(batch, lettersX, lettersY);
		nameBG.renderAt(batch, nameX, nameY);
		
		FontHolder font = MGlobal.ui.getFont();
		int lx = lettersX + LETTERS_PADDING_HORIZ;
		int ly = (int) (lettersY + LETTERS_PADDING_VERT + lettersBG.getHeight() -
				(font.getLineHeight() + LETTERS_PADDING_VERT));
		int col = 0;
		batch.begin();
		for (Character c : chars) {
			font.draw(batch, c, lx, ly);
			lx += font.getCharWidth() + LETTERS_PADDING_HORIZ;
			col += 1;
			if (col >= letterCols) {
				col = 0;
				lx = lettersX + LETTERS_PADDING_HORIZ;
				ly -= (font.getLineHeight() + LETTERS_PADDING_VERT);
			}
		}
		batch.end();
		
		font.draw(batch, nameFormat, name, 0);
		font.draw(batch, nameFormat, underName, -3);
		
		sprite.renderAt(batch, spriteX, spriteY);
		
		Graphic cursor = MGlobal.ui.getCursor();
		int cursorX = (int) (lettersX + selectX * (font.getCharWidth() + LETTERS_PADDING_HORIZ)) -
				cursor.getWidth() / 2;
		int cursorY = (int) (lettersY + lettersBG.getHeight() -
				(selectY * (font.getLineHeight() + LETTERS_PADDING_VERT)) - 
				(cursor.getHeight() + font.getLineHeight() + LETTERS_PADDING_VERT - 1));
		cursor.renderAt(batch, cursorX, cursorY);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		pushCommandContext(new CMapMenu() {
			@Override protected InputCommand parseCharacter(char character) {
				//addLetter(character);
				return super.parseCharacter(character);
			}
		});
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		switch (command) {
		case MOVE_DOWN:		selectY += 1;	break;
		case MOVE_LEFT:		selectX -= 1;	break;
		case MOVE_RIGHT:	selectX += 1;	break;
		case MOVE_UP:		selectY -= 1;	break;
		case UI_CONFIRM:	addLetter();	break;
		case UI_CANCEL:		removeLetter();	break;
		case UI_FINISH:		finish();		break;
		default:			super.onCommand(command);
		}
		if (selectX < 0) selectX = letterCols-1;
		if (selectX >= letterCols) selectX = 0;
		if (selectY < 0) selectY = letterRows-1;
		if (selectY >= letterRows) selectY = 0;
		if (selectX + selectY * letterCols >= chars.size()) {
			selectY = letterRows-1;
			selectX = 0;
		}
		return super.onCommand(command);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusLost()
	 */
	@Override
	public void onFocusLost() {
		super.onFocusLost();
		popCommandContext();
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		nameBG.dispose();
		lettersBG.dispose();
		sprite.dispose();
	}
	
	/**
	 * Checks if this screen has served its purpose and is done naming.
	 * @return					True if the chara is named
	 */
	public boolean isDone() {
		return finished;
	}
	
	/**
	 * Creates the underscore string beneath the name.
	 */
	protected void reconstructUnderName() {
		underName = "";
		for (int i = 0; i < spot; i += 1) {
			underName += SConstants.NBSP.charAt(0);
		}
		underName += "_";
	}
	
	/**
	 * Appends the selected letter to the name string.
	 */
	protected void addLetter() {
		int index = selectY * letterCols + selectX;
		addLetter(chars.get(index));
	}
	
	/**
	 * Adds a character to the name string.
	 * @param	character		The character to append
	 */
	protected void addLetter(char character) {
		String oldName = name;
		name = name.substring(0, spot) + String.valueOf(character);
		if (spot < oldName.length()) {
			name += oldName.substring(spot+1);
		}
		if (spot < 9) {
			spot += 1;
		}
		reconstructUnderName();
	}
	
	/**
	 * Deletes the currently selected character.
	 */
	protected void removeLetter() {
		if (name.length() > 0) {
			if (spot == name.length()) {
				name = name.substring(0, name.length() - 1);
				spot -= 1;
				reconstructUnderName();
			} else {
				name = name.substring(0, spot) + name.substring(spot+1);
			}
		}
	}
	
	/**
	 * Assigns the name to the character and closes the screen.
	 */
	protected void finish() {
		chara.setName(name);
		if (listener != null) {
			listener.onFinish();
		}
		finished = true;
	}

}
