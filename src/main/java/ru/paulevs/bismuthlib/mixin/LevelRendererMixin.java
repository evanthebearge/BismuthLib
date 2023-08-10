package ru.paulevs.bismuthlib.mixin;

import com.mojang.math.Matrix4f;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.paulevs.bismuthlib.BismuthLibClient;

@Mixin(WorldRenderer.class)
public class LevelRendererMixin {
	@Shadow private @Nullable ClientWorld level;
	
	@Inject(method = "renderLevel", at = @At("HEAD"))
	private void cf_onRenderLevel(MatrixStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightTexture, Matrix4f matrix4f, CallbackInfo info) {
		if (this.level != null) {
			BismuthLibClient.update(
				this.level,
				MathHelper.floor(camera.getPos().x / 16.0),
				MathHelper.floor(camera.getPos().y / 16.0),
				MathHelper.floor(camera.getPos().z / 16.0)
			);
			BismuthLibClient.bindWithUniforms();
		}
	}
	
	@Inject(method = "renderChunkLayer", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/renderer/ShaderInstance;apply()V",
		shift = Shift.BEFORE
	))
	private void cf_onRenderChunkLayer(RenderLayer renderType, MatrixStack poseStack, double d, double e, double f, Matrix4f matrix4f, CallbackInfo info) {
		BismuthLibClient.bindWithUniforms();
	}
}
