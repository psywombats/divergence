/**
 *  OrigConfig.java
 *  Created on May 3, 2014 1:51:13 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam.instances;

import java.util.Arrays;

import net.wombatrpgs.saga.misc.mfam.Config;
import net.wombatrpgs.saga.misc.mfam.data.Family;
import net.wombatrpgs.saga.misc.mfam.data.Group;
import net.wombatrpgs.saga.misc.mfam.data.Member;

/**
 * Optimizer configuration from FFL.
 */
public class OrigConfig extends Config {
	
	/**
	 * Does all the ugly business setting up things copied from towerreversed.
	 */
	public OrigConfig() {
		
		Family inm = new Family("inm");
		inm.members[0] = new Member(	0, 0,	"fly");
		inm.members[1] = new Member(	2, 1,	"drgonfly");
		inm.members[2] = new Member(	4, 3,	"hornet");
		inm.members[3] = new Member(	5, 5,	"mosquito");
		inm.members[4] = new Member(	12, 11,	"cicada");
		
		Family fsh = new Family("fsh");
		fsh.members[0] = new Member(	1, 0,	"barracud");
		fsh.members[1] = new Member(	2, 1,	"pirahna");
		fsh.members[2] = new Member(	5, 4,	"shark");
		fsh.members[3] = new Member(	8, 7,	"gunfish");
		fsh.members[4] = new Member(	12, 11,	"elec eel");
		
		Family plt = new Family("plt");
		plt.members[0] = new Member(	3, 2,	"cactus");
		plt.members[1] = new Member(	4, 3,	"p-flower");
		plt.members[2] = new Member(	6, 5,	"garlic");
		plt.members[3] = new Member(	8, 7,	"thorn");
		plt.members[4] = new Member(	12, 11,	"f-flower");
		
		Family ins = new Family("ins");
		ins.members[0] = new Member(	0, 0,	"clipper");
		ins.members[1] = new Member(	6, 5,	"beetle");
		ins.members[2] = new Member(	7, 7,	"ant lion");
		ins.members[3] = new Member(	8, 8,	"atom ant");
		ins.members[4] = new Member(	12, 11,	"scorpion");
		
		Family crb = new Family("crb");
		crb.members[0] = new Member(	3, 2,	"shrimp");
		crb.members[1] = new Member(	4, 3,	"atomcrab");
		crb.members[2] = new Member(	7, 6,	"crab");
		crb.members[3] = new Member(	8, 8,	"ice crab");
		crb.members[4] = new Member(	12, 11,	"kingcrab");
		
		Family tgr = new Family("tgr");
		tgr.members[0] = new Member(	1, 0,	"wolf");
		tgr.members[1] = new Member(	3, 2,	"jaguar");
		tgr.members[2] = new Member(	6, 5,	"sabercat");
		tgr.members[3] = new Member(	9, 8,	"snowcat");
		tgr.members[4] = new Member(	12, 11,	"blackcat");
		
		Family rhi = new Family("rhi");
		rhi.members[0] = new Member(	0, 0,	"redbull");
		rhi.members[1] = new Member(	3, 2,	"rhino");
		rhi.members[2] = new Member(	7, 6,	"triceras");
		rhi.members[3] = new Member(	9, 8,	"behemoth");
		rhi.members[4] = new Member(	12, 11,	"baku");
		
		Family bdm = new Family("bdm");
		bdm.members[0] = new Member(	2, 1,	"condor");
		bdm.members[1] = new Member(	4, 3,	"raven");
		bdm.members[2] = new Member(	6, 5,	"harpy");
		bdm.members[3] = new Member(	9, 8,	"ten-gu");
		bdm.members[4] = new Member(	12, 11,	"garuda");
		
		Family snk = new Family("snk");
		snk.members[0] = new Member(	1, 0,	"snake");
		snk.members[1] = new Member(	4, 3,	"serpent");
		snk.members[2] = new Member(	6, 5,	"anaconda");
		snk.members[3] = new Member(	9, 8,	"hydra");
		snk.members[4] = new Member(	12, 11,	"ko-run");
		
		Family oct = new Family("oct");
		oct.members[0] = new Member(	3, 2,	"octopus");
		oct.members[1] = new Member(	5, 4,	"clam");
		oct.members[2] = new Member(	7, 6,	"amoeba");
		oct.members[3] = new Member(	9, 8,	"ammonite");
		oct.members[4] = new Member(	12, 11,	"squid");
		
		Family wrm = new Family("wrm");
		wrm.members[0] = new Member(	1, 0,	"worm");
		wrm.members[1] = new Member(	6, 5,	"p-worm");
		wrm.members[2] = new Member(	7, 7,	"crawler");
		wrm.members[3] = new Member(	9, 8,	"lavaworm");
		wrm.members[4] = new Member(	12, 11,	"sandworm");
		
		Family liz = new Family("liz");
		liz.members[0] = new Member(	0, 0,	"lizard");
		liz.members[1] = new Member(	1, 1,	"p-frog");
		liz.members[2] = new Member(	2, 2,	"gecko");
		liz.members[3] = new Member(	10, 9,	"dinosaur");
		liz.members[4] = new Member(	12, 11,	"salamand");
		
		Family wfm = new Family("wfm");
		wfm.members[0] = new Member(	0, 0,	"wererat");
		wfm.members[1] = new Member(	3, 2,	"werewolf");
		wfm.members[2] = new Member(	5, 4,	"catwoman");
		wfm.members[3] = new Member(	10, 9,	"minotaur");
		wfm.members[4] = new Member(	12, 11,	"rakshasa");
		
		Family oni = new Family("oni");
		oni.members[0] = new Member(	0, 0,	"goblin");
		oni.members[1] = new Member(	1, 1,	"oni");
		oni.members[2] = new Member(	7, 6,	"ogre");
		oni.members[3] = new Member(	10, 9,	"giant");
		oni.members[4] = new Member(	13, 12,	"titan");
		
		Family chi = new Family("chi");
		chi.members[0] = new Member(	3, 2,	"griffin");
		chi.members[1] = new Member(	5, 4,	"mantcore");
		chi.members[2] = new Member(	8, 7,	"chimera");
		chi.members[3] = new Member(	10, 9,	"nue");
		chi.members[4] = new Member(	13, 12,	"sphinx");
		
		Family eye = new Family("eye");
		eye.members[0] = new Member(	3, 2,	"big eye");
		eye.members[1] = new Member(	6, 5,	"gazer");
		eye.members[2] = new Member(	8, 7,	"seeker");
		eye.members[3] = new Member(	11, 10,	"watcher");
		eye.members[4] = new Member(	13, 12,	"evil eye");
		
		Family skl = new Family("skl");
		skl.members[0] = new Member(	0, 0,	"skeleton");
		skl.members[1] = new Member(	3, 2,	"red bone");
		skl.members[2] = new Member(	9, 8,	"dokuro");
		skl.members[3] = new Member(	11, 10,	"warrior");
		skl.members[4] = new Member(	13, 12,	"boneking");
		
		Family slm = new Family("slm");
		slm.members[0] = new Member(	1, 0,	"slime");
		slm.members[1] = new Member(	7, 6,	"jelly");
		slm.members[2] = new Member(	9, 8,	"tororo");
		slm.members[3] = new Member(	11, 10,	"gummy");
		slm.members[4] = new Member(	12, 11,	"pudding");
		
		Family dvl = new Family("dvl");
		dvl.members[0] = new Member(	2, 1,	"gargoyle");
		dvl.members[1] = new Member(	6, 5,	"imp");
		dvl.members[2] = new Member(	10, 9,	"demon");
		dvl.members[3] = new Member(	11, 11,	"demolord");
		dvl.members[4] = new Member(	13, 12,	"demoking");
		
		Family snw = new Family("snw");
		snw.members[0] = new Member(	4, 3,	"medusa");
		snw.members[1] = new Member(	5, 4,	"siren");
		snw.members[2] = new Member(	10, 9,	"lamia");
		snw.members[3] = new Member(	11, 11,	"naga");
		snw.members[4] = new Member(	13, 12,	"scylla");
		
		Family zom = new Family("zom");
		zom.members[0] = new Member(	0, 0,	"zombie");
		zom.members[1] = new Member(	2, 1,	"ghoul");
		zom.members[2] = new Member(	8, 7,	"mou-jya");
		zom.members[3] = new Member(	11, 10,	"wight");
		zom.members[4] = new Member(	13, 12,	"ghast");
		
		Family gst = new Family("gst");
		gst.members[0] = new Member(	1, 0,	"o-bake");
		gst.members[1] = new Member(	2, 1,	"phantom");
		gst.members[2] = new Member(	8, 7,	"buruburu");
		gst.members[3] = new Member(	11, 10,	"wraith");
		gst.members[4] = new Member(	13, 12,	"spector");
		
		Family gol = new Family("gol");
		gol.members[0] = new Member(	4, 3,	"woodman");
		gol.members[1] = new Member(	7, 6,	"clayman");
		gol.members[2] = new Member(	8, 8,	"stoneman");
		gol.members[3] = new Member(	11, 10,	"ironman");
		gol.members[4] = new Member(	13, 12,	"fireman");
		
		Family brd = new Family("brd");
		brd.members[0] = new Member(	0, 0,	"albatros");
		brd.members[1] = new Member(	5, 4,	"eagle");
		brd.members[2] = new Member(	9, 8,	"thunder");
		brd.members[3] = new Member(	11, 10,	"cocatris");
		brd.members[4] = new Member(	13, 12,	"rock");
		
		Family dgn = new Family("dgn");
		dgn.members[0] = new Member(	6, 5,	"dragon 1");
		dgn.members[1] = new Member(	7, 6,	"dragon 2");
		dgn.members[2] = new Member(	9, 8,	"dragon 3");
		dgn.members[3] = new Member(	11, 10,	"dragon 4");
		dgn.members[4] = new Member(	13, 12,	"dragon 5");
		
		Group insect = new Group(ins, crb, inm);
		Group fishes = new Group(fsh);
		Group slimes = new Group(eye, slm);
		Group snakes = new Group(snk, wrm, snw);
		Group undead = new Group(gst, skl, zom);
		Group winged = new Group(bdm, dvl);
		Group clawed = new Group(tgr, wfm, oni);
		Group reptis = new Group(liz, rhi);
		Group plants = new Group(plt, oct);
		Group golems = new Group(gol);
		Group fliers = new Group(chi, dgn, brd);
		
		Family xxx = null;
		
		ins.links.put(	insect,		xxx		);
		ins.links.put(	fishes,		xxx		);
		ins.links.put(	slimes,		brd		);
		ins.links.put(	snakes,		snk		);
		ins.links.put(	undead,		skl		);
		ins.links.put(	winged,		wrm		);
		ins.links.put(	clawed,		inm		);
		ins.links.put(	reptis,		liz		);
		ins.links.put(	plants,		slm		);
		ins.links.put(	golems,		eye		);
		ins.links.put(	fliers,		rhi		);
		
		fsh.links.put(	insect,		inm		);
		fsh.links.put(	fishes,		xxx		);
		fsh.links.put(	slimes,		tgr		);
		fsh.links.put(	snakes,		liz		);
		fsh.links.put(	undead,		wrm		);
		fsh.links.put(	winged,		bdm		);
		fsh.links.put(	clawed,		oni		);
		fsh.links.put(	reptis,		rhi		);
		fsh.links.put(	plants,		oct		);
		fsh.links.put(	golems,		skl		);
		fsh.links.put(	fliers,		null	);
		
		eye.links.put(	insect,		dvl		);
		eye.links.put(	fishes,		oct		);
		eye.links.put(	slimes,		xxx		);
		eye.links.put(	snakes,		xxx		);
		eye.links.put(	undead,		bdm		);
		eye.links.put(	winged,		wfm		);
		eye.links.put(	clawed,		fsh		);
		eye.links.put(	reptis,		snk		);
		eye.links.put(	plants,		zom		);
		eye.links.put(	golems,		wrm		);
		eye.links.put(	fliers,		plt		);
		
		slm.links.put(	insect,		brd		);
		slm.links.put(	fishes,		ins		);
		slm.links.put(	slimes,		xxx		);
		slm.links.put(	snakes,		fsh		);
		slm.links.put(	undead,		gst		);
		slm.links.put(	winged,		snk		);
		slm.links.put(	clawed,		xxx		);
		slm.links.put(	reptis,		zom		);
		slm.links.put(	plants,		wrm		);
		slm.links.put(	golems,		plt		);
		slm.links.put(	fliers,		ins		);
		
		snk.links.put(	insect,		rhi		);
		snk.links.put(	fishes,		liz		);
		snk.links.put(	slimes,		xxx		);
		snk.links.put(	snakes,		xxx		);
		snk.links.put(	undead,		slm		);
		snk.links.put(	winged,		zom		);
		snk.links.put(	clawed,		wrm		);
		snk.links.put(	reptis,		fsh		);
		snk.links.put(	plants,		wfm		);
		snk.links.put(	golems,		gol		);
		snk.links.put(	fliers,		gst		);
		
		wrm.links.put(	insect,		snk		);
		wrm.links.put(	fishes,		fsh		);
		wrm.links.put(	slimes,		inm		);
		wrm.links.put(	snakes,		xxx		);
		wrm.links.put(	undead,		fsh		);
		wrm.links.put(	winged,		oni		);
		wrm.links.put(	clawed,		ins		);
		wrm.links.put(	reptis,		slm		);
		wrm.links.put(	plants,		zom		);
		wrm.links.put(	golems,		oct		);
		wrm.links.put(	fliers,		tgr		);
		
		gst.links.put(	insect,		skl		);
		gst.links.put(	fishes,		wfm		);
		gst.links.put(	slimes,		oni		);
		gst.links.put(	snakes,		ins		);
		gst.links.put(	undead,		xxx		);
		gst.links.put(	winged,		brd		);
		gst.links.put(	clawed,		slm		);
		gst.links.put(	reptis,		inm		);
		gst.links.put(	plants,		rhi		);
		gst.links.put(	golems,		liz		);
		gst.links.put(	fliers,		zom		);
		
		snw.links.put(	insect,		oct		);
		snw.links.put(	fishes,		dvl		);
		snw.links.put(	slimes,		crb		);
		snw.links.put(	snakes,		xxx		);
		snw.links.put(	undead,		plt		);
		snw.links.put(	winged,		gol		);
		snw.links.put(	clawed,		chi		);
		snw.links.put(	reptis,		eye		);
		snw.links.put(	plants,		skl		);
		snw.links.put(	golems,		xxx		);
		snw.links.put(	fliers,		gol		);
		
		bdm.links.put(	insect,		slm		);
		bdm.links.put(	fishes,		brd		);
		bdm.links.put(	slimes,		wrm		);
		bdm.links.put(	snakes,		zom		);
		bdm.links.put(	undead,		snk		);
		bdm.links.put(	winged,		xxx		);
		bdm.links.put(	clawed,		tgr		);
		bdm.links.put(	reptis,		dvl		);
		bdm.links.put(	plants,		xxx		);
		bdm.links.put(	golems,		inm		);
		bdm.links.put(	fliers,		fsh		);
		
		tgr.links.put(	insect,		fsh		);
		tgr.links.put(	fishes,		xxx		);
		tgr.links.put(	slimes,		slm		);
		tgr.links.put(	snakes,		gst		);
		tgr.links.put(	undead,		zom		);
		tgr.links.put(	winged,		liz		);
		tgr.links.put(	clawed,		xxx		);
		tgr.links.put(	reptis,		wfm		);
		tgr.links.put(	plants,		brd		);
		tgr.links.put(	golems,		oni		);
		tgr.links.put(	fliers,		snk		);
		
		liz.links.put(	insect,		wfm		);
		liz.links.put(	fishes,		rhi		);
		liz.links.put(	slimes,		ins		);
		liz.links.put(	snakes,		tgr		);
		liz.links.put(	undead,		brd		);
		liz.links.put(	winged,		xxx		);
		liz.links.put(	clawed,		skl		);
		liz.links.put(	reptis,		xxx		);
		liz.links.put(	plants,		crb		);
		liz.links.put(	golems,		chi		);
		liz.links.put(	fliers,		oni		);
		
		plt.links.put(	insect,		eye		);
		plt.links.put(	fishes,		gst		);
		plt.links.put(	slimes,		bdm		);
		plt.links.put(	snakes,		crb		);
		plt.links.put(	undead,		dvl		);
		plt.links.put(	winged,		fsh		);
		plt.links.put(	clawed,		oct		);
		plt.links.put(	reptis,		crb		);
		plt.links.put(	plants,		xxx		);
		plt.links.put(	golems,		ins		);
		plt.links.put(	fliers,		chi		);
		
		skl.links.put(	insect,		xxx		);
		skl.links.put(	fishes,		inm		);
		skl.links.put(	slimes,		zom		);
		skl.links.put(	snakes,		wfm		);
		skl.links.put(	undead,		xxx		);
		skl.links.put(	winged,		ins		);
		skl.links.put(	clawed,		brd		);
		skl.links.put(	reptis,		oni		);
		skl.links.put(	plants,		snk		);
		skl.links.put(	golems,		crb		);
		skl.links.put(	fliers,		liz		);
		
		wfm.links.put(	insect,		zom		);
		wfm.links.put(	fishes,		skl		);
		wfm.links.put(	slimes,		gst		);
		wfm.links.put(	snakes,		oni		);
		wfm.links.put(	undead,		rhi		);
		wfm.links.put(	winged,		tgr		);
		wfm.links.put(	clawed,		xxx		);
		wfm.links.put(	reptis,		xxx		);
		wfm.links.put(	plants,		liz		);
		wfm.links.put(	golems,		wrm		);
		wfm.links.put(	fliers,		inm		);
		
		crb.links.put(	insect,		xxx		);
		crb.links.put(	fishes,		bdm		);
		crb.links.put(	slimes,		oct		);
		crb.links.put(	snakes,		dvl		);
		crb.links.put(	undead,		tgr		);
		crb.links.put(	winged,		skl		);
		crb.links.put(	clawed,		rhi		);
		crb.links.put(	reptis,		wrm		);
		crb.links.put(	plants,		inm		);
		crb.links.put(	golems,		xxx		);
		crb.links.put(	fliers,		eye		);
		
		inm.links.put(	insect,		xxx		);
		inm.links.put(	fishes,		oni		);
		inm.links.put(	slimes,		xxx		);
		inm.links.put(	snakes,		slm		);
		inm.links.put(	undead,		wfm		);
		inm.links.put(	winged,		rhi		);
		inm.links.put(	clawed,		zom		);
		inm.links.put(	reptis,		ins		);
		inm.links.put(	plants,		bdm		);
		inm.links.put(	golems,		dvl		);
		inm.links.put(	fliers,		brd		);
		
		oct.links.put(	insect,		wrm		);
		oct.links.put(	fishes,		slm		);
		oct.links.put(	slimes,		fsh		);
		oct.links.put(	snakes,		eye		);
		oct.links.put(	undead,		crb		);
		oct.links.put(	winged,		inm		);
		oct.links.put(	clawed,		dvl		);
		oct.links.put(	reptis,		tgr		);
		oct.links.put(	plants,		xxx		);
		oct.links.put(	golems,		zom		);
		oct.links.put(	fliers,		bdm		);
		
		zom.links.put(	insect,		oni		);
		zom.links.put(	fishes,		wrm		);
		zom.links.put(	slimes,		liz		);
		zom.links.put(	snakes,		inm		);
		zom.links.put(	undead,		xxx		);
		zom.links.put(	winged,		slm		);
		zom.links.put(	clawed,		gst		);
		zom.links.put(	reptis,		skl		);
		zom.links.put(	plants,		fsh		);
		zom.links.put(	golems,		tgr		);
		zom.links.put(	fliers,		wfm		);
		
		gol.links.put(	insect,		chi		);
		gol.links.put(	fishes,		plt		);
		gol.links.put(	slimes,		eye		);
		gol.links.put(	snakes,		bdm		);
		gol.links.put(	undead,		xxx		);
		gol.links.put(	winged,		snw		);
		gol.links.put(	clawed,		crb		);
		gol.links.put(	reptis,		snw		);
		gol.links.put(	plants,		gst		);
		gol.links.put(	golems,		xxx		);
		gol.links.put(	fliers,		oct		);
		
		dvl.links.put(	insect,		dvl		);
		dvl.links.put(	fishes,		zom		);
		dvl.links.put(	slimes,		snk		);
		dvl.links.put(	snakes,		skl		);
		dvl.links.put(	undead,		ins		);
		dvl.links.put(	winged,		xxx		);
		dvl.links.put(	clawed,		bdm		);
		dvl.links.put(	reptis,		gst		);
		dvl.links.put(	plants,		xxx		);
		dvl.links.put(	golems,		brd		);
		dvl.links.put(	fliers,		wrm		);
		
		rhi.links.put(	insect,		liz		);
		rhi.links.put(	fishes,		tgr		);
		rhi.links.put(	slimes,		skl		);
		rhi.links.put(	snakes,		brd		);
		rhi.links.put(	undead,		inm		);
		rhi.links.put(	winged,		xxx		);
		rhi.links.put(	clawed,		wfm		);
		rhi.links.put(	reptis,		xxx		);
		rhi.links.put(	plants,		oni		);
		rhi.links.put(	golems,		snk		);
		rhi.links.put(	fliers,		slm		);
		
		chi.links.put(	insect,		plt		);
		chi.links.put(	fishes,		crb		);
		chi.links.put(	slimes,		oct		);
		chi.links.put(	snakes,		oct		);
		chi.links.put(	undead,		eye		);
		chi.links.put(	winged,		plt		);
		chi.links.put(	clawed,		snk		);
		chi.links.put(	reptis,		bdm		);
		chi.links.put(	plants,		tgr		);
		chi.links.put(	golems,		xxx		);
		chi.links.put(	fliers,		xxx		);
		
		dgn.links.put(	insect,		bdm		);
		dgn.links.put(	fishes,		eye		);
		dgn.links.put(	slimes,		snw		);
		dgn.links.put(	snakes,		gol		);
		dgn.links.put(	undead,		chi		);
		dgn.links.put(	winged,		crb		);
		dgn.links.put(	clawed,		plt		);
		dgn.links.put(	reptis,		oct		);
		dgn.links.put(	plants,		snw		);
		dgn.links.put(	golems,		rhi		);
		dgn.links.put(	fliers,		xxx		);
		
		brd.links.put(	insect,		ins		);
		brd.links.put(	fishes,		snk		);
		brd.links.put(	slimes,		rhi		);
		brd.links.put(	snakes,		wrm		);
		brd.links.put(	undead,		oni		);
		brd.links.put(	winged,		gst		);
		brd.links.put(	clawed,		liz		);
		brd.links.put(	reptis,		xxx		);
		brd.links.put(	plants,		dvl		);
		brd.links.put(	golems,		bdm		);
		brd.links.put(	fliers,		xxx		);
		
		oni.links.put(	insect,		gst		);
		oni.links.put(	fishes,		xxx		);
		oni.links.put(	slimes,		wfm		);
		oni.links.put(	snakes,		rhi		);
		oni.links.put(	undead,		liz		);
		oni.links.put(	winged,		dvl		);
		oni.links.put(	clawed,		xxx		);
		oni.links.put(	reptis,		brd		);
		oni.links.put(	plants,		eye		);
		oni.links.put(	golems,		snw		);
		oni.links.put(	fliers,		skl		);
		
		groups.addAll(Arrays.asList(new Group[] {
				insect, fishes, slimes, snakes, undead, winged, clawed, reptis,
				plants, golems, fliers}));
		
		families.addAll(Arrays.asList(new Family[] {
				ins, crb, inm, fsh, eye, slm, snk, wrm, snw, gst, skl, zom, bdm,
				dvl, tgr, wfm, oni, liz, rhi, plt, oct, gol, chi, dgn, brd}));
		
	}

}
