/**
 *  GeneratorCellular.java
 *  Created on Oct 24, 2013 11:30:28 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.Tile;
import net.wombatrpgs.mrogue.maps.events.DoorEvent;
import net.wombatrpgs.mrogue.maps.gen.dec.Decorator;
import net.wombatrpgs.mrogue.rpg.Enemy;
import net.wombatrpgs.mrogueschema.maps.MapGeneratorMDO;
import net.wombatrpgs.mrogueschema.maps.data.OrthoDir;
import net.wombatrpgs.mrogueschema.maps.data.TileType;

/**
 * Uses a cellular automata algorithm to generate a building interior.
 */
public class GeneratorCellular extends MapGenerator {

	/**
	 * @param	mdo				The data to construct from
	 * @param	parent			The level to generate for
	 */
	public GeneratorCellular(MapGeneratorMDO mdo, Level parent) {
		super(mdo, parent);
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.gen.MapGenerator#generateInternal()
	 */
	@Override
	protected void generateInternal() {
		int w = parent.getWidth();
		int h = parent.getHeight();
		TileType types[][] = new TileType[h][w];
		fillRect(types, TileType.ANY_CEILING, 0, 0, w-1, h-1);
		int cellStartX[] = new int[w];
		cellStartX[0] = 1;
		int totalWidth = 1;
		int lastWidth = 0;
		int cellsW = 0;
		for (int i = 1; true; i += 1) {
			int width;
			if (lastWidth != 1 && r.nextFloat() > mdo.connectivity) {
				width = 1;
			} else {
				if (mdo.maxRoomWidth-mdo.minRoomWidth == 0) {
					width = mdo.minRoomWidth;
				} else {
					width = mdo.minRoomWidth + r.nextInt(mdo.maxRoomWidth-mdo.minRoomWidth);
				}
			}
			totalWidth += (width + 1);
			cellStartX[i] = cellStartX[i-1] + 1 + width;
			if (totalWidth >= w) break;
			cellsW += 1;
			lastWidth = width;
		}
		int cellStartY[] = new int[h];
		cellStartY[0] = 3;
		int totalHeight = 3;
		int lastHeight = 0;
		int cellsH = 0;
		for (int i = 1; true; i += 1) {
			int height;
			if (lastHeight != 1 && r.nextBoolean()) {
				height = 1;
			} else {
				if (mdo.maxRoomHeight-mdo.minRoomHeight == 0) {
					height = mdo.minRoomHeight;
				} else {
					height = mdo.minRoomHeight + r.nextInt(mdo.maxRoomHeight-mdo.minRoomHeight);
				}
			}
			totalHeight += (height + 3);
			cellStartY[i] = cellStartY[i-1] + 3 + height;
			if (totalHeight >= h) break;
			cellsH += 1;
			lastHeight = height;
		}
		List<CRoom> allrooms = new ArrayList<CRoom>();
		CRoom[][] rooms = new CRoom[cellsH][cellsW];
		for (int x = 0; x < cellsW; x += 1) {
			for (int y = 0; y < cellsH; y += 1) {
				CRoom cr = new CRoom(
						x, y,
						cellStartX[x],
						cellStartY[y],
						cellStartX[x+1]-cellStartX[x]-1, 
						cellStartY[y+1]-cellStartY[y]-3);
				allrooms.add(cr);
				rooms[y][x] = cr;
				fillRect(types, TileType.FLOOR,
						cr.x,
						cr.y, 
						cr.x + cr.rw-1,
						cr.y + cr.rh-1);
			}
		}
		List<CRoom> fringe = new ArrayList<CRoom>();
		CRoom origin = allrooms.get(r.nextInt(allrooms.size()));
		origin.added = true;
		fringe.add(origin);
		while (fringe.size() > 0) {
			CRoom cr = fringe.get(r.nextInt(fringe.size()));
			if (!cr.hallway && r.nextBoolean()) continue;
			int off = r.nextInt(OrthoDir.values().length);
			int dirs = OrthoDir.values().length;
			OrthoDir d = null;
			CRoom cr2 = null;
			for (int i = 0; i < dirs; i += 1) {
				d = OrthoDir.values()[(i + off) % dirs];
				int nx = (int) (cr.cellX + d.getVector().x);
				int ny = (int) (cr.cellY + d.getVector().y);
				if (nx < 0 || nx >= cellsW) continue;
				if (ny < 0 || ny >= cellsH) continue;
				cr2 = rooms[ny][nx];
				if (cr2.added) {
					cr2 = null;
					continue;
				}
				break;
			}
			if (cr2 == null) {
				fringe.remove(cr);
				continue;
			}
			if (!cr2.hallway && r.nextBoolean()) continue;
			
			cr.tensionAvailable = false;
			cr2.added = true;
			cr2.addside = OrthoDir.getOpposite(d);
			fringe.add(cr2);
			
			if (r.nextFloat() < mdo.density && !cr2.hallway && !cr.hallway) {
				cr2.tensionAvailable = false;
				if (cr2.x == cr.x) {
					fillRect(types, TileType.FLOOR, cr.x, cr.y, cr2.x + cr2.rw-1, cr2.y);
				} else {
					fillRect(types, TileType.FLOOR, cr.x, cr.y, cr2.x, cr2.y + cr2.rh-1);
				}
			} else {
				carve(types, TileType.FLOOR, cr.ctx(), cr.cty(), cr2.ctx(), cr2.cty(), Halt.NONE);
			}
		}
		
		for (int x = 0; x < cellsW; x += 1) {
			if (cellStartX[x+1] - cellStartX[x] <= 2) {
				boolean replaced = true;
				while (replaced) {
					replaced = false;
					for (int atX = cellStartX[x]-1; atX <= cellStartX[x]+1; atX++) {
						for (int atY = 1; atY < h-1; atY += 1) {
							if (!types[atY][atX].isPassable()) continue;
							int touch = 0;
							if (types[atY][atX+1].isPassable()) touch += 1;
							if (types[atY][atX-1].isPassable()) touch += 1;
							if (types[atY+1][atX].isPassable()) touch += 1;
							if (types[atY-1][atX].isPassable()) touch += 1;
							if (touch <= 1) {
								types[atY][atX] = TileType.ANY_CEILING;
								replaced = true;
							}
						}
					}
				}
			}
		}
		for (int y = 0; y < cellsH; y += 1) {
			if (cellStartY[y+1] - cellStartY[y] <= 4) {
				boolean replaced = true;
				while (replaced) {
					replaced = false;
					for (int atY = cellStartY[y]-3; atY <= cellStartY[y]+3; atY++) {
						for (int atX = 1; atX < w-1; atX += 1) {
							if (!types[atY][atX].isPassable()) continue;
							int touch = 0;
							if (types[atY][atX+1].isPassable()) touch += 1;
							if (types[atY][atX-1].isPassable()) touch += 1;
							if (types[atY+1][atX].isPassable()) touch += 1;
							if (types[atY-1][atX].isPassable()) touch += 1;
							if (touch <= 1) {
								types[atY][atX] = TileType.ANY_CEILING;
								replaced = true;
							}
						}
					}
				}
			}
		}
		for (int x = 0; x < cellsW; x += 1) {
			if (cellStartX[x+1] - cellStartX[x] <= 2) {
				for (int atX = cellStartX[x]-1; atX <= cellStartX[x]+1; atX++) {
					int mid = cellStartX[x];
					if (atX == mid) continue;
					for (int atY = 1; atY < h-1; atY += 1) {
						if (isPassable(types, atX, atY)) {
							if (atX < mid && !isPassable(types, atX+2, atY)) {
								DoorEvent door = genDoor();
								parent.addEvent(door, atX, atY);
								door.setFacing(OrthoDir.WEST);
							}
							if (atX > mid && !isPassable(types, atX-2, atY)) {
								DoorEvent door = genDoor();
								parent.addEvent(door, atX, atY);
								door.setFacing(OrthoDir.EAST);
							}
						}
					}
				}
			}
		}
		for (int y = 0; y < cellsH; y += 1) {
			if (cellStartY[y+1] - cellStartY[y] <= 4) {
				for (int atY = cellStartY[y]-1; atY <= cellStartY[y]+1; atY++) {
					int mid = cellStartY[y];
					if (atY == mid) continue;
					for (int atX = 1; atX < w-1; atX += 1) {
						if (isPassable(types, atX, atY)) {
							if (atY < mid && !isPassable(types, atX, atY+2)) {
								DoorEvent door = genDoor();
								parent.addEvent(door, atX, atY-2);
								door.setFacing(OrthoDir.NORTH);
							}
							if (atY > mid && !isPassable(types, atX, atY-2)) {
								DoorEvent door = genDoor();
								parent.addEvent(door, atX, atY);
								door.setFacing(OrthoDir.SOUTH);
							}
						}
					}
				}
			}
		}
		
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
		
		for (CRoom cr : allrooms) {
			if (!cr.tensionAvailable) continue;
			if (cr.rw * cr.rh > 30) continue;
			if (r.nextFloat() < .6) continue;
			if (parent.getDanger() < 2) continue;
			cr.tensionSelected = true;
			int count = (cr.x + cr.rw - 1) * (cr.y + cr.rh - 1);
			List<Enemy> enemies = parent.getMonsterGenerator().createSet(count);
			for (int x = cr.x; x < cr.x + cr.rw; x += 1) {
				for (int y = cr.y; y < cr.y + cr.rh; y += 1) {
					if (r.nextFloat() > .1 && parent.isTilePassable(null, x, y)) {
						parent.addEvent(enemies.get(0), x, y);
						enemies.remove(0);
					}
				}
			}
			for (int loot = 0; loot <= 2 || r.nextBoolean(); loot += 1) {
				int spawnX = 0;
				int spawnY = 0;
				while (!parent.isTilePassable(null, spawnX, spawnY)) {
					spawnX = cr.ctx() + r.nextInt(3)-2;
					spawnY = cr.cty() + r.nextInt(3)-2;
				}
				parent.addEvent(parent.getLootGenerator().createEvent(), spawnX, spawnY);
			}
			break;
		}
	}

}
