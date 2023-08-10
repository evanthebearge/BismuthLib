package ru.paulevs.bismuthlib.data.transformer;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ProviderLightTransformer extends LightTransformer {
	private final BlockState state;
	private final BlockColorProvider color;
	private final int index;
	
	public ProviderLightTransformer(BlockState state, BlockColorProvider color, int index) {
		this.state = state;
		this.color = color;
		this.index = index;
	}
	
	@Override
	public int getColor(World level, BlockPos pos) {
		return color.getColor(state, level, pos, index);
	}
}
