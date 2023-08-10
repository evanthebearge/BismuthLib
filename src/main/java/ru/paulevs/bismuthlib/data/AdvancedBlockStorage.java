package ru.paulevs.bismuthlib.data;

import ru.paulevs.bismuthlib.data.info.LightInfo;
import ru.paulevs.bismuthlib.data.transformer.LightTransformer;
import ru.paulevs.bismuthlib.gui.CFOptions;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AdvancedBlockStorage {
	private static final Direction[] DIRECTIONS = new Direction[] { Direction.UP, Direction.NORTH, Direction.EAST };
	private static final byte[] FLAGS     = new byte[] { 0b001, 0b010, 0b100 };
	private static final byte[] FLAGS_INV = new byte[] { 0b110, 0b101, 0b011 };
	private static final byte MAX = 0b111;
	
	private Map<BlockPos, LightTransformer> transformers = new HashMap<>();
	private Map<BlockPos, LightInfo> lights = new HashMap<>();
	private Mutable pos = new Mutable();
	private byte[] storage = new byte[110592];
	private int storedIndex;
	
	public void fill(World level, int x1, int y1, int z1) {
		storedIndex = 0;
		for (byte dx = 0; dx < 48; dx++) {
			pos.setX(x1 + dx);
			for (byte dy = 0; dy < 48; dy++) {
				pos.setY(y1 + dy);
				for (byte dz = 0; dz < 48; dz++) {
					pos.setZ(z1 + dz);
					storage[storedIndex] = MAX;
					BlockState state = level.getBlockState(pos);
					
					LightInfo light = BlockLights.getLight(state);
					if (light != null) {
						lights.put(pos.toImmutable(), light);
						storage[storedIndex++] = 0;
						continue;
					}
					
					if (CFOptions.modifyColor()) {
						LightTransformer transformer = BlockLights.getTransformer(state);
						if (transformer != null) {
							transformers.put(pos.toImmutable(), transformer);
							storage[storedIndex++] = 0;
							continue;
						}
					}
					
					if (blockLight(state, level, pos)) {
						storage[storedIndex] = 0;
					}
					else {
						for (Direction dir: DIRECTIONS) {
							if (blockFace(state, level, pos, dir)) {
								storage[storedIndex] &= FLAGS_INV[dir.getAxis().ordinal()];
							}
						}
					}
					storedIndex++;
				}
			}
		}
		pos.set(x1, y1, z1);
	}
	
	private boolean blockFace(BlockState state, World level, BlockPos pos, Direction dir) {
		return state.isSideSolidFullSquare(level, pos, dir) || state.isSideSolidFullSquare(level, pos, dir.getOpposite());
	}
	
	private boolean blockLight(BlockState state, World level, BlockPos pos) {
		return state.getMaterial().isSolidBlocking() || !state.isTransparent(level, pos);
	}
}
