package ru.paulevs.bismuthlib.data;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SimpleBlockStorage implements BlockView {
	private static final BlockState AIR = Blocks.AIR.getDefaultState();
	private Mutable pos = new Mutable();
	private BlockState[] storage = new BlockState[110592];
	
	public void fill(World level, int x1, int y1, int z1) {
		int index = 0;
		for (byte dx = 0; dx < 48; dx++) {
			pos.setX(x1 + dx);
			for (byte dy = 0; dy < 48; dy++) {
				pos.setY(y1 + dy);
				for (byte dz = 0; dz < 48; dz++) {
					pos.setZ(z1 + dz);
					storage[index++] = level.getBlockState(pos);
				}
			}
		}
		pos.set(x1, y1, z1);
	}
	
	private int getIndex(int x, int y, int z) {
		return  x * 2304 + y * 48 + z;
	}
	
	@Nullable
	@Override
	public BlockEntity getBlockEntity(BlockPos blockPos) {
		return null;
	}
	
	@Override
	public BlockState getBlockState(BlockPos blockPos) {
		int px = blockPos.getX() - pos.getX();
		int py = blockPos.getY() - pos.getY();
		int pz = blockPos.getZ() - pos.getZ();
		if (px < 0 || px > 47 || py < 0 || py > 47 || pz < 0 || pz > 47) return AIR;
		return storage[getIndex(px, py, pz)];
	}
	
	@Override
	public FluidState getFluidState(BlockPos blockPos) {
		return null;
	}
	
	@Override
	public int getHeight() {
		return 0;
	}
	
	@Override
	public int getBottomY() {
		return 0;
	}
}
