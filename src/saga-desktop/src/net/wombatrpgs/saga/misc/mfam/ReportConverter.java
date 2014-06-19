/**
 *  ConfigConverter.java
 *  Created on Jun 17, 2014 6:47:01 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import net.wombatrpgs.saga.misc.mfam.io.PerfectPrinter;
import net.wombatrpgs.sagaschema.rpg.chara.CharaMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.Gender;
import net.wombatrpgs.sagaschema.rpg.chara.data.Race;
import net.wombatrpgs.sagaschema.rpg.stats.StatSetMDO;

/**
 * Converts a stored text report into MDOs.
 */
public class ReportConverter {
	
	protected static final int[] GP_DROPS = {40, 40, 80, 120, 240, 400, 600,
		600, 900, 1200, 1600, 2000, 2400, 2400, 0, 0};
	
	/**
	 * Converts a stored report into MDO stencils. Will need to be filled in by
	 * hand by that's okay.
	 * @param	args				<game dir> <config file>
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("args: <gamedir> <configfile>");
			return;
		}
		
		File report = new File(args[1]);
		
		Scanner sc = null;
		try {
			sc = new Scanner(report);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		while (sc.hasNext()) {
			String line = sc.nextLine();
			if (line.charAt(0) == '-') {
				continue;
			}
			Scanner sub = new Scanner(line);
			sub.useDelimiter("\\|");
			String familyTag = sub.next();
			familyTag = familyTag.trim();
			List<String> monsterNames = new ArrayList<String>();
			for (int i = 0; i < 6; i += 1) {
				monsterNames.add(sub.next().trim());
			}
			String meatString = sub.next().trim();
			String targetString = sub.next().trim();
			sub.close();
			
			for (int i = 0; i < 6; i += 1) {
				int meat = Integer.valueOf(String.valueOf(meatString.charAt(i)), 16);
				int target = Integer.valueOf(String.valueOf(targetString.charAt(i)), 16);
				String name = monsterNames.get(i).toUpperCase();
				String levelString = String.valueOf(target);
				if (levelString.length() < 2) levelString = "0" + levelString;
				
				CharaMDO mdo = new CharaMDO();
				mdo.appearance = "4dir_ffl2_human";
				mdo.description = "Converted from a report";
				mdo.equipped = new String[0];
				mdo.family = "mfamily_" + familyTag;
				mdo.gender = Gender.NONE;
				mdo.gp = GP_DROPS[target];
				mdo.key = "enemy_" + familyTag + levelString + "_" + name;
				mdo.meatEatLevel = meat;
				mdo.meatTargetLevel = target;
				mdo.name = name;
				mdo.portrait = "battle_portraits/goblin.png";
				mdo.race = Race.MONSTER;
				mdo.species = name;
				mdo.stats = new StatSetMDO();
				mdo.subfolder = "level" + levelString;
				
				String dirname = args[0] + "/res/data/net/wombatrpgs/" +
						"sagaschema/rpg/chara/charaMDO/";
				File outdir = new File(dirname);
				outdir.mkdir();
				File outfile = new File(dirname + "/" + mdo.key + ".json");
				try {
					outfile.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
					return;
				}
				ObjectMapper mapper = new ObjectMapper();
				ObjectWriter writer = mapper.writer(new PerfectPrinter());
				try {
					writer.writeValue(outfile, mdo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Wrote " + mdo.key);
			}
		}
		sc.close();
		System.out.println("Complete.");
	}

}
