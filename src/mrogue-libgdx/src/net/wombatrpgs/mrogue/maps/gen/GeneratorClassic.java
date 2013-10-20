/**
 *  ClassicGenerator.java
 *  Created on Oct 13, 2013 1:37:18 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen;

import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.Tile;
import net.wombatrpgs.mrogue.maps.gen.dec.Decorator;
import net.wombatrpgs.mrogueschema.maps.MapGeneratorMDO;
import net.wombatrpgs.mrogueschema.maps.data.TileType;

/**
 * Generates maps like GA:ED.
 */
public class GeneratorClassic extends MapGenerator {

	/**
	 * Creates a new generator from data.
	 * @param	mdo				The generator data to create from
	 * @param	parent			The parent to create for
	 */
	public GeneratorClassic(MapGeneratorMDO mdo, Level parent) {
		super(mdo, parent);
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.gen.MapGenerator#generateInternal()
	 */
	@Override
	protected void generateInternal() {
		int w = parent.getWidth();
		int h = parent.getHeight();
		int roomCount = roomCount();
		Room[] rooms = new Room[roomCount];
		TileType types[][] = new TileType[h][w];
		fillRect(types, TileType.ANY_CEILING, 0, 0, w-1, h-1);
		int gen = 0;
		while (gen < roomCount) {
			int rw = mdo.minRoomWidth + r.nextInt(mdo.maxRoomWidth-mdo.minRoomWidth);
			int rh = mdo.minRoomHeight + r.nextInt(mdo.maxRoomHeight-mdo.minRoomHeight);
			int x = 1 + r.nextInt(w - (2 + rw));
			int y = 1 + r.nextInt(h - (2 + rh) - 2);
			boolean overlapping = false;
			for (int i = 0; i < gen; i += 1) {
				Room rm = rooms[i];
				if ((x-1<rm.x+rm.rw+1 && rm.x-1<x+rw+1) && (y-1<rm.y+rm.rh+1 && rm.y-1<y+rh+1)) {
					overlapping = true;
					break;
				}
			}
			if (overlapping) continue;
			Room rm = new Room(x, y, rw, rh);
			rooms[gen] = rm;
			fillRect(types, TileType.FLOOR, x, y, x+rw, y+rh);
			if (gen != 0 && roomUnreachable(types, rooms[gen])) {
				float minDist = Float.MAX_VALUE;
				Room min = rooms[0];
				for (int i = 0; i < gen; i += 1) {
					Room rm2 = rooms[i];
					if (roomDistSq(rm, rm2) < minDist) min = rm2;
				}
				carve(types, TileType.FLOOR, rm.rx(r), rm.ry(r), min.rx(r), min.ry(r), Halt.NONE);
			}
			gen += 1;
		}
		purgeFloatingWalls(types, TileType.FLOOR);
		applyWalls(types);
		addStaircases(types);
		applyCeilings(types);
		Tile lowerTiles[][] = new Tile[h][w];
		convertTiles(types, null, lowerTiles);
		addLayer(lowerTiles, 0);
		
		// upper chip
		Tile upperTiles[][] = new Tile[h][w];
		for (Decorator d : upDecorators) {
			d.apply(lowerTiles, upperTiles);
		}
		addLayer(upperTiles, .5f);
	}

}
