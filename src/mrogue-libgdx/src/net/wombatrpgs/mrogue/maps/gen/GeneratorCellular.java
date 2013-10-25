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
import net.wombatrpgs.mrogue.maps.Loc;
import net.wombatrpgs.mrogue.maps.Tile;
import net.wombatrpgs.mrogue.maps.events.DoorEvent;
import net.wombatrpgs.mrogue.maps.gen.dec.Decorator;
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
			if (lastWidth != 1 && r.nextBoolean()) {
				width = 1;
			} else {
				width = mdo.minRoomWidth + r.nextInt(mdo.maxRoomWidth-mdo.minRoomWidth);
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
				height = mdo.minRoomHeight + r.nextInt(mdo.maxRoomHeight-mdo.minRoomHeight);
			}
			totalHeight += (height + 3);
			cellStartY[i] = cellStartY[i-1] + 3 + height;
			if (totalHeight >= h) break;
			cellsH += 1;
			lastHeight = height;
		}
		for (int x = 0; x < cellsW; x += 1) {
			for (int y = 0; y < cellsH; y += 1) {
				fillRect(types, TileType.FLOOR,
						cellStartX[x],
						cellStartY[y], 
						cellStartX[x+1]-2,
						cellStartY[y+1]-4);
			}
		}
		boolean in[][] = new boolean[cellsH][cellsW];
		List<Loc> fringe = new ArrayList<Loc>();
		fringe.add(new Loc(r.nextInt(cellsW), r.nextInt(cellsH)));
		while (fringe.size() > 0) {
			Loc at = fringe.get(r.nextInt(fringe.size()));
			int off = r.nextInt(OrthoDir.values().length);
			int dirs = OrthoDir.values().length;
			OrthoDir d;
			int nx = 0, ny = 0, i;
			for (i = 0; i < dirs; i += 1) {
				d = OrthoDir.values()[(i + off) % dirs];
				nx = (int) (at.x + d.getVector().x);
				ny = (int) (at.y + d.getVector().y);
				if (nx < 0 || nx >= cellsW) continue;
				if (ny < 0 || ny >= cellsH) continue;
				if (in[ny][nx]) continue;
				break;
			}
			if (i == dirs) {
				fringe.remove(at);
				continue;
			}
			
			in[ny][nx] = true;
			fringe.add(new Loc(nx, ny));
			
			Room arm = new Room(cellStartX[at.x], cellStartY[at.y],
					cellStartX[at.x+1] - cellStartX[at.x] - 2,
					cellStartY[at.y+1] - cellStartY[at.y] - 4);
			Room nrm = new Room(cellStartX[nx], cellStartY[ny],
					cellStartX[nx+1] - cellStartX[nx] - 2,
					cellStartY[ny+1] - cellStartY[ny] - 4);
			if (r.nextFloat() > .1 &&
					nrm.rw > 1 && nrm.rh > 1 &&
					arm.rw > 1 && arm.rh > 1) {
				if (nrm.x == arm.x) {
					fillRect(types, TileType.FLOOR, arm.x, arm.y, nrm.x + nrm.rw, nrm.y);
				} else {
					fillRect(types, TileType.FLOOR, arm.x, arm.y, nrm.x, nrm.y + nrm.rh);
				}
			} else {
				carve(types, TileType.FLOOR, arm.ctx(), arm.cty(), nrm.ctx(), nrm.cty(), Halt.NONE);
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
								door.setFacing(OrthoDir.EAST);
							}
							if (atX > mid && !isPassable(types, atX-2, atY)) {
								DoorEvent door = genDoor();
								parent.addEvent(door, atX, atY);
								door.setFacing(OrthoDir.WEST);
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
	}

}
