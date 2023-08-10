package ru.paulevs.bismuthlib.data.info;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class LightInfo {
	public abstract int getSimple(World level, BlockPos pos, byte i);
	public abstract int getAdvanced(World level, BlockPos pos, byte i);
	public abstract int getRadius();
}
