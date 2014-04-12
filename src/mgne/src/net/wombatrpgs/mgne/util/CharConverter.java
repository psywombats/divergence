/**
 *  CharacterConverter.java
 *  Created on Apr 12, 2014 3:56:00 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.util;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgneschema.settings.CharacterCodesMDO;
import net.wombatrpgs.mgneschema.settings.data.SpecialCharacterMDO;

/**
 * Converts a string with special character to a string with the ASCII codes of
 * the specials converted.
 */
public class CharConverter {
	
	public static final String KEY_CONVERTER_DEFAULT = "codes_default";
	
	protected CharacterCodesMDO mdo;
	
	/**
	 * Creates a new converter from a conversion list
	 * @param	mdo				The data with the conversion list
	 */
	public CharConverter(CharacterCodesMDO mdo) {
		this.mdo = mdo;
	}
	
	/**
	 * Creates a new converter from default MDO.
	 */
	public CharConverter() {
		this(MGlobal.data.getEntryFor(KEY_CONVERTER_DEFAULT, CharacterCodesMDO.class));
	}
	
	/**
	 * Replaces all the special character in the input string. Not overly
	 * efficient.
	 * @param	input			The input string to convert
	 * @return					The converted version of the string
	 */
	public String convert(String input) {
		String result = input;
		for (SpecialCharacterMDO charMDO : mdo.codes) {
			result = result.replace(
					charMDO.code,
					Character.toString((char) charMDO.ascii.intValue()));
		}
		return result;
	}

}
