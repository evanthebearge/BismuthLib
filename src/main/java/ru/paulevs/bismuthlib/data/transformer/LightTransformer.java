package ru.paulevs.bismuthlib.data.transformer;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class LightTransformer {
	public abstract int getColor(World level, BlockPos pos);
}
