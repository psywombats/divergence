/**
 *  CharacterSet.java
 *  Created on Oct 22, 2013 3:50:55 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogueschema.cutscene.CharacterSetMDO;
import net.wombatrpgs.mrogueschema.cutscene.data.ArchetypeEntryMDO;
import net.wombatrpgs.mrogueschema.cutscene.data.ArchetypeMDO;

/**
 * Manages a subset of a bunch of characters.
 */
public class CharacterSet {
	
	protected static final String FULL_PRE = "-FULL";
	
	protected CharacterSetMDO mdo;
	// from HERO to (Nathaniel, Nathaniel Everard the hero etc
	protected Map<String, ArchetypeEntryMDO> charas;
	
	/**
	 * Creates a new character set and randomly selected characters based on
	 * supplied data.
	 * @param	mdo				The data to choose from
	 */
	public CharacterSet(CharacterSetMDO mdo) {
		this.mdo = mdo;
		charas = new HashMap<String, ArchetypeEntryMDO>();
		for (ArchetypeMDO type : mdo.typeName) {
			charas.put(type.charname,
					type.entries[MGlobal.rand.nextInt(type.entries.length)]);
		}
	}
	
	/** @return The name for the given type */
	public String toName(String type) { return charas.get(type).charaname; }
	
	/** @return The full name for the given type */
	public String toFull(String type) { return charas.get(type).fullname; }
	
	/** @return The names of all the character archetypes */
	public Set<String> getNames() { return charas.keySet(); }

	/**
	 * Converts all names in the text to their equivalent names in the set.
	 * @param	allText			All the text to translate
	 * @return					The translated text
	 */
	public String substitute(String text) {
		for (String name : getNames()) {
			text = text.replace(name + FULL_PRE, charas.get(name).fullname);
			text = text.replace(name + ":", charas.get(name).charaname.toUpperCase() + ":");
			text = text.replace(name, charas.get(name).charaname);
		}
		return text;
	}

}
