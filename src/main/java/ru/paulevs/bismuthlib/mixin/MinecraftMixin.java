package ru.paulevs.bismuthlib.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftMixin {
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void cf_onMinecraftInit(RunArgs gameConfig, CallbackInfo info) {
		ru.paulevs.bismuthlib.BismuthLibClient.initData();
	}
}
