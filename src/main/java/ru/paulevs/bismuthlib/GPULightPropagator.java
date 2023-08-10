package ru.paulevs.bismuthlib;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.util.Random;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.math.MathHelper;

public class GPULightPropagator {
	private static ShaderProgram shader;
	private final Framebuffer[] buffers = new Framebuffer[2];
	private final int textureSide;
	private final int dataSide;
	private byte index;
	
	public GPULightPropagator(int dataWidth, int dataHeight) {
		if (shader == null) {
			try {
				shader = new ShaderProgram(
					MinecraftClient.getInstance().getServerResourcePackProvider().getVanillaPack().getFactory(),
					"bismuthlib_gpu_light",
					VertexFormats.POSITION_TEXTURE
				);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		dataSide = (int) Math.ceil(MathHelper.sqrt(dataWidth * dataWidth * dataHeight));
		textureSide = getClosestPowerOfTwo(dataSide << 6);
		buffers[0] = new SimpleFramebuffer(textureSide, textureSide, false, MinecraftClient.IS_SYSTEM_MAC);
		buffers[1] = new SimpleFramebuffer(textureSide, textureSide, false, MinecraftClient.IS_SYSTEM_MAC);
		
		Random random = new Random();
		NativeImage image = new NativeImage(textureSide, textureSide, false);
		for (int x = 0; x < textureSide; x++) {
			for (int y = 0; y < textureSide; y++) {
				image.setColor(x, y, random.nextInt() | (255 << 24));
			}
		}
		
		GlStateManager._bindTexture(buffers[0].getColorAttachment());
		image.upload(0, 0, 0, false);
	}
	
	public void render() {
		int buffer = GL30.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
		//System.out.println(buffer);
		for (byte i = 0; i < 16; i++) {
			Framebuffer source = buffers[index];
			index = (byte) ((index + 1) & 1);
			Framebuffer target = buffers[index];
			target.beginWrite(true);
			target.clear(MinecraftClient.IS_SYSTEM_MAC);
			
			if (shader != null) RenderSystem.setShader(() -> shader);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			RenderSystem.setShaderTexture(0, source.getColorAttachment());
			
			BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
			
			bufferBuilder.vertex(0, textureSide, 0).texture(0.0F, 1.0F).next();
			bufferBuilder.vertex(textureSide, textureSide, 0).texture(1.0F, 1.0F).next();
			bufferBuilder.vertex(textureSide, 0, 0).texture(1.0F, 0.0F).next();
			bufferBuilder.vertex(0, 0, 0).texture(0.0F, 0.0F).next();
			
			BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
			target.endWrite();
		}
		GL30.glBindRenderbuffer(GL30.GL_FRAMEBUFFER, buffer);
	}
	
	public void dispose() {
		buffers[0].delete();
		buffers[1].delete();
	}
	
	private int getClosestPowerOfTwo(int value) {
		if (value <= 0) return 0;
		byte index = 0;
		byte count = 0;
		for (byte i = 0; i < 32; i++) {
			byte bit = (byte) (value & 1);
			if (bit == 1) {
				index = i;
				count++;
			}
			value >>>= 1;
		}
		return count == 1 ? 1 << index : 1 << (index + 1);
	}
	
	public int getTexture() {
		return buffers[index].getColorAttachment();
	}
}
