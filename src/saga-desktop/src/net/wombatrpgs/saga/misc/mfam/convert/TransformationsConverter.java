/**
 *  TransformationsConverter.java
 *  Created on Jun 18, 2014 6:49:51 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam.convert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import net.wombatrpgs.mgne.io.json.PerfectPrinter;
import net.wombatrpgs.sagaschema.rpg.chara.MeatGroupMDO;
import net.wombatrpgs.sagaschema.rpg.chara.MonsterFamilyMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.TransformationMDO;

/**
 * Converts a transformation report into monster family mdos.
 */
public class TransformationsConverter {

	/**
	 * Converts a transformation report into monster family mdos.
	 * @param	args				<gamedir> <reportfile>
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("args: <gamedir> <configfile>");
			return;
		}
		
		File report = new File(args[1]);
		String dirname = args[0] + "/res/data/net/wombatrpgs/" +
				"sagaschema/rpg/chara/";
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new PerfectPrinter());
		
		Scanner sc = null;
		try {
			sc = new Scanner(report);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		sc.useDelimiter("\\n");
		
		List<List<String>> tags = new ArrayList<List<String>>();
		while (sc.hasNext()) {
			String line = sc.next();
			if (line.contains("+")) {
				break;
			}
			Scanner sub = new Scanner(line);
			sub.useDelimiter("\\|");
			int index = 0;
			while (sub.hasNext()) {
				String tag = sub.next().trim();
				if (tags.size() <= index) {
					tags.add(new ArrayList<String>());
				}
				if (tag.length() > 0) {
					tags.get(index).add("mfamily_" + tag);
				}
				index += 1;
			}
			sub.close();
		}
		
		List<MeatGroupMDO> groups = new ArrayList<MeatGroupMDO>();
		for (int i = 1; i < tags.size(); i += 1) {
			List<String> groupTags = tags.get(i);
			MeatGroupMDO mdo = new MeatGroupMDO();
			mdo.description = "Generated from transforms chart";
			mdo.families = new String[groupTags.size()];
			mdo.families = groupTags.toArray(mdo.families);
			mdo.key = "mgroup_" + i;
			mdo.name = "Meat group " + i;
			mdo.subfolder = "";
			groups.add(mdo);
		}
		
		List<MonsterFamilyMDO> families = new ArrayList<MonsterFamilyMDO>();
		while (sc.hasNext()) {
			String line = sc.nextLine();
			if (line.contains("+") || line.length() == 0) {
				continue;
			}
			Scanner sub = new Scanner(line);
			sub.useDelimiter("\\|");
			String tag = sub.next().trim();
			
			List<TransformationMDO> transforms= new ArrayList<TransformationMDO>();
			int groupIndex = 1;
			while (sub.hasNext()) {
				String resultTag = sub.next().trim();
				if (resultTag.length() > 0) {
					TransformationMDO transform = new TransformationMDO();
					transform.eat = "mgroup_" + groupIndex;
					transform.result = "mfamily_" + resultTag;
					transforms.add(transform);
				}
				groupIndex += 1;
			}
			sub.close();
			
			MonsterFamilyMDO mdo = new MonsterFamilyMDO();
			mdo.description = "Generated from transforms chart";
			mdo.key = "mfamily_" + tag;
			mdo.name = tag + " family";
			mdo.subfolder = "";
			mdo.transformations = new TransformationMDO[transforms.size()];
			mdo.transformations = transforms.toArray(mdo.transformations);
			families.add(mdo);
		}
		sc.close();
		
		for (MeatGroupMDO mdo : groups) {
			System.out.println("Writing " + mdo.key);
			File outfile = new File(dirname + "/MeatGroupMDO/" + mdo.key + ".json");
			try {
				outfile.createNewFile();
				writer.writeValue(outfile, mdo);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		
		for (MonsterFamilyMDO mdo : families) {
			System.out.println("Writing " + mdo.key);
			File outfile = new File(dirname + "/MonsterFamilyMDO/" + mdo.key + ".json");
			try {
				outfile.createNewFile();
				writer.writeValue(outfile, mdo);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		System.out.println("Complete.");
	}

}
