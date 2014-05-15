/**
 *  SagaSettings.java
 *  Created on May 2, 2014 7:22:16 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.sagaschema.settings.MonsterSettingsMDO;
import net.wombatrpgs.sagaschema.settings.SagaSettingsMDO;

/**
 * Conglomoration of all saga-specific settings.
 */
public class SagaSettings {
	
	public static final String KEY_DEFAULT = "sagasettings_default";
	
	protected SagaSettingsMDO mdo;
	protected MonsterSettingsMDO monsterSettings;
	
	/**
	 * Constructs a new settings object from data.
	 * @param	mdo				The data to create from
	 */
	public SagaSettings(SagaSettingsMDO mdo) {
		this.mdo = mdo;
		
		monsterSettings = MGlobal.data.getEntryFor(mdo.monsterSettings,
				MonsterSettingsMDO.class);
	}
	
	/**
	 * Constructs a new settings object from the default data.
	 */
	public SagaSettings() {
		this(MGlobal.data.getEntryFor(KEY_DEFAULT, SagaSettingsMDO.class));
	}
	
	/** @return The likelihood of enemy party droppning meat, from 0 to 100 */
	public int getMeatChance() { return monsterSettings.meatChance; }
	
	/** @return The key of the starting player party */
	public String getStartingPartyKey() { return mdo.heroParty; }
	
	/** @return The key of the default graphcis settings */
	public String getGraphicsKey() { return mdo.graphcisSettings; }

}
