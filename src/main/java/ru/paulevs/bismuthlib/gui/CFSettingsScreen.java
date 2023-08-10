package ru.paulevs.bismuthlib.gui;

import java.util.List;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public class CFSettingsScreen extends GameOptionsScreen {
	private OptionListWidget list;
	
	public CFSettingsScreen(Screen screen, GameOptions options) {
		super(screen, options, Text.translatable("bismuthlib.options.settings.title"));
	}
	
	@Override
	protected void init() {
		this.list = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
		this.list.addSingleOptionEntry(CFOptions.MAP_RADIUS_XZ);
		this.list.addSingleOptionEntry(CFOptions.MAP_RADIUS_Y);
		this.list.addSingleOptionEntry(CFOptions.BRIGHTNESS);
		this.list.addAll(CFOptions.OPTIONS);
		this.addSelectableChild(this.list);
		
		Window window = this.client.getWindow();
		this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> {
			CFOptions.save();
			ru.paulevs.bismuthlib.BismuthLibClient.initData();
			window.applyVideoMode();
			this.client.setScreen(this.parent);
		}));
	}
	
	@Override
	public void render(MatrixStack poseStack, int i, int j, float f) {
		this.renderBackground(poseStack);
		this.list.render(poseStack, i, j, f);
		drawCenteredTextWithShadow(poseStack, this.textRenderer, this.title, this.width / 2, 5, 0xFFFFFF);
		super.render(poseStack, i, j, f);
		List<OrderedText> list = tooltipAt(this.list, i, j);
		this.renderTooltip(poseStack, list, i, j);
	}
}
