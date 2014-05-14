/**
 *  TransformationOptimizer.java
 *  Created on May 2, 2014 11:53:00 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import de.javakaffee.kryoserializers.KryoReflectionFactorySupport;
import net.wombatrpgs.saga.misc.mfam.instances.IdealStats;
import net.wombatrpgs.saga.misc.mfam.instances.OrigConfig;

/**
 * Attempts to optimize monster transformations based on specifications.
 */
public class MFamOptimizer {

	/**
	 * Runs the optimizer for SaGa monster families.
	 * @param args <trials> <generations> <reports> [seed]
	 */
	public static void main(String[] args) {

		if (args.length < 3) {
			System.out.println("Usage: MFamOptimizer <trials> <generations> " +
					"<reports> [seed]");
			storeFFL();
			return;
		}
		
		int trials = Integer.valueOf(args[0]);
		int generations = Integer.valueOf(args[1]);
		int toSave = Integer.valueOf(args[2]);
		System.out.println("Running " + trials + " trials, saving top " + 
				toSave + " results");
		List<Config> best = new ArrayList<Config>();
		FitnessStats ideal = new IdealStats();
		KryoReflectionFactorySupport kryo = new KryoReflectionFactorySupport();
		
		Config seed = null;
		if (args.length >= 4) {
			File seedFile = new File(args[3]);
			if (!seedFile.exists() || seedFile.isDirectory()) {
				System.out.println("Bad seed file " + args[2]);
			}
			try {
				Input input = new Input(new FileInputStream(seedFile));
				seed = kryo.readObject(input, Config.class);
				input.close();
			} catch (FileNotFoundException e) {
				System.out.println("Bad seed file " + args[3]);
				return;
			}
			System.out.println("Using seed " + args[3]);
			seed.genInfo = "fromSeed";
			seed.target = ideal;
		}
		
		for (int i = 0; i < trials; i += 1) {
			System.out.println("Running trial " + i + "...");
			Optimizer optimizer;
			if (seed == null) {
				optimizer = new GeneticOptimizer(ideal);
			} else {
				optimizer = new TweakOptimizer(ideal, seed);
			}
			Config winner = optimizer.run(generations);
			best.add(winner);
			if (best.size() > toSave) {
				Collections.sort(best);
				best.remove(best.size() - 1);
			}
		}
		
		System.out.println("Trials finished, printing reports.");
		
		int labelNo = 1;
		for (Config top : best) {
			String label = Integer.toString(labelNo);
			while (label.length() < 3) label = "0" + label;
			label = "result" + label;
			try {
				// cryo file
				Output output = new Output(new FileOutputStream(new File(label + ".cryo")));
				kryo.writeObject(output, top);
				output.close();
				// report file
				File info = new File(label + ".txt");
				info.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(info));
				writer.write("Report for config " + label + ", error " + top.error);
				writer.write(top.stats.report());
				writer.write("\n");
				writer.write(top.stats.ranks());
				writer.write("\n");
				writer.write(top.graph());
				writer.close();
				labelNo += 1;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("All done!");

	}
	
	/**
	 * Writes the original stats to a file.
	 */
	protected static void storeFFL() {
		KryoReflectionFactorySupport kryo = new KryoReflectionFactorySupport();
		try {
			// cryo file
			Output output = new Output(
					new FileOutputStream(new File("ffl.cryo")));
			Config origConfig = new OrigConfig();
			FitnessStats origStats = new FitnessStats(origConfig);
			origConfig.stats = origStats;
			kryo.writeObject(output, origConfig);
			output.close();
			// report file
			File info = new File("ffl.txt");
			info.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(info));
			writer.write("Report for config FFL:");
			writer.write(origStats.report());
			writer.write("\n");
			writer.write(origStats.ranks());
			writer.write("\n");
			writer.write(origConfig.graph());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
