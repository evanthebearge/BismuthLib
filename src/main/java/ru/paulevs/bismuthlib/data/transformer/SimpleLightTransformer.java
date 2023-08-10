package ru.paulevs.bismuthlib.data.transformer;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.paulevs.bismuthlib.ColorMath;

public class SimpleLightTransformer extends LightTransformer {
	private final int color;
	
	public SimpleLightTransformer(int color) {
		this.color = ColorMath.reverse(color);
	}
	
	@Override
	public int getColor(World level, BlockPos pos) {
		return color;
	}
}
